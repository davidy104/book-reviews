package nz.co.bookreviews.data.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_VOTE")
@AssociationOverrides({
		@AssociationOverride(name = "votePK.book", joinColumns = @JoinColumn(name = "BOOK_ID")),
		@AssociationOverride(name = "votePK.user", joinColumns = @JoinColumn(name = "USER_ID")) })
public class VoteEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VOTE_ID", insertable = false, updatable = false)
	private Long voteId;

	@Column(name = "SCORE")
	private Integer score;

	@Column(name = "CREATE_TIME")
	@Temporal(TemporalType.TIME)
	private Date createTime;

	@Embedded
	private VotePK votePK = new VotePK();

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public VotePK getVotePK() {
		return votePK;
	}

	public void setVotePK(VotePK votePK) {
		this.votePK = votePK;
	}

	@Transient
	public BookEntity getBook() {
		return getVotePK().getBook();
	}

	public void setBook(BookEntity book) {
		getVotePK().setBook(book);
	}

	@Transient
	public UserEntity getUser() {
		return getVotePK().getUser();
	}

	public void setUser(final UserEntity user) {
		getVotePK().setUser(user);
	}

	public static Builder getBuilder(BookEntity book, UserEntity user,
			Integer score, Date createTime) {
		return new Builder(book, user, score, createTime);
	}

	public static class Builder {

		private VoteEntity built;

		public Builder(BookEntity book, UserEntity user, Integer score,
				Date createTime) {
			built = new VoteEntity();
			built.setBook(book);
			built.setUser(user);
			built.setScore(score);
			built.setCreateTime(createTime);
		}

		public VoteEntity build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.votePK, ((VoteEntity) obj).votePK)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.votePK).toHashCode();
	}
}
