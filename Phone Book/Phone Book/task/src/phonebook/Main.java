package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        LinkedList<Integer> phoneBaseNumberPart = new LinkedList<>();
        LinkedList<String> phoneBaseNamePart = new LinkedList<>();
        LinkedList<String> searchList = new LinkedList<>();
        try (Scanner scanner = new Scanner(new File("D:\\users\\druzhinin_yy\\affairs\\hyperskill\\Phone Book DataFiles\\directory.txt"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int splitIndex = line.indexOf(' ');
                phoneBaseNumberPart.addLast(Integer.parseInt(line.substring(0, splitIndex)));
                phoneBaseNamePart.addLast(line.substring(splitIndex + 1).trim());
            }
        } catch (FileNotFoundException exc) {
            System.out.println("File directory.txt do not exist.");
        }
        try (Scanner scanner = new Scanner(new File("D:\\users\\druzhinin_yy\\affairs\\hyperskill\\Phone Book DataFiles\\find.txt"))) {
            while (scanner.hasNextLine()) {
                searchList.addLast(scanner.nextLine().trim());
            }
        } catch (FileNotFoundException exc) {
            System.out.println("File find.txt do not exist.");
        }
        PhoneBase base = new PhoneBase(phoneBaseNumberPart.size());
        Iterator<Integer> numberIterator = phoneBaseNumberPart.iterator();
        Iterator<String> nameIterator = phoneBaseNamePart.iterator();
        for (int k = 0; k < phoneBaseNamePart.size(); ++k) {
            base.setEntry(k, numberIterator.next(), nameIterator.next());
        }
        PhoneBase baseForQuickSort = base.getCopy();
        PhoneBase baseForHashTable = base.getCopy();

        System.out.println("Start searching (linear search)...");
        int entriesFoundLinearSearch = 0;
        long startTimeLinearSearch = System.currentTimeMillis();
        for (String name : searchList) {
            if (base.searchLinear(name) >= 0) {
                ++entriesFoundLinearSearch;
            }
        }
        long durationLinearSearch = System.currentTimeMillis() - startTimeLinearSearch;
        System.out.print("Found " + entriesFoundLinearSearch + " / " + searchList.size() + " entries.");
        System.out.println(" Time taken: " + printDuration(durationLinearSearch));
        System.out.println();

        System.out.println("Start searching (bubble sort + jump search)...");
        int entriesFoundJumpSearch = 0;
        long durationLimit = durationLinearSearch * 10;
        long durationBubbleSort = base.sortBubbleTimeBounded(durationLimit);
        long startTimeJumpSearch = System.currentTimeMillis();
        if (durationBubbleSort > durationLimit) {
            for (String name : searchList) {
                if (base.searchLinear(name) >= 0) {
                    ++entriesFoundJumpSearch;
                }
            }
        } else {
            for (String name : searchList) {
                if (base.searchJump(name) >= 0) {
                    ++entriesFoundJumpSearch;
                }
            }
        }
        long durationJumpSearch = System.currentTimeMillis() - startTimeJumpSearch;
        System.out.print("Found " + entriesFoundJumpSearch + " / " + searchList.size() + " entries.");
        System.out.println(" Time taken: " + printDuration(durationBubbleSort + durationJumpSearch));
        System.out.print("Sorting time: " + printDuration(durationBubbleSort));
        if (durationBubbleSort > durationLimit) {
            System.out.println(" STOPPED, moved to linear search");
        } else {
            System.out.println();
        }
        System.out.println("Searching time: " + printDuration(durationJumpSearch));
        System.out.println();

        System.out.println("Start searching (quick sort + binary search)...");
        int entriesFoundBinarySearch = 0;
        long startTimeQuickSort = System.currentTimeMillis();
        baseForQuickSort.sortQuick();
        long durationQuickSort = System.currentTimeMillis() - startTimeQuickSort;
        long startTimeBinarySearch = System.currentTimeMillis();
        for (String name : searchList) {
            if (baseForQuickSort.searchBinary(name) >= 0) {
                ++entriesFoundBinarySearch;
            }
        }
        long durationBinarySearch = System.currentTimeMillis() - startTimeBinarySearch;
        System.out.print("Found " + entriesFoundBinarySearch + " / " + searchList.size() + " entries.");
        System.out.println(" Time taken: " + printDuration(durationQuickSort + durationBinarySearch));
        System.out.println("Sorting time: " + printDuration(durationQuickSort));
        System.out.println("Searching time: " + printDuration(durationBinarySearch));
        System.out.println();

        System.out.println("Start searching (hash table)...");
        long startTimeTableCreating = System.currentTimeMillis();
        HashTable<Integer> hashTable = new HashTable<>(phoneBaseNamePart.size() * 3);
        for (int k = 0; k < baseForHashTable.getSize(); ++k) {
            hashTable.put(Math.abs(baseForHashTable.getEntryName(k).hashCode()), baseForHashTable.getEntryNumber(k));
        }
        long durationTableCreating = System.currentTimeMillis() - startTimeTableCreating;
        long startTimeTableSearch = System.currentTimeMillis();
        int entriesFoundTableSearch = 0;
        for (String name : searchList) {
            if (hashTable.get(Math.abs(name.hashCode())) >= 0) {
                ++entriesFoundTableSearch;
            }
        }
        long durationTableSearch = System.currentTimeMillis() - startTimeTableSearch;
        System.out.print("Found " + entriesFoundTableSearch + " / " + searchList.size() + " entries.");
        System.out.println(" Time taken: " + printDuration(durationTableCreating + durationTableSearch));
        System.out.println("Creating time: " + printDuration(durationTableCreating));
        System.out.println("Searching time: " + printDuration(durationTableSearch));
        System.out.println();
    }

    private static String printDuration(long duration) {
        StringBuilder builder = new StringBuilder();
        builder.append((duration / 60000) + " min. ");
        builder.append((duration % 60000 / 1000) + " sec. ");
        builder.append((duration % 1000) + " ms.");
        return builder.toString();
    }
}
