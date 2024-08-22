import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a portfolio of stocks with various operations to manage and retrieve stock
 * information.
 * Implements the IPortfolio interface, allowing for adding and removing stocks,
 * retrieving stock details,
 * and managing the portfolio's overall composition and transactions.
 */
public class Portfolio implements IPortfolio {
  private final List<Stock> stocks;
  private final String name;
  private final List<String> purchaseDates;
  private String date;

  /**
   * Constructs a SimplePortfolio object.
   *
   * @param name a String representing the name given to the portfolio by the user.
   */
  public Portfolio(String name) {
    this.stocks = new ArrayList<>();
    this.name = name;
    this.purchaseDates = new ArrayList<>();
    this.date = "";
  }

  // used in add and remove to return a new Portfolio object to avoid mutation
  private Portfolio(List<Stock> stocks, String name, List<String> purchaseDates) {
    this.stocks = stocks;
    this.name = name;
    this.purchaseDates = purchaseDates;
    this.date = "";
  }

  @Override
  public IPortfolio addStock(String ticker, double quantity, String datePurchased) {
    List<Stock> tempStocks = new ArrayList<>(this.stocks);
    List<String> tempPurchaseDate = new ArrayList<>(this.purchaseDates);
    boolean alreadyExists = false;
    int stockIdx = 0;

    for (int i = 0; i < tempStocks.size(); i++) {
      if (tempStocks.get(i).getTicker().equalsIgnoreCase(ticker) &&
              tempStocks.get(i).getDatePurchased().equalsIgnoreCase(datePurchased)) {
        alreadyExists = true;
        stockIdx = i;
        break;
      }
    }

    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    } else if (alreadyExists) {
      tempStocks.set(stockIdx, tempStocks.get(stockIdx).addShares(quantity));
    } else {
      Stock newStock = new Stock(ticker, quantity, datePurchased);
      tempStocks.add(newStock);
      tempPurchaseDate.add(datePurchased);
    }
    return new Portfolio(tempStocks, this.name, tempPurchaseDate);
  }

  @Override
  public IPortfolio removeStock(String ticker, double quantity, int stockIdx,
                                boolean removeAll) {
    List<Stock> tempStocks = new ArrayList<>(stocks);
    List<String> tempPurchaseDate = new ArrayList<>(purchaseDates);
    int nonPartialStockIdx = 0;

    if (stockIdx == -1) {
      nonPartialStockIdx = getIndex(ticker);
    } else {
      nonPartialStockIdx = stockIdx;
    }

    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative.");
    } else if (tempStocks.get(nonPartialStockIdx).getQuantity() < quantity) {
      throw new IllegalArgumentException("Not enough shares to remove.");
    } else if (nonPartialStockIdx == -1) {
      throw new IllegalArgumentException("Stock is not in the portfolio.");
    } else if (tempStocks.get(nonPartialStockIdx).getQuantity() > quantity) {
      tempStocks.set(nonPartialStockIdx, tempStocks.get(nonPartialStockIdx).sellShares(quantity));
    } else {
      tempStocks.remove(nonPartialStockIdx);
      tempPurchaseDate.remove(nonPartialStockIdx);
    }
    return new Portfolio(tempStocks, this.name, tempPurchaseDate);
  }

  @Override
  public Stock getStock(int index) {
    if (this.stocks.isEmpty() || this.stocks.size() < index || index < 0) {
      throw new IllegalArgumentException("Index is not valid");
    }
    return this.stocks.get(index);
  }

  @Override
  public int getStockListSize() {
    return this.stocks.size();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getOrganizedPortfolio(String date) {
    List<Integer> indexes = new ArrayList<>(getStocksDateConstraint(date));
    String output = "Portfolio Name: " + this.name + "\n";
    if (stocks.isEmpty()) {
      output += "The portfolio is empty. Please add stocks.\n";
    } else if (indexes.isEmpty()) {
      output += "There are no stocks in the portfolio for the date inputted. Please try again.\n";
    } else {
      for (int i = 0; i < indexes.size(); i++) {
        output += stocks.get(indexes.get(i)).getTicker() + ": " +
                stocks.get(indexes.get(i)).getQuantity() + "" +
                " shares purchased " + stocks.get(indexes.get(i)).getDatePurchased() + "\n";
      }
    }
    return output;
  }

  @Override
  public boolean containsTicker(String ticker) {
    for (Stock stock : stocks) {
      if (stock.getTicker().equals(ticker)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<Integer> getIndexes(String ticker, String date) {
    LocalDate sellDate = LocalDate.parse(date);
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < stocks.size(); i++) {
      Stock currStock = stocks.get(i);
      LocalDate purchaseDate = LocalDate.parse(stocks.get(i).getDatePurchased());
      if ((currStock.getTicker().equals(ticker)) &&
              (purchaseDate.isBefore(sellDate) || currStock.getDatePurchased().equals(date))) {
        indexes.add(i);
      }
    }
    return indexes;
  }

  @Override
  public String removeStockOptions(String ticker, String date) {
    String output = "";
    int choiceNum = 1;
    for (int index : getIndexes(ticker, date)) {
      output += choiceNum + ") " + ticker + ": " + stocks.get(index).getQuantity() + " shares" +
              " purchased on " + stocks.get(index).getDatePurchased() + "\n";
      choiceNum++;
    }
    return output;
  }

  @Override
  public double getTotalShares(String ticker, String date) {
    double totalShares = 0;
    for (int index : getIndexes(ticker, date)) {
      totalShares += stocks.get(index).getQuantity();
    }
    return totalShares;
  }

  @Override
  public List<Stock> distributionOfValue(String date) {
    List<Integer> indexes = new ArrayList<>(getStocksDateConstraint(date));
    List<String> tempStocks = new ArrayList<>();
    List<Double> tempQuantities = new ArrayList<>();
    List<Stock> stocksInPortfolio = new ArrayList<>();
    for (int i = 0; i < indexes.size(); i++) {
      String currStock = stocks.get(indexes.get(i)).getTicker();
      double currQuantity = stocks.get(indexes.get(i)).getQuantity();
      if (tempStocks.contains(currStock)) {
        int stockIdx = tempStocks.indexOf(currStock);
        tempQuantities.set(stockIdx, tempQuantities.get(stockIdx) + currQuantity);
      } else {
        tempStocks.add(currStock);
        tempQuantities.add(currQuantity);
      }
    }

    for (int j = 0; j < tempStocks.size(); j++) {
      stocksInPortfolio.add(new Stock(tempStocks.get(j), tempQuantities.get(j), ""));
    }
    return stocksInPortfolio;
  }

  @Override
  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public String getDate() {
    return this.date;
  }

  // ------------------------------------- private helpers ------------------------------------

  // puts all the stocks that are before or on the specified date in a list
  private List<Integer> getStocksDateConstraint(String date) {
    LocalDate requestedDate = LocalDate.parse(date);
    List<Integer> indexes = new ArrayList<>();
    for (int i = 0; i < purchaseDates.size(); i++) {
      String stockDate = stocks.get(i).getDatePurchased();
      LocalDate stockPurchaseDate = LocalDate.parse(stockDate);
      if (stockPurchaseDate.isBefore(requestedDate) || stockDate.equalsIgnoreCase(date)) {
        indexes.add(i);
      }
    }
    return indexes;
  }

  // gets the index of the stock in the portfolio's list of stocks using the ticker symbol
  private int getIndex(String ticker) {
    for (int i = 0; i < stocks.size(); i++) {
      if (stocks.get(i).getTicker().equals(ticker)) {
        return i;
      }
    }
    return -1;
  }
}