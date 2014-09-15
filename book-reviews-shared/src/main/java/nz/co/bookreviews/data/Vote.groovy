package nz.co.bookreviews.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["voteId"])
class Vote {
	Long voteId
	int score
	Date createTime
	@Delegate
	Book book = new Book()
	@Delegate
	User user = new User()
}
