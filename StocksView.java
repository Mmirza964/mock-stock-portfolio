/**
 * The StocksView interface provides a set of methods for displaying messages
 * and options. Implementations of this interface are responsible for presenting various
 * user interface elements such as welcome messages, options for portfolio management, and gain or
 * loss information.
 */
public interface StocksView {

  /**
   * Prints the message to the console for the user to see.
   *
   * @param message a String representing the message to print to the console.
   */
  void display(String message);

  /**
   * Prints the options the user can select from in the welcome stage of the program.
   */
  void welcomeOptions();

  /**
   * Prints the welcome message when the user first runs the program.
   */
  void welcomeMessage();

  /**
   * Prints the message telling the user what the gain or loss option does and what
   * information the user needs to input to make the method work.
   */
  void gainOrLoss();

  /**
   * Prints the actions the user can take to manage multiple portfolios or to create more.
   */
  void portfolioHelperOptions();

  /**
   * Prints the actions the user can take inside each portfolio.
   */
  void portfolioManagerOptions();
}
