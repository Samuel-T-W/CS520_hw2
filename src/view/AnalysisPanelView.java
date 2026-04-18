package view;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the View component for the "Analysis" tab in the application.
 * * This panel provides the user interface for generating data visualizations, 
 * including input fields for specifying a time window (start and end dates) 
 * and a display area for rendering the charts.
 */
public class AnalysisPanelView extends JPanel {
    private ChartStrategy chartStrategy;
    private JPanel chartDisplayArea;
    private JButton generateChartBtn;
    
    private JTextField startDateField;
    private JTextField endDateField;

    public AnalysisPanelView() {
        setLayout(new BorderLayout());
        
        JPanel controlPanel = new JPanel();
        
        controlPanel.add(new JLabel("Start Date (dd-MM-yyyy):"));
        startDateField = new JTextField(10);
        controlPanel.add(startDateField);
        
        controlPanel.add(new JLabel("End Date (dd-MM-yyyy):"));
        endDateField = new JTextField(10);
        controlPanel.add(endDateField);
        
        generateChartBtn = new JButton("Generate Chart");
        controlPanel.add(generateChartBtn);
        
        add(controlPanel, BorderLayout.NORTH);

        chartDisplayArea = new JPanel(new BorderLayout());
        add(chartDisplayArea, BorderLayout.CENTER);
        
        this.chartStrategy = new PieChartStrategy(); 
    }

    /**
     * Retrieves the button used to trigger the chart generation.
     * * @return The "Generate Chart" JButton.
     */
    public JButton getGenerateChartBtn() {
        return generateChartBtn;
    }

    /**
     * Retrieves the user-inputted start date from the text field.
     * * @return The start date as a String (expected format: dd-MM-yyyy).
     */
    public String getStartDate() {
        return startDateField.getText();
    }

    /**
     * Retrieves the user-inputted end date from the text field.
     * * @return The end date as a String (expected format: dd-MM-yyyy).
     */
    public String getEndDate() {
        return endDateField.getText();
    }

    /**
     * Displays the provided chart panel in the center of the Analysis view.
     * * @param chartPanel The JPanel containing the rendered chart to display.
     */
    public void displayChart(JPanel chartPanel) {
        chartDisplayArea.removeAll();
        chartDisplayArea.add(chartPanel, BorderLayout.CENTER);
        chartDisplayArea.revalidate();
        chartDisplayArea.repaint();
    }

    /**
     * Clears the current chart from the display area. 
     * Used when an error occurs or when no transactions are found for the selected time window.
     */
    public void clearChart() {
        chartDisplayArea.removeAll();
        chartDisplayArea.revalidate();
        chartDisplayArea.repaint();
    }

    /**
     * Retrieves the currently active chart strategy (e.g., PieChartStrategy).
     * * @return The active {@link ChartStrategy}.
     */
    public ChartStrategy getChartStrategy() {
        return chartStrategy;
    }
}