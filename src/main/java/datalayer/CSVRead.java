package datalayer;

import businesslayer.BaseProduct;
import businesslayer.MenuItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVRead {
    public static ArrayList<MenuItem> read(String file) throws FileNotFoundException {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            menuItems = new ArrayList<>(br.lines().skip(1).map(mapToItem).collect(Collectors.toList()));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return menuItems;
    }
    private static Function<String, MenuItem> mapToItem = (line) -> {
        String[] item = line.split(",");
        BaseProduct baseProduct = new BaseProduct(item[0], Double.parseDouble(item[1]), Integer.parseInt(item[2]), Integer.parseInt(item[3]), Integer.parseInt(item[4]), Integer.parseInt(item[5]), Integer.parseInt(item[6]));
        return baseProduct;
    };
}
