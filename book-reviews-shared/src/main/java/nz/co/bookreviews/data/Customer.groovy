package nz.co.bookreviews.data

import groovy.transform.ToString

import org.codehaus.jackson.annotate.JsonIgnore

@ToString(includeSuper=true, includeNames=true)
class Customer extends Person {
	@JsonIgnore
	boolean member
}
