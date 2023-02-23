# Fetch_Internship

# Project Description
This project is designed to help track payers and points per payer. When a user spends points, there are specific rules for determining what points to "spend" first. They are:
● We want the oldest points to be spent first (oldest based on transaction timestamp, not the order they’re received)
● We want no payer's points to go negative.

# How to run the program
run the program in your terminal by running the command "java Fetch.java <number of points to spend> <transactions csv file name>"
For example, if you want to spend 5000 points, it would look like this: "java Fetch.java 5000 transactions.csv"
