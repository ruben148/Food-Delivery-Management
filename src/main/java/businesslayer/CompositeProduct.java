package businesslayer;

import java.util.ArrayList;
import java.util.List;

public class CompositeProduct extends MenuItem {

    private ArrayList<MenuItem> products = new ArrayList<>();

    public CompositeProduct(String title, int price){
        this.title = title;
        this.price = price;
    }

    public ArrayList<MenuItem> getProducts() {
        return products;
    }

    @Override
    public void addProduct(MenuItem product) {
        products.add(product);
        calories += product.getCalories();
        protein += product.getProtein();
        fat += product.getFat();
        sodium += product.getSodium();
        rating = (getRating()*(products.size()-1)+product.rating)/products.size();
    }
}
