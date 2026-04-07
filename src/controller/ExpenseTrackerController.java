package controller;

import java.io.IOException;
import java.util.List;

import model.CSVExporter;
import model.CSVImporter;
import model.ExpenseTrackerModel;
import model.Transaction;
import view.ExpenseTrackerView;

/**
 * Provides the application programming layer to support the
 * following interface: addTransaction, delete, import, export.
 * 
 * NOTE) Represents the Controller in the MVC architecture pattern.
 */
public class ExpenseTrackerController {
	private ExpenseTrackerModel model = new ExpenseTrackerModel();    
    private ExpenseTrackerView view = new ExpenseTrackerView(model);
    
    public ExpenseTrackerController() {
    	super();
    	
    	// Hook up the view and controller
    	
        // Handle add transaction button clicks
        view.getAddTransactionBtn().addActionListener(e -> {
        	addTransaction();
        });
        
        // Handle "Delete" menu item clicks
        view.getDeleteMenuItem().addActionListener(e -> {
        	delete();
        });
        
        // Handle "Open File..." menu item clicks
        view.getOpenFileMenuItem().addActionListener(e -> {
        	openFile();
        });
        
        // Handle "Save" menu item clicks
        view.getSaveAsMenuItem().addActionListener(e -> {	  
        	saveAs();
        });
        
        // Initialize view
        view.setVisible(true);
    }
    
    public ExpenseTrackerModel getModel() {
    	// For testing purposes
    	return this.model;
    }
    
    public ExpenseTrackerView getView() {
    	// For testing purposes
    	return this.view;
    }
    
    public void addTransaction() {        
        // Get transaction data from view
        double amount = view.getAmount(); 
        String category = view.getCategory();

        // Create transaction object
        Transaction t = new Transaction(amount, category);

        // Call controller to add transaction
        model.addTransaction(t);
        view.refresh();
    }
    
    public void delete() {
        int selectedTransactionID = view.getSelectedTransactionID();
    	boolean removed = model.removeTransaction(selectedTransactionID);
    	if (! removed) {
    		view.displayErrorMessage("A valid transaction was not selected to be removed.");
    	}
    	else {
    		view.refresh();
    	}
    }
    
    public void openFile() {
    	String inputFileName = view.showFileChooser(true);
    	if (inputFileName != null) {  	    
    		int transactionCount = model.getTransactions().size();
    		for (int i = 0; i < transactionCount; i++) {
    			model.removeTransaction(0);
    		}

    		try {
    			CSVImporter csvImporter = new CSVImporter();
    			List<Transaction> importedTransactionsList = csvImporter.importTransactions(inputFileName);
    			for (Transaction importedTransaction : importedTransactionsList) {				
    				model.addTransaction(importedTransaction);
    			}
    		}
    		catch (IOException ioe) {
    			view.displayErrorMessage(ioe.getMessage());
    		}
    		view.refresh();
    	}
    }
    
    public void saveAs() {
    	String outputFileName = view.showFileChooser(false);
    	if (outputFileName != null) {
    		CSVExporter csvExporter = new CSVExporter();
    		String errorMessage = csvExporter.exportTransactions(model.getTransactions(), outputFileName);
    		if (errorMessage != null) {
    			view.displayErrorMessage(errorMessage);
    		}
    	}
    }
}
