import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * The StocksControllerImpl class implements the StocksController interface
 * and provides the main control logic for the stock portfolio management application.
 * It interacts with both the StocksModel and StocksView interfaces to handle user inputs,
 * perform stock-related operations, and update the view accordingly.
 */
public class StocksControllerImpl implements StocksController {
  private final StocksModel model;
  private final StocksView view;
  private final Scanner scanner;

  /**
   * Constructs a StocksControllerImpl object.
   *
   * @param model a StockModel object representing the model called by the controller.
   * @param view  a StocksView object representing the view called by the controller.
   */
  public StocksControllerImpl(StocksModel model, StocksView view, InputStream in) {
    this.model = model;
    this.view = view;
    this.scanner = new Scanner(in);
  }

  @Override
  public void execute() {
    view.welcomeMessage();
    this.choiceHelper();
  }

  // checks if the user inputs a valid number from the welcome options
  private void choiceHelper() {
    view.welcomeOptions();
    int choice = checkIntScanner();
    while (choice < 1 || choice > 6) {
      view.display("Input a valid number between 1 and 6\n");
      choice = checkIntScanner();
    }
    this.scanner.nextLine();
    if (choice == 1) {
      gainOrLossHelper();
    } else if (choice == 2) {
      movingAvgHelper();
    } else if (choice == 3) {
      crossoverHelper();
    } else if (choice == 4) {
      performanceOfStock();
    } else if (choice == 5) {
      portfolioHelper();
    } else {
      return;
    }
  }

  // checks if the ticker inputted into the method is a valid stock
  private String checkValidStock(String stock) {
    String s = stock;
    while (true) {
      if (this.model.checkValidStock(s)) {
        return s;
      } else {
        view.display("Input a valid ticker symbol\n");
        s = this.scanner.nextLine();
      }
    }
  }

  // asks the user for a stock and date to output the gain or loss of the stock
  private void gainOrLossHelper() {
    view.display("To determine the gain or loss of a stock over a period of time\n" +
            "please input the ticker symbol for the stock you want to look into.\n");
    String command = this.scanner.nextLine();
    command = this.checkValidStock(command);
    view.gainOrLoss();
    String firstdate = this.scanner.nextLine();
    // "1\nGOOG\n2024-06-03\n2024-05-28\nS\n"
    String properFirstDate = this.validDateInput(firstdate);
    view.display("Please input the the second date in 'YYYY-MM-DD' format.\n");
    String secondDate = this.scanner.nextLine();
    String properSecondDate = this.validDateInput(secondDate);
    String output = this.model.checkStockPrice(command);
    double gainorloss = 0;
    while (true) {
      try {
        double firstprice = model.parseHelper(properFirstDate, output, command);
        double secondprice = model.parseHelper(properSecondDate, output, command);
        gainorloss = (firstprice - secondprice);
        break;
      } catch (IllegalArgumentException e) {
        view.display("Please input a valid date this stock was sold in YYYY-MM-DD format.\n");
        firstdate = this.scanner.nextLine();
        properFirstDate = this.validDateInput(firstdate);
        view.display("Please input the the second date in YYYY-MM-DD format.\n");
        secondDate = this.scanner.nextLine();
        properSecondDate = this.validDateInput(secondDate);
      }
    }

    view.display("The total gain or loss between the two days you provided are $" +
            String.format("%.2f", gainorloss) + "\n");
    view.display("Press C if you would like to restart, or input anything else to quit the " +
            "application.\n");
    String c = this.scanner.nextLine();
    if (c.equals("C")) {
      choiceHelper();
    }
  }

  // checks if the date inputted into the method has stock data
  private String validDateInput(String firstdate) {
    LocalDate date = null;
    while (date == null) {
      if (StocksModelImpl.isValidDate(firstdate, "yyyy-MM-dd")) {
        date = LocalDate.parse(firstdate);
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
          view.display("This is a weekend. Please pick a date that is Monday-Friday.\n");
          date = null;
          firstdate = this.scanner.nextLine();
        }
      } else {
        view.display("Input a valid date in YYYY-MM-DD format.\n");
        firstdate = this.scanner.nextLine();
      }
    }
    return firstdate;
  }

  // if the date inputted by the user is not a valid date, then the method keeps asking the user
  // for dates until a valid date is inputted
  private String getValidDate() {
    String date = this.scanner.nextLine();
    while (true) {
      if (StocksModelImpl.isValidDate(date, "yyyy-MM-dd")) {
        return date;
      }
      view.display("Please input a date in YYYY-MM-DD format.\n");
      date = this.scanner.nextLine();
    }
  }


  // asks the user for the stock ticker, date, and a value for x to display the
  // x-day crossover of the stock
  private void crossoverHelper() {
    view.display("Which stock would you like to see an X day crossover for?\n");
    String stock = this.scanner.nextLine();
    stock = this.checkValidStock(stock);
    view.display("Please input a date in YYYY-MM-DD format.\n");
    String date = this.getValidDate();
    int x;
    while (true) {
      try {
        view.display("Please input the X number of days you want to see the crossover of.\n");
        x = Integer.parseInt(scanner.nextLine());
        break;
      } catch (NumberFormatException e) {
        view.display("Invalid input. Please enter a valid number.\n");
      }
    }
    String[] dates = date.split("-");
    int firstYear = Integer.parseInt(dates[0]);
    int firstDay = Integer.parseInt(dates[2]);
    int firstMonth = Integer.parseInt(dates[1]);

    String output = this.model.checkStockPrice(stock);
    double avg = this.model.movingavg(output, firstDay, firstMonth, firstYear, stock, x);
    ArrayList<String> ans = this.model.xDayCrossover(output, firstDay, firstMonth, firstYear,
            x, avg);
    for (String an : ans) {
      view.display(an);
    }
    view.display("Press C if you would like to restart, or input anything else to quit the " +
            "application\n");
    String c = this.scanner.nextLine();
    if (c.equals("C")) {
      choiceHelper();
    }
  }

  // asks the user for the ticker of the stock, and date, and x days back from the date
  // to display the x-day moving average of the stock
  private void movingAvgHelper() {
    boolean going = true;
    while (going) {
      view.display("To determine the x day moving avg, you need to provide ticker symbol " +
              "for the stock you want to see\n");
      String command = this.scanner.nextLine();
      command = this.checkValidStock(command);
      view.display("Please input a date in YYYY-MM-DD format.\n");
      String date = this.getValidDate();
      String[] dates = date.split("-");
      int month = Integer.parseInt(dates[1]);
      int day = Integer.parseInt(dates[2]);
      int year = Integer.parseInt(dates[0]);
      going = this.validDate(command);
      if (!going) {
        String output = this.model.checkStockPrice(command);
        this.view.display("Please input the x number of days you want to check.\n");
        int x = checkIntScanner();
        double ans = this.model.movingavg(output, day, month, year, command, x);
        String out = String.format("%.2f", ans);
        view.display("The last " + x + " day moving avg of " + command + " is $" + out + ".\n");
      }
      if (going) {
        view.display("Please input a valid date.\n");
      }
    }
    view.display("Press C if you would like to restart, or input anything else to quit the " +
            "application\n");
    this.scanner.nextLine();
    String c = this.scanner.nextLine();
    if (c.equals("C")) {
      choiceHelper();
    }
  }

  private TreeMap<LocalDate, Double> organize(HashMap<String, Double> ans) {
    TreeMap<LocalDate, Double> sortedMap = new TreeMap<>();
    for (Map.Entry<String, Double> entry : ans.entrySet()) {
      LocalDate dateKey = LocalDate.parse(entry.getKey());
      sortedMap.put(dateKey, entry.getValue());
    }
    return sortedMap;
  }

  // computes the performance of a stock over time
  private void performanceOfStock() {
    view.display("Please input the ticker of the stock you wish to see the performance of.\n");
    String command = this.scanner.nextLine();
    command = this.checkValidStock(command);
    view.display("What span of time would you like to see the performance of?\n"
            + " 1) 5 days\n 2) 2 weeks\n 3) 1 month\n 4) 3 months\n 5) 6 months\n 6) 1 year\n");
    ArrayList<Integer> dates = new ArrayList<>(Arrays.asList(5, 14, 30, 90, 180, 365));
    int timespan = checkIntScanner();
    while (true) {
      if (timespan < 1 || timespan > 6) {
        view.display("Invalid input. Please input a number between 1 and 6.\n");
        timespan = checkIntScanner();
      }
      break;
    }
    timespan = dates.get(timespan - 1);
    String date = this.getValidDate();
    this.model.newPortfolio("temp");
    IPortfolio port = this.model.getPortfolios().get(this.model.getPortfolios().size() - 1);
    port = port.addStock(command, 1, "2000-01-01");
    HashMap<String, Double> ans = this.model.calculatePerformanceOverTime(port, timespan, date);
    TreeMap<LocalDate, Double> sortedMap = organize(ans);
    double total = 0.0;
    for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
      if (total < entry.getValue()) {
        total = entry.getValue();
      }
    }
    total = total / 12;
    total = Math.round(total / 10) * 10;
    String header = "Performance of " + command + " from " +
            sortedMap.firstEntry().getKey() + " to " + sortedMap.lastEntry().getKey() + ":\n";
    view.display(header);
    if (timespan == 365) {
      for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
        date = entry.getKey().toString();
        LocalDate d = LocalDate.parse(date);
        date = d.getMonth().toString();
        int outputDate = d.getYear();
        view.display(date.substring(0, 3) + " " + outputDate +
                ": " + "*".repeat((int) (entry.getValue() / total)) + "\n");
      }
    } else if (timespan == 14 || timespan == 5) {
      for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
        date = entry.getKey().toString();
        LocalDate d = LocalDate.parse(date);
        date = d.getDayOfWeek().toString();
        date = date.substring(0, 3);
        view.display(date + ": " + "*".repeat((int) (entry.getValue() / total)) + "\n");
      }
    } else {
      for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
        date = entry.getKey().toString();
        view.display(date + ": " + "*".repeat((int) (entry.getValue() / total)) + "\n");
      }
    }
    view.display("Scale: * = " + "$" + total + "\n");
    choiceHelper();
  }

  // checks that the user is inputting an int into scanner.nextInt
  private int checkIntScanner() {
    int scannerValue;
    while (true) {
      try {
        scannerValue = this.scanner.nextInt();
        while (scannerValue < 1) {
          view.display("Invalid number. Input a positive, non-zero number.\n");
          scannerValue = this.scanner.nextInt();
        }
        break;
      } catch (InputMismatchException e) {
        view.display("Invalid input. Please make sure to only input an integer.\n");
        this.scanner.next();
      }
    }
    return scannerValue;
  }

  // preforms the action desired by the user depending on the selection the user makes when
  // displayed the portfolioManager options
  private void portfolioManager(int index) {
    view.portfolioManagerOptions();
    int option = checkIntScanner();
    if (option == 1) {
      addStock(index);
    } else if (option == 2) {
      scanner.nextLine();
      if (model.getPortfolios().get(index).getStockListSize() == 0) {
        view.display("Cannot remove a stock if there are no stocks in the portfolio." +
                "Please add a stock first.\n");
        portfolioManager(index);
      } else {
        removeStock(index);
      }
    } else if (option == 3) {
      if (model.getPortfolios().get(index).getStockListSize() == 0) {
        view.display("Portfolio is empty, cannot get the value. Please add a stock first.\n");
        portfolioManager(index);
      } else {
        portfolioValue(index);
      }
    } else if (option == 4) {
      portfolioOverview(index);
      portfolioManager(index);
    } else if (option == 5) {
      model.deletePortfolio(index);
      portfolioHelper();
    } else if (option == 6) {
      distributionOfValue(index);
    } else if (option == 7) {
      saveFile(index);
    } else if (option == 8) {
      rebalancePortfolio(index);
    } else if (option == 9) {
      performanceOverTimeHelper(index);
    } else {
      portfolioHelper();
    }
  }

  // saves a portfolio so it can be accessed later.
  private void saveFile(int index) {
    try {
      model.saveFile(model.getPortfolios().get(index));
      view.display("File saved successfully. \n");
    } catch (IllegalArgumentException e) {
      view.display("Cannot save an empty portfolio. Please add stocks.\n");
      portfolioManager(index);
    }
    portfolioManager(index);
  }

  // Creates a new portfolio the user can use.
  private void newPortfolio() {
    view.display("Please enter a name for the portfolio:\n");
    String portfolioName = this.scanner.nextLine();
    if (!(model.getPortfolios().isEmpty())) {
      for (int i = 0; i < model.getPortfolios().size(); i++) {
        if (model.getPortfolios().get(i).getName().equals(portfolioName)) {
          view.display(portfolioName + " is already a portfolio. Please try again.\n");
          newPortfolio();
        }
      }
    }
    model.newPortfolio(portfolioName);
    view.display("Successfully create the new portfolio " + portfolioName + "\n");
    portfolioHelper();

  }

  // display all avaliable portfolios currently loaded.
  // Then asks the user to select one.
  private void accessPortfolios() {
    if (model.getPortfolios().isEmpty()) {
      view.display("There are no portfolios available. Please create one before trying to " +
              "access.\n");
      portfolioHelper();
    } else {
      view.display("Please select a portfolio (type a number starting at 1): "
              + model.displayPortfolios() + "\n");
      int portfolioSelection;
      while (true) {
        portfolioSelection = checkIntScanner();
        if (portfolioSelection > 0 && portfolioSelection <= model.getPortfolios().size()) {
          break;
        } else {
          view.display("Invalid selection. Please select a number between 1 and "
                  + model.getPortfolios().size() + "\n");
        }
      }
      portfolioManager(portfolioSelection - 1);
    }
  }

  // Outputs all the files that are saved potfolios
  // otherwise displays a msg saying no portfolios to load.
  private void loadFile() {
    ArrayList<String> output = model.getFiles();
    if (output == null) {
      view.display("There are no saved portfolios. Please try again.\n");
      portfolioHelper();
    }
    view.display("Input which portfolio you would like to load (starting at 1):\n");
    for (int i = 1; i <= output.size(); i++) {
      view.display((i) + ") " + output.get(i - 1) + "\n");
    }
    int x = checkIntScanner();

    if (x < 1 || x > output.size()) {
      view.display("Invalid selection. Please input a number between 1 and " + output.size() +
              ".\n");
      loadFile();
    }
    this.model.getFile(output, x);
    this.portfolioManager(model.getPortfolios().size() - 1);
  }

  // preforms the action desired by the user depending on the selection the user makes when
  // displayed the portfolioHelper options
  private void portfolioHelper() {
    view.portfolioHelperOptions();
    int outerOption = 0;
    while (true) {
      int option = checkIntScanner();
      if (option < 1 || option > 4) {
        view.display("Invalid selection. Please input a number between 1 and 4.\n");
      } else {
        outerOption = option;
        break;
      }
    }

    if (outerOption == 1) {
      this.scanner.nextLine();
      newPortfolio();
    } else if (outerOption == 2) {
      accessPortfolios();
    } else if (outerOption == 3) {
      loadFile();
    } else {
      this.scanner.nextLine();
      choiceHelper();
    }
  }

  // checks if the date of the current transaction is after or on the day of the last transaction
  // date of the specified portfolio.
  private String checkTransactionDate(int index) {
    String date = validDateInput(scanner.nextLine());
    boolean isTransactionInOrder = model.checkTranscationOrder(index, date);
    while (!isTransactionInOrder) {
      view.display("Transactions must be done in chronological order. " +
              date + " is before the last transaction date. Please select another date.\n");
      view.display("Input a date in YYYY-MM-DD format.\n");
      date = validDateInput(scanner.nextLine());
      isTransactionInOrder = model.checkTranscationOrder(index, date);
    }
    return date;
  }

  // adds a stock and the number of shares of the stock to the specified portfolio
  private void addStock(int index) {
    view.display("Type in the ticker of the stock you wish to add to the portfolio:\n");
    this.scanner.nextLine();
    String stock = checkValidStock(this.scanner.nextLine());
    view.display("Type in the number of shares you wish to add of " + stock + "\n");
    int quantity = checkIntScanner();
    String date = checkTransactionDate(index);
    try {
      model.addStock(index, stock, quantity, date);
    } catch (IllegalArgumentException e) {
      view.display("Quantity cannot be a negative number. Please try again.\n");
      addStock(index);
    }
    view.display("Successfully added " + quantity + " shares of " + stock + " to the portfolio.\n");
    portfolioManager(index);
  }

  // removes a stock or shares of stock from the specified portfolio
  private void removeStock(int index) {
    view.display("Type in the ticker of the stock you wish to remove from the portfolio:\n");
    String stock = checkValidStock(this.scanner.nextLine());
    if (!(model.getPortfolios().get(index).containsTicker(stock))) {
      view.display("Cannot remove " + stock + " from the portfolio" +
              " because it doesn't exist in the portfolio.\n");
      removeStock(index);
    }
    view.display("Enter a date in format YYYY-MM-DD\n");
    String date = checkTransactionDate(index);
    view.display("Type in the number of shares you wish to remove of " + stock + "\n");
    int quantity = checkIntScanner();
    if (model.getPortfolios().get(index).getTotalShares(stock, date) < quantity) {
      view.display("There are not enough shares in the portfolio to sell " + quantity + " shares." +
              "Please try again.\n");
      this.scanner.nextLine();
      removeStock(index);
    } else if (model.getPortfolios().get(index).getIndexes(stock, date).size() > 1) {
      partialShareRemove(index, quantity, stock, date);
    }
    model.removeStock(index, stock, quantity, -1, false);
    view.display("Successfully removed " + quantity + " shares of " + stock +
            " from the portfolio.\n");
    portfolioManager(index);
  }

  // allows the user to select what stocks to remove from if there are multiple purchases
  // of a stock on different dates
  private void partialShareRemove(int index, double quantity, String ticker, String date) {
    double numShares = quantity;
    while (numShares > 0) {
      view.display("Please select what purchase orders you would like to remove from: (" +
              numShares + " remaining to sell)\n" +
              model.getPortfolios().get(index).removeStockOptions(ticker, date));
      while (true) {
        int choice = checkIntScanner();
        Stock stockChoice = model.getPortfolios().get(index).getStock(choice - 1);
        if (choice > 0 && choice <=
                model.getPortfolios().get(index).getIndexes(ticker, date).size()) {
          if (stockChoice.getQuantity() < numShares) {
            model.removeStock(index, ticker, stockChoice.getQuantity(), choice - 1, true);
            numShares -= stockChoice.getQuantity();
          } else if (stockChoice.getQuantity() > numShares) {
            model.removeStock(index, ticker, numShares,
                    choice - 1, false);
            numShares = 0;
          } else if (stockChoice.getQuantity() == numShares) {
            model.removeStock(index, ticker, stockChoice.getQuantity(), choice - 1, true);
            numShares -= stockChoice.getQuantity();
          }
        } else {
          view.display("Invalid selection. Please input a number between 1 and " +
                  model.getPortfolios().get(index).getIndexes(ticker, date) + "\n");
          partialShareRemove(index, quantity, ticker, date);
        }
        view.display("Successfully removed " + quantity + " shares.\n");
        break;
      }
    }
    portfolioManager(index);
  }

  // calculates the value of the specified portfolio at the specified date
  private void portfolioValue(int index) {
    String inputtedDate = checkTransactionDate(index);
    view.display(model.portfolioValue(inputtedDate, index));
    portfolioManager(index);
  }

  // handles when the user selects to get the distribution of value of the stock
  private void distributionOfValue(int index) {
    if (model.getPortfolios().get(index).getStockListSize() == 0) {
      view.display("There are no stocks in the portfolio. " +
              "Cannot produce a distribution of values.");
      portfolioManager(index);
    }
    String inputtedDate = checkTransactionDate(index);
    String output = model.distributionOfValue(index, inputtedDate);
    view.display(output);
    portfolioManager(index);
  }

  // returns a String displaying the specified portfolio name and all the stocks and their
  // respective number of shares
  private void portfolioOverview(int index) {
    String date = checkTransactionDate(index);
    view.display(model.getPortfolios().get(index).getOrganizedPortfolio(date));
    portfolioManager(index);
  }

  // re-balances the portfolio composition based on the inputs made by the user.
  private void rebalancePortfolio(int index) {
    String date = checkTransactionDate(index);
    String currDistribution = model.distributionOfValue(index, date);
    view.display("The current distribution of value is below:\n" + currDistribution + "\n");
    List<Stock> listOfStocks = model.distributionOfValueStocks(index, date);
    if (listOfStocks.size() <= 1) {
      view.display("Cannot re-balance with one or fewer stocks in the portfolio. " +
              "Add more stocks and try again.\n");
      portfolioManager(index);
    }
    List<Integer> weights = new ArrayList<>();
    for (int i = 0; i < listOfStocks.size(); i++) {
      view.display("Please enter the percentage weight of " + listOfStocks.get(i).getTicker() +
              ". Enter the percentage as a whole number (ie. 40 for 40%).\n");
      int weight = checkIntScanner();
      weights.add(weight);
    }

    int sum = 0;
    for (int weight : weights) {
      sum += weight;
    }

    if (sum > 100) {
      view.display("The weights inputted add up to more than a 100. Please try again\n");
      portfolioManager(index);
    } else if (sum < 100) {
      view.display("The weights inputted do not add up to 100. Please try again\n");
      portfolioManager(index);
    } else {
      String output = model.rebalancePortfolio(weights, index, date);
      view.display(output);
      model.setDate(index, date);
      portfolioManager(index);
    }
  }

  // checks if the date is a valid date
  private boolean validDate(String command) {
    try {
      this.model.checkStockPrice(command);
    } catch (IllegalArgumentException e) {
      view.display("That is an invalid date. Please input a valid date.");
      return true;
    }
    return false;
  }

  // gets the correct portfolio using the parameter index,
  // then uses the stocks in that portfolio to determine
  // the performance of that portfolio over time.
  private void performanceOverTimeHelper(int index) {
    IPortfolio port = this.model.getPortfolios().get(index);
    view.display("What span of time would you like to see the performance of?\n"
            + " 1) 5 days\n 2) 2 weeks\n 3) 1 month\n 4) 3 months\n 5) 6 months\n 6) 1 year\n");
    ArrayList<Integer> dates = new ArrayList<>(Arrays.asList(5, 14, 30, 90, 180, 365));
    int timespan = checkIntScanner();
    while (true) {
      if (timespan < 1 || timespan > 6) {
        view.display("Invalid input. Please input a number between 1 and 6.\n");
        timespan = checkIntScanner();
      }
      break;
    }
    timespan = dates.get(timespan - 1);
    String date = this.getValidDate();
    HashMap<String, Double> ans = this.model.calculatePerformanceOverTime(port, timespan, date);
    TreeMap<LocalDate, Double> sortedMap = organize(ans);
    double total = 0.0;
    for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
      if (total < entry.getValue()) {
        total = entry.getValue();
      }
    }
    total = total / 12;
    total = Math.round(total / 10) * 10;
    String header = "Performance of " + port.getName() + " from " +
            sortedMap.firstEntry().getKey() + " to " + sortedMap.lastEntry().getKey() + ":\n";
    String display = "";
    view.display(header);
    if (timespan == 365) {
      for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
        date = entry.getKey().toString();
        LocalDate d = LocalDate.parse(date);
        date = d.getMonth().toString();
        int outputDate = d.getYear();
        display += date.substring(0, 3) + " " + outputDate +
                ": " + "*".repeat((int) (entry.getValue() / total)) + "\n";
      }
    } else if (timespan == 14 || timespan == 5) {
      for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
        date = entry.getKey().toString();
        LocalDate d = LocalDate.parse(date);
        date = d.getDayOfWeek().toString();
        date = date.substring(0, 3);
        display += date + ": " + "*".repeat((int) (entry.getValue() / total)) + "\n";
      }
    } else {
      for (Map.Entry<LocalDate, Double> entry : sortedMap.entrySet()) {
        date = entry.getKey().toString();
        display += date + ": " + "*".repeat((int) (entry.getValue() / total)) + "\n";
      }
    }
    view.display(display);
    view.display("Scale: * = " + total + "\n\n");
    portfolioManager(index);
  }
}