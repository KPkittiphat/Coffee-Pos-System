package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVManager {
    private static final String FILE_NAME = "products.csv";

    public CSVManager() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("id,name,price\n");

                fw.write("1,Espresso,50.0\n");
                fw.write("2,Latte,65.0\n");
                fw.write("3,Cappuccino,70.0\n");
                fw.write("4,Cappuccino Freddo,75.0\n");
                fw.write("5,Americano,45.0\n");
                fw.write("6,Mocha,80.0\n");
                fw.write("7,Macchiato,70.0\n");
                fw.write("8,Flat White,70.0\n");
                fw.write("9,Croissant,45.0\n");
                fw.write("10,Danish Pastry,55.0\n");
                fw.write("11,Muffin,40.0\n");
                fw.write("12,Donut,35.0\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 3) {
                    try {
                        int id = Integer.parseInt(values[0].trim());
                        String name = values[1].trim();
                        double price = Double.parseDouble(values[2].trim());
                        products.add(new Product(id, name, price));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in CSV: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

}