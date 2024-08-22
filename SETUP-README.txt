Run Jar File:
The Stocks.jar file is located in the res folder which is the same folder this readme
file is in. To run the jar file from the terminal, change the directory in your terminal
until you reach the Stocks folder where all of our code is held in. Then type in:
cd res (change directory into the res folder), then type in: java -jar Stocks.jar for the GUI
or java -jar Stocks.jar -text for the original text-based program. This should execute the main
method and separate java swing window will open for the GUI or a welcome message with options
should print to the console for the text-based program.

Data Supported:
Stocks: The program can check if the stock ticker is valid on all stocks in the NASDAQ
stock exchange which includes stocks on the S&P 500 and the Dow Jones Industrial.

Dates: All dates are supported up to twenty years from the current date per the
guidelines of the AlphaVantage API. Any dates that are weekends or holidays will not have
any stock data so the program will require the user to input another date. This functionality is
similar to any dates that are over the twenty-year mark from AlphaVantage.
