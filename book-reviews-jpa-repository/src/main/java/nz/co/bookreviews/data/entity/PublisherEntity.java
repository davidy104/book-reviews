package nz.co.bookreviews.data.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_PUBLISHER")
public class PublisherEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PUBLISHER_ID", insertable = false, updatable = false)
	private Long publisherId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "CREATE_TIME")
	@Temporal(value = TemporalType.TIME)
	private Date createTime;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<BookEntity> books = Collections.emptySet();

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Set<BookEntity> getBooks() {
		return books;
	}

	public void setBooks(Set<BookEntity> books) {
		this.books = books;
	}

	public void addBook(final BookEntity book) {
		if (this.books.isEmpty()) {
			this.books = new HashSet<>();
		}
		this.books.add(book);
	}

	public static Builder getBuilder(String name, Date createTime) {
		return new Builder(name, createTime);
	}

	public static Builder getBuilder(String name) {
		return new Builder(name);
	}

	public static class Builder {

		private PublisherEntity built;

		public Builder(String name, Date createTime) {
			built = new PublisherEntity();
			built.name = name;
			built.createTime = createTime;
		}

		public Builder(String name) {
			built = new PublisherEntity();
			built.name = name;
		}

		public PublisherEntity build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.name, ((PublisherEntity) obj).name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.name).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("publisherId", publisherId).append("name", name)
				.append("createTime", createTime).toString();
	}
}
