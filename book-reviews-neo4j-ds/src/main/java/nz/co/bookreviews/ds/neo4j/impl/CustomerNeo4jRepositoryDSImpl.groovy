package nz.co.bookreviews.ds.neo4j.impl

import static nz.co.bookreviews.util.JerseyClientUtil.getResponsePayload
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.neo4j.CustomerNeo4jRepositoryDS
import nz.co.bookreviews.ds.neo4j.Neo4jSupport
import nz.co.bookreviews.ds.neo4j.UserNeo4jRepositoryDS

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
@Service("customerNeo4jRepositoryDs")
@Slf4j
class CustomerNeo4jRepositoryDSImpl implements CustomerNeo4jRepositoryDS{

	@Resource
	Client jerseyClient

	@Resource
	Neo4jSupport neo4jSupport

	@Value('${neo4j.host:http://localhost:7474/db/data/}')
	String neo4jHttpUri

	@Resource
	JsonSlurper jsonSlurper

	@Resource
	UserNeo4jRepositoryDS userNeo4jRepositoryDs

	@Resource
	CustomerConverter customerConverter

	/**
	 * 
	 */
	@Override
	Customer createCustomer(Customer customer,final User newUser) {
		boolean createNewUser = false
		String userNodeUri
		if(newUser){
			User addUser = userNeo4jRepositoryDs.createUser(newUser.userName, newUser.password)
			userNodeUri = addUser.nodeUri
			createNewUser = true
		}
		String addCustomerStr = customerConverter.convertTo(customer,'create')
		String jsonBody = "{\"statements\":[{\"statement\":\"CREATE (p:Person{"+addCustomerStr+"}) RETURN p\",\"resultDataContents\":[\"REST\"]}"
		String jsonEnd ="]}"
		if(createNewUser){
			String userRelationshipJson = ",{\"statement\":\"MATCH (p:Person {email: '"+customer.email+"'}), (u:User {userName:'"+newUser.userName+"'}) CREATE (u)<-[:Has]-(p)\"}"
			jsonBody = jsonBody +userRelationshipJson + jsonEnd
		} else {
			jsonBody = jsonBody + jsonEnd
		}
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("transaction/commit")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody)
		if(response.getStatus() != Status.OK.code){
			if(createNewUser){
				//rollback created User
				neo4jSupport.deleteNodeByUri(userNodeUri)
			}
			throw new RuntimeException('Customer create failed.')
		}
		String responseStr = getResponsePayload(response)
		log.info "responseStr: {} ${responseStr}"

		String self = neo4jSupport.getNodeUriFromTransStatementsResponse(responseStr,0)
		log.debug 'self:{} $self'
		String uniqueNodeReqBody = "{\"value\" : \""+customer.email+"\",\"uri\" : \""+self+"\",\"key\" : \"email\"}"
		webResource = jerseyClient.resource(neo4jHttpUri)
				.path("index/node/favorites").queryParam("uniqueness", "create_or_fail")
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, uniqueNodeReqBody)

		if(response.getStatus() != Status.CREATED.code){
			//TODO we need delete created Customer and user if created
			throw new RuntimeException('Customer create failed.')
		}
		customer.nodeUri = self
		return customer
	}



	@Override
	Customer assignUserToCustomer(final String customerNodeUri,
			final String userNodeUri) {
		String body ="{\"to\" : \""+userNodeUri+"\",\"type\" : \"Has\"}"
		WebResource webResource = jerseyClient.resource(customerNodeUri).path('/relationships')
		ClientResponse response =  webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class,body)
		if(response.getStatus() != Status.CREATED.code){
			throw new RuntimeException('assignUserToCustomer failed.')
		}
		User user= userNeo4jRepositoryDs.getUserByUri(userNodeUri)
		Customer customer = this.getCustomerByUri(customerNodeUri)
		customer.user = user
		return customer
	}



	@Override
	Customer getCustomerByEmail(final String email) {
		Customer customer
		User user
		Map<String,Map<String,String>> resultMap
		String queryJson ="{\"query\":\"MATCH (p:Person) WHERE p.email = '"+email+"' OPTIONAL MATCH (p) -[:Has]-> (u) RETURN p, u\"}"

		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryJson)

		if(response.getStatus() != Status.OK.code){
			throw new RuntimeException('Unknown exception.')
		}
		String responStr = getResponsePayload(response)
		log.info "getCustomerByEmail response: {} ${responStr}"
		try {
			resultMap = this.neo4jSupport.getDataFromCypherStatement(responStr)
			int index =0
			resultMap.each {key,value->
				Map valueMap = value
				if(valueMap && !valueMap.isEmpty()){
					if(index == 0){
						customer = customerConverter.convertFrom(key,valueMap)
					} else {
						user = new User(nodeUri:key,userName:valueMap.get('userName'),password:valueMap.get('password'))
						customer.user = user
					}
				}
				index++
			}
		} catch (e) {
			if(e instanceof RuntimeException && e.message == 'Data Not found.'){
				throw new NotFoundException("Customer not found by email[${email}].")
			}else {
				throw e
			}
		}
		return customer
	}



	@Override
	Page getAllCustomers(int pageOffset) {
		Page page
		long totalCount = 0
		String queryTotalCount = "{\"query\":\"MATCH (p:Person) WHERE HAS(p.member) RETURN COUNT(*) as total\"}"
		String queryPageJson = "{\"query\":\"MATCH (p:Person) WHERE HAS(p.member) RETURN p SKIP "+pageOffset+" LIMIT "+Page.PAGE_SIZE+"\"}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryTotalCount)
		if(response.getStatus() != Status.OK.code){
			throw new RuntimeException('getCustomers fail.')
		}
		String respStr = getResponsePayload(response)
		Map rMap = (Map)jsonSlurper.parseText(respStr)
		def dataResult = rMap.get('data')

		totalCount = Long.valueOf(((ArrayList)((ArrayList)dataResult).get(0)).get(0))
		log.debug "totalCount: {} $totalCount"
		page = new Page(currentPageNo:pageOffset+1,totalCount:totalCount)
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryPageJson)
		if(response.getStatus() != Status.OK.code){
			throw new RuntimeException('getCustomers fail.')
		}
		try {
			Map<String,Map<String,String>> contentResultMap = neo4jSupport.getDataFromCypherStatement(getResponsePayload(response))
			log.info "contentResultMap size: {} "+contentResultMap.size()
			contentResultMap.each {k,v->
				Map valueMap = v
				page.content << customerConverter.convertFrom(k,valueMap)
			}
		} catch (e) {
		}
		return page
	}

	@Override
	Customer updateCustomer(String email, Customer updatedCustomer) {
		Map resultMap
		String uptCustomerStr = customerConverter.convertTo(updatedCustomer,'update')
		String updateJson = "{\"query\":\"MATCH (p:Person {email: {email}}) SET p = { props } RETURN p\",\"params\":{\"email\":\""+email+"\",\"props\":{"+uptCustomerStr+"}}}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, updateJson)
		if(response.getStatus() != Status.OK.code){
			throw new RuntimeException('Customer update fail.')
		}
		try {
			resultMap =neo4jSupport.getSingleResultFromCypherStatement(getResponsePayload(response))
		} catch (Exception e) {
			if(e instanceof RuntimeException && e.message == 'Data Not found.'){
				throw new NotFoundException("Customer not found by email[${email}].")
			}else {
				throw e
			}
		}
		String nodeUril = resultMap.get('nodeUri')
		return customerConverter.convertFrom(nodeUril, resultMap)
	}

	@Override
	Customer getCustomerByUri(String custNodeUri) {
		Map resultMap = neo4jSupport.getNodeByUri(custNodeUri)
		String uri = resultMap.get('self')
		Map dataMap = (Map)resultMap.get('data')
		return customerConverter.convertFrom(uri, dataMap)
	}

	@Override
	List getTopMostVoteCustomers(int listSize) {
		return null
	}

	@Override
	void deleteCustomerByUri(final String custNodeUri) {
		neo4jSupport.deleteNodeByUri(custNodeUri)
	}

	@Override
	void deleteCustomerByEmail(final String email) {
		String queryJson ="{\"query\":\"MATCH (p:Person) WHERE p.email = '"+email+"' OPTIONAL MATCH (p) -[r:Has]-> (u) DELETE p,r,u\"}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryJson)

		if(response.getStatus() != Status.NO_CONTENT.code){
			throw new RuntimeException('Unknown exception.')
		}
	}
}
