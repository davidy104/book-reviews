package nz.co.bookreviews.data.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//@NamedQueries({ @NamedQuery(name = "BookEntity.countVotes", query = "SELECT COUNT(v) FROM BookEntity b, VoteEntity v WHERE b.bookId = v.bookId AND LOWER(b.title) = LOWER(:bookTitle)") })
@SuppressWarnings("serial")
@Entity
@Table(name = "T_BOOK")
public class BookEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "BOOK_ID", insertable = false, updatable = false)
	private Long bookId;

	@Column(name = "ISBN")
	private String isbn;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "PAGES")
	private Integer pages;

	@Column(name = "TAGS")
	private String tags;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "bookAuthorPK.book")
	private Set<BookAuthorEntity> bookAuthors = Collections.emptySet();

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, mappedBy = "votePK.book")
	private Set<VoteEntity> votes = Collections.emptySet();

	@Embedded
	private PublicationEntity publication;

	public void addBookAuthor(final BookAuthorEntity bookAuthor) {
		if (this.bookAuthors.isEmpty()) {
			this.bookAuthors = new HashSet<>();
		}
		this.bookAuthors.add(bookAuthor);
	}

	public void addVote(final VoteEntity vote) {
		if (this.votes.isEmpty()) {
			this.votes = new HashSet<>();
		}
		this.votes.add(vote);
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Set<BookAuthorEntity> getBookAuthors() {
		return bookAuthors;
	}

	public void setBookAuthors(Set<BookAuthorEntity> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}

	public Set<VoteEntity> getVotes() {
		return votes;
	}

	public void setVotes(Set<VoteEntity> votes) {
		this.votes = votes;
	}

	public PublicationEntity getPublication() {
		return publication;
	}

	public void setPublication(PublicationEntity publication) {
		this.publication = publication;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public static Builder getBuilder(String isbn, String title, Integer pages, String tags,
			PublicationEntity publication) {
		return new Builder(isbn, title, pages, tags, publication);
	}

	public static Builder getBuilder(String isbn, String title, Integer pages, String tags) {
		return new Builder(isbn, title, pages, tags);
	}

	public static Builder getBuilder(String isbn, String title, Integer pages) {
		return new Builder(isbn, title, pages);
	}

	public static class Builder {

		private BookEntity built;

		public Builder(String isbn, String title, Integer pages, String tags) {
			built = new BookEntity();
			built.isbn = isbn;
			built.title = title;
			built.pages = pages;
			built.tags = tags;
		}

		public Builder(String isbn, String title, Integer pages) {
			built = new BookEntity();
			built.isbn = isbn;
			built.title = title;
			built.pages = pages;
		}

		public Builder(String isbn, String title, Integer pages, String tags,
				PublicationEntity publication) {
			built = new BookEntity();
			built.isbn = isbn;
			built.title = title;
			built.pages = pages;
			built.tags = tags;
			built.publication = publication;
		}

		public BookEntity build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.isbn, ((BookEntity) obj).isbn)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.isbn)
				.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("bookId", bookId).append("title", title).append("isbn", isbn)
				.append("pages", pages).append("tags", tags).toString();
	}

}
