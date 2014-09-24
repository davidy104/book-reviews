package nz.co.bookreviews.data

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class Page {
	int totalCount
	int currentPageNo
	List content =[]
	static int PAGE_SIZE=5
}
