package nz.co.bookreviews.data

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class Page {
	long totalCount
	int totalPages
	int currentPageNo
	List content =[]
	static int PAGE_SIZE=5
}
