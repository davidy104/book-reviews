package nz.co.bookreviews.data.util;

import java.util.Date;

import nz.co.bookreviews.data.entity.AuthorEntity;
import nz.co.bookreviews.data.entity.UserEntity;

public class AuthorBuilder extends EntityBuilder<AuthorEntity> {

	@Override
	void initProduct() {
	}

	public AuthorBuilder create(String authorNo, String lastName, String firstName,
			String email, Date birthDate) {
		if (birthDate != null) {
			this.product = AuthorEntity.getBuilder(authorNo, lastName, firstName, email,
					birthDate).build();
		} else {
			this.product = AuthorEntity.getBuilder(authorNo, lastName, firstName, email)
					.build();
		}
		return this;
	}

	public AuthorBuilder setUser(final UserEntity user) {
		this.product.setUser(user);
		return this;
	}

	@Override
	AuthorEntity assembleProduct() {
		return this.product;
	}

}
