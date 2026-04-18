package view;

import model.Transaction;
import javax.swing.JPanel;
import java.util.List;
/**
 * The Strategy interface for generating different types of data visualizations.
 * * By applying the Strategy design pattern, the application can easily be extended
 * to support new chart types (e.g., Bar Charts, Line Charts) without modifying 
 * the core controller logic.
 */
public interface ChartStrategy {
    /**
     * Generates a chart visualization based on a list of transactions.
     * * @param transactions The list of transactions to be visualized.
     * @return A JPanel containing the rendered chart.
     */
    JPanel getChartPanel(List<Transaction> transactions);
}