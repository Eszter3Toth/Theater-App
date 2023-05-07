package com.example.theaterapp;

public class ModelPlay {
    String id, play, uid;
    long timestamp, ticket_money;

    public ModelPlay() {
    }

    public ModelPlay(String id, String play, String uid, long timestamp, long ticket_money) {
        this.id = id;
        this.play = play;
        this.uid = uid;
        this.timestamp = timestamp;
        this.ticket_money = ticket_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlay() {
        return play;
    }

    public void setPlay(String play) {
        this.play = play;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTicket_money() {
        return ticket_money;
    }

    public void setTicket_money(long ticket_money) {
        this.ticket_money = ticket_money;
    }


}
