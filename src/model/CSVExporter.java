package model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * CSV (Comma Separated Value) implementation of {@link TransactionExporter}.
 */
public class CSVExporter implements TransactionExporter {

  public static final String COMMA_SEPARATOR = ",";
  public static final String CSV_HEADERS = "Amount" + COMMA_SEPARATOR + "Category" + COMMA_SEPARATOR + "Date";
  public static final String TRANSACTION_LIST_ERROR_MESSAGE = "The transaction list must be non-null and not empty.";
  public static final String FILENAME_ERROR_MESSAGE = "Filename must be non-empty and end with .csv";
    
  public boolean isValidFilename(String filename) {
	    if (filename == null) return false;
	    String trimmed = filename.trim();
	    if (trimmed.isEmpty()) return false;
	    // Disallow obvious path traversal
	    if (trimmed.contains("..")) return false;
	    // Use the file name portion for checking the extension so absolute paths are allowed
	    java.io.File f = new java.io.File(trimmed);
	    String name = f.getName();
	    if (name == null || name.trim().isEmpty()) return false;
	    String lower = name.toLowerCase();
	    if (!lower.endsWith(".csv")) return false;
	    // Ensure there's at least one character before the extension
	    if (name.length() <= 4) return false;
	    return true;
	  }
  
  @Override
  public String exportTransactions(List<Transaction> txns, String filename) {
    if (txns == null) return TRANSACTION_LIST_ERROR_MESSAGE;
    if (!isValidFilename(filename)) return FILENAME_ERROR_MESSAGE;

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
      bw.write(CSV_HEADERS);
      bw.newLine();
      for (Transaction t : txns) {
        String line = String.format("%s" + COMMA_SEPARATOR + "%s" + COMMA_SEPARATOR + "%s", Double.toString(t.getAmount()), t.getCategory(), t.getTimestamp());
        bw.write(line);
        bw.newLine();
      }
      bw.flush();
      return null;
    } catch (IOException e) {
      return e.getMessage();
    }
  }

}