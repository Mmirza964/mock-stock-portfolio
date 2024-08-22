import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Holds all the tests for the Stock class.
 */
public class StockTest {
  Stock s1;

  /**
   * Initializes the fields of the test class. This method is called before every test method.
   */
  @Before
  public void setUp() {
    s1 = new Stock("GOOG", 5.0, "2024-06-12");
  }

  /**
   * Tests inputting a negative number for shares in the addShares method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeParameterAddShares() {
    s1.addShares(-10);
  }

  /**
   * Tests inputting a negative number for shares in the sellShares method.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeParameterSellShares() {
    s1.sellShares(-10);
  }

  /**
   * Tests selling shares such that the number of shares will be negative.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeSharesAfterSellShares() {
    s1.sellShares(12.0);
  }

  /**
   * Tests selling partial shares such that the number of shares will be negative.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeSharesAfterSellPartialShares() {
    s1.sellShares(5.5);
  }

  /**
   * Tests that getTicker returns the correct information.
   */
  @Test
  public void testGetTicker() {
    assertEquals("GOOG", s1.getTicker());
  }

  /**
   * Tests that getQuantity returns the correct information.
   */
  @Test
  public void testGetQuantity() {
    assertEquals(5.0, s1.getQuantity(), 0.01);
  }

  /**
   * Tests that getPurchaseDate returns the correct information.
   */
  @Test
  public void testGetPurchaseDate() {
    assertEquals("2024-06-12", s1.getDatePurchased());
  }

  /**
   * Tests adding whole number shares normally of a stock.
   */
  @Test
  public void testAddSharesNormal() {
    Stock s = s1.addShares(12.0);
    assertEquals(17.0, s.getQuantity(), 0.01);
  }

  /**
   * Tests adding partial shares of a stock.
   */
  @Test
  public void testAddSharesPartialShares() {
    Stock s = s1.addShares(2.5);
    assertEquals(7.5, s.getQuantity(), 0.01);
  }

  /**
   * Tests selling partial shares of a stock.
   */
  @Test
  public void testSellSharesPartialShares() {
    Stock s = s1.sellShares(2.5);
    assertEquals(2.5, s.getQuantity(), 0.01);
  }

  /**
   * Tests selling shares of a stock.
   */
  @Test
  public void testSellSharesNormal() {
    Stock s = s1.sellShares(3.0);
    assertEquals(2.0, s.getQuantity(), 0.01);
  }
}