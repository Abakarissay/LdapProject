package com.example.ldapproject.mapper;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.ldap.LdapName;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToNameConverter implements Converter<String, Name> {

    @Override
    public Name convert(String source) {
        try {
            return new LdapName(source);
        } catch (InvalidNameException e) {
            throw new IllegalArgumentException("Invalid DN: " + source, e);
        }
    }
}
