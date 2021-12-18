package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private JdbcAccountDao jdbcAccountDao;

    public JdbcTransferDao(DataSource dataSource, AccountDao accountDao) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.accountDao = accountDao;
    }

    public void sendAndReceiveTransfer(long account_from, long account_to, double amount){
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (2, 2, ?, ?, ?);";
        jdbcTemplate.update(sql, account_from, account_to ,amount);
        accountDao.addToBalance(amount, account_to);
        accountDao.subtractFromBalance(amount, account_from);
    }

    public List<Transfer> getTransferHistoryByAccountId(long account_id) {
        Transfer transfer = new Transfer();
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT *, a2.username AS sender_name, b2.username AS receiver_name FROM transfers " +
                "JOIN accounts AS a1 ON a.account_id = transfers.account_from " +
                "JOIN accounts AS b1 ON b.account_id = transfers.account_to " +
                "JOIN users AS a2 ON a2.user_id = a1.user_id " +
                "JOIN users AS b2 ON b2.user_id = b1.user_id " +
                "WHERE transfers.account_from = ? OR transfers.account_to = ?" +
                "ORDER BY transfers.transfer_id;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, account_id, account_id);
        while (rowSet.next()) {
            transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        }
        return transfers;
    }

    /*public Transfer getTransferInfoByTransferId(long transfer_id) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfers WHERE transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transfer_id);
        if (rowSet.next()){
            transfer = mapRowToTransfer(rowSet);
        }
        return transfer;
    }*/

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rs.getLong("transfer_id"));
        transfer.setTransfer_type_id(rs.getLong("transfer_type_id"));
        transfer.setTransfer_status_id(rs.getLong("transfer_status_id"));
        transfer.setAccount_from(rs.getLong("account_from"));
        transfer.setAccount_to(rs.getLong("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        transfer.setUser_id_from(rs.getLong("user_id"));
        transfer.setSender_name(rs.getString("sender_name"));
        transfer.setReceiver_name(rs.getString("receiver_name"));
        return transfer;
    }
}
