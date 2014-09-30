package nz.co.bookreviews

interface GenericConverter<T> {
	T convertFrom(Object... other)
	Object convertTo(T model,Object... additionalSource)
}
