package nz.co.bookreviews.ds.jpa

import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User


interface UserDS {
	User createUser(String userName, String password)
	User loginUser(String userName, String password)
	User getUserById(Long userId) throws NotFoundException
	User getUserByName(String userName) throws NotFoundException
	User updateUser(Long userId,User updatedUser) throws NotFoundException
	Page getUsers(int pageOffset)
}
