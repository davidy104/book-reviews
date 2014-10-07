package nz.co.bookreviews.ds.neo4j.convert

import java.text.DateFormat
import java.text.SimpleDateFormat

import nz.co.bookreviews.GenericConverter
import nz.co.bookreviews.data.Author

import org.springframework.stereotype.Service
@Service
class AuthorConverter implements GenericConverter<Author> {
	static DateFormat FORMAT= new SimpleDateFormat("yyyy-MM-dd")
	@Override
	Author convertFrom(Object... other) {
		String uri = other[0]
		Map resultMap = other[1]
		def birthDate
		String dateStr = resultMap.get('birthDate')
		if(dateStr){
			birthDate = FORMAT.parse(dateStr)
		}
		return new Author(nodeUri:uri,lastName:resultMap.get('lastName'),firstName:resultMap.get('firstName'),birthDate:birthDate,email:resultMap.get('email'))
	}

	@Override
	Object convertTo(Author author, Object... additionalSource) {
		String convertType = (String)additionalSource[0]
		String birthDateStr = FORMAT.format(author.birthDate)
		if(convertType == 'create'){
			return "firstName:'"+author.firstName+"',lastName:'"+author.lastName+"',birthDate:'"+birthDateStr+"',authorNo:'"+author.authorNo+"',email:'"+author.email+"'"
		}else {
			return "\"firstName\":\""+author.firstName+"\",\"lastName\":\""+author.lastName+"\",\"birthDate\":\""+birthDateStr+"\",\"authorNo\":\""+author.authorNo+"\",\"email\":\""+author.email+"\""
		}
	}
}
