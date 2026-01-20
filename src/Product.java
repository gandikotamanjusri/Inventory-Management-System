import java.util.*;

class Product implements Comparable<Product> {
    private String sku;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private Date lastUpdated;
    
    public Product(String sku, String name, String category, double price, int quantity) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.lastUpdated = new Date();
    }
    
    @Override
    public int compareTo(Product other) {
        return this.sku.compareTo(other.sku);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return sku.equals(product.sku);
    }
    
    @Override
    public int hashCode() {
        return sku.hashCode();
    }
    
    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Date getLastUpdated() { return lastUpdated; }
    
    public void setPrice(double price) { 
        this.price = price; 
        this.lastUpdated = new Date();
    }
    
    public void setQuantity(int quantity) { 
        this.quantity = quantity; 
        this.lastUpdated = new Date();
    }
    
    public double getInventoryValue() {
        return price * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("SKU: %s, Name: %s, Category: %s, Price: ₹%.2f, Qty: %d, Value: ₹%.2f",
                           sku, name, category, price, quantity, getInventoryValue());
    }
}