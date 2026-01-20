import java.util.Comparator;
class ValueComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p2.getInventoryValue(), p1.getInventoryValue()); 
    }
}