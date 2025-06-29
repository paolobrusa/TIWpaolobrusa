package it.polimi.tiwpaolobrusa.beans;

import java.util.Date;
import java.util.List;

public class Asta {
    private int id;
    private List<Articolo> items;
    private float initialPrice;
    private int minBid;
    private Date date;
    private State state;

    public Asta(int id, List<Articolo> items, float initialPrice, int minBid, Date date, State state) {
        this.id = id;
        this.items = items;
        this.initialPrice = initialPrice;
        this.minBid = minBid;
        this.date = date;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Articolo> getItems() {
        return items;
    }

    public void setItems(List<Articolo> items) {
        this.items = items;
    }

    public float getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(float initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getMinBid() {
        return minBid;
    }

    public void setMinBid(int minBid) {
        this.minBid = minBid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
