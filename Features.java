/**
 * The Features interface defines the key operations that a user can perform
 * within the application's graphical user interface (GUI). This includes methods
 * for managing portfolios such as getting portfolio names, loading and saving
 * portfolios, choosing a specific portfolio, displaying all portfolios, adding or
 * selling assets in a portfolio, and obtaining an overview of a selected portfolio.
 * These operations facilitate user interaction and portfolio management within the
 * application.
 */
public interface Features {

  /**
   * Gets the name of the portfolio specified by the user and creates a new portfolio using
   * the name.
   *
   * @param name a String representing the name of the portfolio specified by the user.
   */
  void getPortfolioName(String name);

  /**
   * Loads the savedPortfolios folder and displays all the portfolios saved by the user.
   */
  void loadFile();

  /**
   * Saves the current portfolio to the savedPortfolios folder in the res folder in the program.
   */
  void saveFile();

  /**
   * Loads a saved portfolio specified by the user selection in the drop-down menu in the GUI.
   *
   * @param port a String representing the selected portfolio's name.
   */
  void choosePortfolio(String port);

  /**
   * Selects a portfolio from the list of portfolios created or loaded in the program.
   *
   * @param option an int representing the portfolio selection made by the user.
   */
  void allPortfolio(int option);

  /**
   * Allows the user to add or sell a specified share of stock to a specified portfolio on
   * a specified date.
   *
   * @param portfolio a String representing the portfolio to add or sell the stock to
   *                  specified by the user.
   * @param ticker    a String representing the ticker symbol of the stock.
   * @param quantity  a String representing the quantity of stock. Negative for sell.
   * @param date      a String representing a formatted date.
   */
  void addOrSell(String portfolio, String ticker, String quantity, String date);

  /**
   * Returns the composition of the portfolio. Displays the stock ticker symbol, the quantity of
   * that stock, the date purchased, and the current value of shares.
   *
   * @param selectedItem a String representing the portfolio specified by the user.
   */
  void getPortfolioOverview(String selectedItem, String date);
}
