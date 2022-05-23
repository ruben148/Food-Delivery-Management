package presentationlayer;

import businesslayer.BaseProduct;
import businesslayer.CompositeProduct;
import businesslayer.DeliveryService;
import datalayer.CSVRead;
import businesslayer.MenuItem;
import datalayer.Serializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class AdminGUI extends JFrame{
    JFrame frame;
    String filename;
    ArrayList<MenuItem> menuItems;
    ArrayList<MenuItem> compositeProducts;
    ProductScrollPane menuItemsScrollPane;
    ProductScrollPane compositeItemScrollPane;
    int selected;
    public AdminGUI(){
        super();
        setLayout(new FlowLayout());
        setSize(620,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new FlowLayout());
        frame = this;

        JButton importButton = new JButton("Import .csv file");
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileDialog fd = new FileDialog(frame, "Choose .csv file", FileDialog.LOAD);
                fd.setDirectory("C:\\");
                fd.setFile("*.csv");
                fd.setVisible(true);
                filename = fd.getDirectory()+fd.getFile();
                if (fd.getFile() != null) {
                    menuItems = DeliveryService.getDeliveryService().getMenuItemsFromCSV(filename);
                    menuItemsScrollPane.changeList(menuItems);
                }
            }
        });


        JPanel baseProductInfoPanel1 = new JPanel();
        JPanel baseProductInfoPanel2 = new JPanel();
        JTextField titleTextField = new JTextField(12);
        JTextField caloriesTextField = new JTextField(5);
        JTextField proteinTextField = new JTextField(5);
        JTextField fatTextField = new JTextField(5);
        JTextField sodiumTextField = new JTextField(5);
        JTextField priceTextField = new JTextField(5);
        JTextField ratingTextField = new JTextField(5);
        JLabel titleLabel = new JLabel("Title:");
        JLabel caloriesLabel = new JLabel("Calories:");
        JLabel proteinLabel = new JLabel(" Protein:");
        JLabel fatLabel = new JLabel(" Fat:");
        JLabel sodiumLabel = new JLabel(" Sodium:");
        JLabel priceLabel = new JLabel(" Price:");
        JLabel ratingLabel = new JLabel(" Rating:");
        baseProductInfoPanel1.add(titleLabel);
        baseProductInfoPanel1.add(titleTextField);
        baseProductInfoPanel2.add(caloriesLabel);
        baseProductInfoPanel2.add(caloriesTextField);
        baseProductInfoPanel2.add(proteinLabel);
        baseProductInfoPanel2.add(proteinTextField);
        baseProductInfoPanel2.add(fatLabel);
        baseProductInfoPanel2.add(fatTextField);
        baseProductInfoPanel2.add(sodiumLabel);
        baseProductInfoPanel2.add(sodiumTextField);
        baseProductInfoPanel1.add(ratingLabel);
        baseProductInfoPanel1.add(ratingTextField);
        baseProductInfoPanel1.add(priceLabel);
        baseProductInfoPanel1.add(priceTextField);
        add(importButton);
        add(baseProductInfoPanel1);
        add(baseProductInfoPanel2);

        menuItems = DeliveryService.getDeliveryService().getMenuItems();
        compositeProducts = new ArrayList<>();
        menuItemsScrollPane = new ProductScrollPane("Menu", menuItems);
        menuItemsScrollPane.addDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    compositeProducts.add(menuItemsScrollPane.getSelected());
                    compositeItemScrollPane.addItem(menuItemsScrollPane.getSelected());
                }
                else if (e.getClickCount() == 3) {
                    if (menuItemsScrollPane.getSelected() instanceof CompositeProduct) {
                        JDialog jDialog = new JDialog(frame);
                        jDialog.add(new ProductScrollPane("Composite product", ((CompositeProduct) menuItemsScrollPane.getSelected()).getProducts()));
                        jDialog.show();
                    }
                }
            }
        });
        compositeItemScrollPane = new ProductScrollPane("Compose product", compositeProducts);
        compositeItemScrollPane.addDoubleClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    compositeProducts.remove(compositeItemScrollPane.removeSelected());
                }
            }
        });
        add(menuItemsScrollPane);
        add(compositeItemScrollPane);
        JButton createCompositeProductButton = new JButton("Create composite product");
        createCompositeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CompositeProduct product = new CompositeProduct(titleTextField.getText(), Integer.parseInt(priceTextField.getText()));
                for(MenuItem item : compositeProducts)
                    product.addProduct(item);
                menuItemsScrollPane.addItem(product);
                DeliveryService.getDeliveryService().addItemToMenu(product);
                compositeItemScrollPane.changeList(new ArrayList<>());
            }
        });
        JButton addProductButton = new JButton("Add product");
        JButton removeProductButton = new JButton("Remove product");
        JButton modifyProductButton = new JButton("Modify product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BaseProduct product = new BaseProduct(titleTextField.getText(),
                        Double.parseDouble(ratingTextField.getText()),
                        Integer.parseInt(caloriesTextField.getText()),
                        Integer.parseInt(proteinTextField.getText()),
                        Integer.parseInt(fatTextField.getText()),
                        Integer.parseInt(sodiumTextField.getText()),
                        Integer.parseInt(priceTextField.getText()));
                DeliveryService.getDeliveryService().addItemToMenu(product);
                menuItemsScrollPane.addItem(product);
            }
        });
        removeProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeliveryService.getDeliveryService().removeItemFromMenu(menuItemsScrollPane.removeSelected());
                menuItemsScrollPane.changeList(DeliveryService.getDeliveryService().getMenuItems());
            }
        });
        modifyProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //TODO if it is base product or composed
                BaseProduct baseProduct = new BaseProduct(titleTextField.getText(),
                        Double.parseDouble(ratingTextField.getText()),
                        Integer.parseInt(caloriesTextField.getText()),
                        Integer.parseInt(proteinTextField.getText()),
                        Integer.parseInt(fatTextField.getText()),
                        Integer.parseInt(sodiumTextField.getText()),
                        Integer.parseInt(priceTextField.getText()));
                int index = menuItemsScrollPane.getSelectedIndex();
                DeliveryService.getDeliveryService().editItem(index, baseProduct);
                menuItemsScrollPane.changeList(DeliveryService.getDeliveryService().getMenuItems());
            }
        });
        JButton reportsButton = new JButton("Reports");
        reportsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportGUI reportGUI = new ReportGUI();
            }
        });
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.PAGE_AXIS));
        buttons.add(addProductButton);
        buttons.add(Box.createRigidArea(new Dimension(0, 5)));
        buttons.add(modifyProductButton);
        buttons.add(Box.createRigidArea(new Dimension(0, 5)));
        buttons.add(removeProductButton);
        add(buttons);
        add(createCompositeProductButton);
        add(reportsButton);
        setVisible(true);
    }
}
