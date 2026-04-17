import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.ExpenseTrackerModel;
import model.Transaction;

public class ExpenseTrackerModelTest {

    private ExpenseTrackerModel model;

    @Before
    public void setup() {
        model = new ExpenseTrackerModel();
    }

    @Test
    public void testRemoveTransactionInvalidNegativeIndex() {
        // Pre-condition: model has no transactions
        assertEquals(0, model.getTransactions().size());
        // Act: attempt to remove with a negative index
        boolean result = model.removeTransaction(-1);
        // Post-condition: returns false, list is unchanged
        assertFalse(result);
        assertEquals(0, model.getTransactions().size());
    }

    @Test
    public void testRemoveTransactionInvalidIndexOutOfBounds() {
        // Pre-condition: model has exactly 1 transaction
        model.addTransaction(new Transaction(30.0, "food"));
        assertEquals(1, model.getTransactions().size());
        // Act: attempt to remove at index 1 (== size, out of range)
        boolean result = model.removeTransaction(1);
        // Post-condition: returns false, list is unchanged
        assertFalse(result);
        assertEquals(1, model.getTransactions().size());
    }

    @Test
    public void testRemoveTransactionFromMultipleTransactions() {
        // Pre-condition: model has 2 transactions
        model.addTransaction(new Transaction(50.0, "food"));
        model.addTransaction(new Transaction(75.0, "travel"));
        assertEquals(2, model.getTransactions().size());
        // Act: remove the first transaction
        boolean result = model.removeTransaction(0);
        // Post-condition: returns true, only the second transaction remains
        assertTrue(result);
        assertEquals(1, model.getTransactions().size());
        assertEquals(75.0, model.getTransactions().get(0).getAmount(), 0.001);
        assertEquals(75.0, model.computeTransactionsTotalCost(), 0.001);
    }

    @Test
    public void testTotalCostMultipleTransactions() {
        // Pre-condition: model has no transactions
        assertEquals(0, model.getTransactions().size());
        // Act: add 3 transactions and compute the total
        model.addTransaction(new Transaction(10.0, "food"));
        model.addTransaction(new Transaction(20.0, "travel"));
        model.addTransaction(new Transaction(30.0, "bills"));
        double total = model.computeTransactionsTotalCost();
        // Post-condition: total equals the sum of all amounts
        assertEquals(60.0, total, 0.001);
    }
}
