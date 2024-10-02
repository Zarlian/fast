package be.howest.ti.adria.logic.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {
    private static Transaction transactionSingleTicket;
    private static Transaction transactionUnlimited;
    private static Transaction transactionLimited;

    @BeforeAll
    static void setUp() {
        transactionUnlimited = new Transaction(1,"SUBSCRIPTION", "Unlimited", 200.0, null);
        transactionLimited = new Transaction(2,"SUBSCRIPTION", "Limited", 70.0, 40);
        transactionSingleTicket = new Transaction(3, "TICKET", "Single ticket", 2.0, 1);
    }

    @Test
    void getId() {
        assertEquals(1, transactionUnlimited.getId());
        assertEquals(2, transactionLimited.getId());
        assertEquals(3, transactionSingleTicket.getId());
    }

    @Test
    void getType() {
        assertEquals("SUBSCRIPTION", transactionUnlimited.getType());
        assertEquals("SUBSCRIPTION", transactionLimited.getType());
        assertEquals("TICKET", transactionSingleTicket.getType());
    }

    @Test
    void getName() {
        assertEquals("Unlimited", transactionUnlimited.getName());
        assertEquals("Limited", transactionLimited.getName());
        assertEquals("Single ticket", transactionSingleTicket.getName());
    }

    @Test
    void getPrice() {
        assertEquals(200.0, transactionUnlimited.getPrice());
        assertEquals(70.0, transactionLimited.getPrice());
        assertEquals(2.0, transactionSingleTicket.getPrice());
    }

    @Test
    void getMaxUses() {
        assertNull(transactionUnlimited.getMaxUses());
        assertEquals(40, transactionLimited.getMaxUses());
        assertEquals(1, transactionSingleTicket.getMaxUses());
    }
}