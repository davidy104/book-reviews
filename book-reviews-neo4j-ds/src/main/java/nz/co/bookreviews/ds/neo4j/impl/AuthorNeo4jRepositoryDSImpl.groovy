package nz.co.bookreviews.ds.neo4j.impl

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.data.Author
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.ds.neo4j.AuthorNeo4jRepositoryDS
import nz.co.bookreviews.ds.neo4j.Neo4jSupport
import nz.co.bookreviews.ds.neo4j.convert.AuthorConverter

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.sun.jersey.api.client.Client
@Service("authorNeo4jRepositoryDs")
@Slf4j
class AuthorNeo4jRepositoryDSImpl implements AuthorNeo4jRepositoryDS{
	@Resource
	Client jerseyClient

	@Resource
	Neo4jSupport neo4jSupport

	@Value('${neo4j.host:http://localhost:7474/db/data/}')
	String neo4jHttpUri

	@Resource
	JsonSlurper jsonSlurper
	
	@Resource
	AuthorConverter authorConverter

	@Override
	Author createAuthor(final Author author) {

		return null
	}

	@Override
	public Set<Author> getAuthorByName(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Author> getTopMostBooksAuthors(int listSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Author getAuthorByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page getAuthors(int pageOffset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAuthorByUri(String nodeUri) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAuthorByEmail(String email) {
		// TODO Auto-generated method stub

	}

	@Override
	public Author updateAuthor(String email, Author author) {
		// TODO Auto-generated method stub
		return null;
	}
}
