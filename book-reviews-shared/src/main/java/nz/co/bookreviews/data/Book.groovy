package nz.co.bookreviews.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["isbn"])
class Book {
	Long bookId
	String isbn
	String title
	int pages
	String[] tags = []
	Set<Author> authors = []
	@Delegate
	Publication publication = new Publication()
	Set<Vote> votes = []
}
