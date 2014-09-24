package nz.co.bookreviews.ds.neo4j.impl


import static nz.co.bookreviews.util.JerseyClientUtil.getResponsePayload
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.bookreviews.AuthenticationException
import nz.co.bookreviews.ConflictException
import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.Page;
import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.neo4j.Neo4jSupport
import nz.co.bookreviews.ds.neo4j.UserDS

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource

@Service("userNeo4jRepositoryDs")
@Slf4j
class UserNeo4jDSImpl implements UserDS{

	@Resource
	Client jerseyClient
	@Resource
	Neo4jSupport neo4jSupport

	@Value('${neo4j.host:http://localhost:7474/db/data/}')
	String neo4jHttpUri

	JsonSlurper jsonSlurper = new JsonSlurper()

	@Override
	User createUser(final String userName,final String password) {
		log.debug 'createUser start'
		String transLocation
		String createCypherStatement="{\"statements\":[{\"statement\":\"CREATE (n:User { userName : '"+userName+"',password : '"+password+"' }) \"},{\"statement\":\"MATCH (u {userName : '"+userName+"',password : '"+password+"'}) SET u :User RETURN u\", \"resultDataContents\":[\"REST\"]}]}"
		log.debug 'statement json:{} $createCypherStatement'
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("transaction/commit")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, createCypherStatement)
		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new Exception('User create failed.')
		}
		String respStr = getResponsePayload(response)
		log.debug 'response:{} $respStr'

		String self = neo4jSupport.getNodeUriFromTransStatementsResponse(respStr,1)
		log.debug 'self:{} $self'
		String uniqueNodeReqBody = "{\"value\" : \""+userName+"\",\"uri\" : \""+self+"\",\"key\" : \"userName\"}"

		webResource = jerseyClient.resource(neo4jHttpUri)
				.path("index/node/favorites").queryParam("uniqueness", "create_or_fail")
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, uniqueNodeReqBody)
		if(response.getStatusInfo().statusCode != Status.CREATED.code){
			try {
				neo4jSupport.deleteNodeByUri(self)
			} catch (e) {
				throw new Exception('User['+self+'] create failed. it is supposed to be deleted manually.')
			}
			throw new Exception('User create failed.')
		}
		return new User(nodeUri:self,userName:userName,password:password)
	}

	@Override
	User loginUser(final String userName,final String password) {
		User found = getUserByName(userName)
		if(password != found.password){
			throw new AuthenticationException('Password is incorrect.')
		}
		return found
	}

	@Override
	User getUserByUri(final String userNodeUri) {
		Map resultMap
		try {
			resultMap = neo4jSupport.getNodeByUri(userNodeUri)
		} catch (e) {
			throw new NotFoundException('User not found by uri[${userNodeUri}].',e)
		}
		Map dataMap = (Map)resultMap.get('data')
		User found = new User(userName:dataMap.get('userName'),password:dataMap.get('password'),nodeUri:userNodeUri)
		return found
	}

	@Override
	User getUserByName(final String userName) {
		Map<String,String> mapResult =  doQueryUserByName(userName)
		return new User(nodeUri:mapResult.get('nodeUri'),password:mapResult.get('password'),userName:userName)
	}

	@Override
	User updateUserByUserName(final String userName,final User updatedUser){
		Map<String,String> mapResult = doQueryUserByName(userName)
		String updateUserName = updatedUser.userName?:mapResult.get('userName')
		String updatePassword = updatedUser.password?:mapResult.get('password')
		String updateJson = "{\"query\":\"MATCH (u:User {userName: {userName}}) SET u = { props } RETURN u\",\"params\":{\"userName\":\""+userName+"\",\"props\":{\"userName\":\""+updateUserName+"\",\"password\":\""+updatePassword+"\"}}}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, updateJson)
		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new RuntimeException('User update fail.')
		}
		return new User(nodeUri:mapResult.get('nodeUri'),password:updatePassword,userName:updateUserName)
	}

	/**
	 * User who has no relationships with other, can only be deleted
	 */
	@Override
	public void deleteUserByName(final String userName) {
		String queryNodeHasNoRelationship = "{\"query\":\"MATCH (u:User{userName:{userName}}) WHERE NOT (u)-[]->() RETURN u\",\"params\":{\"userName\":\""+userName+"\"}}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryNodeHasNoRelationship)
		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new RuntimeException('Delete User fail.')
		}
		String respStr = getResponsePayload(response)
		ArrayList dataList = (ArrayList)((Map)jsonSlurper.parseText(respStr)).get('data')
		if(dataList.isEmpty()){
			throw new ConflictException('User[${userName} can not be deleted as he has relationships with others]')
		}
		String deleteNodeJson = "{\"query\":\"MATCH (u:User{userName:'"+userName+"'}) DELETE u\"}}"
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, deleteNodeJson)
		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new RuntimeException('Delete User fail.')
		}
	}

	@Override
	Page getUsers(final int currentPageNo) {
		Page page
		String queryTotalCount = "{\"query\":\"MATCH (u:User) RETURN COUNT(*) as total\"}"
		String queryPageJson = "{\"query\":\"MATCH (u:User) RETURN u SKIP "+currentPageNo+" LIMIT "+Page.PAGE_SIZE+"\"}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryTotalCount)
		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new RuntimeException('getUsers fail.')
		}
		String respStr = getResponsePayload(response)
		int totalCount = Integer.valueOf(((ArrayList)((ArrayList)((Map)jsonSlurper.parseText(respStr)).get('data')).get(0)).get(0))
		log.debug "totalCount: {} $totalCount"
		page = new Page(currentPageNo:currentPageNo,totalCount:totalCount)
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryPageJson)
		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new RuntimeException('getUsers fail.')
		}
		Map<String,Map<String,String>> contentResultMap = neo4jSupport.getDataFromCypherStatement(getResponsePayload(response))
		log.info "contentResultMap size: {} "+contentResultMap.size()
		contentResultMap.each {k,v->
			Map usrMap = v
			page.content << new User(nodeUri:k,userName:usrMap.get('userName'),password:usrMap.get('password'))
		}
		return page
	}

	Map<String,String> doQueryUserByName(final String userName){
		String queryJson = "{\"query\":\"MATCH (u:User) WHERE u.userName = '"+userName+"' RETURN u \"}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryJson)

		if(response.getStatusInfo().statusCode != Status.OK.code){
			throw new RuntimeException('Unknown exception.')
		}
		def data = [:]
		Map<String,Map<String,String>> resultMap = [:]
		try {
			resultMap = neo4jSupport.getDataFromCypherStatement(getResponsePayload(response))
			Map.Entry<String,Map<String,String>> entry = resultMap.entrySet().iterator().next()
			data.put('nodeUri', entry.getKey())
			data.putAll(entry.getValue())
		} catch (e) {
			throw new NotFoundException("User not found by name[${userName}].")
		}
		return data
	}
}
