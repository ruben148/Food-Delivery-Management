package businesslayer;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Observable;

public class Order implements Serializable {
    private Date date;
    private User user;
    private boolean served;
    private int id;

    public Order(User user, int id){
        date = new Date();
        this.user = user;
        served = false;
        this.id=Objects.hash(date, user, id);
    }

    public Date getDate() {
        return date;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public void serve(){
        served = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && date.equals(order.date) && user.equals(order.user);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
