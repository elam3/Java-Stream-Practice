import java.util.stream.*;
import java.util.*;
import java.io.*;
import java.lang.*;

public class Driver {
    public static void main(String[] args) {
        List<CustomerPractice> customerList = new ArrayList<>();
        fillList(customerList);


        // Stream Practice Starts Here!!
        ;
    }

    private static void fillList(List<CustomerPractice> list) {
        List<String[]> items = parseItems();

        // Read Names & Addresses
        List<String[]>  names = new ArrayList<>(),
                        addrs = new ArrayList<>();
        try (
            Scanner nameScan = new Scanner(new FileReader(
                new File("names.csv")));
            Scanner addrScan = new Scanner(new FileReader(
                new File("addresses.csv")))
        ) {
            while (nameScan.hasNext()) {
                String nameLine = nameScan.nextLine();
                String[] nameArr = nameLine.split(",");
                names.add(nameArr);

                String addrLine = addrScan.nextLine();
                String[] addrArr = addrLine.split(",");
                addrs.add(addrArr);
            }
            Collections.shuffle(names);
            Collections.shuffle(addrs);
		} catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        // names & addrs should have same cardinality/size
        if (names.size() != addrs.size())
            throw new IllegalStateException();

        for (int i=0; i<names.size(); i++) {
            String[] nameArr = names.get(i);
            String[] addrArr = addrs.get(i);

            Address addr = new Address(
                    addrArr[0], //streetNumber
                    addrArr[1], //street
                    addrArr[2], //city
                    addrArr[3], //state
                    addrArr[4]);//zip

            CustomerPractice cp = new CustomerPractice();
            cp.setFirstName(nameArr[0]);
            cp.setLastName(nameArr[1]);
            cp.setAge((new Random()).nextInt(100)+18);
            cp.setGender(getRandomGender());
            cp.setAddress(addr);
            cp.setOrders(makeRandomOrders(items));

            list.add(cp);
        }
    }

    private static List<Order> makeRandomOrders(List<String[]> items) {
        List<Order> orders = new ArrayList<>();
        int rand = (new Random()).nextInt(99)+1; 
        // Randomly make 1 - 100 orders
        for (int i=0; i<rand; i++) {
            orders.add(makeRandomOrder(items));
        }
        return orders;
    }

    private static Order makeRandomOrder(List<String[]> items) {
        List<String> list = new ArrayList<>();
        double total = 0.0;
        Random rng = new Random();
        int rand = rng.nextInt(20) + 1; // [1,20] items
        for (int i=0; i<rand; i++) {
            String[] arr = getRandomItem(items);
            list.add(arr[0]);
            total += Double.parseDouble(arr[1]);
        }

        Order newOrder = new Order(list, total);
        return newOrder;
    }

    private static String[] getRandomItem(List<String[]> list) {
        Random rng = new Random();
        int rand = rng.nextInt(list.size());
        String[] arr = list.get(rand);
        return arr;
    }

    private static Gender getRandomGender() {
        Gender gender;
        Random rng = new Random();
        int rand = rng.nextInt(3);
        switch (rand) {
            case 0:
                return Gender.MALE;
            case 1:
                return Gender.FEMALE;
            case 2:
                return Gender.UNSPECIFIED;
        }
        return null;
    }

    private static List<String[]> parseItems() {
        // Read list of items
        List<String[]> items = new ArrayList<>();
        try ( Scanner itemScan = new Scanner(new FileReader(
                        new File("items.csv"))) ) {
            while (itemScan.hasNext()) {
                items.add(itemScan.nextLine().split(","));
            }
            Collections.shuffle(items);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        return items;
    }
}
