package nz.co.bookreviews.data.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Embeddable
public class VotePK implements Serializable {
	@ManyToOne(fetch = FetchType.LAZY)
	private BookEntity book;

	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

	public BookEntity getBook() {
		return book;
	}

	public void setBook(BookEntity book) {
		this.book = book;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.book, ((VotePK) obj).book)
				.append(this.user, ((VotePK) obj).user).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.book).append(this.user).toHashCode();
	}
}
