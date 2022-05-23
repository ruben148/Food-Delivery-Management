package presentationlayer;

import businesslayer.BaseProduct;
import businesslayer.CompositeProduct;
import businesslayer.MenuItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class ProductScrollPane extends JScrollPane {
    DefaultListModel<MenuItem> model;
    JList<MenuItem> menuItemJList;
    public ProductScrollPane(String title, ArrayList<MenuItem> menuItems){
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        model = new DefaultListModel<>();
        model.addAll(menuItems);
        menuItemJList = new JList<>(model);
        menuItemJList.setPrototypeCellValue(new BaseProduct("Titluuuuuuuuuuuuuuuuuuuuuuuuuuuuu",1,1,1,1,1,1));
        menuItemJList.setCellRenderer(new ProductList());
        setViewportView(menuItemJList);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
    }
    public void addDoubleClickListener(MouseAdapter evt){
        menuItemJList.addMouseListener(evt);
    }
    public MenuItem getSelected(){
        return menuItemJList.getSelectedValue();
    }
    public int getSelectedIndex(){
        return menuItemJList.getSelectedIndex();
    }
    public MenuItem removeSelected(){
        MenuItem menuItem = menuItemJList.getSelectedValue();
        model.removeElement(menuItem);
        return menuItem;
    }
    public void changeList(ArrayList<MenuItem> menuItems){
        model.removeAllElements();
        model.addAll(menuItems);
    }
    public void addItem(MenuItem item){
        model.addElement(item);
    }

    private class ProductList extends JPanel implements ListCellRenderer<MenuItem> {
        private JLabel rating;
        private JLabel title;
        private JLabel calories;
        private JLabel protein;
        private JLabel fat;
        private JLabel sodium;
        private JLabel price;

        private Font font1;
        private Font font2;
        private Font font3;

        private JPanel bottom;

        public ProductList() {
            super(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.black));
            rating = new JLabel();
            title = new JLabel();
            calories = new JLabel();
            protein = new JLabel();
            fat = new JLabel();
            sodium = new JLabel();
            price = new JLabel();
            font1 = new Font("Arial",Font.PLAIN,9);
            font2 = new Font("Arial",Font.BOLD,13);
            font3 = new Font("Arial",Font.BOLD,8);
            calories.setFont(font1);
            protein.setFont(font1);
            fat.setFont(font1);
            sodium.setFont(font1);
            calories.setFont(font1);
            title.setFont(font2);
            rating.setFont(font3);

            bottom = new JPanel(new FlowLayout());

            bottom.add(calories);
            bottom.add(protein);
            bottom.add(fat);
            bottom.add(sodium);


            add(price, BorderLayout.LINE_END);
            add(rating, BorderLayout.CENTER);
            add(title, BorderLayout.PAGE_START);
            add(bottom, BorderLayout.PAGE_END);
            setOpaque(true);
            bottom.setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends MenuItem> list, MenuItem value, int index, boolean isSelected, boolean cellHasFocus) {
            title.setText(value.getTitle());
            calories.setText("Calories: "+value.getCalories());
            protein.setText("Protein: "+value.getProtein());
            fat.setText("Fat: "+value.getFat());
            sodium.setText("Sodium: "+value.getSodium());
            price.setText(value.getPrice()+" $      ");
            rating.setText(String.format("%.2f", value.getRating())+"/5");

            if(value instanceof CompositeProduct) {
                String toolTip = "";
                for(MenuItem menuItem : ((CompositeProduct) value).getProducts())
                    toolTip += menuItem.getTitle()+"\n";
                setToolTipText(toolTip);
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                bottom.setBackground(list.getSelectionBackground());
                bottom.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
                bottom.setBackground(list.getBackground());
                bottom.setForeground(list.getForeground());
            }
            return this;
        }
    }
}
