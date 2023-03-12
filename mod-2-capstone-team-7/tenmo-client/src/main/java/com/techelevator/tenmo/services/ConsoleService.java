package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.Map;
import java.util.Scanner;

public class ConsoleService {
    private final TransferService transferService;
    private final Scanner scanner = new Scanner(System.in);


    public ConsoleService(TransferService transferService){
        this.transferService = transferService;
    }
    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printUserAccountBalance(BigDecimal balance){
        System.out.println("You current account balance is: $" + balance.setScale(2, RoundingMode.HALF_UP));
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }


    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void printOutAllUsers(User[] list){
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println("ID              Name");
        System.out.println("-------------------------------------------");

        for(User item :list){
            System.out.println(item.getId() + "              " + item.getUsername());
        }
    }

    public void successOrFailure(Transfer transfer){
        if(transfer == null){
            System.out.println("---------------------");
            System.out.println("Transfer was rejected, insufficient funds");
            System.out.println("---------------------");
        } else if(transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("---------------------");
            System.out.println("Transfer was rejected, must be greater than $0");
            System.out.println("---------------------");
        } else {
            System.out.println("---------------------");
            System.out.println("Transfer successfully made!");
            System.out.println("---------------------");
        }
    }



    public void printOutAllTransfers(Transfer[] transfers, AuthenticatedUser currentUser){
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
        System.out.println("-------------------------------------------");
        String currentUsername = currentUser.getUser().getUsername();
        for(Transfer item : transfers){
            String accountToUsername = transferService.getUsernameByAccountID(item.getAccountTo());
            String accountFromUsername = transferService.getUsernameByAccountID(item.getAccountFrom());
            if(accountToUsername.equals(currentUsername)) {
                System.out.println(item.getTransferID() + "          From: " + accountFromUsername + "                 $ " + item.getAmount().setScale(2, RoundingMode.HALF_UP));
            } else {
                System.out.println(item.getTransferID() + "          To: "  + accountToUsername + "                 $ " + item.getAmount().setScale(2, RoundingMode.HALF_UP));
            }

        }

        System.out.println("---------");

    }

    public void printOutSelectedTransfer(int transferId){

        try{
            if(transferId == 0){
                return;
            } else {
                Transfer transfer = transferService.getTransferByTransferID(transferId);
                String accountToUsername = transferService.getUsernameByAccountID(transfer.getAccountTo());
                String accountFromUsername = transferService.getUsernameByAccountID(transfer.getAccountFrom());
                System.out.println(transferService.getTransferByTransferID(transferId).toString(accountFromUsername, accountToUsername));
            }
        }catch(Exception ex){
            int selectedTransfer = promptForInt("Please choose a valid transfer ID (0 to cancel): ");

            printOutSelectedTransfer(selectedTransfer);
        }

    }


    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }


    public void printErrorLogInMessage(){
        System.out.println("-------------------------------------------");
        System.out.println("Invalid credentials. Please try again or register first.");
        System.out.println("-------------------------------------------");
    }

    public void printErrorRegisterExistingUser(){
        System.out.println("-------------------------------------------");
        System.out.println("Username already registered. Please choose a different username.");
        System.out.println("-------------------------------------------");
    }

    public void printErrorUsernameOrPasswordEmpty(){
        System.out.println("-------------------------------------------");
        System.out.println("Username/Password cannot be empty.");
        System.out.println("-------------------------------------------");
    }

    public void printInvalidUserId(){
        System.out.println("---------------------");
        System.out.println("That is not a valid User ID");
        System.out.println("---------------------");
    }


}
