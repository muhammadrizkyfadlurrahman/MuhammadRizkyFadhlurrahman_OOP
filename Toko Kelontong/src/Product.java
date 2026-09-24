public class Product implements Taggable, Sellable {
    private String productID;
    private String name;
    private int price;
    private int quantity;
    private String category;

    public Product(String productID, String name, int price, int quantity, String category) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }
}
