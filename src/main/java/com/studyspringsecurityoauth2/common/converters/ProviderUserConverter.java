package com.studyspringsecurityoauth2.common.converters;

public interface ProviderUserConverter<T, R> {

    R convert(T t);

}
