package nz.co.bookreviews.data.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_AUTHOR")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
public class AuthorEntity extends PersonEntity implements Serializable {
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bookAuthorPK.author")
	private Set<BookAuthorEntity> bookAuthors = Collections.emptySet();

	public Set<BookAuthorEntity> getBookAuthors() {
		return bookAuthors;
	}

	public void setBookAuthors(Set<BookAuthorEntity> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}

	public static Builder getBuilder(String lastName, String firstName,
			String email, Date birthDate) {
		return new Builder(lastName, firstName, email, birthDate);
	}

	public static Builder getBuilder(String lastName, String firstName,
			String email) {
		return new Builder(lastName, firstName, email);
	}

	public static class Builder {
		private AuthorEntity built;

		public Builder(String lastName, String firstName, String email) {
			built = new AuthorEntity();
			built.lastName = lastName;
			built.firstName = firstName;
			built.email = email;
		}

		public Builder(String lastName, String firstName, String email,
				Date birthDate) {
			built = new AuthorEntity();
			built.lastName = lastName;
			built.firstName = firstName;
			built.email = email;
			built.birthDate = birthDate;
		}

		public AuthorEntity build() {
			return built;
		}
	}

}
