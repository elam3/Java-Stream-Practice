import java.util.stream.*;
import java.util.*;
import java.io.*;
import java.lang.*;

public class Driver {
    public static void main(String[] args) {
        List<CustomerPractice> customerList = new ArrayList<>();
        fillList(customerList);


        // Stream Practice Starts Here!!
        System.out.println("What is the average age of all customers?");

        // Sanity Check (Iteratively)
        long itr_ages = 0;
        long itr_count = 0;
        for (CustomerPractice cp : customerList) {
            itr_ages += cp.getAge();
            itr_count++;
        }
        System.out.println("ages: " + itr_ages);
        System.out.println("count: " + itr_count);
        System.out.println("itr: " + 1.0*itr_ages/itr_count);


        // Try with Streams
        System.out.print("str: ");
        double str_num = customerList.stream()
            .map(x->x.getAge())
            .reduce(0, 
                    (sum, age) -> sum + age,
                    (age1, age2) -> age1 + age2
            )/(customerList.size()/1.0);
        System.out.println(str_num);

        //Averages (Attempt 2)
        System.out.println("Average (Attempt2): "
            + customerList.stream()
                .mapToLong( x -> x.getAge())
                .average()
                .getAsDouble()
        );
        System.out.println();


        System.out.println("Create a list of all female customers that are 18-25 years old.");
        List<CustomerPractice> femaleCustomers =
            customerList.stream()
                .filter(x -> x.getGender() == Gender.FEMALE)
                .filter(x -> x.getAge() >= 18 && x.getAge() <= 25)
                .collect(Collectors.toList());

        for (CustomerPractice cp : femaleCustomers) {
            System.out.println(cp.getGender() + "\t" + cp.getAge() + "\t" + cp.getLastName()+", "+cp.getFirstName());
        }
        System.out.println();


        //Query 3: Are there any customers who haven't spent any money?


        //Query 4: Which customer spent the most money?
        

        //Query 5: What is the average amount psent by all male custoemrs?


        //Query 6: Create a list of all addreses of customers in California
        
        
        //Query 7: Create a map of the customers in each state
        //          key = state, value = list of customers in that state

        
        //Query 8: Create a map of the highest spending customers in each
        //state (key:value :: state:customer)
        
        
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
