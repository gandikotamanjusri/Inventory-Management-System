import java.util.*;
public class InventoryManagementSystem {

    private HashSet<Product> productSet = new HashSet<>();
    private LinkedList<String> transactionHistory = new LinkedList<>();
    private Stack<Transaction> undoStack = new Stack<>();
    private Queue<Product> lowStockQueue = new LinkedList<>();

    private static final int LOW_STOCK_LIMIT = 10;

    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();
        ims.start();
    }

    private void start() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            printMenu();
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addProduct(sc);
                case 2 -> updateQuantity(sc);
                case 3 -> viewProducts(sc);
                case 4 -> searchProduct(sc);
                case 5 -> showLowStock();
                case 6 -> showTransactions(sc);
                case 7 -> showStatistics();
                case 8 -> undoLastUpdate();
                case 9 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice");
            }
        } while (choice != 9);
    }

    private void printMenu() {
        System.out.println("\n=== INVENTORY MANAGEMENT SYSTEM ===");
        System.out.println("1. Add Product");
        System.out.println("2. Update Quantity");
        System.out.println("3. View Products (Sorted)");
        System.out.println("4. Search Products");
        System.out.println("5. Low Stock Alerts");
        System.out.println("6. Transaction History");
        System.out.println("7. Inventory Statistics");
        System.out.println("8. Undo Last Update");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addProduct(Scanner sc) {
        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();

        for (Product p : productSet) {
            if (p.getSku().equals(sku)) {
                System.out.println(" SKU already exists");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Category: ");
        String category = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        Product product = new Product(sku, name, category, price, qty);
        productSet.add(product);

        transactionHistory.addFirst(
                "ADD: " + sku + " (" + qty + ") at " + new Date()
        );

        if (qty < LOW_STOCK_LIMIT) {
            lowStockQueue.add(product);
            System.out.println(" Low stock alert for " + sku);
        }

        System.out.println(" Product added successfully!");
    }

    private void updateQuantity(Scanner sc) {
        System.out.print("Enter SKU: ");
        String sku = sc.nextLine();

        for (Product p : productSet) {
            if (p.getSku().equals(sku)) {
                System.out.print("Enter new quantity: ");
                int newQty = sc.nextInt();

                undoStack.push(new Transaction(sku, p.getQuantity()));
                p.setQuantity(newQty);

                transactionHistory.addFirst(
                        "UPDATE: " + sku + " from " + undoStack.peek().oldQuantity +
                        " to " + newQty + " at " + new Date()
                );

                if (newQty < LOW_STOCK_LIMIT) {
                    lowStockQueue.add(p);
                }

                System.out.println(" Quantity updated successfully!");
                return;
            }
        }
        System.out.println(" Product not found");
    }

    private void undoLastUpdate() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo");
            return;
        }

        Transaction t = undoStack.pop();

        for (Product p : productSet) {
            if (p.getSku().equals(t.sku)) {
                p.setQuantity(t.oldQuantity);
                System.out.println(" Last update undone!");
                return;
            }
        }
    }

    private void viewProducts(Scanner sc) {
        System.out.print("Sort by (sku/price/value/name): ");
        String sort = sc.nextLine();

        List<Product> list = new ArrayList<>(productSet);

        switch (sort.toLowerCase()) {
            case "sku" -> Collections.sort(list);
            case "price" -> list.sort(new PriceComparator());
            case "value" -> list.sort(new ValueComparator());
            case "name" -> list.sort(new NameComparator());
            default -> {
                System.out.println("Invalid sort");
                return;
            }
        }

        System.out.printf("%-10s %-15s %-12s %-8s %-6s %-10s%n",
                "SKU", "Name", "Category", "Price", "Qty", "Value");

        for (Product p : list) {
            System.out.printf("%-10s %-15s %-12s %.2f %-6d %.2f%n",
                    p.getSku(), p.getName(), p.getCategory(),
                    p.getPrice(), p.getQuantity(), p.getInventoryValue());
        }
    }

    private void searchProduct(Scanner sc) {
        System.out.print("Enter SKU to search: ");
        String sku = sc.nextLine();

        for (Product p : productSet) {
            if (p.getSku().equals(sku)) {
                System.out.println(p);
                return;
            }
        }
        System.out.println("Product not found");
    }

    private void showLowStock() {
        System.out.println("\n=== LOW STOCK ALERTS ===");
        for (Product p : lowStockQueue) {
            System.out.println(p.getSku() + " - Qty: " + p.getQuantity());
        }
    }

    private void showTransactions(Scanner sc) {
        System.out.print("Enter number of transactions: ");
        int n = sc.nextInt();

        int count = 0;
        for (String t : transactionHistory) {
            if (count++ == n) break;
            System.out.println(t);
        }
    }

    private void showStatistics() {
        double totalValue = 0;
        for (Product p : productSet) {
            totalValue += p.getInventoryValue();
        }

        System.out.println("Total Products: " + productSet.size());
        System.out.println("Total Inventory Value: ₹" + totalValue);
    }
}
