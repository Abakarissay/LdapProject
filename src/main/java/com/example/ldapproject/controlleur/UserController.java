package com.example.ldapproject.controlleur;

import com.example.ldapproject.config.UserNotFoundException;
import com.example.ldapproject.model.User;
import com.example.ldapproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{cn}")
    public ResponseEntity<?> getUserByCn(@PathVariable String cn) throws NamingException {
        try {
            User user = userService.getUserByCn(cn);
            return ResponseEntity.ok(user);
            //return userService.getUserByCn(cn);
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.saveOrUpdateUser(user);
    }

    @PutMapping("/update")
    public User updateUser(@RequestBody User user) throws NamingException {
        return userService.updateUser(user.getCn(), user.getMail());
    }

    @DeleteMapping("/{cn}")
    public void deleteUser(@PathVariable String cn) throws NamingException {
        userService.deleteUser(cn);
    }
}