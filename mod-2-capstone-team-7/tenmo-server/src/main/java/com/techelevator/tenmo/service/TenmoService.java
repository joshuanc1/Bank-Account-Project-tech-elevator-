package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


@Component
public class TenmoService {

    UserDao userDao;
    TransferDao transferDao;
    AccountDao accountDao;


    public TenmoService(UserDao userDao, TransferDao transferDao, AccountDao accountDao){
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }


    public BigDecimal getBalanceByUsername(String username){
            int id = userDao.findIdByUsername(username);
            return userDao.getAccountBalanceByUserID(id);
        }

   public List<Transfer> getAllTransfersByUser(String username){
        int id = userDao.findIdByUsername(username);
        return transferDao.getAllTransactionsForUserById(id);
    }



    public boolean updateAccountBalance(Transfer transfer){

        boolean success = false;

        int senderAccountID = transfer.getAccountFrom();
        int receiverAccountID = transfer.getAccountTo();

        if(senderAccountID != receiverAccountID){

            BigDecimal currentSenderAccountBalance = accountDao.getBalanceForAccount(senderAccountID);
            BigDecimal currentReceiverAccountBalance = accountDao.getBalanceForAccount(receiverAccountID);

            if((currentSenderAccountBalance.compareTo(transfer.getAmount()) >=0) && (transfer.getAmount().compareTo(BigDecimal.ZERO)) > 0){

                accountDao.updateAccountBalance(senderAccountID, (currentSenderAccountBalance).subtract(transfer.getAmount()));
                accountDao.updateAccountBalance(receiverAccountID, currentReceiverAccountBalance.add(transfer.getAmount()));

                success = true;

            } else{
                transferDao.updateTransferStatus(3, transfer);
            }

        }

        return success;

    }

    public boolean compareAccountBalanceToTransferAmount(Transfer transfer){
        boolean transferAmountOk = false;

        BigDecimal sendingAmount = transfer.getAmount();
        BigDecimal userBalance = getBalanceByUsername(getUsernameSenderOrReciever(transfer.getAccountFrom()));

        if(sendingAmount.compareTo(userBalance) <= 0){
            transferAmountOk = true;
        }
        return transferAmountOk;
    }

    public Transfer changeUserIdToAccountID(Transfer transfer){

        int recipientUserID = transfer.getAccountTo();
        int senderUserID = transfer.getAccountFrom();

        int recipientAccountID = accountDao.getAccountIdFromUserId(recipientUserID);
        int senderAccountID = accountDao.getAccountIdFromUserId(senderUserID);

        transfer.setAccountTo(recipientAccountID);
        transfer.setAccountFrom(senderAccountID);

        return transfer;
        
    }


    public Transfer getTransferByIDOnlyIfSenderOrReceiver(int transferID, String username){

        Transfer returnTransfer = null;

        int currentUserAccountID = userDao.getAccountIDByUsername(username);

        Transfer transferRetrieved = transferDao.getTransferByTransferID(transferID);

        if(transferRetrieved.getAccountTo() == currentUserAccountID || transferRetrieved.getAccountFrom() == currentUserAccountID){

            returnTransfer = transferRetrieved;
        }

        return returnTransfer;
    }



    public String getUsernameSenderOrReciever(int accountID){
        String currentUsername = "";
        return currentUsername = userDao.getUserByAccountID(accountID);
    }


}
