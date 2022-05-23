package businesslayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public interface DeliveryServiceProcessing {
    ArrayList<MenuItem> menuItems = null;
    int totalNumberOfOrders = 0;
    Map<Order, ArrayList<MenuItem>> orders = null;
    Map<Integer, Integer> numberOfOrdersPerDay = null;
    static DeliveryService deliveryService = null;

    ArrayList<MenuItem> getMenuItemsFromCSV(String file);

    void newOrder(ArrayList<MenuItem> menuItems, User user);

    void orderDone(Order order, Employee employee);

    void generateBill(Order order);

    void saveEmployeeStatus();

    ArrayList<MenuItem> getMenuItems();

    void addItemToMenu(MenuItem menuItem);

    void removeItemFromMenu(MenuItem menuItem);

    void editItem(int index, MenuItem menuItem);

    double getAverageBetween(Date hour1, Date hour2);

    double getAveragePriceBetween(Date hour1, Date hour2);

    int computeOrderPrice(Order order);

    double getAveragePerDay();

    double getAveragePricePerDay();

    ArrayList<MenuItem> getItemsOrderedMoreThan(int n);

    ArrayList<User> getUsers(int minimumOrderQuantity, int minimumOrderValue);

    ArrayList<MenuItem> getItemsOrderedAtDate(Date date);
}
