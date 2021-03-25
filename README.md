A Covid 19 tracker app for India which shows the number of cases in India each day from 30th January.
It shows data for number of recovered cases,number of positive cases and number of deaths each day.
It has 3 options,data for cases in last week or data for last month or we can see data for all time.

It uses Sparkchart for showing those data on the graph.
We can scrub on the graph for the recovered,positive and deaths data on the specified date.
The data is shown on the Ticker View which changes as we scrub on the chart.It has a nice animation for change.
We are using 3rd party library Robinhood for the Sparkchart and Ticker View.

We are using Radio Buttons for changing the data we want to see like max,1 week,month,positive,recovered and deaths.

An API is called for all the data.
The App uses Retrofit Library to get the JSON response from the API.
We are also using Gson for working with JSON data.
