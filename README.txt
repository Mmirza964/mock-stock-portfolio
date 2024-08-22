All features in the program are correct and work. The features are each highlighted more in
depth below:

Gain or Loss Between Dates:
The user can determine the gain or loss of a stock over a specific period of dates.The user must
input a valid stock ticker symbol, and along with this must input two valid dates that the stock
market was open, both dates must also be in YYYY-MM-DD format otherwise you will be asked to input
a valid date.Once both valid dates are input the system will determine the total loss or gain
between those days.


X-Day Moving Average:
The user can determine a X day moving average of a specific stock. The user will be prompted to
input the ticker symbol for a valid stock. After this they must input a valid date in YYYY-MM-DD
format. If an invalid date, or a date the stock market was closed was input the user will be
prompted to input a valid date. After a valid date the user must input x, the number of days
they want to see the moving average of. The system will then output the x day moving
average of whatever stock was input earlier.After this the user will be prompted to input C if
you want to restart, otherwise you can input anything other than "C" and end the program.


X-Day Crossover:
The user can determine what days were crossovers in the last X days of a stock.The user will be
prompted to input the ticker of a valid stock first. After this the user will be asked to input
a valid date in YYYY-MM-DD format.If an invalid date, or a date the stock market was closed was
input the user will be prompted to input a valid date. After this they will be asked to input
a number X, which will be the last X days from the given date that crossovers will be checked for.
The system will then output which days were crossovers in the last x days for  whatever stock
was input earlier. After this the user will be prompted to input C if you want to restart,
otherwise you can input anything other than "C" and end the program.


Portfolio:
The user can create and delete as many portfolios as desired. Once the portfolio is named it
cannot be renamed. Inside each portfolio, the user can add stocks, remove stocks, get the value
of the portfolio at any specified date 20 years back from the current date, get an organized visual
in the console of the stocks in the portfolio and the number of shares of each stock, directly
delete the portfolio from inside, and exit back to the portfolio creation screen or all the way
back to the welcome screen. The welcome screen allows the user to exit and end the program. The new
functionality allows the user to save a portfolio to a file within the program folder. You can
also access previously saved portfolios and load them into the program. Loading them allows
more editing of the portfolio. Empty files cannot be saved. You can also re-balance the portfolio
to any specified weights. After re-balancing the program will not allow any transactions past
the date of re-balancing. Finally, you can get the distribution of value of the portfolio on
a specified day.

Performance Over Time:
The user can now also view the performance of any stock supported by the AlphaVantageAPI over
various periods of time. Currently, the options supported are: 1 year, 6 months, 3 months, 1 month,
2 weeks, and 5 days. Along with this, the user can view the performance of any portfolio over time,
with the same options being supported. The performance over time is displayed as a bar chart, with
a dynamic scale that changes depending on the price of the portfolio or stock. We represent a value
as an '*', and show the time range at the top of the bar chart. The dates on the left are displayed
differently depending on the time period selected, with 1 year only showing months and year of the
data, while 5 days for example only shows the day of the week.

GUI:
The graphical user interface is fully functional and allows the user to create portfolios,
save those portfolios, load already saved portfolios, add stock, remove stock, and get
the composition of the portfolio of a specified day.
