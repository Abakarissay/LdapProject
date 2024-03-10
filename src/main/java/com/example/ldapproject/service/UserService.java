package com.example.ldapproject.service;

import com.example.ldapproject.mapper.UserNotFoundException;
import com.example.ldapproject.model.User;
import com.example.ldapproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private LdapTemplate ldapTemplate;

    private static final String BASE_DN = "ou=users,ou=system";

    public List<User> getAllUsers() {
        return ldapTemplate.search(BASE_DN, "(objectclass=inetOrgPerson)",
                (AttributesMapper<User>) attributes ->{
                    User ldapuser = new User();
                    ldapuser.setCn(attributes.get("cn").get().toString());
                    ldapuser.setSn(attributes.get("sn").get().toString());
                    ldapuser.setMail(attributes.get("mail").get().toString());
                    return ldapuser;
                });
    }




    public User getUserByCn(String dn) {
        try {
            // Extraire le CN de la chaîne DN
            String cn = dn.split(",")[0].substring(3); // Supprimer "cn=" de la chaîne DN
            //System.out.println("dn :" + dn);
            String filter = "(cn=" + cn + ")";
            Attributes attributes = ldapTemplate.search("", filter,
                    (Attributes attrs) -> attrs).stream().findFirst().orElse(null);
            if (attributes == null) {
                throw new UserNotFoundException("Aucun attribut trouvé pour l'utilisateur avec le CN : " + cn);
            }
            // System.out.println("data :" + attributes);
            return mapToUser(attributes);
        } catch (NamingException e){
            throw new UserNotFoundException("Une erreur s'est produite lors de la récupération de l'utilisateur par CN : " + e.getMessage());
        }
    }

    private User mapToUser(Attributes attributes) throws NamingException {
        // Mapper les attributs LDAP à l'objet User
        User user = new User();
        user.setCn(attributes.get("cn").get().toString());
        user.setSn(attributes.get("sn").get().toString());
        user.setMail(attributes.get("mail").get().toString());
        // Ajoutez d'autres attributs LDAP si nécessaire
        return user;

    }

    public void saveOrUpdateUser(User user) {
        userRepository.save(user);
    }

    public User updateUser(String cn, String newMail) throws NamingException {
        // Rechercher l'utilisateur par CN
        User user = getUserByCn(cn);
        // Vérifier si l'utilisateur existe
        if (user != null) {
            // Créer un objet Attribute pour stocker les modifications
            BasicAttribute mailAttribute = new BasicAttribute("mail", newMail);
            ModificationItem[] modificationItems = new ModificationItem[] {
                    new ModificationItem(DirContext.REPLACE_ATTRIBUTE, mailAttribute)
                    // Ajouter d'autres modifications si nécessaire
            };

            // Appliquer les modifications à l'utilisateur
            ldapTemplate.modifyAttributes(cn +",ou=users,ou=system", modificationItems);
            // Mettre à jour l'adresse e-mail dans l'objet User
            user.setMail(newMail);

            // Retourner l'utilisateur modifié
            return user;
        } else {
            // L'utilisateur n'existe pas
            throw new NamingException("Utilisateur non trouvé pour le CN " + cn);
        }
    }


    public void deleteUser(String dn) throws NamingException {
        // Supprimer l'utilisateur à l'aide de son DN
        ldapTemplate.unbind(dn);
    }



}
