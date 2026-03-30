package com.nodo.retotecnico.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class CartTest {

    private Cart cart;
    private ExpansionPack pack1;
    private ExpansionPack pack2;
    private Platform platform1;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
        
        pack1 = new ExpansionPack();
        ReflectionTestUtils.setField(pack1, "id", 1);
        ReflectionTestUtils.setField(pack1, "price", 19.99);

        pack2 = new ExpansionPack();
        ReflectionTestUtils.setField(pack2, "id", 2);
        ReflectionTestUtils.setField(pack2, "price", 29.99);

        platform1 = new Platform();
        ReflectionTestUtils.setField(platform1, "id", 1);
    }

    @Test
    public void testAddExpansionUpdatesTotal() {
        cart.addExpansion(pack1, platform1);
        assertEquals(19.99, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);

        cart.addExpansion(pack2, platform1);
        assertEquals(49.98, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);
    }

    @Test
    public void testRemoveExpansionUpdatesTotal() {
        cart.addExpansion(pack1, platform1);
        cart.addExpansion(pack2, platform1);
        assertEquals(49.98, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);

        cart.removeExpansion(pack1);
        assertEquals(29.99, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);

        cart.removeExpansion(pack2);
        assertEquals(0.0, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);
    }

    @Test
    public void testClearCartResetsTotal() {
        cart.addExpansion(pack1, platform1);
        cart.addExpansion(pack2, platform1);
        assertEquals(49.98, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);

        cart.clearCart();
        assertEquals(0.0, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);
        assertEquals(0, ((java.util.List<?>) ReflectionTestUtils.getField(cart, "details")).size());
    }

    @Test
    public void testAddDuplicateExpansionDoesNotChangeTotal() {
        cart.addExpansion(pack1, platform1);
        assertEquals(19.99, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);

        // Attempting to add the same expansion again
        cart.addExpansion(pack1, platform1);
        assertEquals(19.99, (Double) ReflectionTestUtils.getField(cart, "total"), 0.001);
        assertEquals(1, ((java.util.List<?>) ReflectionTestUtils.getField(cart, "details")).size());
    }
}
