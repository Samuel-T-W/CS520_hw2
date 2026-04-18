package view;

import model.Transaction;
import javax.swing.JPanel;
import java.util.List;

public interface ChartStrategy {
    JPanel getChartPanel(List<Transaction> transactions);
}