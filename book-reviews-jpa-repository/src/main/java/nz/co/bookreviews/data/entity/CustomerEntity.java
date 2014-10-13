package nz.co.bookreviews.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@SuppressWarnings("serial")
@Entity
@Table(name = "T_CUSTOMER")
@PrimaryKeyJoinColumn(name = "PERSON_ID")
public class CustomerEntity extends PersonEntity implements Serializable {

	public enum MemberShip {
		yes(0), no(1);
		MemberShip(int value) {
			this.value = value;
		}

		private final int value;

		public int value() {
			return value;
		}
	}

	@Column(name = "CUSTOMER_NO")
	private String customerNo;

	@Column(name = "MEMBERSHIP")
	private Integer membership = MemberShip.no.value();

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public Integer getMembership() {
		return membership;
	}

	public void setMembership(Integer membership) {
		this.membership = membership;
	}

	public static Builder getBuilder(String customerNo, String lastName, String firstName,
			String email, Date birthDate) {
		return new Builder(customerNo, lastName, firstName, email, birthDate);
	}

	public static Builder getBuilder(String customerNo, String lastName, String firstName,
			String email) {
		return new Builder(customerNo, lastName, firstName, email);
	}

	public static class Builder {
		private CustomerEntity built;

		public Builder(String customerNo, String lastName, String firstName, String email) {
			built = new CustomerEntity();
			built.customerNo = customerNo;
			built.lastName = lastName;
			built.firstName = firstName;
			built.email = email;
		}

		public Builder(String customerNo, String lastName, String firstName, String email,
				Date birthDate) {
			built = new CustomerEntity();
			built.customerNo = customerNo;
			built.lastName = lastName;
			built.firstName = firstName;
			built.email = email;
			built.birthDate = birthDate;
		}

		public CustomerEntity build() {
			return built;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("personId", personId).append("customerNo", customerNo)
				.append("lastName", lastName)
				.append("firstName", firstName).append("email", email)
				.append("birthDate", birthDate)
				.append("membership", membership).toString();
	}

}
