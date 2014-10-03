package nz.co.bookreviews.ds.neo4j

import nz.co.bookreviews.data.Author
import nz.co.bookreviews.data.Book
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.Publication

interface BookNeo4jRepositoryDS {
	Book createBook(Book book)
	Book getBookByTitle(String title)
	Page getBooks(int pageoffset)
	Page getBooksByAuthor(int pageoffset,String authorLastName,String authorFirstName)
	Page getBooksByPublisher(int pageoffset,Publication publication)
	Set<Book> getTopMostPopularBooks(int listSize)
	Page getBooksByPublication(String publisherName,int publishYear)
	Book updateBookByIsbn(String isbn,Book updateBook)
	Book updateBookWithAuthors(String isbn,Author... authors)
	Book updateBookWithPublication(String isbn,Publication publication)
	void deleteBook(String isbn)
}
