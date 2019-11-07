package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import  org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserControllerTest {

   private UserController userController;
   private UserRepository userRepository = mock(UserRepository.class);
   private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);
   private CartRepository cartRepository = mock(CartRepository.class);

   @Before
  public void setUp() throws NoSuchFieldException, IllegalAccessException {

       userController = new UserController();
       TestUtils.injectObjects(userController,"userRepository",userRepository);
       TestUtils.injectObjects(userController,"cartRepository",cartRepository);
       TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);
   }

   @Test
    public void createUserTest() throws  Exception
     {
         when(encoder.encode("testpassword")).thenReturn("thisishashed");
         CreateUserRequest userRequest = new CreateUserRequest();
         userRequest.setUsername("test");
         userRequest.setPassword("testpassword");
         userRequest.setConfirmpassword("testpassword");

         final ResponseEntity<User> responseEntity = userController.createUser(userRequest);
         assertNotNull(responseEntity);
         assertEquals(200, responseEntity.getStatusCodeValue());

         User user = responseEntity.getBody();
         assertNotNull(user);
         assertEquals("test",user.getUsername());
         assertEquals("thisishashed",user.getPassword());
     }

    @Test
    public void findByUserNameTest()
    {
        User user = new User();
        when(userRepository.findByUsername("ons")).thenReturn(user);
        user.setUsername("ons");
        final ResponseEntity<User> responseEntity = userController.findByUserName("ons");
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());


    }


    @Test(expected = AssertionError.class)
    public void WrongUserPasswordTest() throws  Exception
    {
        when(encoder.encode("test")).thenReturn("thisishashed");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("ons");
        userRequest.setPassword("test");
        userRequest.setConfirmpassword("test");
        User user = new User();
        ResponseEntity<User> userResponseEntity=null;
        when(userController.createUser(userRequest)).thenReturn(userResponseEntity).
                thenThrow(AssertionError.class);

        assertThatThrownBy(() -> userController.createUser(userRequest))
                .isInstanceOf(AssertionError.class)
                .hasNoCause();

    }

    @Test
    public void UserNameNotFoundException_If_UserIsNotFoundTest() {
       String fakeUser = "test";
       User user = new User();
       when(userRepository.findByUsername(fakeUser))
                .thenThrow(UsernameNotFoundException.class);
        user.setUsername("ons");
        assertThatThrownBy(() -> userRepository.findByUsername(fakeUser))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasNoCause();
    }

}
