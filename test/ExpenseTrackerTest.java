

import static org.junit.Assert.assertEquals;

import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;

import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

public class ExpenseTrackerTest {

  private ExpenseTrackerModel model;

  @Before
  public void setup() {
	model = new ExpenseTrackerModel();
  }
  
  @Test
  public void testInitialConfiguration() {
    // There aren't any pre-conditions to be checked
    // The setup method called the constructors
    // Check the post-conditions
    assertEquals(0, model.getTransactions().size());
  }

  private void testAddTransactionHelper(double amount, String category) {
	    // Check the pre-conditions
	    assertEquals(0, model.getTransactions().size());
		
	    // Create a new transaction and add it
	    Transaction transaction = new Transaction(amount, category);
	    model.addTransaction(transaction);

	    // Check the post-conditions: 
	    // Verify that the transaction was added appropriately
	    java.util.List<Transaction> transactions = model.getTransactions();
	    assertEquals(1, transactions.size());
	    assertEquals(amount, transactions.get(0).getAmount(), 0.001);
	    assertEquals(category, transactions.get(0).getCategory());
	    assertEquals(amount, model.computeTransactionsTotalCost(), 0.001);
  }
  
  @Test
  public void testAddTransaction() {
	  double amount = 100.0;
	  String category = "Food";
	  this.testAddTransactionHelper(amount, category);
  }
  
  @Test
  public void testRemoveTransaction() {
	  // Initialize: Add a new transaction
	  double amount = 100.0;
	  String category = "Food";
	  this.testAddTransactionHelper(amount, category);
	  // Remove that transaction
	  model.removeTransaction(0);
	  // Check the post-conditions
	  assertEquals(0, model.getTransactions().size());
	  assertEquals(0, model.computeTransactionsTotalCost(), 0.001);
  }
}