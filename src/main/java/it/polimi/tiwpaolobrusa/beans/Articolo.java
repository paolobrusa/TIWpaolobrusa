package it.polimi.tiwpaolobrusa.beans;

public class Articolo {
    private int code;
    private String name;
    private String description;
    private String owner;
    private String path;
    private float price;

    public Articolo(int code, String name, String description, String owner, String path, float price) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.path = path;
        this.price = price;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
