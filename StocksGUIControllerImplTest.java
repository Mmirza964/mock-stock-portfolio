import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Holds all the tests for the GUI controller.
 */
public class StocksGUIControllerImplTest {
  StocksModel model;
  MockStocksGUIController controller;
  StocksGUIView view;

  /**
   * Sets up the controller with a model and view before every test.
   */
  @Before
  public void setUp() {
    model = new MockModel();
    view = new StocksGUIViewImpl();
    controller = new MockStocksGUIController(view, model);
  }

  /**
   * Tests that adding a portfolio is done correctly.
   */
  @Test
  public void testAddPortfolio() {
    controller.getPortfolioName("Test");
    assertEquals(1, model.getPortfolios().size());
    assertEquals("Test", model.getPortfolios().get(0).getName());
  }

  /**
   * Tests if the ticker is invalid.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddStockInvalidTicker() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "/", "5", "2024-06-03");
  }

  /**
   * Tests if the date is in an invalid format.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddStockInvalidDate() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024206-03");
  }

  /**
   * Tests if the date is a weekend.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddStockWeekend() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-08");
  }

  /**
   * Tests if the inputted quantity is not an integer when parsing.
   */
  @Test(expected = NumberFormatException.class)
  public void testAddStockQuantityNotInteger() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "-", "2024-06-08");
  }

  /**
   * Tests adding a normal stock.
   */
  @Test
  public void normalAddStock() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-03");
    assertEquals("GOOG", model.getPortfolios().get(0).getStock(0).getTicker());
    assertEquals(5.0, model.getPortfolios().get(0).getStock(0).getQuantity(), 0.01);
  }

  /**
   * Tests that removing more shares than there are in the portfolio of a certain stock
   * will result in an error.
   */
  @Test(expected = IllegalArgumentException.class)
  public void notEnoughSharesToRemove() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-03");
    controller.addOrSell("Test", "GOOG", "-10", "2024-06-03");
  }

  /**
   * Test removing a partial number of shares of a stock from the portfolio.
   */
  @Test
  public void normalRemoveStockPartial() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-03");
    controller.addOrSell("Test", "GOOG", "-3", "2024-06-03");
    assertEquals("GOOG", model.getPortfolios().get(0).getStock(0).getTicker());
    assertEquals(2.0, model.getPortfolios().get(0).getStock(0).getQuantity(), 0.01);
  }

  /**
   * Tests removing all the shares of a stock from the portfolio.
   */
  @Test
  public void normalRemoveStockFull() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-03");
    controller.addOrSell("Test", "GOOG", "-5", "2024-06-03");
    assertEquals(0, model.getPortfolios().get(0).getStockListSize());
  }

  /**
   * Tests removing shares where the shares were bought on two different days.
   */
  @Test
  public void normalRemoveStockDifferntDays() {
    controller.getPortfolioName("Test");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-03");
    controller.addOrSell("Test", "GOOG", "5", "2024-06-04");
    controller.addOrSell("Test", "GOOG", "-8", "2024-06-06");
    assertEquals("GOOG", model.getPortfolios().get(0).getStock(0).getTicker());
    assertEquals(2.0, model.getPortfolios().get(0).getStock(0).getQuantity(), 0.01);
    assertEquals("2024-06-04", model.getPortfolios().get(0).getStock(0).getDatePurchased());
  }
}