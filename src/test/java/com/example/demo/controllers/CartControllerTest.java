package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {



    private CartController cartController;

    @Autowired
    private CartRepository cartRepository = mock(CartRepository.class);
    @Autowired
    private ItemRepository itemRepository = mock(ItemRepository.class);
    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);

    }

    @Test
    public void addToCartTest() throws  Exception
    {

        User user;
        Item item;
        user = new User();

        when(userRepository.findByUsername("ons")).thenReturn(user);
        user.setUsername("ons");

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        System.out.println(user.getUsername());
        modifyCartRequest.setUsername(user.getUsername());

        item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("Round Widget");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

       System.out.println("ID "+ Optional.of(item).get().getId());

      modifyCartRequest.setItemId(Optional.of(item).get().getId());
        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);
        modifyCartRequest.setQuantity(1);

        final ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());



    }
}
