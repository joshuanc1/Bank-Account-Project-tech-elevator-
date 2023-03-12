package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private TransferService transferService = new TransferService();
    private final ConsoleService consoleService = new ConsoleService(transferService);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else if(credentials.getUsername().equals("") || credentials.getPassword().equals("")){
            consoleService.printErrorUsernameOrPasswordEmpty();
        }else
        {
            consoleService.printErrorRegisterExistingUser();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if(currentUser != null){
            transferService.setAuthToken(currentUser.getToken());
        } else {
            consoleService.printErrorLogInMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {

		transferService.viewCurrentBalance();

        consoleService.printUserAccountBalance(transferService.viewCurrentBalance());

	}

	private void viewTransferHistory() {

		consoleService.printOutAllTransfers(transferService.listAllTransfers(), currentUser);

        int transferSelection = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");

		consoleService.printOutSelectedTransfer(transferSelection);

	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {

        consoleService.printOutAllUsers(transferService.listAllUsers());

        int userSelection = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");

        if(transferService.validateUserID(userSelection)) {

            BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: ");

            Transfer newTransfer = transferService.addTransfer(2, 2, currentUser.getUser().getId(), userSelection, amountToSend);

            consoleService.successOrFailure(newTransfer);

        } else {
            if(userSelection == 0){

                return;

            } else {

                consoleService.printInvalidUserId();

                return;

            }
        }
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
