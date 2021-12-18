package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public double getBalance(long user_id) {
        Account account = new Account();
        String sql = "SELECT * FROM accounts WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user_id);

        if(rowSet.next()){
            account = mapRowToAccount(rowSet);
        }
        return account.getBalance();
    }

    public Account getAccountByAccountId(long account_id){
        Account account = new Account();
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, account_id);
        if (rowSet.next()){
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    public Account getAccountByUserId(long user_id){
        Account account = new Account();
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, user_id);
        if (rowSet.next()){
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    public double addToBalance(double amountToAdd, long account_id){
        Account account = getAccountByAccountId(account_id);
        double newBalance = account.getBalance()+ amountToAdd;
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, newBalance, account_id);
        return newBalance;
    }

    public double subtractFromBalance(double amountToSubtract, long account_id){
        Account account = getAccountByAccountId(account_id);
        double newBalance = account.getBalance() - amountToSubtract;
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, newBalance, account_id);
        return newBalance;
    }

    private Account mapRowToAccount(SqlRowSet row){
        Account account = new Account();
        account.setAccount_id(row.getLong("account_id"));
        account.setUser_id(row.getLong("user_id"));
        account.setBalance(row.getDouble("balance"));
        return account;
        // change big decimal in starting balance
    }


}
