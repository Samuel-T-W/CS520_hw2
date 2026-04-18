package view;

import model.Transaction;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import javax.swing.JPanel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A concrete implementation of the {@link ChartStrategy} interface that generates 
 * a Pie Chart using the XChart library. 
 * * This strategy groups transactions by their category and sums up the total amount 
 * spent in each category to render the visualization.
 */
public class PieChartStrategy implements ChartStrategy {
    
    /**
     * Groups the provided transactions by category, calculates the total cost per category, 
     * and builds an XChart PieChart.
     * * @param transactions The filtered list of transactions to visualize.
     * @return An XChartPanel wrapping the generated PieChart for Swing integration.
     */
    @Override
    public JPanel getChartPanel(List<Transaction> transactions) {
        // Group transactions by category and sum up the amounts
        Map<String, Double> expensesByCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        // Build the PieChart
        PieChart chart = new PieChartBuilder()
                .width(600)
                .height(400)
                .title("Expenses by Category")
                .build();

        // Add data series to the chart
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            chart.addSeries(entry.getKey(), entry.getValue());
        }

        // Return the chart wrapped in a JPanel for Swing integration
        return new XChartPanel<>(chart);
    }
}