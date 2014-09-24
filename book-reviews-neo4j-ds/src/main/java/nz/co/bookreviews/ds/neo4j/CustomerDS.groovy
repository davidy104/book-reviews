package nz.co.bookreviews.ds.neo4j;

import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.User

interface CustomerDS {
	Customer createCustomer(Customer customer,User newUser)
	Customer assignUserToCustomer(String customerNodeUri,String userNodeUri)
	Page getAllCustomers(int pageOffset)
	Customer updateCustomer(String custNodeUri,Customer updatedCustomer)
	Customer getCustomerByUri(String custNodeUri)
	Customer getCustomerByEmail(String email)
	List getTopMostVoteCustomers(int listSize)
	void deleteCustomerByUri(String custNodeUri)
}
