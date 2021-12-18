package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

//    public void createAccount(Account account);

    public double getBalance(long user_id);

    public double addToBalance(double amountToAdd, long account_id);

    public double subtractFromBalance(double amountToSubtract, long account_id);

    public Account getAccountByUserId(long user_id);
}
