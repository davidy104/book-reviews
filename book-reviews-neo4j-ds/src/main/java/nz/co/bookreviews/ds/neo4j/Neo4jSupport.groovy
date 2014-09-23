package nz.co.bookreviews.ds.neo4j

import java.util.Map;

import groovy.json.JsonSlurper

import javax.annotation.Resource
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response.Status

import org.springframework.stereotype.Component

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource

import static nz.co.bookreviews.util.JerseyClientUtil.getResponsePayload
@Component
class Neo4jSupport {
	def jsonSlurper = new JsonSlurper()
	@Resource
	Client jerseyClient

	Map getNodeByUri(final String uri){
		WebResource webResource = jerseyClient.resource(uri)
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class)
		if (response.getStatusInfo().statusCode != Status.OK.code) {
			throw new RuntimeException('getNodeByUri[${uri}] fail.')
		}
		String respStr = getResponsePayload(response)
		return (Map)jsonSlurper.parseText(respStr)
	}

	void deleteNodeByUri(final String uri){
		WebResource webResource = jerseyClient.resource(uri)
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class)
		if (response.getStatusInfo().statusCode == Status.CONFLICT.code) {
			throw new RuntimeException('Relationship conflict.')
		} else if(response.getStatusInfo().statusCode != Status.NO_CONTENT.code){
			throw new RuntimeException('Unknown error.')
		}
	}

	String getNodeUriFromTransStatementsResponse(final String response,final int position){
		return ((Map)((ArrayList)((Map)((ArrayList)((Map)((ArrayList)((Map)jsonSlurper.parseText(response)).get('results')).get(position)).get('data')).get(0)).get('rest')).get(0)).get('self')
	}


	Map<String,String> getDataFromCypherStatement(final String response){
		def result = [:]
		def resultDataMap = [:]
		JsonSlurper jsonSlurper = new JsonSlurper()
		Map jsonResult = (Map) jsonSlurper.parseText(response)
		ArrayList datajson = (ArrayList)jsonResult.get("data")
		if(datajson.isEmpty()){
			throw new RuntimeException('Data Not found.')
		}
		if(datajson){
			ArrayList innerData = (ArrayList)datajson.get(0)
			if(innerData){
				innerData.each {
					Map datamap = (Map)it
					resultDataMap = (Map)datamap['data']
					result.putAll(resultDataMap)
					result.put('nodeUri' , datamap['self'])
				}
			}
		}
		return result
	}
}
