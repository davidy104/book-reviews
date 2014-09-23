package nz.co.bookreviews.ds.test;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.data.User
import nz.co.bookreviews.ds.neo4j.UserDS

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

	@Test
	public void test() {
		User add = userNeo4jRepositoryDs.createUser("dav", "654321")
		log.info 'add user: {} '+add
	}
}
