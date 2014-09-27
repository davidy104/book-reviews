package nz.co.bookreviews.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeSuper=true, includeNames=true)
class Author extends Person{
	Set<Book> books = []
}
