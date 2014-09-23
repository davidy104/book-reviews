package nz.co.bookreviews.ds.neo4j

import groovy.json.JsonSlurper;

class Neo4jJsonConvertUtil {

	static String getNodeUriFromTransStatementsResponse(final String response,final int position){
		JsonSlurper jsonSlurper = new JsonSlurper()
		return ((Map)((ArrayList)((Map)((ArrayList)((Map)((ArrayList)((Map)jsonSlurper.parseText(response)).get('results')).get(position)).get('data')).get(0)).get('rest')).get(0)).get('self')
	}
}
