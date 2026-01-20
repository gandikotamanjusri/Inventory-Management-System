public class Transaction {
    String sku;
    int oldQuantity;

    public Transaction(String sku, int oldQuantity) {
        this.sku = sku;
        this.oldQuantity = oldQuantity;
    }
}
