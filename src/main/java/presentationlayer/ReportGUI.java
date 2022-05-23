package presentationlayer;

import businesslayer.DeliveryService;
import businesslayer.MenuItem;
import businesslayer.User;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportGUI extends JDialog {
    public ReportGUI(){
        super();
        setTitle("Reports");
        setSize(800,700);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setResizable(false);

        JPanel report1Panel = new JPanel();
        report1Panel.setLayout(new BorderLayout());
        JPanel report1DataPanel = new JPanel(new FlowLayout());
        report1Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        JSpinner hourSpinner1 = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.HOUR_OF_DAY));
        JSpinner.DateEditor hourEditor1 = new JSpinner.DateEditor(hourSpinner1,"HH:mm");
        hourSpinner1.setEditor(hourEditor1);
        JSpinner hourSpinner2 = new JSpinner(new SpinnerDateModel(new Date(),null,null,Calendar.HOUR_OF_DAY));
        JSpinner.DateEditor hourEditor2 = new JSpinner.DateEditor(hourSpinner2,"HH:mm");
        hourSpinner2.setEditor(hourEditor2);
        JButton report1Button = new JButton("Create report 1");
        JPanel spinners = new JPanel();
        spinners.add(hourSpinner1);
        spinners.add(hourSpinner2);
        JLabel result1 = new JLabel();
        JLabel result2 = new JLabel();
        report1DataPanel.add(spinners);
        report1DataPanel.add(report1Button);
        JPanel report1ResultPanel = new JPanel();
        report1ResultPanel.setLayout(new BoxLayout(report1ResultPanel, BoxLayout.PAGE_AXIS));
        report1Panel.add(report1DataPanel, BorderLayout.CENTER);
        report1ResultPanel.add(result1);
        report1ResultPanel.add(result2);
        report1Panel.add(report1ResultPanel, BorderLayout.PAGE_END);

        JPanel report2Panel = new JPanel();
        report2Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        report2Panel.setLayout(new BoxLayout(report2Panel, BoxLayout.PAGE_AXIS));
        JTextField numberOfOrders = new JTextField(6);
        JLabel numberOfOrdersLabel = new JLabel("Number of orders");
        JButton report2Button = new JButton("Create report 2");
        ProductWithCountScrollPane itemsOrderedMoreThan = new ProductWithCountScrollPane("Products ordered more than n times", new ArrayList<>());
        report2Panel.add(numberOfOrdersLabel);
        report2Panel.add(numberOfOrders);
        report2Panel.add(itemsOrderedMoreThan);
        report2Panel.add(report2Button);

        JPanel report3Panel = new JPanel();
        report3Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        report3Panel.setLayout(new BoxLayout(report3Panel, BoxLayout.PAGE_AXIS));
        JTextField numberOfOrders2 = new JTextField(8);
        JTextField valueOfOrders2 = new JTextField(8);
        JLabel numberOfOrdersLabel2 = new JLabel("Number of orders");
        UserScrollPane userScrollPane = new UserScrollPane("Users", new ArrayList<>());
        report3Panel.add(numberOfOrdersLabel2);
        report3Panel.add(numberOfOrders2);
        report3Panel.add(new JLabel("Value of orders"));
        report3Panel.add(valueOfOrders2);
        report3Panel.add(userScrollPane);
        JButton report3Button = new JButton("Create report 3");
        report3Panel.add(report3Button);

        JPanel report4Panel = new JPanel();
        report4Panel.setBorder(BorderFactory.createLineBorder(Color.black));
        report4Panel.setLayout(new BoxLayout(report4Panel, BoxLayout.PAGE_AXIS));
        JXDatePicker datePicker = new JXDatePicker();
        ProductWithCountScrollPane productWithCountScrollPane = new ProductWithCountScrollPane("Products", DeliveryService.getDeliveryService().getMenuItems());
        JButton report4Button = new JButton("Create report 4");
        report4Panel.add(datePicker);
        report4Panel.add(productWithCountScrollPane);
        report4Panel.add(report4Button);

        report1Button.addActionListener(e -> {
            Date date1 = ((SpinnerDateModel)hourSpinner1.getModel()).getDate();
            Date date2 = ((SpinnerDateModel)hourSpinner2.getModel()).getDate();
            double avg1 = DeliveryService.getDeliveryService().getAverageBetween(date1, date2);
            double avg2 = DeliveryService.getDeliveryService().getAveragePerDay();
            double avg3 = DeliveryService.getDeliveryService().getAveragePriceBetween(date1, date2);
            double avg4 = DeliveryService.getDeliveryService().getAveragePricePerDay();
            result1.setText(String.format("%.2f", avg1)+" comenzi in medie. Total pe zi: "+String.format("%.2f", avg2)+".");
            result2.setText(String.format("%.2f", avg3)+"$ pe zi, din totalul de "+String.format("%.2f", avg4)+"$.");
        });

        report2Button.addActionListener(e -> {
            try {
                itemsOrderedMoreThan.changeList(DeliveryService.getDeliveryService().getItemsOrderedMoreThan(Integer.parseInt(numberOfOrders.getText())));
            }
            catch(NumberFormatException ignored){}
        });

        report3Button.addActionListener(e -> {
            try {
                int numberOfOrders1 = Integer.parseInt(numberOfOrders2.getText());
                int valueOfOrders = Integer.parseInt(valueOfOrders2.getText());
                userScrollPane.changeList(DeliveryService.getDeliveryService().getUsers(numberOfOrders1, valueOfOrders));
                //userScrollPane.changeList(User.getUsers());
            }
            catch(NumberFormatException exception){
                exception.printStackTrace();
            }
        });

        report4Button.addActionListener(e -> {
            try {
                Date date = datePicker.getDate();
                productWithCountScrollPane.changeList(DeliveryService.getDeliveryService().getItemsOrderedAtDate(date));
            }
            catch(NullPointerException ignored){}
        });

        add(report1Panel);
        add(report2Panel);
        add(report3Panel);
        add(report4Panel);
        pack();
        setVisible(true);
    }
}