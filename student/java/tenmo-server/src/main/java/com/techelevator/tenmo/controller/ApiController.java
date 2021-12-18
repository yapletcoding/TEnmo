package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("")
//PreAuthorize is currently stopping our Step 5 and Step 6 method
@PreAuthorize("isAuthenticated()")
public class ApiController {
    private AccountDao accountDao;
    private UserDao userDao;
    private TransferDao transferDao;
    private JdbcAccountDao jdbcAccountDao;

    public ApiController(AccountDao accountDao, UserDao userDao, TransferDao transferDao, JdbcAccountDao jdbcAccountDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDao = transferDao;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    @RequestMapping(path = "/user/account/balance", method = RequestMethod.GET)
    public double getBalance(Principal principal){
        long user_id = userDao.findIdByUsername(principal.getName());
        return accountDao.getBalance(user_id);
    }

    @RequestMapping(path = "/user" , method = RequestMethod.GET)
    public List<User> list(){
        return userDao.findAll();
    }

    @RequestMapping(path = "/user/transfer/send", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createTransfer(@RequestBody Transfer transfer, Principal principal){
        long userId = userDao.findIdByUsername(principal.getName());
        long account_from = jdbcAccountDao.getAccountByUserId(userId).getAccount_id();
        transferDao.sendAndReceiveTransfer(account_from, transfer.getAccount_to() , transfer.getAmount());
    }

    @RequestMapping(path = "/user/account/transfer/{account_id}", method = RequestMethod.GET)
    public List<Transfer> getTransferHistoryByAccountId(@PathVariable long account_id) {
        return transferDao.getTransferHistoryByAccountId(account_id);
    }

    @RequestMapping(path = "/user/account", method = RequestMethod.GET)
    public Account getOtherPeopleAccountByUserId(Principal principal) {
        long user_id= userDao.findIdByUsername(principal.getName());
        return accountDao.getAccountByUserId(user_id);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/user/{userId}/account", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable long userId) {
        return accountDao.getAccountByUserId(userId);
    }

    @PreAuthorize("permitAll")
    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public User getUserByUserId(@PathVariable long userId){
        return userDao.findUserByUserId(userId);
    }

//Not being used
    /*@RequestMapping(path = "/account/transfer/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransferInfoByTransferId(@PathVariable long transfer_id) {
        return transferDao.getTransferInfoByTransferId(transfer_id);
    }*/
}