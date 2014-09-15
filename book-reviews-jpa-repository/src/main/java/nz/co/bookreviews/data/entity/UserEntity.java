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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_USER")
public class UserEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "PASSWORD")
	private String password;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID", referencedColumnName = "PERSON_ID")
	protected PersonEntity person;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "votePK.user")
	private Set<VoteEntity> votes = Collections.emptySet();

	public void addVote(final VoteEntity vote) {
		if (this.votes.isEmpty()) {
			this.votes = new HashSet<>();
		}
		this.votes.add(vote);
	}

	public Set<VoteEntity> getVotes() {
		return votes;
	}

	public void setVotes(Set<VoteEntity> votes) {
		this.votes = votes;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public PersonEntity getPerson() {
		return person;
	}

	public void setPerson(PersonEntity person) {
		this.person = person;
	}

	public static Builder getBuilder(String userName, String password,
			Date createDate) {
		return new Builder(userName, password, createDate);
	}

	public static Builder getBuilder(String userName, Date createDate) {
		return new Builder(userName, createDate);
	}

	public static class Builder {

		private UserEntity built;

		public Builder(String userName, String password, Date createDate) {
			built = new UserEntity();
			built.userName = userName;
			built.password = password;
			built.createDate = createDate;
		}

		public Builder(String userName, Date createDate) {
			built = new UserEntity();
			built.userName = userName;
			built.createDate = createDate;
		}

		public UserEntity build() {
			return built;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.userName, ((UserEntity) obj).userName)
				.append(this.password, ((UserEntity) obj).password).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.userName).append(this.password).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("userId", userId).append("userName", userName)
				.append("createDate", createDate).toString();
	}

}
