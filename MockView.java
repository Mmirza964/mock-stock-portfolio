import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Mock View to test with.
 */
public class MockView implements StocksView {
  private final List<String> msg = new ArrayList<String>();

  /**
   * Gets list of messages used for testing.
   *
   * @return The list of messages printed by the view, but are stored for testing.
   */
  public List<String> getMsg() {
    return this.msg;
  }

  @Override
  public void display(String msg) {
    this.msg.add(msg);
    System.out.print(msg);
  }

  @Override
  public void welcomeMessage() {
    String msg = "Hello, this application is focused on stocks and can be used to help you\n" +
            "create a fake portfolio that uses real life data. With this application\n" +
            "you can determine the gain or loss of a stock over a period of time, determine \n"
            + "the X day moving average, and along with this create one or more portfolios\n"
            + "new portfolio and find the value of this portfolio on any date you want.\n"
            + "You can start a by inputting 'new portfolio' or you can determine data on\na "
            + "specific stock by inputting " + "it's unique ticker symbol. "
            + "All transactions done to\n mock portfolios must be executed in " +
            "chronological order.\n";
    this.display(msg);
  }

  @Override
  public void welcomeOptions() {
    String msg = "Pick which option you would like to continue with:\n"
            + "1) Examine the gain or loss of a stock over a specified period.\n"
            + "2) Examine the x-day moving average of a stock for a specified date\n " +
            "and a specified value of x\n"
            + "3) Determine which days are x-day crossovers any stock " +
            "over a specified date range with any value x.\n"
            + "4) View stock performance.\n"
            + "5) Portfolio Manager\n"
            + "6) End Program\n";
    this.display(msg);
  }

  @Override
  public void gainOrLoss() {
    String msg = "To determine the gain or loss over a period of time you will need to input\n" +
            "two dates, the start and end end dates. Please input the date in 'YYYY-MM-DD' " +
            "format.\n" +
            "Any values which are not an integer will be ignored.\n";
    this.display(msg);
  }

  @Override
  public void portfolioHelperOptions() {
    String msg = "Pick which option you would like to continue with:\n"
            + "1) Create a new portfolio\n"
            + "2) Access an existing portfolio.\n"
            + "3) Load an existing portfolio.\n"
            + "4) Exit.\n";
    this.display(msg);
  }

  @Override
  public void portfolioManagerOptions() {
    String msg = "Select an action to preform on the portfolio:\n"
            + "1) Add a stock to the portfolio.\n"
            + "2) Remove a stock from the portfolio.\n"
            + "3) Get the total value of the portfolio at a specific day.\n"
            + "4) Get portfolio composition.\n"
            + "5) Delete the portfolio.\n"
            + "6) Get distribution of value.\n"
            + "7) Save this portfolio.\n"
            + "8) Re-balance portfolio.\n"
            + "9) View performance over time.\n"
            + "10) Exit.\n";
    this.display(msg);
  }
}