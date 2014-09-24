package nz.co.bookreviews.ds.neo4j.impl

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.neo4j.CustomerDS
import nz.co.bookreviews.ds.neo4j.Neo4jSupport
import nz.co.bookreviews.ds.neo4j.UserDS

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import static nz.co.bookreviews.util.JerseyClientUtil.getResponsePayload
@Service("customerNeo4jRepositoryDs")
@Slf4j
class CustomerNeo4jRepositoryDSImpl implements CustomerDS{

	@Resource
	Client jerseyClient
	@Resource
	Neo4jSupport neo4jSupport

	@Value('${neo4j.host:http://localhost:7474/db/data/}')
	String neo4jHttpUri

	@Resource
	JsonSlurper jsonSlurper

	@Resource
	UserDS userNeo4jRepositoryDs

	@Override
	Customer createCustomer(Customer customer,final User newUser) {
		boolean createNewUser = false
		String userNodeUri
		if(newUser){
			User addUser = userNeo4jRepositoryDs.createUser(newUser.userName, newUser.password)
			userNodeUri = addUser.nodeUri
			createNewUser = true
		}
		int memeber = customer.member?1:0
		String birthDateStr = new SimpleDateFormat("yyyy-MM-dd").parse(customer.birthDate)
		String jsonBody = "{\"statements\":[{\"statement\":\"CREATE (p:Person{firstName:'"+customer.firstName+"',lastName:'"+customer.lastName+"',birthDate:'"+birthDateStr+"',email:'"+customer.email+"',member:'"+member+"'}) RETURN p\"}"
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
		if(response.getStatusInfo().statusCode != Status.OK.code){
			if(createNewUser){
				//rollback created User
				neo4jSupport.deleteNodeByUri(userNodeUri)
			}
			throw new RuntimeException('Customer create failed.')
		}
		String self = neo4jSupport.getNodeUriFromTransStatementsResponse(getResponsePayload(response),0)
		log.debug 'self:{} $self'
		String uniqueNodeReqBody = "{\"value\" : \""+customer.email+"\",\"uri\" : \""+self+"\",\"key\" : \"email\"}"
		webResource = jerseyClient.resource(neo4jHttpUri)
				.path("index/node/favorites").queryParam("uniqueness", "create_or_fail")
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, uniqueNodeReqBody)

		if(response.getStatusInfo().statusCode != Status.CREATED.code){
			//TODO we need delete created Customer and user if created
			throw new RuntimeException('Customer create failed.')
		}
		customer.nodeUri = self
		return customer
	}



	@Override
	Customer assignUserToCustomer(final String customerNodeUri,
			final String userNodeUri) {

		return null
	}



	@Override
	public Customer getCustomerByEmail(String email) {
		return null;
	}



	@Override
	public Page getAllCustomers(int pageOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer updateCustomer(String custNodeUri, Customer updatedCustomer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer getCustomerByUri(String custNodeUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getTopMostVoteCustomers(int listSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCustomerByUri(String custNodeUri) {
		// TODO Auto-generated method stub

	}
}
