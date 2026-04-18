package controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import model.CSVExporter;
import model.CSVImporter;
import model.ExpenseTrackerModel;
import model.InputValidation;
import model.Transaction;
import view.ChartStrategy;
import view.ExpenseTrackerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import view.ChartStrategy;

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
        view.getDataPanelView().getAddTransactionBtn().addActionListener(e -> {
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

		view.getAnalysisPanelView().getGenerateChartBtn().addActionListener(e -> {
    		generateChart();
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
    	try {
    		// Get transaction data from view
    		double amount = view.getDataPanelView().getAmount(); 
    		String category = view.getDataPanelView().getCategory();

    		// Create transaction object
    		Transaction t = new Transaction(amount, category);

    		// Call controller to add transaction
    		model.addTransaction(t);
    		view.refresh();
    	}
    	catch (NumberFormatException nfe) {
    		view.displayErrorMessage("The amount cannot be parsed as a double number.");
    	}
    	catch (IllegalArgumentException iae) {
    		view.displayErrorMessage(iae.getMessage());
    	}
    }
    
    public void delete() {
        int selectedTransactionID = view.getDataPanelView().getSelectedTransactionID();
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

	/**
     * Handles the logic for generating a chart in the Analysis tab.
     * * This method fetches all transactions from the model, filters them based on the 
     * user-specified start and end dates from the view, and passes the filtered list 
     * to the active {@link ChartStrategy} for rendering. It also handles input validation 
     * and displays appropriate error messages if no transactions match the criteria.
     */
	public void generateChart() {
		List<Transaction> allTransactions = model.getTransactions();
		
		if (allTransactions == null || allTransactions.isEmpty()) {
			view.getAnalysisPanelView().clearChart();
			view.displayErrorMessage("No transactions available to generate a chart.");
			return;
		}

		String startDateText = view.getAnalysisPanelView().getStartDate();
		String endDateText = view.getAnalysisPanelView().getEndDate();

		if (startDateText == null || startDateText.trim().isEmpty() || 
			endDateText == null || endDateText.trim().isEmpty()) {
			view.displayErrorMessage("Please specify both a Start Date and an End Date (dd-MM-yyyy).");
			return;
		}

		List<Transaction> filteredTransactions = new ArrayList<>();

		try {
 			SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
  			inputFormat.setLenient(false); 
    
			SimpleDateFormat txnFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    		txnFormat.setLenient(false); 
			
			Date startDate = inputFormat.parse(startDateText);
			Date endDate = new Date(inputFormat.parse(endDateText).getTime() + 86399999L); 

			for (Transaction t : allTransactions) {
				Date txnDate = txnFormat.parse(t.getTimestamp());
				
				if (!txnDate.before(startDate) && !txnDate.after(endDate)) {
					filteredTransactions.add(t);
				}
			}
		} catch (ParseException ex) {
			view.displayErrorMessage("Invalid date format or invalid date. Please strictly use dd-MM-yyyy.");
			return;
		}

		if (filteredTransactions.isEmpty()) {
			view.getAnalysisPanelView().clearChart();
			view.displayErrorMessage("No transactions found for the specified time window.");
			return;
		}

		try {
			ChartStrategy strategy = view.getAnalysisPanelView().getChartStrategy();
			javax.swing.JPanel chartPanel = strategy.getChartPanel(filteredTransactions);
			view.getAnalysisPanelView().displayChart(chartPanel);
		} catch (Exception ex) {
			view.displayErrorMessage("Error generating chart: " + ex.getMessage());
		}
	}
}
