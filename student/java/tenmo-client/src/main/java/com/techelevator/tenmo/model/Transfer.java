package com.techelevator.tenmo.model;

public class Transfer {
    private long transfer_id;
    private long transfer_type_id;
    private long transfer_status_id;
    private long account_from;
    private long account_to;
    private double amount;
    private long user_id_from;
    private long user_id_to;
    private String sender_name;
    private String receiver_name;

    public long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public long getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(long transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public long getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(long transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public long getAccount_from() {
        return account_from;
    }

    public void setAccount_from(long account_from) {
        this.account_from = account_from;
    }

    public long getAccount_to() {
        return account_to;
    }

    public void setAccount_to(long account_to) {
        this.account_to = account_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getUser_id_from() {
        return user_id_from;
    }

    public void setUser_id_from(long user_id_from) {
        this.user_id_from = user_id_from;
    }

    public long getUser_id_to() {
        return user_id_to;
    }

    public void setUser_id_to(long user_id_to) {
        this.user_id_to = user_id_to;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

}
