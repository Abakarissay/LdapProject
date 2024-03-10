package com.example.ldapproject.repository;

import com.example.ldapproject.model.User;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.ldap.repository.Query;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.Optional;

@Repository
public interface UserRepository extends LdapRepository<User> {
    User getUserByCn(String cn);
    //void delete(Optional<User> user);

}
