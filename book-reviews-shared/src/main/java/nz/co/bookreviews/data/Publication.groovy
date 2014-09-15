package nz.co.bookreviews.data

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class Publication {
	int year
	@Delegate
	Publisher publisher = new Publisher()
}
