package nz.co.bookreviews.ds.jpa.test.logic;

import static nz.co.bookreviews.data.entity.QUserEntity.userEntity
import static org.junit.Assert.*
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyNoMoreInteractions
import static org.mockito.Mockito.when
import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.User
import nz.co.bookreviews.data.entity.UserEntity
import nz.co.bookreviews.data.repository.UserRepository
import nz.co.bookreviews.ds.jpa.impl.UserJpaRepositoryDSImpl;

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner.class)
class UserJpaRepositoryDSMockTest {

	@Mock
	UserRepository userRepository

	@InjectMocks
	UserJpaRepositoryDSImpl userJpaRepositoryDs

	static final String TEST_USERNAME="foo"
	static final String TEST_PASSWORD="123"
	static final Long ID = 3L

	@Test
	public void testCreateUser() {
		when(userRepository.findOne(userEntity.userName.eq(TEST_USERNAME))).thenReturn(null);
		ArgumentCaptor<UserEntity> userArgument = ArgumentCaptor.forClass(UserEntity.class);

		userJpaRepositoryDs.createUser(TEST_USERNAME,TEST_PASSWORD)
		verify(userRepository, times(1)).findOne(userEntity.userName.eq(TEST_USERNAME))
		verify(userRepository, times(1)).save(userArgument.capture())
		verifyNoMoreInteractions(userRepository)

		UserEntity actual = userArgument.getValue();
		assertEquals(TEST_USERNAME,actual.userName)
		assertEquals(TEST_PASSWORD,actual.password)
	}

	@Test
	void testFindUserByName(){
		UserEntity expected = new UserEntity()
		when(userRepository.findOne(userEntity.userName.eq(TEST_USERNAME)))
				.thenReturn(expected);
		User actual = userJpaRepositoryDs.getUserByName(TEST_USERNAME)
		verify(userRepository, times(1)).findOne(userEntity.userName.eq(TEST_USERNAME))
		verifyNoMoreInteractions(userRepository)
		assertEquals(expected.userName,actual.userName)
	}

	@Test(expected = NotFoundException.class)
	void testFindByIdWhenIsNotFound(){
		when(userRepository.findOne(ID)).thenReturn(null)
		userJpaRepositoryDs.getUserById(ID)
		verify(userRepository, times(1)).findOne(ID)
		verifyNoMoreInteractions(userRepository)
	}

	//	@Test
	//	void testDeleteById(){
	//		userJpaRepositoryDs.deleteUser(ID)
	//		verify(userRepository, times(1)).deleteById(ID)
	//		verifyNoMoreInteractions(userRepository)
	//	}


	void assertUser(UserEntity entity,User user){
		assertEquals(entity.userName, user.userName)
		assertEquals(entity.password, user.password)
		assertEquals(entity.createDate, user.createDate)
	}
}
