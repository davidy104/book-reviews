package nz.co.bookreviews.integration.test

import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder

class Neo4jHttpRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:start")
				.routeId('Neo4jHttpClient')
				.transform()
	}
}
