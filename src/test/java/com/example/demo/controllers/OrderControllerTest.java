package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    @Autowired
    private UserRepository userRepository = mock(UserRepository.class);;
    @Autowired
    private OrderRepository orderRepository = mock(OrderRepository.class);
    @Autowired
    private ItemRepository itemRepository = mock(ItemRepository.class);;


    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);
    }

@Test
    public void submitTest(){

    User user = new User();
    Cart cart = new Cart();
    Item item = new Item();
    when(userRepository.findByUsername("ons")).thenReturn(user);
    user.setUsername("ons");
    assertNotNull(user);
    ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
    System.out.println(user.getUsername());
    modifyCartRequest.setUsername(user.getUsername());

    item = new Item();
    item.setId(1L);
    item.setName("Round Widget");
    item.setPrice(BigDecimal.valueOf(2.99));
    item.setDescription("Round Widget");

    when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    modifyCartRequest.setItemId(Optional.of(item).get().getId());

    cart.addItem(item);
    user.setCart(cart);

    final ResponseEntity<UserOrder>  responseEntity = orderController.submit(user.getUsername());
    assertEquals(200, responseEntity.getStatusCodeValue());



}

}
