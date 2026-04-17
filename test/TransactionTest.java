import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import model.Transaction;

public class TransactionTest {

    private Transaction validTransaction;

    @Before
    public void setup() {
        validTransaction = new Transaction(50.0, "food");
    }

    // Group A — Constructor validation: invalid amounts

    @Test(expected = IllegalArgumentException.class)
    public void testTransactionInvalidAmountZero() {
        // Pre-condition: no transaction exists yet
        // Act: attempt to create a transaction with amount 0
        new Transaction(0, "food");
        // Post-condition: IllegalArgumentException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransactionInvalidAmountNegative() {
        // Pre-condition: no transaction exists yet
        // Act: attempt to create a transaction with a negative amount
        new Transaction(-5.0, "food");
        // Post-condition: IllegalArgumentException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransactionInvalidAmountTooLarge() {
        // Pre-condition: no transaction exists yet
        // Act: attempt to create a transaction with amount > 1000
        new Transaction(1000.01, "food");
        // Post-condition: IllegalArgumentException is thrown
    }

    // Group A — Constructor validation: invalid categories

    @Test(expected = IllegalArgumentException.class)
    public void testTransactionInvalidCategoryNull() {
        // Pre-condition: no transaction exists yet
        // Act: attempt to create a transaction with a null category
        new Transaction(50.0, null);
        // Post-condition: IllegalArgumentException is thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransactionInvalidCategoryUnrecognized() {
        // Pre-condition: no transaction exists yet
        // Act: attempt to create a transaction with an unrecognized category
        new Transaction(50.0, "xyz");
        // Post-condition: IllegalArgumentException is thrown
    }

    // Group B — getTimestamp

    @Test
    public void testTransactionGetTimestamp() {
        // Pre-condition: a valid transaction was created in setup
        // Act: retrieve the timestamp
        String timestamp = validTransaction.getTimestamp();
        // Post-condition: timestamp is not null and not empty
        assertNotNull(timestamp);
        assertFalse(timestamp.isEmpty());
    }

    // Verify getters return values set by constructor

    @Test
    public void testTransactionGetAmount() {
        // Pre-condition: a valid transaction was created in setup with amount 50.0
        // Act: retrieve the amount
        double amount = validTransaction.getAmount();
        // Post-condition: amount matches the constructor argument
        assertEquals(50.0, amount, 0.001);
    }

    @Test
    public void testTransactionGetCategory() {
        // Pre-condition: a valid transaction was created in setup with category "food"
        // Act: retrieve the category
        String category = validTransaction.getCategory();
        // Post-condition: category matches the constructor argument
        assertEquals("food", category);
    }
}
