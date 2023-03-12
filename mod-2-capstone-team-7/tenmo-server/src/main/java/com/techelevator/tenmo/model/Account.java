package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    int accountID;
    int userID;
    BigDecimal balance;

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account(){}

    @Override
    public String toString() {
        return "Account{" +
                "accountID=" + accountID +
                ", userID=" + userID +
                ", balance=" + balance +
                '}';
    }
}
