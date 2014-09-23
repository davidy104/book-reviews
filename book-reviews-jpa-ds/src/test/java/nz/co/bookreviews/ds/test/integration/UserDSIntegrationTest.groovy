package nz.co.bookreviews.ds.test.integration;

import static org.junit.Assert.assertNotNull
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.UserDS
import nz.co.bookreviews.ds.test.JpaDsTestContextConfiguration

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
public class UserDSIntegrationTest {

	@Resource
	UserDS userJpaRepositoryDs

	@Test
	void testCreate() {
		User added = userJpaRepositoryDs.createUser("Test01","123456");
		assertNotNull(added)
		log.info("added:{} $added")
	}

	@Test
	void testFindByName(){
		User found = userJpaRepositoryDs.getUserByName("jordan")
		assertNotNull(found)
		log.info("found:{} $found")
	}

	@Test(expected=NotFoundException.class)
	void testFindByNameNotFound(){
		User found = userJpaRepositoryDs.getUserByName("DAVVV")
	}

	@Test
	void testUpdateUser(){
		User update = new User(userName:"UpdateTest")
		update = userJpaRepositoryDs.updateUser(1L, update)
		log.info("update:{} $update")
	}
}
