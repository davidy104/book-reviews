

package nz.co.bookreviews.ds.neo4j.test;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

import javax.annotation.Resource

import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.neo4j.CustomerNeo4jRepositoryDS
import nz.co.bookreviews.ds.neo4j.UserNeo4jRepositoryDS

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Neo4jDsTestContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class CustomerNeo4jDSIntegrationTest {

	@Resource
	CustomerNeo4jRepositoryDS customerNeo4jRepositoryDs

	@Resource
	UserNeo4jRepositoryDS userNeo4jRepositoryDs

	def testCustomerNodeUris = []

	@Before
	void initial(){
		Customer initialCustomer = new Customer(lastName:'Jordan',firstName:'Michael',member:true,birthDate:new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01"),email:'jordan@gmail.com')
		initialCustomer = customerNeo4jRepositoryDs.createCustomer(initialCustomer, null)
		log.info "initialCustomer: {} ${initialCustomer}"
		testCustomerNodeUris << initialCustomer.nodeUri
	}

	@After
	void tearDown(){
		testCustomerNodeUris.each { customerNeo4jRepositoryDs.deleteCustomerByUri(it) }
	}

	@Test
	void testUpdateCustomer(){
		try {
			Customer updateCustomer = customerNeo4jRepositoryDs.getCustomerByEmail("jordan@gmail.com")
			log.info "updateCustomer: {} ${updateCustomer}"
			updateCustomer.email="update@gmail.com"
			updateCustomer.lastName='UpdateLast'
			updateCustomer.firstName='UpdateFirst'
			updateCustomer = customerNeo4jRepositoryDs.updateCustomer("jordan@gmail.com", updateCustomer)
			log.info "updateCustomer: {} ${updateCustomer}"
		} catch (e) {
			e.printStackTrace()
		}
	}

	@Test
	void testPagingCustomers(){
		Page page = customerNeo4jRepositoryDs.getAllCustomers(1)
		List<Customer> customers = page.getContent()
		long totalCount = page.getTotalCount()
		log.info "totalCount:{} ${totalCount}"
		customers.each { log.info "customer:{} ${it}" }
	}

	@Test
	void testCreateCustomerWithUser(){
		try {
			User user = new User(userName:"dav",password:"654321")
			Customer customer = new Customer(lastName:'Li',firstName:'John',member:false,birthDate:new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01"),email:'john@gmail.com')
			customerNeo4jRepositoryDs.createCustomer(customer, user)
			customer = customerNeo4jRepositoryDs.getCustomerByEmail("john@gmail.com")
			log.info "get customer:{} ${customer}"
			log.info "user: {} "+customer.user
			customerNeo4jRepositoryDs.deleteCustomerByEmail("john@gmail.com")
		} catch (e) {
			e.printStackTrace()
		}
	}

	@Test
	void testGetCustomerByEmail() {
		try {
			Customer found = customerNeo4jRepositoryDs.getCustomerByEmail("jordan@gmail.com")
			log.info "found: {} ${found}"
		} catch (e) {
			e.printStackTrace()
		}
	}
}
