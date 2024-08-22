import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.event.ListSelectionListener;

/**
 * The StocksGUIView interface defines the methods required for displaying
 * and interacting with the stock portfolio management GUI. It extends
 * ActionListener, ItemListener, and ListSelectionListener to handle various
 * user interactions. This interface includes methods for displaying the GUI,
 * adding features, saving portfolios, retrieving all portfolios, and showing
 * portfolio overviews.
 */
public interface StocksGUIView extends ActionListener, ItemListener, ListSelectionListener {

  /**
   * Sets the visibility if the JFrame to true showing the GUI to the user.
   */
  void display();

  /**
   * Adds buttons for the features the GUI can handle like saving and loading portfolios, adding
   * and selling stock, and checking the stock overview.
   *
   * @param features a Features method used to create the buttons and update the functionality.
   */
  void addFeatures(Features features);

  /**
   * Displays messages to the user in the GUI and delegates to model to save the portfolio.
   *
   * @param portfolios represents a list of portfolios created in the program.
   */
  void savePortfolioHelper(List<IPortfolio> portfolios);

  /**
   * Displays all the portfolios created or loaded in by the user.
   *
   * @param portfolios a list of portfolios representing the portfolios created or loaded.
   * @param option     an int representing the chosen portfolio.
   */
  void getAllPortfolios(List<IPortfolio> portfolios, int option);

  /**
   * Displays the page where the portfolio composition is printed for the user to see.
   *
   * @param stocks a list of Strings representing all the of stocks in the portfolio.
   */
  void showPortfolioOverviewPage(List<String> stocks);
}
