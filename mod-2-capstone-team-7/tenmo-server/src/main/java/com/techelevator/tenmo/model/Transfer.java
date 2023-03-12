package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {

    @NotNull
    int transferID;
    @NotNull
    int transferTypeID;
    @NotNull
    int transferStatus;
    @NotNull
    int accountFrom;
    @NotNull
    int accountTo;
    @NotNull
    @Positive(message = "Transfer amount must be more than $0.00.")
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

    @Override
    public String toString() {
        return "Transfer{" +
                "transferID=" + transferID +
                ", transferTypeID=" + transferTypeID +
                ", transferStatus=" + transferStatus +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", amount=" + amount +
                '}';
    }
}
