package businesslayer;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public abstract class MenuItem implements Serializable {
    protected String title;
    protected double rating;
    protected int calories;
    protected int protein;
    protected int fat;
    protected int sodium;
    protected int price;
    protected int quantity;
    protected int quantity_temporary;

    public void addProduct(MenuItem menuItem){
        throw new UnsupportedOperationException();
    }
    public String getTitle(){
        return title;
    }
    public int getPrice(){
        return price;
    }
    public double getRating(){
        return rating;
    }
    public int getCalories() {
        return calories;
    }
    public int getProtein() {
        return protein;
    }
    public int getFat(){
        return fat;
    }
    public int getSodium(){
        return sodium;
    }
    public int getQuantity() {
        return quantity;
    }
    public int getTemporaryQuantity(){
        return quantity_temporary;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean setTemporaryQuantity(int quantity_temporary) {
        this.quantity_temporary = quantity_temporary;
        return true;
    }

    public boolean addQuantity(){
        quantity++;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem menuItem = (MenuItem) o;
        return title.equals(menuItem.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
