package nz.co.bookreviews.data.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Embeddable
public class PublicationEntity implements Serializable {

	@Column(name = "YEAR")
	private Integer year;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "PUBLISHER_ID", referencedColumnName = "PUBLISHER_ID")
	private PublisherEntity publisher;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public PublisherEntity getPublisher() {
		return publisher;
	}

	public void setPublisher(PublisherEntity publisher) {
		this.publisher = publisher;
	}

	public static Builder getBuilder(Integer year) {
		return new Builder(year);
	}

	public static Builder getBuilder(Integer year, PublisherEntity publisher) {
		return new Builder(year, publisher);
	}

	public static class Builder {

		private PublicationEntity built;

		public Builder(Integer year) {
			built = new PublicationEntity();
			built.year = year;
		}

		public Builder(Integer year, PublisherEntity publisher) {
			built = new PublicationEntity();
			built.year = year;
			built.publisher = publisher;
		}

		public PublicationEntity build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append(
				"year", year).toString();
	}
}
