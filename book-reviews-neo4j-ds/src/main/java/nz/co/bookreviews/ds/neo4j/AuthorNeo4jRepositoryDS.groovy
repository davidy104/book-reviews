package nz.co.bookreviews.ds.neo4j

import nz.co.bookreviews.data.Author
import nz.co.bookreviews.data.Page

interface AuthorNeo4jRepositoryDS {
	Author createAuthor(Author author)
	Set<Author> getAuthorByName(String firstName,String lastName)
	Set<Author> getTopMostBooksAuthors(int listSize)
	Author getAuthorByEmail(String email)
	Page getAuthors(int pageOffset)
	void deleteAuthorByUri(String nodeUri)
	void deleteAuthorByEmail(String email)
}
