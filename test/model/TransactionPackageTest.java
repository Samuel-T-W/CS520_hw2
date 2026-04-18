package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TransactionPackageTest {

    @Test
    public void testPackagePrivateConstructor() {
        // Pre-condition: no transaction exists yet
        // Act: create a transaction using the package-private constructor (used by CSVImporter)
        Transaction t = new Transaction(25.0, "bills", "01-01-2025 10:00");
        // Post-condition: all fields are set directly without validation
        assertEquals(25.0, t.getAmount(), 0.001);
        assertEquals("bills", t.getCategory());
        assertEquals("01-01-2025 10:00", t.getTimestamp());
    }
}
