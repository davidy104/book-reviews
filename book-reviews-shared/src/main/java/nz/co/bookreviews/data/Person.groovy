package nz.co.bookreviews.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["personId"])
class Person {
	Long personId
	String lastName
	String firstName
	Date birthDate
	User user
}
