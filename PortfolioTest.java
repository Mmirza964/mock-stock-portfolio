import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The PortfolioTest class holds all the tests for the Portfolio class.
 */
public class PortfolioTest {
  IPortfolio p1;

  /**
   * Initializes the fields in the class. Used in testing. Called before every test method.
   */
  @Before
  public void setUp() {
    p1 = new Portfolio("Portfolio 1");
  }

  /**
   * Tests if inputting a negative quantity will result in an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidQuantityInputAdd() {
    IPortfolio p = p1.addStock("GOOG", -1.0, "2024-06-12");
  }

  /**
   * Tests adding a stock to an empty list.
   */
  @Test
  public void testAddStockEmptyList() {
    IPortfolio p = p1.addStock("GOOG", 1.0, "2024-06-12");
    assertEquals(1, p.getStockListSize());
    assertEquals("GOOG", p.getStock(0).getTicker());
    assertEquals(1.0, p.getStock(0).getQuantity(), 0.01);
    assertEquals("2024-06-12", p.getStock(0).getDatePurchased());
  }

  /**
   * Tests adding multiple stocks to a portfolio.
   */
  @Test
  public void testAddMultipleStock() {
    IPortfolio p = p1.addStock("AAPL", 1.0, "2024-06-03")
            .addStock("GOOG", 5.0, "2024-06-03")
            .addStock("AMZN", 2.0, "2024-05-28");
    assertEquals(3, p.getStockListSize());

    String actual = "Portfolio Name: Portfolio 1\nAAPL: 1.0 shares purchased 2024-06-03\n" +
            "GOOG: 5.0 shares purchased 2024-06-03\nAMZN: 2.0 shares purchased 2024-05-28\n";
    assertEquals(actual, p.getOrganizedPortfolio("2024-06-04"));
  }

  /**
   * Tests adding shares of a stock that already exists in the portfolio.
   */
  @Test
  public void testAddSharesOfExistingStock() {
    IPortfolio p = p1.addStock("AAPL", 2.0, "2024-06-03")
            .addStock("GOOG", 2.0, "2024-05-28");
    assertEquals(2, p.getStockListSize());

    String actual1 = "Portfolio Name: Portfolio 1\nAAPL: 2.0 shares purchased 2024-06-03\n" +
            "GOOG: 2.0 shares purchased 2024-05-28\n";
    assertEquals(actual1, p.getOrganizedPortfolio("2024-06-04"));

    IPortfolio pTest = p.addStock("AAPL", 5.0, "2024-06-03");
    assertEquals(2, pTest.getStockListSize());

    String actual2 = "Portfolio Name: Portfolio 1\nAAPL: 7.0 shares purchased 2024-06-03\n" +
            "GOOG: 2.0 shares purchased 2024-05-28\n";
    assertEquals(actual2, pTest.getOrganizedPortfolio("2024-06-04"));
  }

  /**
   * Tests adding shares to a portfolio where the same stock is purchased on two different days.
   */
  @Test
  public void testAddSharesOfExistingStockDifferentDays() {
    IPortfolio p = p1.addStock("AAPL", 2.0, "2024-06-03")
            .addStock("GOOG", 2.0, "2024-05-28");
    assertEquals(2, p.getStockListSize());

    String actual1 = "Portfolio Name: Portfolio 1\nAAPL: 2.0 shares purchased 2024-06-03\n" +
            "GOOG: 2.0 shares purchased 2024-05-28\n";
    assertEquals(actual1, p.getOrganizedPortfolio("2024-06-04"));

    IPortfolio pTest = p.addStock("AAPL", 5.0, "2024-05-28");
    assertEquals(3, pTest.getStockListSize());
    String actual2 = "Portfolio Name: Portfolio 1\nAAPL: 2.0 shares purchased 2024-06-03\n" +
            "GOOG: 2.0 shares purchased 2024-05-28\nAAPL: 5.0 shares purchased 2024-05-28\n";
    assertEquals(actual2, pTest.getOrganizedPortfolio("2024-06-04"));
  }

  /**
   * Tests if trying to remove a stock from an empty portfolio.
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemovingStockEmptyList() {
    IPortfolio p = p1.removeStock("GOOG", 2.0, -1,false);
  }

  /**
   * Tests if inputting a negative quantity will result in an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidQuantityInputRemove() {
    IPortfolio p = p1.addStock("GOOG", 5, "2024-06-03")
            .removeStock("GOOG", -1, 0, false);
  }

  /**
   * Tests if removing more shares of a stock than there are in the portfolio
   * will result in an exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemovingMoreSharesThanInPortfolio() {
    IPortfolio p = p1.addStock("GOOG", 5, "2024-06-03")
            .removeStock("GOOG", 8, 0, true);
  }

  /**
   * Tests if trying to remove a stock that does not exist in the portfolio
   * will return an exception.
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void testRemovingStockNotInPortfolio() {
    IPortfolio p = p1.addStock("GOOG", 5, "2024-06-03")
            .addStock("AAPL", 5, "2024-06-03")
            .removeStock("AMZN", 3, -1, true);
  }

  /**
   * Tests removing all the shares of a stock in the portfolio. Tests to make sure the
   * stock is removed from the stock list.
   */
  @Test
  public void testRemovingStockCompletely() {
    IPortfolio p = p1.addStock("GOOG", 5, "2024-06-03")
            .addStock("AAPL", 5, "2024-06-03")
            .removeStock("GOOG", 5, 0, true);
    assertEquals(1, p.getStockListSize());

    String expected = "Portfolio Name: Portfolio 1\nAAPL: 5.0 shares purchased 2024-06-03\n";
    assertEquals(expected, p.getOrganizedPortfolio("2024-06-04"));
  }

  /**
   * Tests removing partial shares of stocks in the portfolio.
   */
  @Test
  public void testRemovingPartialShares() {
    IPortfolio p = p1.addStock("GOOG", 3, "2024-06-03")
            .addStock("MSFT", 5, "2024-06-03")
            .removeStock("MSFT", 2, 1, false)
            .removeStock("GOOG", 2, 0, false);
    assertEquals(2, p.getStockListSize());

    String expected = "Portfolio Name: Portfolio 1\nGOOG: 1.0 shares purchased 2024-06-03\n" +
            "MSFT: 3.0 shares purchased 2024-06-03\n";
    assertEquals(expected, p.getOrganizedPortfolio("2024-06-03"));
  }

  /**
   * Tests removing a combo of a full stock and multiple shares from the portfolio.
   */
  @Test
  public void testRemovingFullStockAndPartialShares() {
    IPortfolio p = p1.addStock("GOOG", 5, "2024-06-03")
            .addStock("AMZN", 3, "2024-06-03")
            .removeStock("GOOG", 3, 0, false)
            .removeStock("AMZN", 3, 1, true);
    assertEquals(1, p.getStockListSize());

    String expected = "Portfolio Name: Portfolio 1\nGOOG: 2.0 shares purchased 2024-06-03\n";
    assertEquals(expected, p.getOrganizedPortfolio("2024-06-03"));
  }

  /**
   * Tests getting the stock from an empty portfolio.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetStockEmptyPortfolio() {
    p1.getStock(0);
  }

  /**
   * Tests getting the stock using an empty index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetStockNegativeIndex() {
    p1.getStock(-1);
  }

  /**
   * Tests getting the stock with an invalid index.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetStockInvalidIndex() {
    p1.addStock("GOOG", 5, "2024-06-03")
            .addStock("AAPL", 5, "2024-06-03")
            .getStock(3);
  }

  /**
   * Tests getting the stock.
   */
  @Test
  public void testGetStockNormal() {
    Stock s = p1.addStock("GOOG", 5.0, "2024-06-03")
            .addStock("AAPL", 5.0, "2024-06-03")
            .getStock(1);
    assertEquals("AAPL", s.getTicker());
    assertEquals(5.0, s.getQuantity(), 0.01);
    assertEquals("2024-06-03", s.getDatePurchased());
  }

  /**
   * Tests getting the size of the stock list of an empty portfolio.
   */
  @Test
  public void testGetStockListSizeEmptyPortfolio() {
    int actual = p1.getStockListSize();
    assertEquals(0, actual);
  }

  /**
   * Tests getting the size of the stock list.
   */
  @Test
  public void testGetStockListSizeNormal() {
    int actual = p1.addStock("GOOG", 5, "2024-06-03")
            .addStock("AAPL", 5, "2024-06-03").getStockListSize();
    assertEquals(2, actual);
  }

  /**
   * Tests getting the name of the portfolio.
   */
  @Test
  public void testGetName() {
    String actual = p1.getName();
    assertEquals("Portfolio 1", actual);
  }

  /**
   * Tests getting an organized list of stocks from an empty portfolio.
   */
  @Test
  public void testGetOrganizedListEmptyPortfolio() {
    String actual = p1.getOrganizedPortfolio("2024-06-03");
    String expected = "Portfolio Name: Portfolio 1\nThe portfolio is empty. Please add stocks.\n";
    assertEquals(expected, actual);
  }

  /**
   * Tests getting an organized list of stocks.
   */
  @Test
  public void testGetOrganizedListNormal() {
    String actual = p1.addStock("GOOG", 5.0, "2024-06-03")
            .addStock("MSFT", 2.0, "2024-06-03")
            .getOrganizedPortfolio("2024-06-04");
    String expected = "Portfolio Name: Portfolio 1\nGOOG: 5.0 shares purchased 2024-06-03\n" +
            "MSFT: 2.0 shares purchased 2024-06-03\n";
    assertEquals(expected, actual);
  }

  /**
   * Tests if the ticker is in an empty portfolio.
   */
  @Test
  public void testContainsStockEmptyPortfolio() {
    boolean actual = p1.containsTicker("GOOG");
    assertFalse(actual);
  }

  /**
   * Tests if the ticker is in the portfolio.
   */
  @Test
  public void testContainsStockInputNotInPortfolio() {
    boolean actual = p1.addStock("AAPL", 5, "2024-06-03")
            .addStock("AMZN", 2, "2024-06-03").containsTicker("GOOG");
    assertFalse(actual);
  }

  /**
   * Tests if the ticker is in the portfolio.
   */
  @Test
  public void testContainsStockNormal() {
    boolean actual = p1.addStock("AAPL", 5, "2024-06-03")
            .addStock("AMZN", 2, "2024-06-03").containsTicker("AAPL");
    assertTrue(actual);
  }

  /**
   * Tests calling getIndexes on an empty portfolio. Makes sure it returns an empty list.
   */
  @Test
  public void testGetIndexesEmptyPortfolio() {
    List<Integer> actual = p1.getIndexes("GOOG", "2024-06-03");
    List<Integer> expected = new ArrayList<>();
    assertEquals(expected.size(), actual.size());
  }

  /**
   * Tests calling getIndexes on a stock that is not in the portfolio.
   * Makes sure it returns an empty list.
   */
  @Test
  public void testGetIndexesDifferentStock() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03");
    List<Integer> actual = p1.getIndexes("GOOG", "2024-06-03");
    List<Integer> expected = new ArrayList<>();
    assertEquals(expected.size(), actual.size());
  }

  /**
   * Tests calling getIndexes on a stock that exists in the portfolio.
   */
  @Test
  public void testGetIndexesNormal() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03");
    List<Integer> actual = p1.getIndexes("AAPL", "2024-06-03");
    List<Integer> expected = new ArrayList<>(0);
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  /**
   * Tests calling getIndexes on a stock exists in the portfolio but the stock was purchased on
   * two different days.
   */
  @Test
  public void testGetIndexesMultipleStocksDifferentDays() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03")
            .addStock("GOOG", 5.0, "2024-06-03")
            .addStock("AMZN", 2.0, "2024-06-03")
            .addStock("GOOG", 10.0, "2024-05-28");
    List<Integer> actual = p.getIndexes("GOOG", "2024-06-03");
    List<Integer> expected = new ArrayList<>();
    expected.add(1);
    expected.add(3);

    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  /**
   * Tests calling getIndexes on a stock exists in the portfolio but the method is called
   * before one of the purchases of the stock. The portfolio has two transactions for the stock
   * but getIndexes should only return one of them.
   */
  @Test
  public void testGetIndexesMultipleStocksDayConstraint() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03")
            .addStock("GOOG", 5.0, "2024-06-03")
            .addStock("AMZN", 2.0, "2024-06-03")
            .addStock("GOOG", 10.0, "2024-05-27");
    List<Integer> actual = p.getIndexes("GOOG", "2024-05-28");
    List<Integer> expected = new ArrayList<>();
    expected.add(3);

    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i), actual.get(i));
    }
  }

  /**
   * Tests getting the options on an empty portfolio.
   */
  @Test
  public void testRemoveStockOptionEmptyPortfolio() {
    String actual = p1.removeStockOptions("GOOG", "2024-06-03");
    assertEquals("", actual);
  }

  /**
   * Tests getting the options for a stock that is not in the portfolio.
   */
  @Test
  public void testRemoveStockOptionWrongStock() {
    IPortfolio p = p1.addStock("AAPL", 2.0, "2024-06-03");
    String actual = p.removeStockOptions("GOOG", "2024-06-03");
    assertEquals("", actual);
  }

  /**
   * Tests getting the options for a stock that was purchased after the specified date.
   */
  @Test
  public void testRemoveStockOptionIncorrectDate() {
    IPortfolio p = p1.addStock("AAPL", 2.0, "2024-06-03");
    String actual = p.removeStockOptions("AAPL", "2024-05-28");
    assertEquals("", actual);
  }

  /**
   * Tests getting the options for a stock.
   */
  @Test
  public void testRemoveStockOptionNormal() {
    IPortfolio p = p1.addStock("AAPL", 2.0, "2024-06-03")
            .addStock("GOOG", 5.0, "2024-06-03");
    String actual = p.removeStockOptions("AAPL", "2024-06-04");
    String expected = "1) AAPL: 2.0 shares purchased on 2024-06-03\n";
    assertEquals(expected, actual);
  }

  /**
   * Tests getting the options for a stock with multiple purchases.
   */
  @Test
  public void testRemoveStockOptionMultipleStocks() {
    IPortfolio p = p1.addStock("AAPL", 2.0, "2024-06-03")
            .addStock("GOOG", 5.0, "2024-06-03")
            .addStock("AAPL", 7.0, "2024-05-28")
            .addStock("MSFT", 2.0, "2024-06-03")
            .addStock("AAPL", 1.0, "2024-05-27");
    String actual = p.removeStockOptions("AAPL", "2024-06-04");
    String expected = "1) AAPL: 2.0 shares purchased on 2024-06-03\n"
            + "2) AAPL: 7.0 shares purchased on 2024-05-28\n"
            + "3) AAPL: 1.0 shares purchased on 2024-05-27\n";
    assertEquals(expected, actual);
  }

  /**
   * Tests getting the total shares of an empty portfolio.
   */
  @Test
  public void testGetTotalSharesEmptyPortfolio() {
    double actual = p1.getTotalShares("GOOG", "2024-06-03");
    assertEquals(0.0, actual, 0.01);
  }

  /**
   * Tests getting the total shares of a stock that doesn't exist in the portfolio.
   */
  @Test
  public void testGetTotalSharesWrongStock() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03");
    double actual = p.getTotalShares("GOOG", "2024-06-03");
    assertEquals(0.0, actual, 0.01);
  }

  /**
   * Tests getting the total shares of a stock that was purchased on a date later than the one
   * used to call the method.
   */
  @Test
  public void testGetTotalSharesPastDate() {
    IPortfolio p = p1.addStock("GOOG", 5.0, "2024-06-03");
    double actual = p.getTotalShares("GOOG", "2024-05-28");
    assertEquals(0.0, actual, 0.01);
  }

  /**
   * Tests getting the total shares of a stock.
   */
  @Test
  public void testGetTotalSharesOneStock() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03");
    double actual = p.getTotalShares("AAPL", "2024-06-03");
    assertEquals(5.0, actual, 0.01);
  }

  /**
   * Tests getting the total shares of a stock in a portfolio with multiple stocks.
   */
  @Test
  public void testGetTotalSharesMultipleStocks() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03")
            .addStock("GOOG", 2.0, "2024-06-03")
            .addStock("AMZN", 3.0, "2025-05-28")
            .addStock("GOOG", 10.0, "2024-05-27");
    double actual = p.getTotalShares("GOOG", "2024-06-04");
    assertEquals(12.0, actual, 0.01);
  }

  /**
   * Tests getting the total shares of a stock in a portfolio with multiple stocks where one of
   * the purchases are after the date of the method is called on.
   */
  @Test
  public void testGetTotalSharesMultipleStocksWrongDate() {
    IPortfolio p = p1.addStock("AAPL", 5.0, "2024-06-03")
            .addStock("GOOG", 2.0, "2024-06-03")
            .addStock("AMZN", 3.0, "2025-05-28")
            .addStock("GOOG", 10.0, "2024-05-28")
            .addStock("GOOG", 6.0, "2024-05-27");
    double actual = p.getTotalShares("GOOG", "2024-05-28");
    assertEquals(16.0, actual, 0.01);
  }

  /**
   * Tests calling distribution of value on an empty portfolio.
   */
  @Test
  public void testDistributionOfValueEmptyPortfolio() {
    List<Stock> actual = p1.distributionOfValue("2024-06-03");
    assertTrue(actual.isEmpty());
  }

  /**
   * Tests calling distribution of value where the stock in the portfolio was purchased later
   * than the date the method is called on.
   */
  @Test
  public void testDistributionOfValueWrongDate() {
    IPortfolio p = p1.addStock("GOOG", 5.0, "2024-06-03");
    List<Stock> actual = p.distributionOfValue("2024-05-28");
    assertTrue(actual.isEmpty());
  }

  /**
   * Tests calling distribution of value on a portfolio with multiple stocks in the date range.
   */
  @Test
  public void testDistributionOfValueNormal() {
    IPortfolio p = p1.addStock("GOOG", 5.0, "2024-06-03")
            .addStock("AAPL", 5.0, "2024-06-03");
    List<Stock> actual = p.distributionOfValue("2024-06-04");
    List<Stock> expected = new ArrayList<>();
    expected.add(new Stock("GOOG", 5.0, ""));
    expected.add(new Stock("AAPL", 5.0, ""));
    assertEquals(expected.size(), actual.size());

    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i).getTicker(), actual.get(i).getTicker());
      assertEquals(expected.get(i).getQuantity(), actual.get(i).getQuantity(), 0.01);
      assertEquals(expected.get(i).getDatePurchased(), actual.get(i).getDatePurchased());
    }
  }

  /**
   * Tests calling distribution of value on a portfolio with multiple stocks in the date range
   * and where one was purchased on a different day.
   */
  @Test
  public void testDistributionOfCombineShares() {
    IPortfolio p = p1.addStock("GOOG", 5.0, "2024-06-03")
            .addStock("GOOG", 5.0, "2024-05-28")
            .addStock("AAPL", 5.0, "2024-06-03");
    List<Stock> actual = p.distributionOfValue("2024-06-04");
    List<Stock> expected = new ArrayList<>();
    expected.add(new Stock("GOOG", 10.0, ""));
    expected.add(new Stock("AAPL", 5.0, ""));
    assertEquals(expected.size(), actual.size());

    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i).getTicker(), actual.get(i).getTicker());
      assertEquals(expected.get(i).getQuantity(), actual.get(i).getQuantity(), 0.01);
      assertEquals(expected.get(i).getDatePurchased(), actual.get(i).getDatePurchased());
    }
  }

  /**
   * Tests calling distribution of value on a portfolio with multiple stocks in the date range
   * and where one was purchased on a date after the date calling on the method.
   */
  @Test
  public void testDistributionOfWrongDate() {
    IPortfolio p = p1.addStock("GOOG", 5.0, "2024-05-28")
            .addStock("AMZN", 5.0, "2024-06-03")
            .addStock("AAPL", 5.0, "2024-05-27");
    List<Stock> actual = p.distributionOfValue("2024-05-28");
    List<Stock> expected = new ArrayList<>();
    expected.add(new Stock("GOOG", 5.0, ""));
    expected.add(new Stock("AAPL", 5.0, ""));
    assertEquals(expected.size(), actual.size());

    for (int i = 0; i < actual.size(); i++) {
      assertEquals(expected.get(i).getTicker(), actual.get(i).getTicker());
      assertEquals(expected.get(i).getQuantity(), actual.get(i).getQuantity(), 0.01);
      assertEquals(expected.get(i).getDatePurchased(), actual.get(i).getDatePurchased());
    }
  }

  /**
   * Tests get date before a date is set.
   */
  @Test
  public void testGetDate() {
    String actual = p1.getDate();
    assertEquals("", actual);
  }

  /**
   * Tests setting a date.
   */
  @Test
  public void testSetDate() {
    String actual1 = p1.getDate();
    assertEquals("", actual1);

    p1.setDate("Test");
    String actual2 = p1.getDate();
    assertEquals("Test", actual2);
  }
}


