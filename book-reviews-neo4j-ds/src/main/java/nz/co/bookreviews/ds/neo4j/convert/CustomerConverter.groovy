package nz.co.bookreviews.ds.neo4j.convert

import java.text.DateFormat
import java.text.SimpleDateFormat

import nz.co.bookreviews.GenericConverter
import nz.co.bookreviews.data.Customer

import org.springframework.stereotype.Service

@Service
class CustomerConverter implements GenericConverter<Customer> {

	static DateFormat FORMAT= new SimpleDateFormat("yyyy-MM-dd")

	@Override
	Customer convertFrom(Object... other) {
		String uri = other[0]
		Map resultMap = other[1]
		def birthDate
		def member
		String dateStr = resultMap.get('birthDate')
		if(dateStr){
			birthDate = FORMAT.parse(dateStr)
		}
		if(resultMap.get('member')){
			member = resultMap.get('member')==1?true:false
		}
		return new Customer(nodeUri:uri,lastName:resultMap.get('lastName'),firstName:resultMap.get('firstName'),member:member,birthDate:birthDate,email:resultMap.get('email'),customerNo:resultMap.get('customerNo'))
	}

	@Override
	Object convertTo(Customer customer,Object... additionalSource) {
		String convertType = (String)additionalSource[0]
		int member = customer.member?1:0
		String birthDateStr = FORMAT.format(customer.birthDate)
		if(convertType == 'create'){
			return "firstName:'"+customer.firstName+"',lastName:'"+customer.lastName+"',birthDate:'"+birthDateStr+"',customerNo:'"+customer.customerNo+"',email:'"+customer.email+"',member:'"+member+"'"
		}else {
			return "\"firstName\":\""+customer.firstName+"\",\"lastName\":\""+customer.lastName+"\",\"birthDate\":\""+birthDateStr+"\",\"customerNo\":\""+customer.customerNo+"\",\"email\":\""+customer.email+"\",\"member\":\""+member+"\""
		}
	}
}
