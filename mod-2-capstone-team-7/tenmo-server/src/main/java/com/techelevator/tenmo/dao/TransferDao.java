package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;


import java.util.List;


public interface TransferDao {


    List<Transfer> getAllTransactionsForUserById(int userID);

    Transfer getTransferByTransferID(int transferID);

    Transfer createNewTransfer(Transfer newTransfer);

    void updateTransferStatus(int newStatus, Transfer transferToUpdate);




}
