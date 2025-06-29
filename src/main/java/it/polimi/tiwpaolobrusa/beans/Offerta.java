package it.polimi.tiwpaolobrusa.beans;

public class Offerta {
    private String usnUser;
    private int bid;
    private final Asta asta;

    public Offerta(String usnUser, int bid, Asta asta) {
        this.usnUser = usnUser;
        this.bid = bid;
        this.asta = asta;
    }

    public String getUsnUser() {
        return usnUser;
    }

    public void setUsnUser(String usnUser) {
        this.usnUser = usnUser;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public Asta getAsta() {
        return asta;
    }
}
