package nz.co.bookreviews.ds.neo4j.test;

import static org.junit.Assert.*
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.neo4j.cypher.javacompat.ExecutionEngine
import org.neo4j.graphdb.GraphDatabaseService
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LocalCypherTestContextConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class UserCypherSlowTest {

	@Resource
	GraphDatabaseService graphDatabaseService

	@Resource
	ExecutionEngine executionEngine

	@Test
	void testTopPopularBooks(){
		dump("MATCH (b:Book) <-[r:Votes]- (:User) RETURN b,r.score ORDER BY r.score DESC LIMIT 20")
	}
	
	@Test
	void testGetVotesMostCustomer(){
		dump("MATCH () <-[r:Votes]- (u:User) <-[:Has]- (p:Person) RETURN p,u,COUNT(*) as votes ORDER BY votes DESC")
	}
	
	@Test
	void testUserVotes() {
		dump("START b=node(5) MATCH (b:Book) <-[r:Votes]- (u:User) RETURN u, COUNT(*) as votes, SUM(r.score) as total")
	}

	void dump(final String query) {
		log.info "${query}"
		log.info(executionEngine.execute(query).dumpToString())
	}
}
