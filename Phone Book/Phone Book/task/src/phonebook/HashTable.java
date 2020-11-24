package phonebook;

public class HashTable<T> {
    private int size;
    private TableEntry[] table;

    public HashTable(int size) {
        this.size = size;
        table = new TableEntry[size];
    }

    public boolean put(int key, T value) {
        // put your code here
        int idx = findKey(key);

        if (idx == -1) {
            rehash();
        }

        idx = findKey(key);
        table[idx] = new TableEntry(key, value);
        return true;
    }

    public T get(int key) {
        int idx = findKey(key);

        if (idx == -1 || table[idx] == null) {
            return null;
        }

        return (T) table[idx].getValue();
    }

    private int findKey(int key) {
        int hash = key % size;

        while (!(table[hash] == null || table[hash].getKey() == key)) {
            hash = (hash + 1) % size;

            if (hash == key % size) {
                return -1;
            }
        }

        return hash;
    }

    private void rehash() {
        // put your code here
        TableEntry[] oldArray = table;
        size *= 2;
        table = new TableEntry[size];
        for (TableEntry entry : oldArray) {
            put(entry.key, (T) entry.value);
        }
    }

    @Override
    public String toString() {
        StringBuilder tableStringBuilder = new StringBuilder();

        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                tableStringBuilder.append(i + ":null");
            } else {
                tableStringBuilder.append(i + ":key=" + table[i].getKey()
                        + ", value=" + table[i].getValue());
            }

            if (i < table.length - 1) {
                tableStringBuilder.append(" ");
            }
        }

        return tableStringBuilder.toString();
    }
}

class TableEntry<T> {
    protected final int key;
    protected final T value;

    public TableEntry(int key, T value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }
}