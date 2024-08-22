import java.util.List;

/**
 * Represents a portfolio of stocks with methods to manage and retrieve information about
 * the stocks.
 * This interface allows for adding and removing stocks, retrieving stock details, and managing the
 * portfolio's overall composition and transactions.
 */
public interface IPortfolio {
  /**
   * Adds a stock and a number of shares of that stock to the portfolio. If the stock is
   * already in the portfolio, the quantity inputted into the method will be added to the
   * existing quantity in the portfolio.
   *
   * @param ticker   a String representing the stock ticker symbol.
   * @param quantity an int representing the number of shares of stock to add to the portfolio.
   * @return a new Portfolio object with the new stock and quantity added to the portfolio.
   */
  IPortfolio addStock(String ticker, double quantity, String datePurchased);

  /**
   * Removes the number of shares of a stock from the portfolio. If the stock doesn't exist
   * in the portfolio or the quantity removed is more than the current shares in the portfolio,
   * an exception is thrown.
   *
   * @param ticker   a String representing the stock ticker.
   * @param quantity an int representing the number of shares to remove from the portfolio.
   * @return a new Portfolio object with the stock and quantity removed from the portfolio.
   */
  IPortfolio removeStock(String ticker, double quantity, int stockIdx, boolean removeAll);

  /**
   * Gets the stock at a specific index and returns the ticker symbol as a String.
   *
   * @param index an int representing the position of the desired stock in the list of stocks.
   * @return a String representing the ticker symbol of the stock pulled from the list.
   */
  Stock getStock(int index);

  //int getQuantity(int index);

  /**
   * Gets the list of the number of stocks that are in the portfolio.
   *
   * @return an int representing the length of the list of stocks in the portfolio.
   */
  int getStockListSize();

  /**
   * Returns the name of the portfolio.
   *
   * @return a String representing the name of the portfolio inputted by the user at construction.
   */
  String getName();

  /**
   * Returns a list of all the stocks in the portfolio and the number of shares of each stock
   * that are present in the portfolio in format- (stock ticker): (shares of stock).
   *
   * @return a formatted String with all the stocks in the portfolio and the shares of
   *          each stock in the portfolio.
   */
  String getOrganizedPortfolio(String date);

  /**
   * Checks if the ticker inputted into the method is in the portfolio of stocks.
   *
   * @param ticker a String representing the ticker symbol of the stock being searched for.
   * @return a boolean representing if true the stock is in the portfolio or false the stock
   *          is not in the portfolio.
   */
  boolean containsTicker(String ticker);

  /**
   * Returns a list of the indexes of the specified stock in the portfolio before the specified day.
   *
   * @param ticker a String representing the ticker symbol of a stock.
   * @param date   a String representing the specified date in 'YYYY-MM-DD' format.
   * @return a list of integers of the indexes of the stock in the portfolio before the specified
   *          date.
   */
  List<Integer> getIndexes(String ticker, String date);

  /**
   * Returns a list of the stocks in the portfolio purchased before the specified date. This is
   * used in the remove operation
   * to allow the user to select which shares to sell if multiple shares of the same stock were
   * purchased on different days.
   *
   * @param ticker a String representing the ticker symbol of the specified stock.
   * @param date   a String representing the specified date in 'YYYY-MM-DD' format.
   * @return a formatted String with the list of shares of the specified stock that were purchased
   *          on different days.
   */
  String removeStockOptions(String ticker, String date);

  /**
   * Returns the number of shares of the specified stock on the specified date.
   *
   * @param ticker a String representing the ticker symbol of the specified stock.
   * @param date   a String representing the specified date in 'YYYY-MM-DD' format.
   * @return a double with the total number of shares of the specified stock in the portfolio
   *          before and up until the specified date.
   */
  double getTotalShares(String ticker, String date);

  /**
   * Returns a list of stocks with all the stocks in the portfolios and their combined total shares.
   * The list is used in the model implementation to calculate the distribution of value
   * of the portfolio.
   *
   * @param date a String representing the specified date in 'YYYY-MM-DD' format.
   * @return a list of stocks with all the shares combined into one entry in the ArrayList.
   */
  List<Stock> distributionOfValue(String date);

  /**
   * Sets the date of the last transaction on the portfolio.
   *
   * @param date a String representing the specified date in 'YYYY-MM-DD' format.
   */
  void setDate(String date);

  /**
   * Gets the date of the last transaction in the portfolio.
   *
   * @return a String with the date of the last transaction in the portfolio, formatted in
   *          'YYYY-MM-DD'.
   */
  String getDate();
}
