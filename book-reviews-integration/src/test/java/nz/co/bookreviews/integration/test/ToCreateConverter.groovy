package nz.co.bookreviews.integration.test

import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.Expression

class ToCreateConverter implements Expression {

	@Override
	<T> T evaluate(Exchange exchange, Class<T> type) {
		CamelContext context = exchange.getContext()
		exchange.getIn().getBody()
		return null
	}
}
