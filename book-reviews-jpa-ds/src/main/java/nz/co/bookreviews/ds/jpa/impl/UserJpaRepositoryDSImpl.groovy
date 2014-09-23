package nz.co.bookreviews.ds.jpa.impl

import static nz.co.bookreviews.data.entity.QUserEntity.userEntity
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.DuplicatedException
import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.User
import nz.co.bookreviews.data.entity.UserEntity
import nz.co.bookreviews.data.repository.UserRepository
import nz.co.bookreviews.ds.jpa.UserDS

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.mysema.query.types.Predicate

@Service("userJpaRepositoryDs")
@Slf4j
@Transactional(readOnly = true)
class UserJpaRepositoryDSImpl implements UserDS{

	@Resource
	UserRepository userRepository

	@Override
	@Transactional(rollbackFor = DuplicatedException.class)
	User createUser(final String userName,final String password) {
		log.debug "createUser start: {} $userName, $password"
		User created
		UserEntity addUserEntity

		Predicate predicate = userEntity.userName.eq(userName)
		addUserEntity = userRepository.findOne(predicate)

		if(addUserEntity){
			throw new DuplicatedException('User[$userName] existed')
		}
		addUserEntity = new UserEntity(userName:userName,password:password,createDate:new Date())
		userRepository.save(addUserEntity);
		created = new User(userId:addUserEntity.userId,userName:addUserEntity.userName,createDate:addUserEntity.createDate)
		log.debug "createUser end: {} $created"
		return created
	}

	@Override
	public User loginUser(final String userName,final String password) {
		return null
	}


	@Override
	public User getUserById(final Long userId) throws NotFoundException {
		UserEntity foundEntity
		foundEntity = userRepository.findOne(userId)
		if(!foundEntity){
			throw new NotFoundException('User not found by id[$userId]')
		}
		return new User(userId:foundEntity.userId,userName:foundEntity.userName,createDate:foundEntity.createDate)
	}

	@Override
	public User getUserByName(final String userName) throws NotFoundException {
		UserEntity foundEntity
		Predicate predicate = userEntity.userName.eq(userName)
		foundEntity = userRepository.findOne(predicate)
		if(!foundEntity){
			throw new NotFoundException('User not found by name[$userName]')
		}
		return new User(userId:foundEntity.userId,userName:foundEntity.userName,createDate:foundEntity.createDate)
	}

	@Override
	@Transactional(rollbackFor = NotFoundException.class)
	User updateUser(Long userId, User updatedUser)
	throws NotFoundException {
		UserEntity foundEntity
		foundEntity = userRepository.findOne(userId)
		if(!foundEntity){
			throw new NotFoundException('User not found by userId[$userId]')
		}
		foundEntity.userName = updatedUser.userName
		if(updatedUser.password){
			foundEntity.password = updatedUser.password
		}
		if(updatedUser.createDate){
			foundEntity.createDate = updatedUser.createDate
		}
		userRepository.save(foundEntity)
		return new User(userId:foundEntity.userId,userName:foundEntity.userName,createDate:foundEntity.createDate)
	}
}
