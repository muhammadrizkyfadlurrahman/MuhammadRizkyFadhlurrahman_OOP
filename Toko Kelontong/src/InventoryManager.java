import java.util.*;

public class InventoryManager {
    private Map<String, Product> inventory = new HashMap<>();
    private Set<String> categories = new HashSet<>();

    public void addProduct(Product product) {
        // hashmap is suitable because productID is unique.
        inventory.put(product.getProductID(), product);
        categories.add(product.getCategory());
    }

    public void removeProduct(String productID) {
        inventory.remove(productID);
    }

    public Product findProductByName(String name) {
        for (Product product : inventory.values()) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public void displayAllProducts() {
        for (Product product : inventory.values()) {
            product.displayInfo();
        }
    }

    public void displayUniqueCategories() {
        // hashset is suitable because it stores unique categories.
        for (String category : categories) {
            System.out.println(category);
        }
    }
}
