package businesslayer;

import datalayer.CSVRead;
import datalayer.Serializer;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public final class DeliveryService extends Observable implements DeliveryServiceProcessing {
    private ArrayList<MenuItem> menuItems;
    int totalNumberOfOrders;
    private Map<Order, ArrayList<MenuItem>> orders;
    private Map<Integer, Integer> numberOfOrdersPerDay;
    private static DeliveryService deliveryService = null;

    private DeliveryService() {
        menuItems = Serializer.readMenuItems();
        orders = Serializer.readOrders();
        numberOfOrdersPerDay = new LinkedHashMap<>();
        for (Order order : orders.keySet()) {
            Calendar calDay = Calendar.getInstance();
            calDay.setTime(order.getDate());
            int day = calDay.get(Calendar.DATE);
            numberOfOrdersPerDay.putIfAbsent(day, 0);
            numberOfOrdersPerDay.put(day, numberOfOrdersPerDay.get(day) + 1);
        }
        totalNumberOfOrders = orders.keySet().size();
        for (Employee employee : User.getEmployees())
            addObserver(employee);
    }

    public static DeliveryService getDeliveryService() {
        if (deliveryService == null)
            deliveryService = new DeliveryService();
        return deliveryService;
    }

    /**
     * Retrieves a list of MenuItem's from the specified file
     * @param file The .csv file which contains the MenuItem's
     * @return The list of MenuItem's
     */
    public ArrayList<MenuItem> getMenuItemsFromCSV(String file) {
        if(file==null)
            throw new NullPointerException("File is null");
        ArrayList<MenuItem> menuItems;
        try {
            menuItems = CSVRead.read(file);
        } catch (FileNotFoundException ex) {
            menuItems = new ArrayList<>();
        }
        this.menuItems = menuItems;
        Serializer.writeMenuItems(menuItems);
        return menuItems;
    }

    /**
     * Registers a new order
     * @param menuItems The list of items in the order
     * @param user The user who made the order
     */
    public void newOrder(ArrayList<MenuItem> menuItems, User user) {
        if(menuItems.size()==0)
            throw new UnsupportedOperationException("Cannot create empty order.");
        Order order = new Order(user, totalNumberOfOrders);
        orders.put(order, menuItems);
        for (MenuItem menuItem : menuItems)
            menuItem.addQuantity();
        Calendar calDay = Calendar.getInstance();
        calDay.setTime(order.getDate());
        int day = calDay.get(Calendar.DATE);
        numberOfOrdersPerDay.putIfAbsent(day, 0);
        numberOfOrdersPerDay.put(day, numberOfOrdersPerDay.get(day) + 1);
        totalNumberOfOrders++;
        generateBill(order);
        user.increaseOrderNumber();
        ArrayList<Object> objects = new ArrayList<>() {{
            add(true);
            add(order);
            add(menuItems);
        }};
        setChanged();
        notifyObservers(objects);
        Serializer.writeOrders(orders);
        Serializer.writeMenuItems(this.menuItems);
        Serializer.writeUserInfo(User.getUsers());
        Serializer.writeEmployeeInfo(User.employees);
    }

    /**
     * Register an order as done
     * @param order the order which is done
     * @param employee the employee who served the order
     */
    public void orderDone(Order order, Employee employee) {
        if(!employee.active)
            throw new IllegalArgumentException("Employee is not active.");
        ArrayList<Object> objects = new ArrayList<>() {{
            add(false);
            add(order);
            add(orders.get(order));
        }};
        order.serve();

        setChanged();
        notifyObservers(objects);

        Serializer.writeOrders(orders);
        Serializer.writeEmployeeInfo(User.employees);
    }

    /**
     * Generates a .txt bill
     * @param order the order for which to generate the bill
     */
    public void generateBill(Order order) {
        if(!orders.containsKey(order))
            throw new UnsupportedOperationException("There is no such order.");
        int bill_number = 0;
        String filename = "bill.txt";
        try {
            File file = new File(filename);
            if (file.createNewFile()) {

            } else {
                while (!file.createNewFile()) {
                    bill_number++;
                    filename = "bill (" + bill_number + ").txt";
                    file = new File(filename);
                }
            }
            FileWriter writer = new FileWriter(filename);
            writer.write("Username: " + order.getUsername() + "   Date: " + order.getDate() + "\n\n");
            int totalPrice = 0;
            for (MenuItem menuItem : orders.get(order)) {
                totalPrice += menuItem.getPrice();
                writer.write(menuItem.getTitle() + "    " + menuItem.getPrice() + "$\n");
            }
            writer.write("\nTotal price: " + totalPrice);
            writer.close();
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Save the status of all Employee instances
     */
    public void saveEmployeeStatus() {
        Serializer.writeEmployeeInfo(User.employees);
    }

    /**
     * Get the list of menu items
     * @return the items in the menu
     */
    public ArrayList<MenuItem> getMenuItems() {
        menuItems = Serializer.readMenuItems();
        assert !menuItems.isEmpty();
        return menuItems;
    }

    /**
     *  Add an item to the menu
     * @param menuItem the MenuItem to add
     */
    public void addItemToMenu(MenuItem menuItem) {
        if(Objects.equals(menuItem.getTitle(), ""))
            throw new UnsupportedOperationException("Title cannot be null");
        if(!menuItems.contains(menuItem))
            menuItems.add(menuItem);
        Serializer.writeMenuItems(menuItems);
    }

    /**
     * Remove an item from the menu
     * @param menuItem the MenuItem to remove
     */
    public void removeItemFromMenu(MenuItem menuItem) {
        if(!menuItems.remove(menuItem))
            throw new NoSuchElementException();
        Serializer.writeMenuItems(menuItems);
    }

    /**
     * Edit one MenuItem
     * @param index the index of the MenuItem to edit
     * @param menuItem the new MenuItem
     */
    public void editItem(int index, MenuItem menuItem) {
        if(index>=menuItems.size())
            throw new NoSuchElementException();
        if(menuItem.getTitle().equals(""))
            throw new UnsupportedOperationException("Title cannot be null");
        menuItems.set(index, menuItem);
        Serializer.writeMenuItems(menuItems);
    }

    /**
     *  Get the average numbers of orders, made between 2 hours, regardless the day
     *  Takes into account every day that the service had orders
     * @param hour1
     * @param hour2
     * @return the average
     */
    public double getAverageBetween(Date hour1, Date hour2) {
        if(hour1.after(hour2))
            throw new IllegalArgumentException("First date is after second date.");
        int total = 0;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(hour1);
        cal2.setTime(hour2);
        int h1 = cal1.get(Calendar.HOUR_OF_DAY);
        int h2 = cal2.get(Calendar.HOUR_OF_DAY);
        int m1 = cal1.get(Calendar.MINUTE);
        int m2 = cal2.get(Calendar.MINUTE);
        Map<Integer, Integer> numberOfOrdersPerDay = new LinkedHashMap<>();
        for (Order order : orders.keySet()) {
            Date hour = order.getDate();
            Calendar calDay = Calendar.getInstance();
            calDay.setTime(order.getDate());
            int day = calDay.get(Calendar.DATE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(hour);
            int h = cal.get(Calendar.HOUR_OF_DAY);

            int m = cal.get(Calendar.MINUTE);

            if (compareTime(h, m, h1, m1) == 1)
                if (compareTime(h2, m2, h, m) == 1) {
                    numberOfOrdersPerDay.putIfAbsent(day, 0);
                    numberOfOrdersPerDay.put(day, numberOfOrdersPerDay.get(day) + 1);
                    total++;
                }
        }
        double avg = 1.0 * total / numberOfOrdersPerDay.keySet().size();
        assert Objects.equals(avg, Double.NaN);
        return avg;
    }

    /**
     * Get the average total price of orders, made between 2 hours, regardless the day
     * Takes into account every day that the service had orders
     * @param hour1
     * @param hour2
     * @return the average
     */
    public double getAveragePriceBetween(Date hour1, Date hour2) {
        if(hour1.after(hour2))
            throw new IllegalArgumentException("First date is after second date.");
        int total = 0;
        for (Order order : orders.keySet()) {
            Date hour = order.getDate();
            Calendar cal = Calendar.getInstance();
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal.setTime(hour);
            cal1.setTime(hour1);
            cal2.setTime(hour2);
            int h = cal.get(Calendar.HOUR_OF_DAY);
            int h1 = cal1.get(Calendar.HOUR_OF_DAY);
            int h2 = cal2.get(Calendar.HOUR_OF_DAY);
            int m = cal.get(Calendar.MINUTE);
            int m1 = cal1.get(Calendar.MINUTE);
            int m2 = cal2.get(Calendar.MINUTE);
            if (compareTime(h, m, h1, m1) == 1)
                if (compareTime(h2, m2, h, m) == 1)
                    total += computeOrderPrice(order);
        }
        double avg = 1.0 * total / numberOfOrdersPerDay.keySet().size();
        assert Objects.equals(avg, Double.NaN);
        return avg;
    }

    /**
     *  Compare hour and minute with another hour and minute
     * @param h1
     * @param m1
     * @param h2
     * @param m2
     * @return 1 if first is after that the second, -1 if first is behind the second and 0 if they are the same
     */
    private int compareTime(int h1, int m1, int h2, int m2) {
        assert h1>=0 & h2>=0 & m1>=0 & m2>=0;
        if (h1 < h2)
            return -1;
        if (h1 > h2)
            return 1;
        if (m1 < m2)
            return -1;
        return 1;
    }

    /**
     * Get the total price of an order
     * @param order
     * @return total price
     */
    public int computeOrderPrice(Order order) {
        int totalPrice = 0;
        for (MenuItem menuItem : orders.get(order))
            totalPrice += menuItem.getPrice();
        assert totalPrice>=0;
        return totalPrice;
    }

    /**
     * Get the average number of orders made in one day
     * @return the average
     */
    public double getAveragePerDay() {
        return 1.0 * totalNumberOfOrders / numberOfOrdersPerDay.keySet().size();
    }

    /**
     * Get the average sum spent in one day
     * @return
     */
    public double getAveragePricePerDay() {
        int total = 0;
        for (Order order : orders.keySet())
            total += computeOrderPrice(order);
        double avg = 1.0 * total / numberOfOrdersPerDay.keySet().size();
        assert Objects.equals(avg, Double.NaN);
        return avg;
    }

    /**
     * Get the MenuItems ordered more that a specified ammount
     * @param n
     * @return the list of items
     */
    public ArrayList<MenuItem> getItemsOrderedMoreThan(int n) {
        if(n<0)
            throw new IllegalArgumentException();
        return menuItems.stream()
                .filter(m -> m.getQuantity() >= n && m.setTemporaryQuantity(m.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get users who ordered more than a specified ammount of times, with a value more than specified
     * @param minimumOrderQuantity
     * @param minimumOrderValue
     * @return A list of users
     */
    public ArrayList<User> getUsers(int minimumOrderQuantity, int minimumOrderValue) {
        if(minimumOrderQuantity<0 || minimumOrderValue<0)
            throw new IllegalArgumentException();
        Map<User, Integer> userOrderMap = new LinkedHashMap<>();
        for (Order order : orders.keySet()) {
            if (computeOrderPrice(order) >= minimumOrderValue) {
                userOrderMap.putIfAbsent(order.getUser(), 0);
                userOrderMap.put(order.getUser(), userOrderMap.get(order.getUser()) + 1);
            }
        }

        return userOrderMap.keySet().stream()
                .filter(m -> userOrderMap.get(m) >= minimumOrderQuantity && m.setTemporaryNumberOfOrders(userOrderMap.get(m)))
                .collect(Collectors.toCollection(ArrayList::new));
        /*
        ArrayList<User> users = new ArrayList<>();
        for (User user : userOrderMap.keySet()) {
            if (userOrderMap.get(user) >= minimumOrderQuantity)
                user.setTemporaryNumberOfOrders(userOrderMap.get(user));
            users.add(user);
        }
        return users;*/
    }

    /**
     * Get the MenuItems ordered at a date
     * @param date the date
     * @return
     */
    public ArrayList<MenuItem> getItemsOrderedAtDate(Date date) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        /*for (Order order : orders.keySet()) {
            LocalDate localOrderDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println("Comanda:");
            for (MenuItem menuItem : orders.get(order)){
                System.out.println(menuItem.getTitle());
            }
            System.out.println("");
            if (localDate.isEqual(localOrderDate)) {
                System.out.println("Am gasit o comanda in acea data:");
                for (MenuItem menuItem : orders.get(order)) {
                    System.out.println(menuItem.getTitle());
                    if (!menuItems.contains(menuItem)) {

                        menuItems.add(menuItem);
                        menuItem.setTemporaryQuantity(1);
                    } else
                        menuItem.setTemporaryQuantity(menuItem.getTemporaryQuantity() + 1);
                }
                System.out.println("");
            }
        }
        return menuItems;*/
        orders.keySet().stream().forEach(o -> {
            LocalDate localOrderDate = o.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (localDate.isEqual(localOrderDate)) {
                for (MenuItem menuItem : orders.get(o)) {
                    if (!menuItems.contains(menuItem)) {
                        menuItems.add(menuItem);
                        menuItem.setTemporaryQuantity(1);
                    } else
                        menuItem.setTemporaryQuantity(menuItem.getTemporaryQuantity() + 1);
                }
            }
        });
        return menuItems;
    }
}
