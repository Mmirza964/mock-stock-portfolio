import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * A mock version of StockModel which has all the same methods
 * and is instead used to help test StockModel.
 */
public class MockModel implements StocksModel {
  private List<IPortfolio> portfolios;


  /**
   * Constructs a MockModel object.
   */
  public MockModel() {
    this.portfolios = new ArrayList<>();
  }

  @Override
  public void setDate(int index, String date) {
    IPortfolio tempPortfolio = this.portfolios.get(index);
    tempPortfolio.setDate(date);
    this.portfolios.set(index, tempPortfolio);
  }

  @Override
  public boolean checkTranscationOrder(int index, String date) {
    String lastTransactionDate = this.portfolios.get(index).getDate();
    if (lastTransactionDate.isEmpty()) {
      return true;
    }
    LocalDate currDate = LocalDate.parse(date);
    LocalDate oldDate = LocalDate.parse(lastTransactionDate);
    return currDate.isAfter(oldDate);
  }

  @Override
  public void newPortfolio(String portfolioName) {
    this.portfolios.add(new Portfolio(portfolioName));
  }

  @Override
  public List<IPortfolio> getPortfolios() {
    return new ArrayList<>(this.portfolios);
  }

  @Override
  public void addStock(int index, String stock, double quantity, String date) {
    this.portfolios.set(index, this.portfolios.get(index).addStock(stock, quantity, date));
  }

  @Override
  public void removeStock(int index, String stock, double quantity, int stockIdx,
                          boolean removeFull) {
    this.portfolios.set(index, this.portfolios.get(index)
            .removeStock(stock, quantity, stockIdx, removeFull));
  }

  @Override
  public void deletePortfolio(int index) {
    this.portfolios.remove(index);
  }

  @Override
  public String displayPortfolios() {
    String output = "[";
    for (IPortfolio portfolio : this.portfolios) {
      output += portfolio.getName() + ", ";
    }
    return output + "]";
  }

  @Override
  public String portfolioValue(String date, int index) {
    List<Stock> stocks = this.portfolios.get(index).distributionOfValue(date);
    double sum = 0;

    int stockListLength = stocks.size();
    for (int i = 0; i < stockListLength; i++) {
      String currentStock = stocks.get(i).getTicker();
      double currentQuantity = stocks.get(i).getQuantity();
      String output = this.checkStockPrice(currentStock);
      sum += this.parseHelper(date, output, currentStock) * currentQuantity;
    }
    return "The value of the portfolio is " + String.format("%.2f", sum) + "\n";
  }

  @Override
  public String distributionOfValue(int index, String date) {
    double portfolioValue = Double.valueOf(portfolioValue(date, index).substring(30));
    List<Stock> workList = this.portfolios.get(index).distributionOfValue(date);
    String output = "Distribution of Value - Portfolio: " + this.portfolios.get(index).getName() +
            "\n";
    for (Stock stock : workList) {
      String csv = checkStockPrice(stock.getTicker());
      double stockVal = (parseHelper(date, csv, stock.getTicker())) * stock.getQuantity();
      double percent = (stockVal / portfolioValue) * 100;
      output += stock.getTicker() + ": $" + String.format("%.2f", stockVal) + " - " +
              String.format("%.2f", percent) + "%\n";
    }
    output += "-------------------------\nTotal: $" + portfolioValue + " - 100.00%\n";
    return output;
  }

  @Override
  public List<Stock> distributionOfValueStocks(int index, String date) {
    return this.portfolios.get(index).distributionOfValue(date);
  }

  @Override
  public String rebalancePortfolio(List<Integer> weights, int index, String date) {
    double portfolioValue = Double.valueOf(portfolioValue(date, index).substring(30));
    List<Stock> workList = this.portfolios.get(index).distributionOfValue(date);
    for (int i = 0; i < weights.size(); i++) {
      Stock currStock = workList.get(i);
      String csv = checkStockPrice(currStock.getTicker());
      double stockVal = parseHelper(date, csv, currStock.getTicker());
      int currWeight = weights.get(i);
      double oldQuantity = currStock.getQuantity();
      double newQuantity = ((currWeight * portfolioValue) / (100 * stockVal));

      if (newQuantity > oldQuantity) {
        addStock(index, currStock.getTicker(), newQuantity - oldQuantity, date);
      } else if (oldQuantity > newQuantity) {
        List<Integer> indexes = this.portfolios.get(index).getIndexes(currStock.getTicker(), date);
        if (indexes.size() > 1) {
          for (int j = 0; j < indexes.size(); j++) {
            double quantityChange = Math.abs(newQuantity - oldQuantity);
            if (this.portfolios.get(index).getStock(indexes.get(i)).getQuantity()
                    > quantityChange) {
              removeStock(index, currStock.getTicker(), quantityChange,
                      indexes.get(i), false);
            }
          }
        } else {
          removeStock(index, currStock.getTicker(), oldQuantity - newQuantity,
                  -1, false);
        }
      }
    }
    return distributionOfValue(index, date);
  }

  /**
   * Checks if the date inputted into the method is a valid date meaning that the date is not a
   * weekend and that there is stock data available.
   *
   * @param date   a String representing the specified date to check for.
   * @param format a String representing how to format the date when checking for data.
   * @return true if the date has stock data and false otherwise.
   */
  public static boolean isValidDate(String date, String format) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      // Parse the date. If the date is not valid, it will throw DateTimeParseException.
      LocalDate.parse(date, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  @Override
  public boolean checkValidStock(String stock) {
    StringBuilder o = new StringBuilder();
    try {
      Integer.parseInt(stock);
      return false;
    } catch (NumberFormatException e) {
      try {
        String currDir = System.getProperty("user.dir");
        if (currDir.contains("src")) {
          currDir = currDir.replace("/src", "/res");
        } else {
          currDir = currDir + "/res";
        }
        File stocks = new File(currDir +
                "/nasdaq_screener_1717539483453.csv");
        Scanner sc = new Scanner(stocks);
        while (sc.hasNext()) {
          o.append(sc.nextLine());
        }
      } catch (FileNotFoundException msg) {
        return false;
      }
    }
    return o.toString().contains(stock);
  }


  @Override
  public String checkStockPrice(String stock) {
    //Key ZHMWP0VBWCYUCZA9
    // Key 975IVVEP0K548ZPQ
    String apiKey = "FVHPQRIRKBYHSTJU";//ticker symbol for Google
    URL url = null;
    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stock + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }
    InputStream in = null;
    StringBuilder output = new StringBuilder();
    try {

      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + stock);
    }

    return output.toString();
  }

  @Override
  public ArrayList<String> xDayCrossover(String csv, int day, int month, int year, int x,
                                         double avg) {
    String[] lines = csv.split("\n");
    int goBack = x;
    int counter = 0;
    ArrayList<String> outputp2 = new ArrayList<>();
    String output = "There were cross overs on the following dates:\n";
    outputp2.add(output);
    for (int i = 1; i < lines.length - 1; i++) {
      String[] info = lines[i].split(",");

      String[] date = info[0].split("-");
      int lookingDate = Integer.parseInt(date[2]);
      int lookingMonth = Integer.parseInt(date[1]);
      int lookingYear = Integer.parseInt(date[0]);
      if (lookingDate == day && lookingMonth == month && lookingYear == year) {
        counter = i;
        break;
      }
    }

    for (int j = 0; j < Math.abs(goBack); j++) {
      String[] data = lines[counter + j].split(",");
      String[] prevdata = lines[counter + j + 1].split(",");
      double total = 0.0;
      for (int k = 0; k < Math.abs(goBack); k++) {
        total += Double.parseDouble(data[4]);
      }
      avg = total / x;
      if ((Double.parseDouble(data[4]) > avg)
              && (Double.parseDouble(prevdata[4]) < avg)) {
        outputp2.add("There was a low to high crossover on " + prevdata[0] + "\n");
      }
    }
    return outputp2;
  }

  @Override
  public double movingavg(String csv, int day, int month, int year, String stock, int x) {
    String[] lines = csv.split("\n");
    ArrayList<Double> allValues = new ArrayList<Double>();
    int goBack = x;
    int counter = 0;
    double total = 0;
    for (int i = 1; i < lines.length - 1; i++) {
      String[] info = lines[i].split(",");

      String[] date = info[0].split("-");
      int lookingDate = Integer.parseInt(date[2]);
      int lookingMonth = Integer.parseInt(date[1]);
      int lookingYear = Integer.parseInt(date[0]);
      if (lookingDate == day && lookingMonth == month && lookingYear == year) {
        allValues.add(Double.parseDouble(info[4]));
        counter = i;
      }
    }
    for (int j = Math.abs(goBack); j != 0; j--) {
      String[] o = lines[counter + j].split(",");
      allValues.add(Double.parseDouble(o[4]));
    }
    for (int k = 0; k < allValues.size(); k++) {
      total += allValues.get(k);
    }
    return total / allValues.size();
  }

  @Override
  public double parseHelper(String formattedDate, String csv, String stock) {
    String[] lines = csv.split("\n");
    LocalDate formatted = LocalDate.parse(formattedDate);
    int day = formatted.getDayOfMonth();
    int month = formatted.getMonthValue();
    int year = formatted.getYear();
    for (int i = 1; i < lines.length - 1; i++) {
      String[] info = lines[i].split(",");

      String[] date = info[0].split("-");
      int lookingDate = Integer.parseInt(date[2]);
      int lookingMonth = Integer.parseInt(date[1]);
      int lookingYear = Integer.parseInt(date[0]);
      if (lookingDate == day && lookingMonth == month && lookingYear == year) {
        return Double.parseDouble(info[4]);
      }
    }
    throw new IllegalArgumentException("No data found for " + stock + " on the date: " +
            formattedDate);
  }

  @Override
  public void saveFile(IPortfolio port) {
    String name = port.getName();
    String currDir = System.getProperty("user.dir");
    currDir = currDir.replace("/src", "/res/savedportfolios");
    currDir = currDir + "/res/savedportfolios";
    currDir = currDir + "/" + name;
    String output = "";
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(currDir, true))) {
      for (int i = 0; i < port.getStockListSize(); i++) {
        output += port.getStock(i).toString();
        output += "\n";
      }
      writer.write(output);
    } catch (IOException e) {
      return;
    }

  }

  @Override
  public void getFile(ArrayList<String> x, int index) {
    String i = x.get(index - 1);
    readFiles(i);
  }

  @Override
  public ArrayList<String> getFiles() {
    String currDir = System.getProperty("user.dir");

    currDir = currDir.replace("/src", "/res/savedportfolios");
    currDir = currDir + "/res/savedportfolios";
    File[] allFiles = new File(currDir).listFiles();
    ArrayList<String> output = new ArrayList<>();
    if (allFiles == null) {
      return null;
    } else {
      for (File file : allFiles) {
        String name = file.getName();
        output.add(name);
      }
    }
    return output;
  }

  @Override
  public void readFiles(String name) {
    // /Users/saranshsingh/Desktop/OOD/Stocks/src
    String currDir = System.getProperty("user.dir");
    //currDir = currDir.replace("/src", "/savedportfolios");
    currDir = currDir + "/res/savedportfolios";
    currDir = currDir + "/" + name;
    try (BufferedReader reader = new BufferedReader(new FileReader(currDir))) {
      String line;
      newPortfolio(name);
      int index = this.getPortfolios().size();
      while ((line = reader.readLine()) != null) {
        String[] s = line.split(",");
        String ticker = s[0];
        double quantity = Double.parseDouble(s[1]);
        String date = s[2];
        this.portfolios.set(index - 1, this.getPortfolios().get(index - 1)
                .addStock(ticker, quantity, date));
      }
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public HashMap<String, Double> calculatePerformanceOverTime(IPortfolio port, int days,
                                                              String date) {
    LocalDate date2 = LocalDate.parse(date);
    IPortfolio tempPort = new Portfolio("Temp");
    for (int i = 0; i < port.getStockListSize(); i++) {
      LocalDate date1 = LocalDate.parse(port.getStock(i).getDatePurchased());
      if (date2.isAfter(date1)) {
        Stock tempStock = port.getStock(i);
        tempPort = port.addStock(tempStock.getTicker(), 0, tempStock.getDatePurchased());
      }
    }
    HashMap<String, Double> ans = new HashMap<>();
    ans = getGraphData(tempPort, days, date);
    return ans;
  }

  // draws the graph
  private HashMap<String, Double> yearGraph(IPortfolio port, String date, int option, int repeat) {
    HashMap<String, Double> ans = new HashMap<>();
    double price;
    // add date and price to hashmap
    for (int i = 0; i < port.getStockListSize(); i++) {
      String csv = checkStockPrice(port.getStock(i).getTicker());
      String date1 = date;
      for (int j = 0; j < repeat; j++) {
        while (true) {
          try {
            price = parseHelper(date1, csv, port.getStock(i).getTicker());
            double tempPrice;
            if (ans.get(date1) == null) {
              tempPrice = 0.0;
            } else {
              tempPrice = ans.get(date1);
            }
            price = price * port.getStock(i).getQuantity();
            price = tempPrice + price;
            ans.put(date1, price);
            break;
          } catch (IllegalArgumentException e) {
            date1 = LocalDate.parse(date1).minusDays(1).toString();
          }
        }
        if (option == 365) {
          LocalDate d = LocalDate.parse(date1).minusMonths(1);
          date1 = YearMonth.from(d).atEndOfMonth().toString();
        } else if (option == 180) {
          LocalDate d = LocalDate.parse(date1).minusWeeks(2);
          date1 = d.toString();
        } else if (option == 90) {
          LocalDate d = LocalDate.parse(date1).minusWeeks(1);
          date1 = d.toString();
        } else if (option == 30) {
          LocalDate d = LocalDate.parse(date1).minusDays(3);
          date1 = d.toString();
        } else if (option == 14) {
          LocalDate d = LocalDate.parse(date1).minusDays(1);
          date1 = d.toString();
        } else if (option == 5) {
          LocalDate d = LocalDate.parse(date1).minusDays(1);
          date1 = d.toString();
        }
      }

    }
    return ans;
  }

  // gets the graph data
  private HashMap<String, Double> getGraphData(IPortfolio port, int days, String date) {
    if (days == 365) {
      return this.yearGraph(port, date, 365, 12);
    } else if (days == 180) {
      return this.yearGraph(port, date, 180, 12);
    } else if (days == 90) {
      return this.yearGraph(port, date, 90, 12);
    } else if (days == 30) {
      return this.yearGraph(port, date, 30, 10);
    } else if (days == 14) {
      return this.yearGraph(port, date, 14, 14);
    } else {
      return this.yearGraph(port, date, 5, 5);
    }
  }
}