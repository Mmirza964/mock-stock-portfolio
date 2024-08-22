import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The StocksModel interface provides a set of methods for managing stock portfolios
 * and performing various operations related to stocks. Implementations of this
 * interface are responsible for creating and managing portfolios, checking stock prices,
 * performing technical analysis such as x-day crossover, and calculating portfolio values.
 */
public interface StocksModel {

  /**
   * Creates a new mock stock portfolio.
   *
   * @param portfolioName a String representing the name of the portfolio.
   */
  void newPortfolio(String portfolioName);

  /**
   * Returns the information available on the stock from the API.
   *
   * @param stock a String representing desired stock ticker symbol.
   * @return returns a String of the information of the stock from the API.
   */
  String checkStockPrice(String stock);

  /**
   * Returns a list of Strings of all the days that are a low to high crossovers of specified stock
   * on the specified date.
   *
   * @param csv   a String containing all the data of the stock from the API.
   * @param day   an int representing the day of the month.
   * @param month an int representing the month of the year.
   * @param year  an int representing the year of the specified date.
   * @param x     an int representing the number of days before the specified date for the
   *              crossover.
   * @param avg   a double average price of the stock from the specified date to x days before.
   * @return a list of Strings with all the days that are a low to high crossover of the stock.
   */
  ArrayList<String> xDayCrossover(String csv, int day, int month, int year, int x, double avg);

  /**
   * Checks if the stock inputted into the method is a valid stock in the market.
   *
   * @param stock a String representing the ticker symbol of the stock.
   * @return a true if the ticker is an actual stock or false of the ticker is not an actual stock.
   */
  boolean checkValidStock(String stock);

  /**
   * Gets the list of portfolios that the user has created.
   *
   * @return a list of portfolios representing all the portfolios the user created or removed.
   */
  List<IPortfolio> getPortfolios();

  /**
   * Gets the closing price of the stock at a specified date.
   *
   * @param formattedDate a String representing the specified date in YYYY-MM-DD format.
   * @param csv           a String containing all the information of the stock on the specified
   *                      date.
   * @param stock         a String representing the ticker symbol of the desired stock.
   * @return a double representing the closing price of the stock at the specified date.
   */
  double parseHelper(String formattedDate, String csv, String stock);

  /**
   * Returns the x-day moving average of a stock from the specified date.
   *
   * @param csv   a String containing all the information of the stock on the specified date.
   * @param day   an int representing the day of the month.
   * @param month an int representing the month of the year.
   * @param year  an int representing the year of the date.
   * @param stock a String representing the ticker symbol of the stock.
   * @param x     an int representing the number of days before the specified date.
   * @return a double with the x-day moving average of the stock from a specified date.
   */
  double movingavg(String csv, int day, int month, int year, String stock, int x);

  /**
   * Adds a stock and the number of shares to the specified portfolio.
   *
   * @param index    an int representing the index of the portfolios in the list of portfolios.
   * @param stock    a String representing the ticker symbol of the stock.
   * @param quantity the number of shares to add to the portfolio.
   */
  void addStock(int index, String stock, double quantity, String date);

  /**
   * Removes a stock and the number of shares to the specified portfolio.
   *
   * @param index    an int representing the index of the portfolios in the list of portfolios.
   * @param stock    a String representing the ticker symbol of the stock.
   * @param quantity the number of shares to add to the portfolio.
   */
  void removeStock(int index, String stock, double quantity, int stockIdx, boolean removeFull);

  /**
   * Deletes the portfolio at the specified index in the list of portfolios created by the user.
   *
   * @param index an int representing the index of the portfolio that is being deleted.
   */
  void deletePortfolio(int index);

  /**
   * Returns a message telling the user the value of the specified portfolio at the specified date.
   *
   * @param date  a String representing the date in format YYYY-MM-DD.
   * @param index an int representing the index of portfolio.
   * @return a String with a message telling the user the value of the portfolio at the date.
   */
  String portfolioValue(String date, int index);

  /**
   * Displays all the portfolios the user has created to the console.
   *
   * @return a String in format [portfolioName, portfolioName, portfolioName].
   */
  String displayPortfolios();

  /**
   * Returns a visual of the distribution of value of the portfolio. The method shows the dollar
   * value of all the stocks on the specified date as well as the percentage of portfolio the stock
   * makes up.
   *
   * @param index an int representing the index of the specified portfolio the method is called on.
   * @param date  a String representing the date specified in YYYY-MM-DD format.
   * @return a formatted String to show each stock in the portfolio, the stock's dollar value,
   *          the percentage of the portfolio, and the total value of the portfolio.
   */
  String distributionOfValue(int index, String date);

  /**
   * Saves the portfolio inputted into the method to a file within the project.
   *
   * @param port is an IPortfolio object representing the portfolio being saved to the device.
   */
  void saveFile(IPortfolio port);

  /**
   * Gets the file containing the saved portfolio.
   * @param x     A list of all the files in the savedportfolio directory.
   * @param index The file a user wants to retrieve from the savedportfolio directory.
   */
  void getFile(ArrayList<String> x, int index);

  /**
   * Retrives a list of all the files in the savedportfolio.
   *
   * @return All the avaliable files in the savedportfolio.
   */
  ArrayList<String> getFiles();

  /**
   * Helps load a portfolio by reading the saved file.
   *
   * @param name The name of the file the user wants to retrieve.
   */
  void readFiles(String name);

  /**
   * Returns a list of stocks purchased within the specified date with all their shares combined.
   * This is used to re-balance the portfolio.
   *
   * @param index an int representing the current portfolio that the method is being called on.
   * @param date  a String representing the specified date in format YYYY-MM-DD.
   * @return a list of all the stocks in the portfolio within the specified date.
   */
  List<Stock> distributionOfValueStocks(int index, String date);

  /**
   * Returns a string with all the stocks in the portfolio within the specified date using
   * the specified weights.
   *
   * @param weights a list of integers representing the weights the user wants to re-balance to
   *                corresponding with the stocks in the portfolio.
   * @param index   an int representing the specified portfolio the re-balancing is done to.
   * @param date    a String representing the specified date in format YYYY-MM-DD.
   * @return a formatted String with each stock, the value of the stock after re-balance, and
   *          what weight of the portfolio the stocks takes up after re-balance.
   */
  String rebalancePortfolio(List<Integer> weights, int index, String date);

  /**
   * Sets the date of the specified portfolio with the date the user inputted to execute
   * the transaction.
   *
   * @param index an int representing the current selected portfolio.
   * @param date  a String representing the specified date in formot YYYY-MM-DD.
   */
  void setDate(int index, String date);

  /**
   * Checks if the inputted date is before or after the date of the last transaction of the
   * selected portfolio.
   *
   * @param index an int representing the index of the selected portfolios out of the portfolios
   *              created by the user.
   * @param date  a String representing the specified date in format YYYY-MM-DD.
   * @return true if the inputted date is at or after the last transaction date of the selected
   *          portfolio and false otherwise.
   */
  boolean checkTranscationOrder(int index, String date);

  /**
   * Calculates the performance of a portfolio over time and returns the information as a
   * hashmap with each date corresponding to a specific value.
   *
   * @param port The portfolio the user wants to calculate the performance over time for.
   * @param days The number of days back the user wants to see the performance of.
   * @param date a String representing the specified date in format YYYY-MM-DD.
   * @return A hashmap where each key(date) corresponds with the price of the
   *          portfolio on that date.
   */
  HashMap<String, Double> calculatePerformanceOverTime(IPortfolio port, int days, String date);
}