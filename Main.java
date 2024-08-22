/**
 * The Main class is the entry point for the stock portfolio management application.
 * It initializes the StocksModel, StocksView, and StocksController implementations
 * and starts the program by calling the execute method on the controller.
 */
public class Main {
  /**
   * The main method serves as the entry point for the stock portfolio management application.
   * It initializes the StocksModel, StocksView, and StocksController implementations
   * and starts the program by calling the execute method on the controller.
   *
   * @param args command-line arguments (not used in this application).
   */
  public static void main(String[] args) {
    StocksModel model = new StocksModelImpl();
    StocksView view = new StocksViewImpl();
    StocksGUIView view2 = new StocksGUIViewImpl();
    StocksController controller2 = new StocksGUIControllerImpl(view2, model);
    StocksController controller = new StocksControllerImpl(model, view, System.in);

    if (args.length == 1 && args[0].equals("-text")) {
      controller.execute();
    } else if (args.length == 0) {
      controller2.execute();
    } else {
      System.out.println("Invalid command arguments.");
      System.out.println("Input: java -jar Stocks.jar -text (text-based program)");
      System.out.println("or java -jar Stocks.jar for (GUI program)");
    }
  }
}
