import java.util.Collection;
public class InventoryUtils {
    public static <T> void printCollection(Collection<T> collection) {
        for (T item : collection) {
            System.out.println(item);
        }
    }
}
