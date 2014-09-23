package nz.co.bookreviews.ds.jpa;

import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.Customer


public interface CustomerDS {
	Customer createCustomer(Customer customer)
	Customer getCustomerByName(String lastName,String firstName)
	Customer getCustomerById(Long customerId)
	void deleteCustomer(Long customerId)throws NotFoundException
	Customer updateCustomer(Long customerId,Customer updateCustomer)throws NotFoundException
}
