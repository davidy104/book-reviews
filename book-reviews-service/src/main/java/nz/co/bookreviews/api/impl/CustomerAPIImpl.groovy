package nz.co.bookreviews.api.impl;

import groovy.util.logging.Slf4j

import javax.annotation.Resource
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

import nz.co.bookreviews.api.CustomerAPI
import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.ds.jpa.CustomerDS

import org.springframework.stereotype.Service
@Service
@Path("/customer/v1")
@Slf4j
class CustomerAPIImpl implements CustomerAPI {

	@Resource
	CustomerDS customerJpaRepositoryDs

	@Override
	@Path("/list")
	@GET
	@Produces("application/json")
	Response getAllCustomers() {
		Set<Customer> customers = customerJpaRepositoryDs.getAllCustomers()
		log.info "customers size:{} "+customers.size()
		customers.each{ log.info "customer: {} $it" }
		return Response.status(Status.OK)
		.entity(customers).build();
	}
}
