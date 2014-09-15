package nz.co.bookreviews.data.entity;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_BOOK_AUTHOR")
@AssociationOverrides({
		@AssociationOverride(name = "bookAuthorPK.book", joinColumns = @JoinColumn(name = "BOOK_ID")),
		@AssociationOverride(name = "bookAuthorPK.author", joinColumns = @JoinColumn(name = "PERSON_ID")) })
public class BookAuthorEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOK_AUTHOR_ID", insertable = false, updatable = false)
	private Long bookAuthorId;

	@Embedded
	private BookAuthorPK bookAuthorPK = new BookAuthorPK();

	public BookAuthorPK getBookAuthorPK() {
		return bookAuthorPK;
	}

	@Transient
	public BookEntity getBook() {
		return getBookAuthorPK().getBook();
	}

	public void setBook(BookEntity book) {
		getBookAuthorPK().setBook(book);
	}

	@Transient
	public AuthorEntity getAuthor() {
		return getBookAuthorPK().getAuthor();
	}

	public void setAuthor(final AuthorEntity author) {
		getBookAuthorPK().setAuthor(author);
	}

	public static Builder getBuilder(BookEntity book, AuthorEntity author) {
		return new Builder(book, author);
	}

	public static class Builder {
		private BookAuthorEntity built;

		public Builder(BookEntity book, AuthorEntity author) {
			built = new BookAuthorEntity();
			built.setBook(book);
			built.setAuthor(author);
		}

		public BookAuthorEntity build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.bookAuthorPK,
				((BookAuthorEntity) obj).bookAuthorPK).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.bookAuthorPK).toHashCode();
	}
}
