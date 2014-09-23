package nz.co.bookreviews.ds

import static nz.co.bookreviews.ds.Neo4jJsonConvertUtil.getNodeUriFromTransStatementsResponse
import static nz.co.bookreviews.util.JerseyClientUtil.getResponsePayload
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.User

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
		//		Map restMap = (Map)((ArrayList)((Map)((ArrayList)((Map)((ArrayList)((Map)jsonSlurper.parseText(respStr)).get('results')).get(1)).get('data')).get(0)).get('rest')).get(0)
		String self = getNodeUriFromTransStatementsResponse(respStr,1)
		log.debug 'self:{} $self'
		Long id = Long.valueOf(self.substring(self.lastIndexOf('/')+1,self.length()))
		log.debug 'id:{} $id'
		return new User(userId:id,userName:userName,password:password)
	}

	@Override
	public User loginUser(String userName, String password) {

		return null;
	}

	//http://localhost:7474/db/data/node/14800000
	@Override
	public User getUserById(final Long userId) throws NotFoundException {
		WebResource webResource = jerseyClient.resource(neo4jHttpUri).path("node/").path(userId)
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		String respStr = getResponsePayload(response)
		return null;
	}

	@Override
	public User getUserByName(String userName) throws NotFoundException {

		return null;
	}

	@Override
	public User updateUser(Long userId, User updatedUser)
	throws NotFoundException {

		return null;
	}
}
