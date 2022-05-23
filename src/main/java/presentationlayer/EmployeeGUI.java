package presentationlayer;

import businesslayer.DeliveryService;
import businesslayer.Employee;
import businesslayer.MenuItem;
import businesslayer.Order;
import datalayer.Serializer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EmployeeGUI extends JFrame {
    ArrayList<JButton> buttons;
    ArrayList<JPanel> orderPanels;
    Employee employee;
    public EmployeeGUI(Employee employee, ArrayList<Order> allOrders,ArrayList<ArrayList<MenuItem>> allOrdersMenuItems){
        this.employee = employee;
        showOrders(allOrders, allOrdersMenuItems);
        setVisible(true);
    }
    public void showOrders(ArrayList<Order> allOrders,ArrayList<ArrayList<MenuItem>> allOrdersMenuItems){
        revalidate();
        repaint();
        setSize(900, 600);
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                employee.setActive(false);
                DeliveryService.getDeliveryService().saveEmployeeStatus();
                System.exit(0);
            }
        });
        setResizable(false);
        buttons = new ArrayList<>();
        orderPanels = new ArrayList<>();
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.LINE_AXIS));
        JScrollPane scrollPanel = new JScrollPane(ordersPanel);
        for (ArrayList<MenuItem> menuItems : allOrdersMenuItems) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.PAGE_AXIS));
            ProductScrollPane productScrollPane = new ProductScrollPane("New Order!", menuItems);
            orderPanel.add(productScrollPane);
            productScrollPane.setVisible(true);
            JButton button = new JButton("Order done");
            button.addActionListener(e -> {
                int index = buttons.indexOf(button);
                Order o = allOrders.get(index);
                DeliveryService.getDeliveryService().orderDone(o, employee);
                ordersPanel.remove(index);
                orderPanels.remove(index);
                buttons.remove(index);
                ordersPanel.revalidate();
                ordersPanel.repaint();
                revalidate();
                repaint();
                //showOrders(allOrders, allOrdersMenuItems);
            });
            buttons.add(button);
            orderPanel.add(button);
            orderPanels.add(orderPanel);
            ordersPanel.add(orderPanel);
        }

        System.out.println("Total number of orders "+allOrdersMenuItems.size());
        System.out.println("");
        getContentPane().removeAll();
        revalidate();
        repaint();
        if (allOrders.size() == 0) {
            add(new JLabel("No orders"));
        }
        else {
            add(scrollPanel);
        }
    }
}
