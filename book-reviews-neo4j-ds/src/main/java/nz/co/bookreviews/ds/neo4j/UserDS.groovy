package nz.co.bookreviews.ds.neo4j;

import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User

interface UserDS {
	User createUser(String userName, String password)
	User loginUser(String userName, String password)
	User getUserByUri(String nodeUri)
	User getUserByName(String userName)
	User updateUserByUserName(String userName,User updatedUser)
	void deleteUserByName(String userName)
	Page getAllUsers(int pageOffset)
}
