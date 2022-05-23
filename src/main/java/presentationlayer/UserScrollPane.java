package presentationlayer;

import businesslayer.MenuItem;
import businesslayer.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserScrollPane extends JScrollPane {
    DefaultListModel<User> model;
    JList<User> userJList;
    public UserScrollPane(String title, ArrayList<User> users){
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        model = new DefaultListModel<>();
        model.addAll(users);
        userJList = new JList<>(model);
        userJList.setPrototypeCellValue(new User("usernameeeeeeee","password", 0));
        userJList.setCellRenderer(new UserList());
        setViewportView(userJList);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
    }
    public void addDoubleClickListener(MouseAdapter evt){
        userJList.addMouseListener(evt);
    }
    public User getSelected(){
        return userJList.getSelectedValue();
    }
    public int getSelectedIndex(){
        return userJList.getSelectedIndex();
    }
    public User removeSelected(){
        User user = userJList.getSelectedValue();
        model.removeElement(user);
        return user;
    }
    public void changeList(ArrayList<User> users){
        model.removeAllElements();
        model.addAll(users);
    }
    public void addItem(User user){
        model.addElement(user);
    }

    private class UserList extends JPanel implements ListCellRenderer<User> {
        private JLabel username;
        private JLabel numberOfOrders;
        private JLabel iconLabel;

        private Font font1;
        private Font font2;
        private Font font3;

        private JPanel text;

        public UserList() {
            super(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            username = new JLabel();
            numberOfOrders = new JLabel();
            iconLabel = new JLabel();
            text = new JPanel(new CardLayout());
            font1 = new Font("Arial",Font.PLAIN,9);
            font2 = new Font("Arial",Font.BOLD,13);
            font3 = new Font("Arial",Font.BOLD,8);
            username.setFont(font2);
            try {
                Image personImg = ImageIO.read(new File("user.png"));
                ImageIcon personIcon = new ImageIcon(personImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                iconLabel.setIcon(personIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            text.add(username);
            add(numberOfOrders, BorderLayout.LINE_END);
            add(iconLabel, BorderLayout.LINE_START);
            add(text, BorderLayout.CENTER);

            setOpaque(true);
            text.setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus) {
            username.setText(value.getUsername());
            numberOfOrders.setText("Orders: "+value.getTemporaryNumberOfOrders());

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                text.setBackground(list.getSelectionBackground());
                text.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                text.setBackground(list.getBackground());
                text.setForeground(list.getForeground());
            }
            return this;
        }
    }
}
