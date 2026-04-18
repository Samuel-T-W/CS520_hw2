

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import view.AnalysisPanelView;
import view.DataPanelView;

public class ExpenseTrackerTest {

  // For unit testing
  private ExpenseTrackerModel model;
  // For end-to-end testing
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
	model = new ExpenseTrackerModel();
	controller = new ExpenseTrackerController();
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
  
  private void dismissDialogAsync() {
    new Thread(() -> {
      try { Thread.sleep(300); } catch (InterruptedException ignored) {}
      for (Window w : Window.getWindows()) {
        if (w instanceof JDialog && w.isVisible()) {
          JDialog dialog = (JDialog) w;
          // Find and click the OK button to dismiss cleanly
          for (java.awt.Component c : dialog.getContentPane().getComponents()) {
            if (c instanceof javax.swing.JOptionPane) {
              javax.swing.JOptionPane pane = (javax.swing.JOptionPane) c;
              for (java.awt.Component btn : ((java.awt.Container) pane.getComponent(pane.getComponentCount() - 1)).getComponents()) {
                if (btn instanceof javax.swing.JButton) {
                  ((javax.swing.JButton) btn).doClick();
                  return;
                }
              }
            }
          }
        }
      }
    }).start();
  }

  @Test
  public void testGenerateChartNoTransactionsE2E() {
    // Pre-condition: no transactions have been added
    assertEquals(0, controller.getModel().getTransactions().size());
    AnalysisPanelView analysisView = controller.getView().getAnalysisPanelView();
    JPanel chartArea = (JPanel) analysisView.getComponent(1);
    assertEquals(0, chartArea.getComponentCount());
    // Act: click Generate Chart with no transactions; auto-dismiss the error dialog
    dismissDialogAsync();
    analysisView.getGenerateChartBtn().doClick();
    // Post-condition: chart area remains empty (error message shown, no chart rendered)
    assertEquals(0, chartArea.getComponentCount());
  }

  @Test
  public void testGenerateChartWithImportedTransactionsE2E() {
    // Pre-condition: add multiple transactions with different categories
    String today = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
    controller.getModel().addTransaction(new Transaction(50.0, "food"));
    controller.getModel().addTransaction(new Transaction(30.0, "travel"));
    controller.getModel().addTransaction(new Transaction(20.0, "bills"));
    assertEquals(3, controller.getModel().getTransactions().size());
    AnalysisPanelView analysisView = controller.getView().getAnalysisPanelView();
    analysisView.getStartDate(); // ensure fields exist
    // Set date window to today so all transactions are included
    analysisView.getComponent(0); // control panel exists
    controller.getView().getAnalysisPanelView().getGenerateChartBtn();
    // Set dates via reflection on the text fields — use today as both start and end
    java.awt.Component[] controls = ((JPanel) analysisView.getComponent(0)).getComponents();
    ((javax.swing.JTextField) controls[1]).setText(today);
    ((javax.swing.JTextField) controls[3]).setText(today);
    JPanel chartArea = (JPanel) analysisView.getComponent(1);
    // Act: click Generate Chart
    analysisView.getGenerateChartBtn().doClick();
    // Post-condition: a chart panel has been rendered in the display area
    assertTrue(chartArea.getComponentCount() > 0);
  }

  @Test
  public void testAddTransactionE2E() {
	  // Perform initialization and check the preconditions
	  double newAmount = 44.0;
	  String newCategory = "Other";
	  DataPanelView view = controller.getView().getDataPanelView();
	  view.setAmount("" + newAmount);
	  view.setCategory(newCategory);
	  assertEquals(0, view.getTransactionsTableRowCount());
	  // Call the unit under test: Add the new transaction
	  view.getAddTransactionBtn().doClick();
	  // Check the post-conditions
	  assertEquals(2, view.getTransactionsTableRowCount());
	  assertEquals(newAmount, view.getTransactionsTableValueAt(0, 1));
	  assertEquals(newCategory, view.getTransactionsTableValueAt(0, 2));
	  assertEquals(newAmount, view.getTransactionsTableValueAt(1, 3));
  }
}