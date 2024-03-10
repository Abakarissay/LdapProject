package com.example.ldapproject.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.ldap.odm.annotations.Entry;

import javax.naming.Name;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entry(objectClasses = { "inetOrgPerson" })
public  final  class User {
    @Id
    @Attribute(name = "cn")
    private String cn;
    @Attribute(name = "sn")
    private String sn;
    @Attribute(name = "mail")
    private String mail;

}

