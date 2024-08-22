import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

/**
 * The StocksGUIControllerImpl class serves as the controller in the MVC pattern for a
 * stock portfolio management application, implementing both the StocksController and Features
 * interfaces. It manages user interactions through the GUI, updates the model, and refreshes
 * the view accordingly. This class includes methods for adding, selling, and managing stocks,
 * loading and saving portfolio data, and providing an overview of portfolios.
 */
public class MockStocksGUIController implements StocksController, Features {
  private StocksGUIView view;
  private StocksModel model;

  /**
   * Constructs a MockStocksGUIController object.
   *
   * @param view  a StocksGUIView representing how the program will display information.
   * @param model a StocksModel representing how the program will process information given by
   *              the user.
   */
  public MockStocksGUIController(StocksGUIView view, StocksModel model) {
    this.view = view;
    this.model = model;
    view.addFeatures(this);
  }

  @Override
  public void execute() {
    // needed becasue of implementaion, not used.
  }

  @Override
  public void getPortfolioName(String name) {
    this.model.newPortfolio(name);
  }

  @Override
  public void loadFile() {
    String currDir = System.getProperty("user.dir");
    if (currDir.contains("src")) {
      currDir = currDir.replace("/src", "/savedportfolios");
    } else {
      currDir = currDir + "/res/savedportfolios";
    }
    final JFileChooser fileChooser = new JFileChooser(currDir);
    int retValue = fileChooser.showOpenDialog(null);
    if (retValue == JFileChooser.APPROVE_OPTION) {
      // Get the selected file
      java.io.File selectedFile = fileChooser.getSelectedFile();

      // Get the name of the selected file
      String fileName = selectedFile.getName();
      this.model.readFiles(fileName);
    }
  }

  @Override
  public void saveFile() {
    this.view.savePortfolioHelper(this.model.getPortfolios());
  }

  @Override
  public void choosePortfolio(String port) {
    List<IPortfolio> s = model.getPortfolios();
    for (int i = 0; i < s.size(); i++) {
      if (s.get(i).getName().equals(port)) {
        this.model.saveFile(s.get(i));
      }
    }
  }

  @Override
  public void allPortfolio(int option) {
    this.view.getAllPortfolios(this.model.getPortfolios(), option);
  }

  // checks if the date inputted into the method has stock data
  private boolean validDateInput(String firstdate) {
    LocalDate date = null;
    while (date == null) {
      if (StocksModelImpl.isValidDate(firstdate, "yyyy-MM-dd")) {
        date = LocalDate.parse(firstdate);
        return date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY;
      }
      return false;
    }
    return true;
  }

  @Override
  public void addOrSell(String portfolio, String ticker, String quantity, String date) {
    int stocks = 0;
    int t = 0;
    int portIndex = 0;
    // number format exception
    t = Integer.parseInt(quantity);

    if (!this.validDateInput(date)) {
      throw new IllegalArgumentException("Invalid Date.");
    }

    if (!this.model.checkValidStock(ticker)) {
      throw new IllegalArgumentException("Invalid Ticker.");
    }

    // gets index of portfolio
    int index = 0;
    for (int i = 0; i < model.getPortfolios().size(); i++) {
      if (model.getPortfolios().get(i).getName().equals(portfolio)) {
        index = i;
      }
    }

    // checks if add stocks or remove
    if (t > 0) {
      this.model.addStock(index, ticker, t, date);
    } else {
      removeStockHelper(index, ticker, Math.abs(t), date);
    }
  }

  // removes the specified number of shares from the specified stock in the portfolio
  private void removeStockHelper(int index, String ticker, int removeQuantity, String date) {
    // gets total shares of stock in portfolio
    List<Integer> indexes = model.getPortfolios().get(index).getIndexes(ticker, date);
    double totalShares = 0;
    for (int i = 0; i < indexes.size(); i++) {
      int stockIdx = indexes.get(i);
      totalShares += model.getPortfolios().get(index).getStock(stockIdx).getQuantity();
    }

    if (totalShares < removeQuantity) {
      throw new IllegalArgumentException("Invalid Purchase.");
    }

    while (removeQuantity > 0) {
      List<Integer> removeIdx = model.getPortfolios().get(index).getIndexes(ticker, date);
      int stockIdx = removeIdx.get(0);
      double stockQuantity = model.getPortfolios().get(index).getStock(stockIdx).getQuantity();
      if (stockQuantity > removeQuantity) {
        model.removeStock(index, ticker, removeQuantity, stockIdx, false);
        removeQuantity = 0;
      } else {
        model.removeStock(index, ticker, stockQuantity, stockIdx, true);
        removeQuantity -= stockQuantity;
      }
    }
  }

  @Override
  public void getPortfolioOverview(String selectedItem, String date) {
    int index = 0;
    for (int i = 0; i < this.model.getPortfolios().size(); i++) {
      if (this.model.getPortfolios().get(i).getName().equals(selectedItem)) {
        index = i;
      }
    }

    if (!this.validDateInput(date)) {
      throw new IllegalArgumentException("Invalid Date.");
    }

    LocalDate purchaseDate = LocalDate.parse(date);

    ArrayList<Stock> w = new ArrayList<>();
    for (int i = 0; i < this.model.getPortfolios().get(index).getStockListSize(); i++) {
      LocalDate d = LocalDate.parse(model.getPortfolios().get(index).
              getStock(i).getDatePurchased());
      if (purchaseDate.isAfter(d)) {
        w.add(this.model.getPortfolios().get(index).getStock(i));
      }
    }


    ArrayList<Stock> s = new ArrayList<>();
    for (int i = 0; i < w.size(); i++) {
      s.add(w.get(i));
    }
    double portfolioTotal = 0.0;
    List<String> x = new ArrayList<>();
    for (Stock stock : s) {
      String y = this.model.checkStockPrice(stock.getTicker());
      String j = "<html>Ticker: " + stock.getTicker() + "<br>Quantity: " +
              stock.getQuantity() + "<br>Date Purchased: " + stock.getDatePurchased() + "<br>";
      double total = stock.getQuantity() *
              this.model.parseHelper(stock.getDatePurchased(), y, stock.getTicker());
      portfolioTotal += total;
      j += "Total Value of " + stock.getTicker() + ": " + total + "</html>";
      x.add(j);
    }
    x.add("<html>Total Portfolio Overview: " + portfolioTotal + "</html>");
    this.view.showPortfolioOverviewPage(x);
  }
}
