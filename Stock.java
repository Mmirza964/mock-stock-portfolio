/**
 * Represents a stock with a specific ticker symbol, quantity owned, and the date of purchase.
 * This class implements the IStock interface and provides functionality to manage stock
 * transactions.
 * It allows retrieval of the stock's ticker, quantity, and purchase date, and supports operations
 * to sell or add shares, resulting in a new Stock instance with updated quantities.
 */
public class Stock implements IStock {
  private final String ticker;
  private final double quantity;
  private final String datePurchased;

  /**
   * Constructs a stock object with a ticker, number of shares, and the purchase date.
   *
   * @param ticker        a String representing the ticker symbol of the stock.
   * @param quantity      a double representing the number of shares of this stock in the portfolio.
   * @param datePurchased a String representing a formatted (YYYY-MM-DD) date of purchase.
   */
  public Stock(String ticker, double quantity, String datePurchased) {
    this.ticker = ticker;
    this.quantity = quantity;
    this.datePurchased = datePurchased;
  }

  @Override
  public String getTicker() {
    return ticker;
  }

  @Override
  public double getQuantity() {
    return quantity;
  }

  @Override
  public String getDatePurchased() {
    return datePurchased;
  }

  @Override
  public Stock sellShares(double shares) {
    if (shares > quantity || shares < 0) {
      throw new IllegalArgumentException("Shares cannot be negative.");
    }
    return new Stock(ticker, quantity - shares, datePurchased);
  }

  @Override
  public Stock addShares(double shares) {
    if (shares < 0) {
      throw new IllegalArgumentException("Shares cannot be negative.");
    }
    return new Stock(ticker, quantity + shares, datePurchased);
  }

  @Override
  public String toString() {
    String output = "";
    output += ticker + "," + quantity + "," + datePurchased;
    return output;
  }
}
