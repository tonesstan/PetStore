package PetStoreTests.models;

import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public class Order {
    private long id;
    private long petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;

    public Order() {}

    public Order(long id, long petId, int quantity, String shipDate, String status, boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getPetId() {return petId;}

    public void setPetId(long petId) {this.petId = petId;}

    public int getQuantity() {return quantity;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public String getShipDate() {return shipDate;}

    public void setShipDate() {this.shipDate = ZonedDateTime.now().toLocalDateTime().truncatedTo(SECONDS).toString();}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public boolean isComplete() {return complete;}

    public void setComplete(boolean complete) {this.complete = complete;}
}
