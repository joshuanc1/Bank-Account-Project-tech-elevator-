package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {


    int transferID;

    int transferTypeID;

    int transferStatus;

    int accountFrom;

    int accountTo;

    BigDecimal amount;

    public int getTransferID() {
        return transferID;
    }

    public void setTransferID(int transferID) {
        this.transferID = transferID;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTransferTypeID() {
        return transferTypeID;
    }

    public void setTransferTypeID(int transferTypeID) {
        this.transferTypeID = transferTypeID;
    }

    public Transfer(){};
    public Transfer(int transferID, int transferTypeID, int transferStatus, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferID = transferID;
        this.transferTypeID = transferTypeID;
        this.transferStatus = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public String transferTypeToString(int transferTypeID){
        String returnedString = "";
        if(transferTypeID == 1){
            returnedString = "Request";
        } else {
            returnedString = "Send";
        }
        return returnedString;
    }

    public String statusTypeToString(int transferStatusID){
        String returnedString = "";
        if(transferStatusID == 1){
            returnedString = "Pending";
        } else if (transferStatusID == 2) {
            returnedString = "Approved";
        } else{
            returnedString = "Rejected";
        }
        return returnedString;
    }


    public String toString(String accountFrom, String accountTo) {
        return "--------------------------------------------\n" +
                "Transfer Details\n" +
                "--------------------------------------------\n" +
                " Id: " + transferID + "\n" +
                " From: " + accountFrom + "\n" +
                " To: " + accountTo + "\n" +
                " Type: " + transferTypeToString(this.transferTypeID) + "\n" +
                " Status: " + statusTypeToString(this.transferStatus) +" \n" +
                " Amount: $" + this.getAmount();
    }
}
