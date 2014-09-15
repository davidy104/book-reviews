package nz.co.bookreviews.data.util;

import java.util.Date;

import nz.co.bookreviews.data.entity.BookEntity;
import nz.co.bookreviews.data.entity.UserEntity;
import nz.co.bookreviews.data.entity.VoteEntity;

public class VoteBuilder extends EntityBuilder<VoteEntity> {

	@Override
	void initProduct() {
	}

	public VoteBuilder create(final BookEntity book, final UserEntity user,
			final Integer score, final Date createTime) {
		this.product = VoteEntity.getBuilder(book, user, score, createTime)
				.build();
		return this;
	}

	@Override
	VoteEntity assembleProduct() {
		return this.product;
	}

}
