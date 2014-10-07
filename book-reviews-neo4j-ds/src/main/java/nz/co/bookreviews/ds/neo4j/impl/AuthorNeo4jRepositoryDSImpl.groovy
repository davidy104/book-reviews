package nz.co.bookreviews.ds.neo4j.impl

import static nz.co.bookreviews.util.JerseyClientUtil.getResponsePayload
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import nz.co.bookreviews.data.Author
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.ds.neo4j.AuthorNeo4jRepositoryDS
import nz.co.bookreviews.ds.neo4j.Neo4jSupport
import nz.co.bookreviews.ds.neo4j.convert.AuthorConverter

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
@Service("authorNeo4jRepositoryDs")
@Slf4j
class AuthorNeo4jRepositoryDSImpl implements AuthorNeo4jRepositoryDS{
	@Resource
	Client jerseyClient

	@Resource
	Neo4jSupport neo4jSupport

	@Value('${neo4j.host:http://localhost:7474/db/data/}')
	String neo4jHttpUri

	@Resource
	JsonSlurper jsonSlurper

	@Resource
	AuthorConverter authorConverter

	/**
	 * create node:
	 * {"statements\":[{"statement":"CREATE (p:Person{"+addAuthorStr+"}) RETURN p","resultDataContents":["REST"]}]}
	 * unique node:
	 * {"value" : "","uri" : "","key" : "authorNo"}
	 */
	@Override
	Author createAuthor(Author author) {
		author.authorNo = "Author_"+UUID.randomUUID().toString()
		String addAuthorStr = authorConverter.convertTo(author,'create')
		String jsonBody = "{\"statements\":[{\"statement\":\"CREATE (p:Person{"+addAuthorStr+"}) RETURN p\",\"resultDataContents\":[\"REST\"]}]}"

		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("transaction/commit")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody)
		if(response.getStatus() != Status.OK.code){
			throw new RuntimeException('Author create failed.')
		}
		String responseStr = getResponsePayload(response)
		log.info "responseStr: {} ${responseStr}"

		String self = neo4jSupport.getNodeUriFromTransStatementsResponse(responseStr,0)
		log.debug 'self:{} $self'
		String uniqueNodeReqBody = "{\"value\" : \""+author.authorNo+"\",\"uri\" : \""+self+"\",\"key\" : \"authorNo\"}"
		webResource = jerseyClient.resource(neo4jHttpUri)
				.path("index/node/favorites").queryParam("uniqueness", "create_or_fail")
		response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, uniqueNodeReqBody)

		if(response.getStatus() != Status.CREATED.code){
			//TODO we need delete created Customer and user if created
			throw new RuntimeException('Author create failed.')
		}
		author.nodeUri = self
		return author
	}

	@Override
	Set<Author> getAuthorByName(final String firstName, final String lastName) {
		Set<Author> resultSet = []
		Map<String,Map<String,String>> resultMap
		String queryJson ="{\"query\":\"MATCH (a:Author) WHERE a.firstName = '"+firstName+"' AND a.lastName = '"+lastName+"' RETURN a\"}"
		WebResource webResource = jerseyClient.resource(neo4jHttpUri)
				.path("cypher")
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, queryJson)

		if(response.getStatus() != Status.OK.code){
			throw new RuntimeException('Unknown exception.')
		}
		String responStr = getResponsePayload(response)
		log.info "getAuthorByName response: {} ${responStr}"
		try {
			resultMap = this.neo4jSupport.getDataFromCypherStatement(responStr)
			resultMap.each {key,value->
				Map valueMap = value
				if(valueMap && !valueMap.isEmpty()){
					resultSet << authorConverter.convertFrom(key,valueMap)
				}
			}
		} catch (e) {
		}
		return resultSet
	}

	@Override
	public Set<Author> getTopMostBooksAuthors(int listSize) {
		
		return null;
	}

	@Override
	public Author getAuthorByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getAuthors(int pageOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAuthorByUri(String nodeUri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAuthorByEmail(String email) {
		// TODO Auto-generated method stub

	}

	@Override
	public Author updateAuthor(String email, Author author) {
		// TODO Auto-generated method stub
		return null;
	}
}
