package be.howest.ti.adria.logic.domain;

public class Transaction {
    private final int id;
    private final String type;
    private final String name;
    private final double price;
    private final Integer maxUses;

    public Transaction(int id, String type, String name, double price, Integer maxUses) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.price = price;
        this.maxUses = maxUses;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Integer getMaxUses() {
        return maxUses;
    }
}
