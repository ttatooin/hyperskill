package phonebook;

public class PhoneBase {

    private PhoneBaseEntry[] entries;

    public PhoneBase(int size) {
        entries = new PhoneBaseEntry[size];
    }

    public void setEntry(int position, int number, String name) {
        entries[position] = new PhoneBaseEntry(number, name);
    }

    public int getEntryNumber(int k) {
        return entries[k].number;
    }

    public String getEntryName(int k) {
        return entries[k].name;
    }

    public int getSize() {
        return entries.length;
    }

    public PhoneBase getCopy() {
        PhoneBase result = new PhoneBase(entries.length);
        for (int k = 0; k < entries.length; ++k) {
            result.entries[k] = new PhoneBaseEntry(entries[k].number, entries[k].name);
        }
        return result;
    }

    public int searchLinear(String name) {
        for (int k = 0; k < entries.length; ++k) {
            if (entries[k].name.equals(name)) {
                return k;
            }
        }
        return -1;
    }

    public int searchJump(String name) {
        int step = (int) Math.sqrt(entries.length);
        int blockFirstIndex = -1;
        int blockLastIndex = -1;
        while (blockFirstIndex < entries.length) {
            blockFirstIndex = blockLastIndex + 1;
            blockLastIndex = (int) Math.min(blockFirstIndex + step - 1, entries.length - 1);
            if (name.compareTo(entries[blockLastIndex].name) <= 0) {
                for (int k = blockLastIndex; k >= blockFirstIndex; --k) {
                    if (name.compareTo(entries[k].name) == 0) {
                        return k;
                    }
                }
            }
        }
        return -1;
    }

    public void sortBubble() {
        for (int n = 0; n < entries.length - 1; ++n) {
            for (int k = 0; k < entries.length - 1 - n; ++k) {
                if (entries[k].name.compareTo(entries[k + 1].name) > 0) {
                    PhoneBaseEntry temp = entries[k];
                    entries[k] = entries[k + 1];
                    entries[k + 1] = temp;
                }
            }
        }
    }

    public long sortBubbleTimeBounded(long timeLimit) {
        long startTime = System.currentTimeMillis();
        long duration = 0;
        for (int n = 0; n < entries.length - 1; ++n) {
            for (int k = 0; k < entries.length - 1 - n; ++k) {
                if (entries[k].name.compareTo(entries[k + 1].name) > 0) {
                    PhoneBaseEntry temp = entries[k];
                    entries[k] = entries[k + 1];
                    entries[k + 1] = temp;
                }
            }
            duration = System.currentTimeMillis() - startTime;
            if (duration > timeLimit) {
                break;
            }
        }
        return duration;
    }

    public int searchBinary(String name) {
        int leftIndex = 0;
        int rightIndex = entries.length - 1;
        while (leftIndex <= rightIndex) {
            int midIndex = leftIndex + (rightIndex - leftIndex) / 2;
            if (entries[midIndex].name.equals(name)) {
                return midIndex;
            } else if (entries[midIndex].name.compareTo(name) < 0) {
                leftIndex = midIndex + 1;
            } else {
                rightIndex = midIndex - 1;
            }
        }
        return -1;
    }

    public void sortQuick() {
        sortQuick(0, entries.length - 1);

    }

    private void sortQuick(int leftIndex, int rightIndex) {
        if (leftIndex < rightIndex) {
            int midIndex = partition(leftIndex, rightIndex);
            sortQuick(leftIndex, midIndex - 1);
            sortQuick(midIndex + 1, rightIndex);
        }
    }

    private void swap(int i, int j) {
        PhoneBaseEntry temp = entries[i];
        entries[i] = entries[j];
        entries[j] = temp;
    }

    private int partition(int leftIndex, int rightIndex) {
        String pivot = entries[rightIndex].name;
        int partitionIndex = leftIndex;
        for (int k = leftIndex; k < rightIndex; ++k) {
            if (entries[k].name.compareTo(pivot) <= 0) {
                swap(k, partitionIndex);
                ++partitionIndex;
            }
        }
        swap(partitionIndex, rightIndex);
        return partitionIndex;
    }
}

class PhoneBaseEntry {

    int number;
    String name;

    public PhoneBaseEntry(int number, String name) {
        this.number = number;
        this.name = name;
    }
}
