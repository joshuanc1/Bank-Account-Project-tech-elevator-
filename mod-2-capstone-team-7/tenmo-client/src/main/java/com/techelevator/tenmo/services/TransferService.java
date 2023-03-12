package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TransferService {



    private static final String API_BASE_URL = "http://localhost:8080/users/";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }



    //get balance
    public BigDecimal viewCurrentBalance(){
        BigDecimal returnedBalance = null;
        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(API_BASE_URL + "balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
           if(response.getBody() !=null){
               returnedBalance = response.getBody();
           }
        } catch(RestClientResponseException | ResponseStatusException ex){
            BasicLogger.log(ex.getMessage());
        }
        return returnedBalance;
    }

    //list Usernames
    public User[] listAllUsers(){
        User[] userList = null;
        try{
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
            if(response.getBody() !=null){
                userList = response.getBody();
            }
        } catch(RestClientResponseException | ResponseStatusException ex){
            BasicLogger.log(ex.getMessage());
        }
        return userList;
    }

    //Return ALL Transfers

    public Transfer[] listAllTransfers(){

        Transfer[] transferList = null;

        try{
        ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/getall", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            if(response.getBody() !=null){
                transferList = response.getBody();
            }
        }catch(RestClientResponseException | ResponseStatusException ex){
            BasicLogger.log(ex.getMessage());
        }

        return transferList;
    }


    //Get UserName by Account ID
    public String getUsernameByAccountID(int id){
        String username = "";
        try {
            ResponseEntity<String> response = restTemplate.exchange(API_BASE_URL + "account/" + id, HttpMethod.GET, makeAuthEntity(), String.class);
            if(response.getBody() != null) {
                username = response.getBody();
            }
        } catch(RestClientResponseException | ResponseStatusException ex){
            BasicLogger.log(ex.getMessage());
        }
        return username;
    }

    public boolean validateUserID(int userSelected){
        boolean isThere = false;
        User[] users = listAllUsers();
        for(User item : users){
            if(userSelected == item.getId()){
                isThere = true;
            }
        }
        return isThere;
    }

    //List ONE Transfer
    public Transfer getTransferByTransferID(int id){
        Transfer transfer = null;
        try{
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            if(response.getBody() != null){
                transfer = response.getBody();
            }
        } catch(RestClientResponseException | ResponseStatusException ex){
            BasicLogger.log(ex.getMessage());
        }
        return transfer;
    }


    // add Transfer
    public Transfer addTransfer(int transferType, int transferStatus, int accountFrom, int accountTo, BigDecimal amount){
        Transfer transfer = new Transfer();
        transfer.setTransferTypeID(transferType);
        transfer.setTransferStatus(transferStatus);
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        transfer.setAmount(amount);
        try {
           transfer = restTemplate.postForObject(API_BASE_URL + "transfer", makeTransferEntity(transfer), Transfer.class);
        } catch(RestClientResponseException | ResponseStatusException ex){
            BasicLogger.log(ex.getMessage());
        }
        return transfer;

    }





    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }





}
