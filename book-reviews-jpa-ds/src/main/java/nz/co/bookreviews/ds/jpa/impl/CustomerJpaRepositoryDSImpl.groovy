package nz.co.bookreviews.ds.jpa.impl;

import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.bookreviews.NotFoundException
import nz.co.bookreviews.data.Customer
import nz.co.bookreviews.data.Page
import nz.co.bookreviews.data.entity.CustomerEntity
import nz.co.bookreviews.data.entity.CustomerEntity.MemberShip
import nz.co.bookreviews.data.repository.CustomerRepository
import nz.co.bookreviews.ds.jpa.CustomerDS

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
@Service("customerJpaRepositoryDs")
@Slf4j
@Transactional(readOnly = true)
class CustomerJpaRepositoryDSImpl implements  CustomerDS{

	@Resource
	CustomerRepository customerRepository

	@Override
	Customer createCustomer(Customer customer) {
		return null
	}

	@Override
	Customer getCustomerByName(String lastName, String firstName) {
		return null
	}

	@Override
	Customer getCustomerById(Long customerId) {
		return null
	}

	@Override
	void deleteCustomer(Long customerId) throws NotFoundException {
	}

	@Override
	Customer updateCustomer(Long customerId, Customer updateCustomer) throws NotFoundException {
		return null
	}

	@Override
	public Page getCustomers(int offset) {
		Page result
		org.springframework.data.domain.Page<CustomerEntity> page = customerRepository.findAll(new PageRequest(offset, Page.PAGE_SIZE))
		int pageNumber = page.getNumber();
		List<CustomerEntity> customers = page.getContent()
		result = new Page(totalPages:page.getTotalPages(),totalCount:page.getTotalElements())
		customers.each {
			boolean member = it.membership==MemberShip.yes?true:false
			result.content << new Customer(personId:it.personId,lastName:it.lastName,firstName:it.firstName,birthDate:it.birthDate,member:member,email:it.email)
		}
		return result
	}

	@Override
	Set<Customer> getAllCustomers() {
		log.info "getAllCustomers start"
		def customers= []
		List<CustomerEntity> foundModels = customerRepository.findAll()
		log.info "getAllCustomers size: {} ${foundModels.size()}"
		foundModels.each {
			log.info "model: {} $it"
			boolean member = it.membership==MemberShip.yes?true:false
			customers << new Customer(personId:it.personId,lastName:it.lastName,firstName:it.firstName,birthDate:it.birthDate,member:member,email:it.email)
		}
		return customers
	}
}
