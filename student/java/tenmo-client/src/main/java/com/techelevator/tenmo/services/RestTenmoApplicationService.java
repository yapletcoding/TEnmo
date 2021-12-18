package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.view.ConsoleService;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class RestTenmoApplicationService {

    private String API_BASE_URL = "http://localhost:8080/";
    private ConsoleService consoleService = new ConsoleService(System.in, System.out);
    private final RestTemplate restTemplate= new RestTemplate();
    Scanner userInput = new Scanner(System.in);

    public RestTenmoApplicationService(String url) {
        this.API_BASE_URL = url;
    }

    public double getBalance(String authToken) {
        double balance = 0;
        try {
            ResponseEntity<Double> response = restTemplate.exchange(API_BASE_URL + "/user/account/balance",
                    HttpMethod.GET, makeAuthEntity(authToken), Double.class);
            balance = response.getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public User[] userList(String authToken){
        User[] users;
        ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "/user", HttpMethod.GET,
                makeAuthEntity(authToken), User[].class);
        users = response.getBody();
        System.out.println("-------------------------------------------");
        System.out.println("User \n ID" + "       " + "Name");
        System.out.println("-------------------------------------------");
        for (User user : users){
            long id = user.getId();
            String username = user.getUsername();
            System.out.println(id+"   :  "+username);
        }
        return users;
    }

    public long getAccountIdByUserId(long userId, String authToken){
        Account account;
        long accountId= 0;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "/user/account", HttpMethod.GET,
                    makeAuthEntity(authToken), Account.class);
            account = response.getBody();
            accountId = account.getAccount_id();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return accountId;
    }

    public Account getOtherPeopleAccountByUserId(long userId){
        Account account = null;
        try {
            account = restTemplate.getForObject(API_BASE_URL + "/user/"+ userId+"/account", Account.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    public User getOtherUserByUserId(long userId){
        User user = null;
        try {
            user = restTemplate.getForObject(API_BASE_URL + "/user/"+ userId, User.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    public void sendTEBucks(long user_from, String authToken) {
        userList(authToken);
        boolean retry = true;
        Transfer newTransfer = new Transfer();
        long userTo = consoleService.getUserInputInteger("Enter ID of user you are sending to (0 to cancel): ");
        long accountTo = getOtherPeopleAccountByUserId(userTo).getAccount_id();
        String usernameTo = getOtherUserByUserId(userTo).getUsername();
        newTransfer.setAccount_to(accountTo);

        Account account= new Account();
        if(newTransfer.getAccount_to() !=0) {
            do {
                double amount = consoleService.getUserInputInteger("Enter amount");
                if (getBalance(authToken) > amount) {
                    newTransfer.setAmount(amount);
                    retry = false;
                } else {
                    System.out.println("Insufficient funds. Try again.");
                }
            } while (retry);
        }
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "/user/transfer/send", HttpMethod.POST,
                    makeTransferEntity(newTransfer, authToken), Transfer.class);
            System.out.println("You sent $" + String.format("%.2f", newTransfer.getAmount()) + " to "+ usernameTo);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public Transfer[] getTransferHistoryFromAccountId (String authToken, long account_id) {
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "/user/account/transfer/" + account_id,
                    HttpMethod.GET, makeAuthEntity(authToken), Transfer[].class);
            transfers = response.getBody();
            System.out.println("-------------------------------------------");
            System.out.println("Transfer \n ID" + "                  " + "From/To" + "       Amount");
            System.out.println("-------------------------------------------");
            for (Transfer transfer : transfers){
                long transferId = transfer.getTransfer_id();
                long accountFrom = transfer.getAccount_from();
                long accountTo = transfer.getAccount_to();
                String userTo = transfer.getReceiver_name();
                String userFrom = transfer.getSender_name();
                double amount = transfer.getAmount();
                if (accountFrom == account_id) {
                    System.out.println("Transfer ID: " + transferId + "     To: " + userTo + "   :   $" + amount);
                }
                else if (accountTo == account_id) {
                    System.out.println("Transfer ID: " + transferId + "   From: " + userFrom + "   :   $" + amount);
                }
            }
            System.out.println("");
            long userChoiceTransferId = consoleService.getUserInputInteger("Please enter transfer ID to view details (0 to cancel): ");
            for (Transfer transfer : transfers) {
                if (userChoiceTransferId == transfer.getTransfer_id()) {

                    System.out.println("--------------------------------------------");
                    System.out.println("Transfer Details");
                    System.out.println("--------------------------------------------");
                    System.out.println("Id: " + userChoiceTransferId);
                    System.out.println("From: " + transfer.getSender_name());
                    System.out.println("To: " + transfer.getReceiver_name());
                    System.out.println("Type: " + transfer.getTransfer_type_id());
                    //Status will need to be changed if we do the optional portion to reflect more than just approved
                    System.out.println("Status: Approved");
                    System.out.println("Amount: $" + transfer.getAmount());
                }
            }
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfers;
    }

    private HttpEntity<Void> makeAuthEntity(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String authToken){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer,headers);
    }
}
