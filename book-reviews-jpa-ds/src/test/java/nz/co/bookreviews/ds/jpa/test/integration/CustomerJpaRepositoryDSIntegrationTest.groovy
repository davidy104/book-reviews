package nz.co.bookreviews.ds.jpa.test.integration;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.ds.jpa.CustomerDS
import nz.co.bookreviews.ds.jpa.test.JpaDsTestContextConfiguration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = [ JpaDsTestContextConfiguration.class ])
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class CustomerJpaRepositoryDSIntegrationTest {

	@Resource
	CustomerDS customerJpaRepositoryDs

	@Test
	void testGetAll() {
		Set<Customer> customers = customerJpaRepositoryDs.getAllCustomers()
		log.info "total size: {} "+customers.size()
		customers.each{ 
			log.info "customer: {} $it" 
			}
	}
}
