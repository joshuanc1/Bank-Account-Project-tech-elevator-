package com.techelevator.tenmo.dao;


import java.math.BigDecimal;


public interface AccountDao {

    BigDecimal getBalanceForAccount(int accountID);

    int getUserIDForAccount(int accountID);

    BigDecimal updateAccountBalance(int accountID, BigDecimal newBalance);

    int getAccountIdFromUserId(int userID);

}
