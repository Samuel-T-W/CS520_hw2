package view;

import javax.swing.*;
import java.awt.*;

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

    public JButton getGenerateChartBtn() {
        return generateChartBtn;
    }

    public String getStartDate() {
        return startDateField.getText();
    }

    public String getEndDate() {
        return endDateField.getText();
    }

    public void displayChart(JPanel chartPanel) {
        chartDisplayArea.removeAll();
        chartDisplayArea.add(chartPanel, BorderLayout.CENTER);
        chartDisplayArea.revalidate();
        chartDisplayArea.repaint();
    }

    public void clearChart() {
        chartDisplayArea.removeAll();
        chartDisplayArea.revalidate();
        chartDisplayArea.repaint();
    }

    public ChartStrategy getChartStrategy() {
        return chartStrategy;
    }
}