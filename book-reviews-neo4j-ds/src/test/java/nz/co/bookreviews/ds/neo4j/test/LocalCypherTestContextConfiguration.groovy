package nz.co.bookreviews.ds.neo4j.test

import java.nio.file.Paths

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

import org.neo4j.cypher.javacompat.ExecutionEngine
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.graphdb.Transaction
import org.neo4j.graphdb.factory.GraphDatabaseFactory
import org.neo4j.graphdb.factory.GraphDatabaseSettings
import org.neo4j.kernel.impl.util.FileUtils
import org.neo4j.kernel.impl.util.StringLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
@Configuration
@ComponentScan("nz.co.bookreviews.ds.neo4j")
class LocalCypherTestContextConfiguration {
	static final String DB_PATH = "database/test"
	static String TEST_SCRIPT="/books_create.txt"

	@Autowired
	GraphDatabaseService graphDatabaseService
	@Autowired
	ExecutionEngine executionEngine

	@Bean
	GraphDatabaseService graphDatabaseService() {
		GraphDatabaseService db = new GraphDatabaseFactory()
				.newEmbeddedDatabaseBuilder(DB_PATH)
				.setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size,
				"20M").newGraphDatabase()
		return db
	}

	@Bean
	ExecutionEngine executionEngine() {
		return new ExecutionEngine(graphDatabaseService(), StringLogger.SYSTEM)
	}

	@PostConstruct
	void initialTestData(){
		Transaction tx
		try {
			tx = graphDatabaseService.beginTx()
			String script = new String(
					java.nio.file.Files.readAllBytes(Paths.get(UserCypherSlowTest.class
					.getResource(TEST_SCRIPT).toURI())), "UTF8")
			executionEngine.execute(script)
			def userVote = "MATCH (b:Book) MATCH (u:User{userName:'david'}) create (b) <-[:Votes{score: round(rand()*4) + 2 }]- (u)"
			executionEngine.execute(userVote)
			userVote = "MATCH (b:Book) MATCH (u:User{userName:'mike'}) create (b) <-[:Votes{score: round(rand()*5) + 2 }]- (u)"
			executionEngine.execute(userVote)
			userVote = "MATCH (b:Book) MATCH (u:User{userName:'john'}) create (b) <-[:Votes{score: round(rand()*6) + 2 }]- (u)"
			executionEngine.execute(userVote)
			userVote = "MATCH (b:Book) MATCH (u:User{userName:'phil'}) create (b) <-[:Votes{score: round(rand()*3) + 2 }]- (u)"
			executionEngine.execute(userVote)
			userVote = "MATCH (b:Book) MATCH (u:User{userName:'bruce'}) create (b) <-[:Votes{score: round(rand()*1) + 2 }]- (u)"
			executionEngine.execute(userVote)
			def customers = "MATCH (u:User{userName:'david'}) WITH u CREATE (u) <-[:Has]-(:Person{name:'Franz Kafka'})"
			def bookWithoutVotes = "CREATE (:Book {title:\"The Art of Prolog\",tags:[\"prolog\"]})"
			executionEngine.execute(bookWithoutVotes)
			tx.success()
		}catch(e){
			println "initial error: {} "+e.printStackTrace()
		}
		finally{
			tx.close()
		}
	}

	@PreDestroy
	void cleanUpTestData(){
		graphDatabaseService.shutdown()
		File dbDirectory = new File(DB_PATH)
		boolean exists = dbDirectory.exists()
		if (exists) {
			FileUtils.deleteRecursively(dbDirectory)
		}
	}
}
