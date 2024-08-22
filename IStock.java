/**
 * Represents a stock with methods to manage its properties and transactions.
 * This interface provides functionality to retrieve the stock's ticker symbol,
 * quantity owned, and the date of purchase. Additionally, it allows for selling
 * and adding shares to the stock, updating its state accordingly.
 */
public interface IStock {

  /**
   * Gets the ticker of the stock.
   *
   * @return a String with the ticker symbol of the stock.
   */
  String getTicker();

  /**
   * Gets the number of shares of this stock that are in the portfolio.
   *
   * @return returns a double with the number of shares of this stock.
   */
  double getQuantity();

  /**
   * Gets the formatted date of when the stock was purchased in the portfolio.
   *
   * @return a String with the date of purchase formatted: 'YYYY-MM-DD'.
   */
  String getDatePurchased();

  /**
   * Remove a specified number of shares from the stock.
   *
   * @param shares a double representing the number of shares of this stock
   *               to sell from the portfolio.
   * @return a new Stock with the quantity of shares updated.
   */
  Stock sellShares(double shares);

  /**
   * Adds a specified number of shares from to the stock.
   *
   * @param shares a double representing the number of shares of this stock to add to the portfolio.
   * @return a new stock with the quantity of shares updated.
   */
  Stock addShares(double shares);
}
