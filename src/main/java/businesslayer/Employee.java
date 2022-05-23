package businesslayer;

import datalayer.Serializer;
import presentationlayer.EmployeeGUI;
import presentationlayer.ProductScrollPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.*;

public class Employee extends User implements Observer, Serializable {
    boolean active;
    ArrayList<Order> allOrders;
    ArrayList<ArrayList<MenuItem>> allOrdersMenuItems;
    transient EmployeeGUI employeeGUI;

    public void execute() {
        active = true;
        employeeGUI = new EmployeeGUI(this,allOrders, allOrdersMenuItems);
        DeliveryService.getDeliveryService().saveEmployeeStatus();
    }

    public Employee(String username, String password){
        super(username,password,1);
        allOrders = new ArrayList<>();
        allOrdersMenuItems = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("comanda noua");
        Order order = (Order) ((ArrayList<Object>) arg).get(1);
        ArrayList<MenuItem> menuItems = (ArrayList<MenuItem>) ((ArrayList<Object>) arg).get(2);
        boolean add = (boolean)((ArrayList<Object>)arg).get(0);
        if(add) {
            System.out.println("S-a adaugat o comanda.");
            allOrders.add(order);
            allOrdersMenuItems.add(new ArrayList<>(menuItems));
        }
        else{
            int index = allOrders.indexOf(order);
            allOrders.remove(index);
            allOrdersMenuItems.remove(index);
            //employeeGUI.dispose();
            //employeeGUI = new EmployeeGUI(this, allOrders, allOrdersMenuItems);
        }
    }

    public void setActive(boolean active){
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(username, employee.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
