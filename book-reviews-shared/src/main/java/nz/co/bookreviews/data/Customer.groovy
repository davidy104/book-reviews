package nz.co.bookreviews.data

import groovy.transform.ToString

import org.codehaus.jackson.annotate.JsonAutoDetect
import org.codehaus.jackson.annotate.JsonIgnore

@ToString(includeSuper=true, includeNames=true)
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.ANY)
class Customer extends Person {
	boolean member
}
