package nz.co.bookreviews.ds.neo4j

import nz.co.bookreviews.data.Book
import nz.co.bookreviews.data.Page

interface BookNeo4jRepositoryDS {
	Book createBook(Book book)
	Book getBookByTitle(String title)
	Page getBooks(int pageoffset)
}
