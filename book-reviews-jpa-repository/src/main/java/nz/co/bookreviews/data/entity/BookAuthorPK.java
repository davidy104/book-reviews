package nz.co.bookreviews.data.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Embeddable
public class BookAuthorPK implements Serializable {
	@ManyToOne(fetch = FetchType.LAZY)
	private BookEntity book;

	@ManyToOne(fetch = FetchType.LAZY)
	private AuthorEntity author;

	public BookEntity getBook() {
		return book;
	}

	public void setBook(BookEntity book) {
		this.book = book;
	}

	public AuthorEntity getAuthor() {
		return author;
	}

	public void setAuthor(AuthorEntity author) {
		this.author = author;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.book, ((BookAuthorPK) obj).book)
				.append(this.author, ((BookAuthorPK) obj).author).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.book).append(this.author).toHashCode();
	}
}
