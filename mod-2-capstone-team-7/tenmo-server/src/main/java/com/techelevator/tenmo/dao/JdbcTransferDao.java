package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.*;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    private final int TRANSFER_REQUEST_TYPE_ID = 1;
    private final int TRANSFER_SEND_TYPE_ID = 2;

    private final int TRANSFER_STATUS_PENDING_ID = 1;
    private final int TRANSFER_STATUS_APPROVED_ID = 2;
    private final int TRANSFER_STATUS_REJECTED_ID = 3;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfer createNewTransfer(Transfer newTransfer) {

        Transfer returnedTransfer = null;

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

        try {

            int newID = jdbcTemplate.queryForObject(sql, int.class, TRANSFER_SEND_TYPE_ID, TRANSFER_STATUS_APPROVED_ID,
                            newTransfer.getAccountFrom(), newTransfer.getAccountTo(), newTransfer.getAmount());

            returnedTransfer = newTransfer;
            returnedTransfer.setTransferID(newID);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return returnedTransfer;

    }

    @Override
    public List<Transfer> getAllTransactionsForUserById(int userID) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM transfer t JOIN account a ON t.account_from = a.account_id OR t.account_to = a.account_id " +
                "WHERE a.user_id=?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userID);
        while(result.next()){
            transferList.add(mapRowToTransfer(result));
        }
        return transferList;
    }


    @Override
    public Transfer getTransferByTransferID(int transferID) {
        Transfer returnedTransfer = null;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferID);
        if(result.next()){
            returnedTransfer = mapRowToTransfer(result);
        }
        return returnedTransfer;
    }

    @Override
    public void updateTransferStatus(int newStatus, Transfer transferToUpdate){

        String sql = "UPDATE transfer " +
                "SET transfer_status_id = ? " +
                "WHERE transfer_id = ?;";

       if(jdbcTemplate.update(sql, newStatus, transferToUpdate.getTransferID()) == 1) {


          if(newStatus == TRANSFER_STATUS_APPROVED_ID){

              System.out.println("Transfer #" + transferToUpdate.getTransferID() + " is now APPROVED.");

          }else if(newStatus == TRANSFER_STATUS_PENDING_ID){

              System.out.println("Transfer #" + transferToUpdate.getTransferID() + " is now PENDING");

          }else if(newStatus == TRANSFER_STATUS_REJECTED_ID){

              System.out.println("Transfer #" + transferToUpdate.getTransferID() + " is now REJECTED");

          }

       }

    }



    private Transfer mapRowToTransfer(SqlRowSet sqlRowSet) {

        Transfer transfer = new Transfer();

        transfer.setTransferID(sqlRowSet.getInt("transfer_id"));
        transfer.setTransferTypeID(sqlRowSet.getInt("transfer_type_id"));
        transfer.setTransferStatus(sqlRowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfer.setAccountTo(sqlRowSet.getInt("account_to"));
        transfer.setAmount(sqlRowSet.getBigDecimal("amount"));

        return transfer;
    }






}