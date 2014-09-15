package nz.co.bookreviews.data.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import nz.co.bookreviews.data.entity.CustomerEntity;
import nz.co.bookreviews.data.entity.UserEntity;
import nz.co.bookreviews.data.repository.CustomerRepository;
import nz.co.bookreviews.data.repository.UserRepository;
import nz.co.bookreviews.data.test.util.RepositoryTestContextConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.Projections;

import static nz.co.bookreviews.data.entity.QCustomerEntity.customerEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerRepositorySlowTest {

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CustomerRepositorySlowTest.class);

	@Test
	public void testFindAll() {
		// test initial customer data
		List<CustomerEntity> customers = customerRepository.findAll();
		LOGGER.info("size:{} ", customers.size());
		assertEquals(customers.size(), 2);
	}

	@Test
	@Transactional(readOnly = true)
	public void testFindById() {
		CustomerEntity found = customerRepository.findOne(1L);
		assertNotNull(found);
		LOGGER.info("found: {} ", found);
		LOGGER.info("customer user:{} ", found.getUser());
	}

	@Test
	@Transactional(readOnly = true)
	public void testFindByName() {
		FactoryExpression<CustomerEntity> factoryExpression = Projections.bean(
				CustomerEntity.class, customerEntity.lastName,
				customerEntity.user, customerEntity.birthDate);
		Predicate predicate = customerEntity.firstName.eq("Jordan").and(
				customerEntity.lastName.eq("Mike"));
		CustomerEntity found = customerRepository.findOne(factoryExpression,
				predicate);
		assertNotNull(found);
		LOGGER.info("found: {} ", found);
	}

	@Test
	@Transactional
	public void testCascadeDelete() throws Exception {
		CustomerEntity customer = customerRepository.findOne(1L);
		assertNotNull(customer);
		LOGGER.info("customer: {} ", customer);
		UserEntity user = customer.getUser();
		LOGGER.info("before customer remove, user: {} ", user);
		Long userId = user.getUserId();
		customerRepository.delete(customer);
		// we need to flush after remove, otherwise cascade delete user not
		// working
		customerRepository.flush();

		user = userRepository.findOne(userId);
		LOGGER.info("after customer remove, user: {} ", user);
		assertNull(user);

	}

	@Test
	@Transactional(readOnly = true)
	public void testMethodNameQuery() {
		List<CustomerEntity> customers = customerRepository
				.findByFirstNameStartingWithOrLastNameStartingWith("Jord",
						"Mike");
		LOGGER.info("firstone: {} ", customers.get(0));
	}

	@Test
	// @Transactional(readOnly = true)
	public void testQueryAnnotation() {
		List<CustomerEntity> customers = customerRepository
				.findCustomers("Jordan");
		LOGGER.info("find one: {} ", customers.get(0));
		// LOGGER.info("user:{} ",customers.get(0).getUser());
	}

}
