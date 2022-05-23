package presentationlayer;

import businesslayer.DeliveryService;
import businesslayer.MenuItem;
import businesslayer.User;
import datalayer.Serializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ClientGUI extends JFrame {
    double ratingMin, ratingMax;
    int caloriesMin, caloriesMax;
    int proteinMin, proteinMax;
    int fatMin, fatMax;
    int sodiumMin, sodiumMax;
    int priceMin, priceMax;
    String titleFilter;
    ArrayList<MenuItem> menuItems;
    ArrayList<MenuItem> basket = new ArrayList<>();

    public ClientGUI(User user) {
        super();

        //TODO view composed product details

        setSize(750, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new FlowLayout());

        menuItems = DeliveryService.getDeliveryService().getMenuItems();
        ProductScrollPane menuItemsScrollPane = new ProductScrollPane("Menu", menuItems);
        ProductScrollPane basketScrollPane = new ProductScrollPane("Basket", new ArrayList<>());

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.PAGE_AXIS));

        JTextField titleFilter = new JTextField(11);

        JLabel titleLabel = new JLabel("Title");

        filterPanel.add(titleLabel);
        filterPanel.add(titleFilter);

        JTabbedPane filterPane = new JTabbedPane();
        filterPanel.add(filterPane);
        JPanel pricePanel = new JPanel(new FlowLayout());
        JTextField price1 = new JTextField(4);
        JTextField price2 = new JTextField(4);
        pricePanel.add(price1);
        pricePanel.add(price2);
        filterPane.addTab("Price", pricePanel);

        JPanel caloriesPanel = new JPanel();
        JTextField calories1 = new JTextField(4);
        JTextField calories2 = new JTextField(4);
        caloriesPanel.add(calories1);
        caloriesPanel.add(calories2);
        filterPane.addTab("Calories", caloriesPanel);

        JPanel proteinPanel = new JPanel();
        JTextField protein1 = new JTextField(4);
        JTextField protein2 = new JTextField(4);
        proteinPanel.add(protein1);
        proteinPanel.add(protein2);
        filterPane.addTab("Protein", proteinPanel);

        JPanel fatPanel = new JPanel();
        JTextField fat1 = new JTextField(4);
        JTextField fat2 = new JTextField(4);
        fatPanel.add(fat1);
        fatPanel.add(fat2);
        filterPane.addTab("Fat", fatPanel);

        JPanel sodiumPanel = new JPanel();
        JTextField sodium1 = new JTextField(4);
        JTextField sodium2 = new JTextField(4);
        sodiumPanel.add(sodium1);
        sodiumPanel.add(sodium2);
        filterPane.addTab("Sodium", sodiumPanel);

        JPanel ratingPanel = new JPanel();
        JTextField rating1 = new JTextField(4);
        JTextField rating2 = new JTextField(4);
        ratingPanel.add(rating1);
        ratingPanel.add(rating2);
        filterPane.addTab("Rating", ratingPanel);

        JButton filterButton = new JButton("Filter");
        filterPanel.add(filterButton);
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ratingMin = Double.parseDouble(rating1.getText());
                } catch (NumberFormatException exception) {
                    ratingMin = 0;
                }
                try {
                    ratingMax = Double.parseDouble(rating2.getText());
                } catch (NumberFormatException exception) {
                    ratingMax = 5;
                }
                try {
                    caloriesMin = Integer.parseInt(calories1.getText());
                } catch (NumberFormatException exception) {
                    caloriesMin = 0;
                }
                try {
                    caloriesMax = Integer.parseInt(calories2.getText());
                } catch (NumberFormatException exception) {
                    caloriesMax = Integer.MAX_VALUE;
                }
                try {
                    proteinMin = Integer.parseInt(protein1.getText());
                } catch (NumberFormatException exception) {
                    proteinMin = 0;
                }
                try {
                    proteinMax = Integer.parseInt(protein2.getText());
                } catch (NumberFormatException exception) {
                    proteinMax = Integer.MAX_VALUE;
                }
                try {
                    fatMin = Integer.parseInt(fat1.getText());
                } catch (NumberFormatException exception) {
                    fatMin = 0;
                }
                try {
                    fatMax = Integer.parseInt(fat2.getText());
                } catch (NumberFormatException exception) {
                    fatMax = Integer.MAX_VALUE;
                }
                try {
                    sodiumMin = Integer.parseInt(sodium1.getText());
                } catch (NumberFormatException exception) {
                    sodiumMin = 0;
                }
                try {
                    sodiumMax = Integer.parseInt(sodium2.getText());
                } catch (NumberFormatException exception) {
                    sodiumMax = Integer.MAX_VALUE;
                }
                try {
                    priceMin = Integer.parseInt(price1.getText());
                } catch (NumberFormatException exception) {
                    priceMin = 0;
                }
                try {
                    priceMax = Integer.parseInt(price2.getText());
                } catch (NumberFormatException exception) {
                    priceMax = Integer.MAX_VALUE;
                }
                ClientGUI.this.titleFilter = titleFilter.getText();
                menuItemsScrollPane.changeList(filterItems(menuItems));
            }
        });

        add(filterPanel);

        menuItemsScrollPane.addDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    basket.add(menuItemsScrollPane.getSelected());
                    basketScrollPane.addItem(menuItemsScrollPane.getSelected());
                }
            }
        });

        basketScrollPane.addDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    basket.remove(basketScrollPane.removeSelected());
                }
            }
        });

        add(menuItemsScrollPane);
        add(basketScrollPane);
        JButton orderButton = new JButton("Order");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeliveryService.getDeliveryService().newOrder(basket, user);
            }
        });
        add(orderButton);
        setVisible(true);
    }

    public ArrayList<MenuItem> filterItems(ArrayList<MenuItem> menuItems) {
        ArrayList<MenuItem> filteredItems = menuItems
                .stream()
                .filter(r -> r.getRating() >= ratingMin && r.getRating() <= ratingMax)
                .filter(r -> r.getPrice() >= priceMin && r.getPrice() <= priceMax)
                .filter(r -> r.getCalories() >= caloriesMin && r.getCalories() <= caloriesMax)
                .filter(r -> r.getProtein() >= proteinMin && r.getProtein() <= proteinMax)
                .filter(r -> r.getSodium() >= sodiumMin && r.getSodium() <= sodiumMax)
                .filter(r -> r.getFat() >= fatMin && r.getFat() <= fatMax)
                .filter(r -> r.getTitle().toLowerCase().contains(titleFilter.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        return filteredItems;
    }

}