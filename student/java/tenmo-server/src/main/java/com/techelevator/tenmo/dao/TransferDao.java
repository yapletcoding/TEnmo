package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public void sendAndReceiveTransfer(long account_from, long account_to, double amount);

    public List<Transfer> getTransferHistoryByAccountId(long account_id);

    //public Transfer getTransferInfoByTransferId(long transfer_id);

}
