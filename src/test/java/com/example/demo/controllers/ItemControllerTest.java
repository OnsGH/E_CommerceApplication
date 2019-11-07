package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    @Autowired
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);

    }

    @Test
    public void getItemByIdTest(){

        Item item = new Item();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        item.setId(1L);
        assertNotNull(item);
        assertEquals(Long.valueOf(1),Optional.of(item).get().getId());

        final ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());


    }

    @Test
    public void getItemsTest(){
        Item item1 = new Item();
        Item item2 = new Item();

            List<Item> itemList = new ArrayList<>();

          when(itemRepository.findAll()).thenReturn(itemList);
            item1.setId(1L);
            item1.setName("itemName1");
            item2.setId(2L);
            item1.setName("itemName2");
            itemList.add(item1);
            itemList.add(item2);

            assertTrue(itemList.size()==2);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

    }
}
