package businesslayer;

import java.util.List;

public class BaseProduct extends MenuItem {

    public int getPrice() {
        return super.getPrice();
    }

    public BaseProduct(String title, double rating, int calories, int protein, int fat, int sodium, int price){
        super.setTitle(title);
        super.setRating(rating);
        super.setCalories(calories);
        super.setProtein(protein);
        super.setFat(fat);
        super.setSodium(sodium);
        super.setPrice(price);
    }

    @Override
    public double getRating() {
        return super.getRating();
    }

    @Override
    public int getCalories() {
        return super.getCalories();
    }

    @Override
    public int getProtein() {
        return super.getProtein();
    }

    @Override
    public int getFat() {
        return super.getFat();
    }

    @Override
    public int getSodium() {
        return super.getSodium();
    }
}
