package nz.co.bookreviews.ds.neo4j.test;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.AuthenticationException
import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.neo4j.UserDS

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
class UserNeo4jDSIntegrationTest {

	@Resource
	UserDS userNeo4jRepositoryDs
	def testUserNames = []


	@Before
	void initial(){
		User initialUser = userNeo4jRepositoryDs.createUser("dav", "654321")
		testUserNames << initialUser.userName
	}

	@After
	void tearDown(){
		testUserNames.each { userNeo4jRepositoryDs.deleteUserByName(it) }
	}

	@Test
	public void testCreateUser() {
		User add = userNeo4jRepositoryDs.createUser("mike", "123")
		assertNotNull(add)
		log.info 'add user: {} '+add
		assertEquals('mike',add.userName)
		testUserNames << add.userName
	}

	@Test
	public void testQueryUserByName(){
		User found = userNeo4jRepositoryDs.getUserByName("dav")
		assertNotNull(found)
		assertEquals('654321',found.password)
		log.info "found: {} $found"
	}

	@Test(expected=NotFoundException.class)
	void testQueryNonexistUserByName(){
		userNeo4jRepositoryDs.getUserByName("NOT FOUND USER")
	}

	@Test
	void testRegistedUserLogin(){
		User loginUser = userNeo4jRepositoryDs.loginUser("dav", "654321")
		assertNotNull(loginUser)
		assertEquals('654321',loginUser.password)
	}

	@Test(expected=AuthenticationException.class)
	void testNotRegistedUserLogin(){
		userNeo4jRepositoryDs.loginUser("dav", "9999900")
	}

	@Test
	void testPagingUsers(){
		testCreateUser()
		Page page = userNeo4jRepositoryDs.getUsers(0)
		log.info "page: {} $page"
	}

	@Test
	void testUpdateUserByUserName(){
		User add = userNeo4jRepositoryDs.createUser("mike", "123")
		assertNotNull(add)
		User updatedUser = new User(userName:'jordan',password:'456')
		updatedUser = userNeo4jRepositoryDs.updateUserByUserName("mike", updatedUser)
		assertNotNull(updatedUser)
		assertEquals('456',updatedUser.password)
		assertEquals('jordan',updatedUser.userName)
		log.info "updatedUser: {} $updatedUser"
		testUserNames << updatedUser.userName
	}
}
