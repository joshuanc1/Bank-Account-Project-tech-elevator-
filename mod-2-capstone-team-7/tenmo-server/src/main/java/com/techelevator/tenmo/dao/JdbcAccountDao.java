package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalanceForAccount(int accountID) {

        BigDecimal accountBalance = null;

        String sql = "SELECT balance FROM account WHERE account_id = ?";

        try {

            accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountID);

        } catch (Exception ex) {

            System.out.println(ex.getMessage());

        }

        return accountBalance;

    }

    @Override
    public int getAccountIdFromUserId(int userID){
        int accountId = 0;

        String sql = "SELECT account_id FROM account WHERE user_id = ?";

        try{
            accountId =jdbcTemplate.queryForObject(sql, int.class, userID);
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return accountId;
    }


    @Override
    public int getUserIDForAccount(int accountID) {

        int userID = 0;

        String sql = "SELECT user_id FROM account WHERE account_id = ?";

        try {

            userID = jdbcTemplate.queryForObject(sql, int.class, accountID);

        } catch (Exception ex) {

            System.out.println(ex.getMessage());

        }

        return userID;


    }

    @Override
    public BigDecimal updateAccountBalance(int accountID, BigDecimal newBalance) {

        BigDecimal updatedBalance = null;
        Account updatedAccount;

        String sql = "UPDATE account " +
                "SET balance = ? " +
                "WHERE account_id = ? RETURNING balance;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, newBalance, accountID);
            if (result.next()) {
                updatedAccount = mapRowToAccount(result);
              updatedBalance = updatedAccount.getBalance();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return updatedBalance;

    }

    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountID(rowSet.getInt("account_id"));
        account.setUserID(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }




}
