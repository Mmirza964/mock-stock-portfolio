import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.CardLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListSelectionEvent;

/**
 * The StocksGUIViewImpl class implements the StocksGUIView interface and provides the
 * graphical user interface (GUI) for managing stock portfolios. This class sets up
 * various panels and components to allow users to add portfolios, trade stocks, view
 * portfolio overviews, and save or load portfolio data. It uses a CardLayout to
 * navigate between different screens and handles user interactions through buttons
 * and other GUI elements.
 */
public class StocksGUIViewImpl extends JFrame implements StocksGUIView {
  private JButton savePortfolioButton;
  private JButton fileOpenButton;
  private JButton fileLoadButton;
  private JButton btn3;
  private JButton portfolioPickerDropdown;
  private JButton emptyPortfolioError;
  private JButton btn2;
  private JButton addStockOption;
  private JButton checkPortfolio;
  private JPanel portfolioPicker;
  private JTextField portfolioNameField;
  private JTextField answer1;
  private JTextField answer2;
  private JTextField answer3;
  private JTextField answer4;
  private CardLayout cardLayout;
  private JComboBox portfolioNameBox;
  private JComboBox addStockDropdown;
  private JComboBox portfolioOverview;
  private JPanel cards;

  /**
   * Constructs a StocksGUIViewImpl object.
   */
  public StocksGUIViewImpl() {
    super("Stocks GUI");
    // first screen that gives options, based on button clicked will bring to next screen
    // 1 screen per welcome msg option
    // take info like a form.
    cardLayout = new CardLayout();
    cards = new JPanel(cardLayout);
    setTitle("Stocks GUI");
    setSize(800, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initalizeHomePage();
    initializeAddPortfolioPage();
    initializeSavePortfolioPage();
    makePortfolioPickerPage();
    errorPage(0);
    initializeEditingPortfolioPage();
    initalizeViewPortfolioPage();

    add(cards);
  }

  // creates the welcome page
  private void initalizeHomePage() {

    // Title at the top
    JPanel homeScreen = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("Welcome to Stocks GUI", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    homeScreen.add(titleLabel, BorderLayout.NORTH);

    JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
    JButton btn1 = new JButton("Add Portfolio");
    btn2 = new JButton("Trade Stocks");
    btn3 = new JButton("View Portfolio");
    JButton btn4 = new JButton("Save and Retrieve Portfolios");
    JButton btn5 = new JButton("Exit");

    buttonPanel.add(btn1);
    buttonPanel.add(btn2);
    buttonPanel.add(btn3);
    buttonPanel.add(btn4);
    buttonPanel.add(btn5);

    homeScreen.add(buttonPanel, BorderLayout.CENTER);

    JPanel portfolioPanel = new JPanel();
    portfolioPanel.add(new JLabel("Portfolio Management"));
    JPanel tradePanel = new JPanel();
    tradePanel.add(new JLabel("Trade in the Stock Market"));

    cards.add(homeScreen, "Home");


    btn1.addActionListener(e -> cardLayout.show(cards, "Add Portfolio"));
    btn4.addActionListener(e -> {
      cardLayout.show(cards, "Save and Retrieve Portfolios");
      pack();
    });
    btn5.addActionListener(e -> System.exit(0));

    add(cards);
    cardLayout.show(cards, "Home");
  }

  // creates the view portfolio page
  private void initalizeViewPortfolioPage() {
    JPanel viewPortfolioPanel = new JPanel(new GridBagLayout());
    // Add multiple lines of text to the textPanel
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);


    JLabel heading = new JLabel("Pick from the below portfolios to get an overview:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    viewPortfolioPanel.add(heading, gbc);

    portfolioOverview = new JComboBox<>(new String[]{});
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    viewPortfolioPanel.add(portfolioOverview, gbc);

    JButton backButton = new JButton("Back to Main Menu");
    backButton.addActionListener(e -> cardLayout.show(cards, "Home"));
    viewPortfolioPanel.add(backButton);


    JLabel label = new JLabel("View the price of the portfolio on this date:");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    viewPortfolioPanel.add(label, gbc);


    answer4 = new JTextField(10);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    viewPortfolioPanel.add(answer4, gbc);

    checkPortfolio = new JButton("View overview of this portfolio");
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    viewPortfolioPanel.add(checkPortfolio, gbc);

    cards.add(viewPortfolioPanel, "View Portfolio");
  }

  @Override
  public void showPortfolioOverviewPage(List<String> stock) {
    JPanel portOverviewPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST; // Align labels to the left
    gbc.fill = GridBagConstraints.HORIZONTAL;

    for (String s : stock) {
      JLabel lbl = new JLabel("<html>" + s.replace("\n", "<br>") + "</html>");

      portOverviewPanel.add(lbl, gbc);
      gbc.gridy++;
    }

    JButton backButton = new JButton("Back to Main Menu");
    backButton.addActionListener(e -> cardLayout.show(cards, "Home"));
    portOverviewPanel.add(backButton);


    cards.add(portOverviewPanel, "Portfolio Overview Panel");
  }

  // creates the add portfolio page
  private void initializeAddPortfolioPage() {

    JPanel addPortfolioPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);

    // Adding components to the panel
    addPortfolioPanel.add(new JLabel("Portfolio Name:"), gbc);
    portfolioNameField = new JTextField(20);
    addPortfolioPanel.add(portfolioNameField, gbc);


    savePortfolioButton = new JButton("Save Portfolio");
    addPortfolioPanel.add(savePortfolioButton, gbc);


    // Add the "Add Portfolio" panel to the cards
    cards.add(addPortfolioPanel, "Add Portfolio");
  }

  // creates the editing portfolio page
  private void initializeEditingPortfolioPage() {
    JPanel surveyPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    JLabel heading = new JLabel("Pick from these portfolios to edit:");
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.WEST;
    surveyPanel.add(heading, gbc);

    // Initialize the dropdown with an empty placeholder
    addStockDropdown = new JComboBox<>(new String[]{});
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    surveyPanel.add(addStockDropdown, gbc);

    JLabel question1 = new JLabel("What stock would you like to buy or sell?");
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    surveyPanel.add(question1, gbc);

    answer1 = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    surveyPanel.add(answer1, gbc);

    JLabel question2 = new JLabel(
            "Number of stocks you want to buy or sell(prefix with '-' for sell)");
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.NONE;
    surveyPanel.add(question2, gbc);

    answer2 = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    surveyPanel.add(answer2, gbc);

    JLabel question3 = new JLabel("Date you would like to purchase, in YYYY-MM-DD format.");
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.NONE;
    surveyPanel.add(question3, gbc);

    answer3 = new JTextField(20);
    gbc.gridx = 1;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    surveyPanel.add(answer3, gbc);


    addStockOption = new JButton("Submit");
    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.CENTER;
    surveyPanel.add(addStockOption, gbc);

    JButton backButton = new JButton("Back to Main Menu");
    backButton.addActionListener(e -> cardLayout.show(cards, "Home"));
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.CENTER;
    surveyPanel.add(backButton, gbc);

    cards.add(surveyPanel, "Trade Stocks");
  }

  @Override
  public void getAllPortfolios(List<IPortfolio> portfolios, int option) {
    ArrayList<String> portfolioNames = new ArrayList<>();
    for (IPortfolio portfolio : portfolios) {
      portfolioNames.add(portfolio.getName());
    }

    // Update the portfolioNameBox with new items
    if (option == 1) {
      addStockDropdown.setModel(new DefaultComboBoxModel<>(portfolioNames.toArray(new String[0])));
    } else if (option == 2) {
      portfolioOverview.setModel(new DefaultComboBoxModel<>(portfolioNames.toArray(new String[0])));
    }
  }

  // creates the save portfolio page
  private void initializeSavePortfolioPage() {
    JPanel displayPanel = new JPanel(new BorderLayout());
    JPanel fileOpenPanel = new JPanel();
    fileOpenPanel.setLayout(new GridLayout(2, 2));
    JLabel retrieve = new JLabel("Save a portfolio below", SwingConstants.CENTER);
    fileOpenButton = new JButton("Load file");
    fileOpenButton.setActionCommand("Open file");


    fileLoadButton = new JButton("Save file");
    fileOpenPanel.add(new JLabel("Retrieve a portfolio below ", SwingConstants.CENTER));
    fileLoadButton.setActionCommand("Load file");


    fileOpenPanel.add(retrieve);
    fileOpenPanel.add(fileOpenButton);
    fileOpenPanel.add(fileLoadButton);
    JButton backButton = new JButton("Back to Main Menu");
    backButton.addActionListener(e -> cardLayout.show(cards, "Home"));
    fileOpenPanel.add(backButton);
    displayPanel.add(fileOpenPanel, BorderLayout.CENTER);
    displayPanel.add(backButton, BorderLayout.SOUTH);
    cards.add(displayPanel, "Save and Retrieve Portfolios");

  }

  // creates the portfolio picker page
  private void makePortfolioPickerPage() {
    portfolioPicker = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel heading = new JLabel("Pick from these portfolios to save:");
    portfolioPickerDropdown = new JButton("Confirm Portfolio");
    portfolioPickerDropdown.setActionCommand("Confirm Portfolio");
    portfolioPicker.add(heading);
    portfolioPicker.add(portfolioPickerDropdown);
    cards.add(portfolioPicker, "All Portfolios");

  }

  @Override
  public void savePortfolioHelper(List<IPortfolio> p) {
    ArrayList<String> portfolioNames = new ArrayList<>();
    for (int i = 0; i < p.size(); i++) {
      portfolioNames.add(p.get(i).getName());
    }
    portfolioNameBox = new JComboBox(portfolioNames.toArray(new String[0]));
    portfolioPicker.add(portfolioNameBox);
    if (portfolioNames.isEmpty()) {
      JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel errorLabel =
              new JLabel("There are no portfolios to save. Please go to main menu.");
      errorPanel.add(errorLabel);
      portfolioPickerDropdown.setVisible(false);
      JButton backButton = new JButton("Back to Main Menu");
      backButton.addActionListener(e -> {
        cardLayout.show(cards, "Home");
        errorPanel.setVisible(false);
      });
      errorPanel.add(backButton, BorderLayout.SOUTH);
      portfolioPicker.add(errorPanel, "Save and Retrieve Portfolios");
    } else {
      portfolioPickerDropdown.setVisible(true);
    }
  }

  // creates the error page
  private void errorPage(int option) {
    JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    emptyPortfolioError = new JButton("Back to Main Menu");
    cards.add(errorPanel, "Error Panel");
    errorPanel.add(emptyPortfolioError, BorderLayout.SOUTH);
    emptyPortfolioError.addActionListener(e -> cardLayout.show(cards, "Home"));
    JLabel emptyportfolio = new JLabel("Cannot save an empty portfolio.");
    JLabel invalidInput = new JLabel("Please provide valid inputs");
    errorPanel.add(emptyportfolio, BorderLayout.SOUTH);
    errorPanel.add(invalidInput);
    emptyportfolio.setVisible(false);
    invalidInput.setVisible(false);
    if (option == 1) {
      emptyportfolio.setVisible(true);
      invalidInput.setVisible(false);
    } else if (option == 2) {
      emptyportfolio.setVisible(false);
      invalidInput.setVisible(true);
    }

  }

  @Override
  public void display() {
    setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    savePortfolioButton.addActionListener(e -> {
      if (portfolioNameField.getText().equals("")) {
        errorPage(1);
        cardLayout.show(cards, "Error Panel");
      } else {
        features.getPortfolioName(portfolioNameField.getText());
        portfolioNameField.setText("");
        cardLayout.show(cards, "Home");
      }
    });


    fileOpenButton.addActionListener(e -> {
      features.loadFile();
      cardLayout.show(cards, "Home");
    });


    fileLoadButton.addActionListener(e -> {
      features.saveFile();
      cardLayout.show(cards, "All Portfolios");
    });


    if (portfolioPickerDropdown != null) {
      portfolioPickerDropdown.addActionListener(e -> {

        try {
          features.choosePortfolio((String) portfolioNameBox.getSelectedItem());
          portfolioPicker.remove(portfolioNameBox);
          cardLayout.show(cards, "Home");
        } catch (IllegalArgumentException x) {
          portfolioPicker.remove(portfolioNameBox);
          errorPage(1);
          cardLayout.show(cards, "Error Panel");
        }
      });
    }


    if (emptyPortfolioError != null) {
      emptyPortfolioError.addActionListener(e -> cardLayout.show(cards, "Home"));
    }

    btn2.addActionListener(e -> {
      cardLayout.show(cards, "Trade Stocks");
      features.allPortfolio(1);
    }
    );

    btn3.addActionListener(e -> {
      cardLayout.show(cards, "View Portfolio");
      features.allPortfolio(2);
    });


    addStockOption.addActionListener(e -> {
      try {
        features.addOrSell((String) addStockDropdown.getSelectedItem()
                , answer1.getText(), answer2.getText(), answer3.getText());
        answer1.setText("");
        answer2.setText("");
        answer3.setText("");
        cardLayout.show(cards, "Home");
      } catch (IllegalArgumentException x) {
        errorPage(2);
        cardLayout.show(cards, "Error Panel");
      }
    });

    checkPortfolio.addActionListener(e -> {
      try {
        features.getPortfolioOverview((String) portfolioOverview.getSelectedItem(),
                answer4.getText());
        cardLayout.show(cards, "Portfolio Overview Panel");
      } catch (IllegalArgumentException x) {
        errorPage(2);
        cardLayout.show(cards, "Error Panel");
      }
    });
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // needed to have by implementation but didn't require it for GUI
  }

  @Override
  public void itemStateChanged(ItemEvent e) {
    // needed to have by implementation but didn't require it for GUI
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    // needed to have by implementation but didn't require it for GUI
  }
}