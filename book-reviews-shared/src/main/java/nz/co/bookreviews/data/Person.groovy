package nz.co.bookreviews.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["email"])
class Person implements Serializable {
	Long personId
	String lastName
	String firstName
	Date birthDate
	String email
	//	@JsonIgnore
	User user
	String nodeUri
}
