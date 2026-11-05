//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        InventoryManager inventory = new InventoryManager();

        Product product1 = new Product("P001", "Indomie Goreng", 3000, 20, "Food");
        Product product2 = new Product("P002", "Teh Botol", 5000, 15, "Drink");
        Product product3 = new Product("P003", "Buku Tulis Sinar Dunia", 6000, 10, "Stationery");
        Product product4 = new Product("P004", "Aqua", 4000, 25, "Drink");
        Product product5 = new Product("P005", "Chitato", 12000, 8, "Snack");

        inventory.addProduct(product1);
        inventory.addProduct(product2);
        inventory.addProduct(product3);
        inventory.addProduct(product4);
        inventory.addProduct(product5);

        System.out.println("All Products:");
        inventory.displayAllProducts();

        System.out.println("\nUnique Categories:");
        inventory.displayUniqueCategories();

        System.out.println("\nFind Product:");
        Product found = inventory.findProductByName("Teh Botol");

        if (found != null) {
            found.displayInfo();
        } else {
            System.out.println("Product not found.");
        }

        inventory.removeProduct("P003");

        System.out.println("\nProducts After Removing P003:");
        inventory.displayAllProducts();
    }
}