package datalayer;

import businesslayer.Employee;
import businesslayer.MenuItem;
import businesslayer.Order;
import businesslayer.User;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Serializer {
    public static void writeMenuItems(ArrayList<MenuItem> menuItems) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("menu_item_save"));
            objectOutputStream.writeObject(menuItems);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeOrders(Map<Order, ArrayList<MenuItem>> orders) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("order_save"));
            objectOutputStream.writeObject(orders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeUserInfo(ArrayList<User> users){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("user_info_save"));
            objectOutputStream.writeObject(users);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void writeEmployeeInfo(ArrayList<Employee> employees){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("employees_save"));
            objectOutputStream.writeObject(employees);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<MenuItem> readMenuItems() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("menu_item_save"));
            return (ArrayList<MenuItem>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    public static Map<Order, ArrayList<MenuItem>> readOrders() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("order_save"));
            return (Map<Order, ArrayList<MenuItem>>) objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            return new LinkedHashMap<>();
        }
    }
    public static ArrayList<User> readUserInfo() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("user_info_save"));
            return (ArrayList<User>) objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            return new ArrayList<>();
        }
    }
    public static ArrayList<Employee> readEmployeeInfo() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("employees_save"));
            return (ArrayList<Employee>) objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            return new ArrayList<>();
        }
    }
    public static ArrayList<MenuItem> menuItemsClone(ArrayList<MenuItem> menuItems){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("menu_item_clone"));
            objectOutputStream.writeObject(menuItems);
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("menu_item_clone"));
            File file = new File("menu_item_clone");
            file.delete();
            return (ArrayList<MenuItem>) objectInputStream.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            return new ArrayList<>();
        }
    }
}
