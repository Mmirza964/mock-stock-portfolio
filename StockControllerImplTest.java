import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Represents a Mock controller to test view and model.
 */
public class StockControllerImplTest {
  private MockModel mockModel;
  private MockView mockview;
  private StocksController controller;
  private String allStockData;

  /**
   * Sets up mock model and view to be used for testing.
   * Also gets all data for the stock GOOG and puts it into allStockData.
   */
  @Before
  public void setUp() {
    mockModel = new MockModel();
    mockview = new MockView();
    InputStream input = new ByteArrayInputStream("6\n".getBytes());
    ByteArrayInputStream testIn = new ByteArrayInputStream("6\n".getBytes());
    controller = new StocksControllerImpl(mockModel, mockview, input);
    allStockData = mockModel.checkStockPrice("GOOG");
  }


  /**
   * Sets up data.
   *
   * @param data the strings you want to input.
   *             Will input whatever string is the parameter.
   *             This will make it so the program thinks
   *             you input a valid sequence when you want to test just 1 part.
   */
  public void setupInput(String data) {
    ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
    controller = new StocksControllerImpl(mockModel, mockview, testIn);
  }


  /**
   * Adds a portfolio, and checks portfolio size is now 1.
   */
  @Test
  public void testNewPortfolio() {
    this.mockModel.newPortfolio("Port");
    assertEquals(this.mockModel.getPortfolios().size(), 1);
  }

  /**
   * Tests removing a portfolio.
   */
  @Test
  public void testRemovePortfolio() {
    this.mockModel.newPortfolio("Port");
    assertEquals(this.mockModel.getPortfolios().size(), 1);
    this.mockModel.deletePortfolio(0);
    assertEquals(this.mockModel.getPortfolios().size(), 0);
  }

  /**
   * Tests the first message displayed by the view when creating a new portfolio.
   */
  @Test
  public void testCreatePortfolioOutput1() {
    setupInput("5\n1\nPortfolio1\n4\n6\n");
    controller.execute();
    String expected = "Pick which option you would like to continue with:\n"
            + "1) Create a new portfolio\n"
            + "2) Access an existing portfolio.\n"
            + "3) Load an existing portfolio.\n"
            + "4) Exit.\n";
    assertEquals(expected, mockview.getMsg().get(2));
  }

  /**
   * Tests the second message displayed by the view when creating a new portfolio.
   */
  @Test
  public void testCreatePortfolioOutput2() {
    setupInput("5\n1\nPortfolio1\n4\n6\n");
    controller.execute();
    String expected = "Please enter a name for the portfolio:\n";
    assertEquals(expected, mockview.getMsg().get(3));
  }

  /**
   * Tests the third message displayed by the view when creating a new portfolio.
   */
  @Test
  public void testCreatePortfolioOutput3() {
    setupInput("5\n1\nPortfolio1\n4\n6\n");
    controller.execute();
    String expected = "Successfully create the new portfolio Portfolio1\n";
    assertEquals(expected, mockview.getMsg().get(4));
  }

  /**
   * Tests the first message displayed by the view when accessing a new portfolio.
   */
  @Test
  public void testAccessPortfolioOutput1() {
    setupInput("5\n1\nPortfolio1\n2\n1\n10\n4\n6\n");
    controller.execute();
    String expected = "Please select a portfolio (type a number starting at 1): [Portfolio1, ]\n";
    assertEquals(expected, mockview.getMsg().get(6));
  }

  /**
   * Tests the second message displayed by the view when accessing a new portfolio.
   */
  @Test
  public void testAccessPortfolioOutput2() {
    setupInput("5\n1\nPortfolio1\n2\n1\n10\n4\n6\n");
    controller.execute();
    String expected = "Select an action to preform on the portfolio:\n"
            + "1) Add a stock to the portfolio.\n"
            + "2) Remove a stock from the portfolio.\n"
            + "3) Get the total value of the portfolio at a specific day.\n"
            + "4) Get portfolio composition.\n"
            + "5) Delete the portfolio.\n"
            + "6) Get distribution of value.\n"
            + "7) Save this portfolio.\n"
            + "8) Re-balance portfolio.\n"
            + "9) View performance over time.\n"
            + "10) Exit.\n";
    assertEquals(expected, mockview.getMsg().get(7));
  }

  /**
   * Tests the first message displayed by the view when adding a stock to a portfolio.
   */
  @Test
  public void testAddStockOutput1() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Type in the ticker of the stock you wish to add to the portfolio:\n";
    assertEquals(expected, mockview.getMsg().get(8));
  }

  /**
   * Tests the second message displayed by the view when adding a stock to a portfolio.
   */
  @Test
  public void testAddStockOutput2() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Type in the number of shares you wish to add of GOOG\n";
    assertEquals(expected, mockview.getMsg().get(9));
  }

  /**
   * Tests the third message displayed by the view when adding a stock to a portfolio.
   */
  @Test
  public void testAddStockOutput3() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Input a valid date in YYYY-MM-DD format.\n";
    assertEquals(expected, mockview.getMsg().get(10));
  }

  /**
   * Tests the third message displayed by the view when adding a stock to a portfolio.
   */
  @Test
  public void testAddStockOutput4() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Successfully added 2 shares of GOOG to the portfolio.\n";
    assertEquals(expected, mockview.getMsg().get(11));
  }

  /**
   * Tests the fourth message displayed by the view when adding a stock to a portfolio.
   */
  @Test
  public void testAddStockOutput5() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Select an action to preform on the portfolio:\n"
            + "1) Add a stock to the portfolio.\n"
            + "2) Remove a stock from the portfolio.\n"
            + "3) Get the total value of the portfolio at a specific day.\n"
            + "4) Get portfolio composition.\n"
            + "5) Delete the portfolio.\n"
            + "6) Get distribution of value.\n"
            + "7) Save this portfolio.\n"
            + "8) Re-balance portfolio.\n"
            + "9) View performance over time.\n"
            + "10) Exit.\n";
    assertEquals(expected, mockview.getMsg().get(12));
  }

  /**
   * Tests if the user is prompted to input the date again after inputting an invalid date.
   */
  @Test
  public void testInvalidDateAddStockOutput1() {
    setupInput("5\n3\n2\n1\nGOOG\n5\n20204-06-03\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Input a valid date in YYYY-MM-DD format.\n";
    assertEquals(expected, mockview.getMsg().get(11));
  }

  /**
   * Tests if the user is prompted to input the date again after inputting an invalid date.
   */
  @Test
  public void testWeekendAddStockOutput1() {
    setupInput("5\n3\n2\n1\nGOOG\n5\n2024-06-08\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "This is a weekend. Please pick a date that is Monday-Friday.\n";
    assertEquals(expected, mockview.getMsg().get(11));
  }

  /**
   * Tests the first message displayed by the view when removing a stock from a portfolio.
   */
  @Test
  public void testRemoveStockOutput1() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n2\nGOOG\n2024-06-03\n2\n10\n4\n" +
            "6\n");
    controller.execute();
    String expected = "Type in the ticker of the stock you wish to remove from the portfolio:\n";
    assertEquals(expected, mockview.getMsg().get(13));
  }

  /**
   * Tests the second message displayed by the view when removing a stock from a portfolio.
   */
  @Test
  public void testRemoveStockOutput2() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n2\nGOOG\n2024-06-03\n2\n10\n4\n" +
            "6\n");
    controller.execute();
    String expected = "Enter a date in format YYYY-MM-DD\n";
    assertEquals(expected, mockview.getMsg().get(14));
  }

  /**
   * Tests the third message displayed by the view when removing a stock from a portfolio.
   */
  @Test
  public void testRemoveStockOutput3() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n2\nGOOG\n2024-06-03\n2\n10\n4\n" +
            "6\n");
    controller.execute();
    String expected = "Type in the number of shares you wish to remove of GOOG\n";
    assertEquals(expected, mockview.getMsg().get(15));
  }

  /**
   * Tests the fourth message displayed by the view when removing a stock from a portfolio.
   */
  @Test
  public void testRemoveStockOutput4() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n2\nGOOG\n2024-06-03\n2\n10\n4\n" +
            "6\n");
    controller.execute();
    String expected = "Successfully removed 2 shares of GOOG from the portfolio.\n";
    assertEquals(expected, mockview.getMsg().get(16));
  }

  /**
   * Tests the fifth message displayed by the view when removing a stock from a portfolio.
   */
  @Test
  public void testRemoveStockOutput5() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n2\nGOOG\n2024-06-03\n2\n10\n4\n" +
            "6\n");
    controller.execute();
    String expected = "Select an action to preform on the portfolio:\n"
            + "1) Add a stock to the portfolio.\n"
            + "2) Remove a stock from the portfolio.\n"
            + "3) Get the total value of the portfolio at a specific day.\n"
            + "4) Get portfolio composition.\n"
            + "5) Delete the portfolio.\n"
            + "6) Get distribution of value.\n"
            + "7) Save this portfolio.\n"
            + "8) Re-balance portfolio.\n"
            + "9) View performance over time.\n"
            + "10) Exit.\n";
    assertEquals(expected, mockview.getMsg().get(17));
  }

  /**
   * Tests if the user is prompted to input the date again after inputting an invalid date.
   */
  @Test
  public void testRemoveStockMoreSharesThanInPortfolioOutput1() {
    setupInput("5\n3\n2\n2\nGOOG\n2024-06-03\n10\nGOOG\n2024-06-03\n5\n10\n4\n6\n10\n4\n6\n");
    controller.execute();
    String expected = "There are not enough shares in the portfolio to sell 10 shares." +
            "Please try again.\n";
    assertEquals(expected, mockview.getMsg().get(11));
  }

  /**
   * Tests if the user is prompted to input the date again after inputting an invalid date.
   */
  @Test
  public void testInvalidDateRemoveStockOutput1() {
    setupInput("5\n3\n2\n2\nGOOG\n20204-06-03\n2024-06-03\n5\n10\n4\n6\n");
    controller.execute();
    String expected = "Input a valid date in YYYY-MM-DD format.\n";
    assertEquals(expected, mockview.getMsg().get(10));
  }

  /**
   * Tests if the user is prompted to input the date again after inputting an invalid date.
   */
  @Test
  public void testWeekendRemoveStockOutput1() {
    setupInput("5\n3\n2\n2\nGOOG\n2024-06-08\n2024-06-03\n5\n10\n4\n6\n");
    controller.execute();
    String expected = "This is a weekend. Please pick a date that is Monday-Friday.\n";
    assertEquals(expected, mockview.getMsg().get(10));
  }

  /**
   * Tests the first message displayed by the view when getting the value of the portfolio.
   */
  @Test
  public void testPortfolioValueOutput1() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n3\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Input a valid date in YYYY-MM-DD format.\n";
    assertEquals(expected, mockview.getMsg().get(13));
  }

  /**
   * Tests the second message displayed by the view when getting the value of the portfolio.
   */
  @Test
  public void testPortfolioValueOutput2() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n3\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "The value of the portfolio is $348.84\n";
    assertEquals(expected, mockview.getMsg().get(14));
  }

  /**
   * Tests the third message displayed by the view when removing a stock from a portfolio.
   */
  @Test
  public void testPortfolioValueOutput3() {
    setupInput("5\n1\nPortfolio1\n2\n1\n1\nGOOG\n2\n2024-06-03\n3\n2024-06-03\n10\n4\n6\n");
    controller.execute();
    String expected = "Select an action to preform on the portfolio:\n"
            + "1) Add a stock to the portfolio.\n"
            + "2) Remove a stock from the portfolio.\n"
            + "3) Get the total value of the portfolio at a specific day.\n"
            + "4) Get portfolio composition.\n"
            + "5) Delete the portfolio.\n"
            + "6) Get distribution of value.\n"
            + "7) Save this portfolio.\n"
            + "8) Re-balance portfolio.\n"
            + "9) View performance over time.\n"
            + "10) Exit.\n";
    assertEquals(expected, mockview.getMsg().get(15));
  }

  /**
   * Tests the first output when getting the protfolio overview.
   */
  @Test
  public void testPortfolioOverviewOutput1() {
    setupInput("5\n1\nPort1\n2\n1\n1\nGOOG\n5\n2024-06-03\n1\nAAPL\n3\n2024-06-03\n4\n2024-06-03" +
            "\n10\n4\n6\n10\n4\n6\n");
    controller.execute();
    String expected = "Portfolio Name: Port1\nGOOG: 5.0 shares purchased 2024-06-03\nAAPL:" +
            " 3.0 shares purchased 2024-06-03\n";
    List<String> m = mockview.getMsg();
    assertEquals(expected, mockview.getMsg().get(19));
  }

  /**
   * Tests the output when the portfolio is deleted.
   */
  @Test
  public void testDeletePortfolio() {
    setupInput("5\n1\nPortfolio1\n2\n1\n5\n4\n6\n");
    controller.execute();
    String expected = "Pick which option you would like to continue with:\n"
            + "1) Create a new portfolio\n"
            + "2) Access an existing portfolio.\n"
            + "3) Load an existing portfolio.\n"
            + "4) Exit.\n";
    assertEquals(expected, mockview.getMsg().get(5));
  }

  /**
   * Tests invalid stock.
   */
  @Test
  public void testInvalidStock() {
    assertEquals(false, this.mockModel.checkValidStock("XXXZZ"));
  }

  @Test
  public void testInvalidDate() {
    assertEquals(false, StocksModelImpl.isValidDate("2000-13-20",
            "yyyy-MM-dd"));
  }

  /**
   * Tests displaying portfolio.
   */
  @Test
  public void testdisplayPortfolio() {
    this.mockModel.newPortfolio("Port");
    assertEquals("[Port, ]", this.mockModel.displayPortfolios());
  }

  @Test
  public void testPortValue() {
    this.mockModel.newPortfolio("Port");
    this.mockModel.addStock(0, "GOOG", 5.0, "2024-06-03");
    assertEquals("The value of the portfolio is 872.10\n",
            this.mockModel.portfolioValue("2024-06-03", 0));
  }

  /**
   * Tests adding a stock to portfolio.
   */
  @Test
  public void testAddStock() {
    this.mockModel.newPortfolio("Port");
    this.mockModel.addStock(0, "GOOG", 5, "2024-06-03");
    assertEquals(this.mockModel.getPortfolios().get(0).getOrganizedPortfolio("2024-06-03"),
            "Portfolio Name: Port\nGOOG: 5.0 shares purchased 2024-06-03\n");
  }

  /**
   * Tests removing a stock from a portfolio.
   */
  @Test
  public void testRemoveStock() {
    this.mockModel.newPortfolio("Port");
    this.mockModel.addStock(0, "GOOG", 5, "2024-06-03");
    assertEquals(this.mockModel.getPortfolios().get(0).getOrganizedPortfolio("2024-06-03"),
            "Portfolio Name: Port\nGOOG: 5.0 shares purchased 2024-06-03\n");
    this.mockModel.removeStock(0, "GOOG", 3, 0, false);
    assertEquals(this.mockModel.getPortfolios().get(0).getOrganizedPortfolio("2024-06-03"),
            "Portfolio Name: Port\nGOOG: 2.0 shares purchased 2024-06-03\n");
  }


  /**
   * Tests 10 day crossover.
   */
  @Test
  public void testCrossovers() {
    double avg = this.mockModel.movingavg(allStockData, 3, 6, 2024, "GOOG", 10);
    ArrayList<String> x = this.mockModel.xDayCrossover(allStockData, 3, 6, 2024,
            10, avg);
    assertEquals("There were cross overs on the following dates:\n", x.get(0));
    assertEquals("There was a low to high crossover on 2024-05-23\n", x.get(1));
  }

  /**
   * Tests 10 day moving avg.
   */
  @Test
  public void test10DayMovingAvg() {
    double avg = this.mockModel.movingavg(allStockData, 3, 6, 2024, "GOOG", 10);
    assertEquals(avg, 176.55, .01);
  }


  /**
   * Tests that ParseHelper gets the correct price of GOOG
   * stock on a valid date.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testParseHelperInvalidDate() {
    double x = this.mockModel.parseHelper("2024-06-08", allStockData, "GOOG");
    assertEquals(174.42, x, .01);
  }

  /**
   * Tests that ParseHelper gets the correct price of GOOG
   * stock on a valid date.
   */
  @Test
  public void testParseHelper() {
    double x = this.mockModel.parseHelper("2024-06-03", allStockData, "GOOG");
    assertEquals(174.42, x, .01);
  }


  /**
   * Checks that the first welcome msg is sent correctly.
   */
  @Test
  public void testWelcomeMsg() {
    controller.execute();
    assertEquals(this.mockview.getMsg().get(0), "Hello, this application is focused on " +
            "stocks and can be used to help you\n" +
            "create a fake portfolio that uses real life data. With this application\n" +
            "you can determine the gain or loss of a stock over a period of time, determine \n" +
            "the X day moving average, and along with this create one or more portfolios\n" +
            "new portfolio and find the value of this portfolio on any date you want.\n" +
            "You can start a by inputting 'new portfolio' or you can determine data on\n" +
            "a specific stock by inputting it's unique ticker symbol. All transactions done to\n" +
            " mock portfolios must be executed in chronological order.\n");
  }

  /**
   * Checks the second welcome message, the one which holds the options is sent correctly.
   */
  @Test
  public void testWelcomeOptions() {
    controller.execute();
    assertEquals(this.mockview.getMsg().get(1), "Pick which option you would like to " +
            "continue with:\n" +
            "1) Examine the gain or loss of a stock over a specified period.\n" +
            "2) Examine the x-day moving average of a stock for a specified date\n" +
            " and a specified value of x\n" +
            "3) Determine which days are x-day crossovers any stock over a specified date range " +
            "with any value x.\n" +
            "4) View stock performance.\n" +
            "5) Portfolio Manager\n" +
            "6) End Program\n");

  }

  /**
   * Tests trying to access a portfolio index that is not valid.
   */
  @Test
  public void testAccessPortfolioErrorMessageIndex() {
    setupInput("5\n1\nPortfolio1\n2\n4\n1\n10\n4\n6\n");
    controller.execute();
    String expected = "Please select a portfolio (type a number starting at 1): [Portfolio1, ]\n";
    assertEquals(expected, mockview.getMsg().get(6));
  }

  /**
   * Tests trying to access a portfolio when there are no portfolios created by the user.
   */

  @Test
  public void testAccessPortfolioErrorMessageNoPortfolio() {
    setupInput("5\n2\n4\n6\n");
    controller.execute();
    String expected = "There are no portfolios available. Please create one before trying to " +
            "access.\n";
    assertEquals(expected, mockview.getMsg().get(3));
  }

  /**
   * Tests trying to remove a stock from the portfolio when there are no stocks in the portfolio.
   */

  @Test
  public void testRemoveStockNoStockInPortfolio() {
    setupInput("5\n1\nPortfolio1\n2\n1\n2\n10\n4\n6\n");
    controller.execute();
    String expected = "Cannot remove a stock if there are no stocks in the portfolio." +
            "Please add a stock first.\n";
    assertEquals(expected, mockview.getMsg().get(8));
  }

  /**
   * Tests trying to get the value of an empty portfolio.
   */
  @Test
  public void testPortfolioValueEmptyPortfolio() {
    setupInput("5\n1\nPortfolio1\n2\n1\n3\n10\n4\n6\n");
    controller.execute();
    String expected = "Portfolio is empty, cannot get the value. Please add a stock first.\n";
    List<String> m = mockview.getMsg();
    assertEquals(expected, mockview.getMsg().get(8));
  }


  /**
   * Checks that the correct message is displayed after options
   * once you have picked a choice.
   */
  @Test
  public void testValidChoice() {
    setupInput("1\nGOOG\n2024-06-03\n2024-05-28\nS\n");
    controller.execute();
    assertEquals("To determine the gain or loss of a stock over a period of time\n" +
                    "please input the ticker symbol for the stock you want to look into.\n",
            mockview.getMsg().get(2));
  }

  /**
   * Checks that the correct message is displayed after options
   * once you have picked a stock.
   */
  @Test
  public void testDate() {
    setupInput("1\nGOOG\n2024-05-28\n2024-05-03\nS\n");
    controller.execute();
    assertEquals("To determine the gain or loss over a period of time you will need to " +
            "input\n" +
            "two dates, the start and end end dates. Please input the date in 'YYYY-MM-DD'" +
            " format.\n" +
            "Any values which are not an integer will be ignored.\n", mockview.getMsg().get(3));
  }

  /**
   * Checks that the correct message is displayed after options
   * once you have picked a stock and first date.
   */
  @Test
  public void testSecDate() {
    setupInput("1\nGOOG\n2024-05-28\n2024-05-03\nS\n");
    controller.execute();
    assertEquals("Please input the the second date in 'YYYY-MM-DD' format.\n",
            mockview.getMsg().get(4));
  }


  /**
   * Checks that the correct message is displayed after options
   * once you have picked a stock and both dates.
   */
  @Test
  public void testCorrectDiff() {
    setupInput("1\nGOOG\n2024-05-28\n2024-05-03\nS\n");
    controller.execute();
    assertEquals("The total gain or loss between the two days you provided are $9.03"
            + "\n", mockview.getMsg().get(5));
  }

  /**
   * Checks that the correct ending msg is displayed.
   */
  @Test
  public void testCorrectEndMsg() {
    setupInput("1\nGOOG\n2024-05-28\n2024-05-03\nS\n");
    controller.execute();
    assertEquals("Press C if you would like to restart, or input anything else " +
            "to quit the application.\n", mockview.getMsg().get(6));
  }

  /**
   * Checks that the correct message is displayed after options
   * once you have picked a choice.
   */
  @Test
  public void testValidOptiontwo() {
    setupInput("2\nGOOG\n2024-06-03\n10\nS\n");
    controller.execute();
    assertEquals("To determine the x day moving avg, you need to provide" +
                    " ticker symbol for the stock you want to see\n",
            mockview.getMsg().get(2));
  }

  /**
   * Checks that the correct message is displayed after options
   * once you have picked a choice and stock.
   */
  @Test
  public void testValidDateOptiontwo() {
    setupInput("2\nGOOG\n2024-06-03\n10\nS\n");
    controller.execute();
    assertEquals("Please input a date in YYYY-MM-DD format.\n",
            mockview.getMsg().get(3));
  }

  /**
   * Checks that the correct message is displayed after options
   * once you have picked a choice, picked a stock, and
   * picked the number of days you want to go back.
   */
  @Test
  public void testValidXDaysOptionTwo() {
    setupInput("2\nGOOG\n2024-06-03\n10\nS\n");
    controller.execute();
    assertEquals("Please input the x number of days you want to check.\n",
            mockview.getMsg().get(4));
  }

  /**
   * Checks that the correct message is displayed after
   * you have input all information, and it should display you the moving avg.
   */
  @Test
  public void testValidCorrectXDayOptionTwo() {
    setupInput("2\nGOOG\n2024-06-03\n10\nS\n");
    controller.execute();
    assertEquals("The last 10 day moving avg of GOOG is $176.55.\n",
            mockview.getMsg().get(5));
  }

  /**
   * Checks that the correct message is displayed after picking a choice.
   */
  @Test
  public void testvalidCrossover() {
    setupInput("3\nGOOG\n2024-06-03\n10\ns\n");
    controller.execute();
    assertEquals("Which stock would you like to see an X day crossover for?\n",
            mockview.getMsg().get(2));
  }

  /**
   * Checks that the correct message is displayed after picking a choice
   * and stock.
   */
  @Test
  public void testValidCrossoverDate() {
    setupInput("3\nGOOG\n2024-06-03\n10\ns\n");
    controller.execute();
    assertEquals("Please input a date in YYYY-MM-DD format.\n"
            , mockview.getMsg().get(3));
  }


  /**
   * Checks that the correct message is displayed after picking a choice
   * and stock, and inputting a valid date.
   */
  @Test
  public void testValidXdayCrossoverDate() {
    setupInput("3\nGOOG\n2024-06-03\n10\ns\n");
    controller.execute();
    assertEquals("Please input the X number of days you want to see the crossover of.\n"
            , mockview.getMsg().get(4));
  }

  /**
   * Checks that the correct starting msg is displayed
   * before the program outputs what dates were crossovers.
   */
  @Test
  public void testValidCrossoverXdayOutput() {
    setupInput("3\nGOOG\n2024-06-03\n10\ns\n");
    controller.execute();
    assertEquals("There were cross overs on the following dates:\n"
            , mockview.getMsg().get(5));
  }

  /**
   * Checks that some crossover date was input.
   */
  @Test
  public void testValidCrossoverXdayOutput1st() {
    setupInput("3\nGOOG\n2024-06-03\n10\ns\n");
    controller.execute();
    assertEquals("There was a low to high crossover on 2024-05-23\n"
            , mockview.getMsg().get(6));
  }

  /**
   * Checks that the correct ending message is displayed.
   */
  @Test
  public void testValidCrossoverXdayOutput2nd() {
    setupInput("3\nGOOG\n2024-06-03\n10\ns\n");
    controller.execute();
    assertEquals("Press C if you would like to restart, or input anything else " +
                    "to quit the application\n"
            , mockview.getMsg().get(7));
  }

  /**
   * Tests rebalancing output 1.
   */
  @Test
  public void testRebalancePortLoadingPort() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(3), "Input which portfolio you would like to load" +
            " (starting at 1):\n");
  }

  /**
   * Tests rebalancing output 2.
   */
  @Test
  public void testRebalancePortSelectingPort() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(5), "2) MATT\n");
  }

  /**
   * Tests rebalancing output 3.
   */
  @Test
  public void testRebalancePortValidDateInput() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(8), "Input a valid date in YYYY-MM-DD format.\n");
  }

  /**
   * Tests rebalancing output 4.
   */
  @Test
  public void testRebalancePortGetDistribution() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(9), "The current distribution of value is below:\n" +
            "Distribution of Value - Portfolio: MATT\n" +
            "GOOG: $891.75 - 20.18%\n" +
            "AMZN: $555.00 - 12.56%\n" +
            "MSFT: $2971.64 - 67.26%\n" +
            "-------------------------\n" +
            "Total: $4418.39 - 100.00%\n\n");
  }

  /**
   * Tests rebalancing output 5.
   */
  @Test
  public void testRebalancePortAskFirstPercent() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(10), "Please enter the percentage weight of GOOG." +
            " Enter the percentage as a whole number (ie. 40 for 40%).\n");
  }

  /**
   * Tests rebalancing output 6.
   */
  @Test
  public void testRebalancePortAskSecondPercent() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(11), "Please enter the percentage weight of AMZN." +
            " Enter the percentage as a whole number (ie. 40 for 40%).\n");
  }

  /**
   * Tests rebalancing output 7.
   */
  @Test
  public void testRebalancePortAskThirdPercent() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    List<String> m = this.mockview.getMsg();
    assertEquals(mockview.getMsg().get(12), "Please enter the percentage weight of MSFT." +
            " Enter the percentage as a whole number (ie. 40 for 40%).\n");
  }

  /**
   * Tests rebalancing output 8.
   */
  @Test
  public void testRebalancePortGetNewDistribution() {
    setupInput("5\n3\n2\n8\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(13), "Distribution of Value - Portfolio: MATT\n" +
            "GOOG: $1767.36 - 40.00%\n" +
            "AMZN: $1767.36 - 40.00%\n" +
            "MSFT: $883.68 - 20.00%\n" +
            "-------------------------\n" +
            "Total: $4418.39 - 100.00%\n");
  }

  /**
   * Tests inputting a weekend when trying to preform a transaction on a portfolio.
   */
  @Test
  public void testPortfolioInvalidDate() {
    setupInput("5\n3\n2\n8\n2024-05-26\n2024-06-06\n40\n40\n20\n10\n4\n6\n");
    controller.execute();
    assertEquals(mockview.getMsg().get(9), "This is a weekend. Please pick a date that is" +
            " Monday-Friday.\n");
  }

  /**
   * Tests performance over time output 1.
   */
  @Test
  public void testPerformancePortGraphOptions() {
    setupInput("5\n3\n2\n9\n6\n2024-06-06\n10\n4\n6\n");
    controller.execute();
    List<String> m = this.mockview.getMsg();
    assertEquals(this.mockview.getMsg().get(8), "What span of time would you like to see " +
            "the performance of?\n" +
            " 1) 5 days\n" +
            " 2) 2 weeks\n" +
            " 3) 1 month\n" +
            " 4) 3 months\n" +
            " 5) 6 months\n" +
            " 6) 1 year\n");
  }

  /**
   * Tests performance over time output 2.
   */
  @Test
  public void testPerformancePortGraphDate() {
    setupInput("5\n3\n2\n9\n6\n2024-06-06\n10\n4\n6\n");
    controller.execute();
    List<String> m = this.mockview.getMsg();
    assertEquals(this.mockview.getMsg().get(9), "Please input a date in YYYY-MM-DD format." +
            "\n");
  }

  /**
   * Tests performance over time output 3.
   */
  @Test
  public void testPerformancePortCorrectHeader() {
    setupInput("5\n3\n2\n9\n6\n2024-06-06\n10\n4\n6\n");
    controller.execute();
    assertEquals(this.mockview.getMsg().get(10),
            "Performance of MATT from 2023-07-31 to 2024-06-06:\n");
  }

  /**
   * Tests performance over time output 4.
   */
  @Test
  public void testPerformancePortCorrectYearGraph() {
    setupInput("5\n3\n2\n9\n6\n2024-06-06\n10\n4\n6\n");
    controller.execute();
    assertEquals(this.mockview.getMsg().get(11),
            "JUL 2023: *********\n" +
                    "AUG 2023: *********\n" +
                    "SEP 2023: ********\n" +
                    "OCT 2023: *********\n" +
                    "NOV 2023: **********\n" +
                    "DEC 2023: **********\n" +
                    "JAN 2024: **********\n" +
                    "FEB 2024: ***********\n" +
                    "MAR 2024: ***********\n" +
                    "APR 2024: ***********\n" +
                    "MAY 2024: ***********\n" +
                    "JUN 2024: ***********\n");
  }

  /**
   * Tests performance over time output 5.
   */
  @Test
  public void testPerformancePortCorrectYearGraphScale() {
    setupInput("5\n3\n2\n9\n6\n2024-06-06\n10\n4\n6\n");
    controller.execute();
    assertEquals(this.mockview.getMsg().get(12),
            "Scale: * = 370.0\n\n");
  }

  /**
   * Tests saving a portfolio to the folder inside the project.
   */
  @Test
  public void savePortfolio() {
    setupInput("5\n1\nTestFile\n2\n1\n1\nGOOG\n5\n2024-06-06\n7\n10\n4\n6\n10\n4\n6\n");
    controller.execute();
    String currDir = System.getProperty("user.dir");
    if (currDir.contains("src")) {
      currDir = currDir.replace("/src", "/res/savedportfolios");
    } else {
      currDir = currDir + "/res/savedportfolios";
    }
    File[] allFiles = new File(currDir).listFiles();
    ArrayList<String> output = new ArrayList<>();
    for (File file : allFiles) {
      String name = file.getName();
      output.add(name);
    }
    String test = "";
    for (String a : output) {
      test += a + "\n";
    }
    assertEquals(test, ".DS_Store\nMATT\nTestFile\n");
  }
}
