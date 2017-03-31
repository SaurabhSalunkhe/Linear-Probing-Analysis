package LinearProbing;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Random;

public class LinearProbing {

    public static void main(String args[]) {

        HashMap map = new HashMap();
        map.setThreshold(0.3f);
        Random random = new Random();
        long startTime = System.nanoTime();
        for (int i = 0; i < 13; i++) {
            map.put(i * 11, random.nextInt(128));
        }
        long endTime = System.nanoTime();
        System.out.println("Run time : " + (endTime - startTime) + " ns");
        System.out.println();
        map.displayTable();
    }
}

class HashEntry {

    private int key;
    private int value;

    HashEntry(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    void setValue(int value) {
        this.value = value;
    }
}
class DeletedEntry extends HashEntry {

    private static DeletedEntry e = null;

    private DeletedEntry() {
        super(-1, -1);
    }

    public static DeletedEntry getDelE() {
        if (e == null) {
            e = new DeletedEntry();
        }
        return e;
    }
}

class HashMap {

    int DEFAULT_TABLE_SIZE = 11;
    float threshold = 0.4f;
    int maxSize = 10;
    int size = 0;
    HashEntry[] table;

    public HashMap() {
        table = new HashEntry[DEFAULT_TABLE_SIZE];
        for (int i = 0; i < DEFAULT_TABLE_SIZE; i++) {
            table[i] = null;
        }
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
        maxSize = (int) (table.length * threshold);
    }

    public void put(int key, int value) {

        int hash = (key % table.length);
        System.out.println(" Key : " + key + " Hash : " + hash);
        int iHash = -1;
        int indexOfDeletedEntry = -1;
        while (hash != iHash&& (table[hash] == DeletedEntry.getDelE()|| table[hash] != null&& table[hash].getKey() != key)) {

            if (iHash == -1) {
                iHash = hash;
            }
            if (table[hash] == DeletedEntry.getDelE()) {
                indexOfDeletedEntry = hash;
            }
            hash = (hash + 1) % table.length;

            System.out.println("Collision!!! ");
            System.out.println("New Hash :" + hash + " Key :" + key);
        }
        if ((table[hash] == null || hash == iHash)
                && indexOfDeletedEntry != -1) {
            table[indexOfDeletedEntry] = new HashEntry(key, value);
            size++;
            System.out.println("Size incremented : " + size);
        } else if (iHash != hash) {
            if (table[hash] != DeletedEntry.getDelE()
                    && table[hash] != null && table[hash].getKey() == key) {
                table[hash].setValue(value);
            } else {
                table[hash] = new HashEntry(key, value);
                size++;
                System.out.println("Size incremented :" + size);
            }
        }
        if (size >= maxSize) {
            resize();
        }
    }

   
    public void insertNewTable(int key, int value) {
        int hash = (key % table.length);
        int initialHash = -1;
        while (hash != initialHash
                && (table[hash] != null && table[hash].getKey() != key)) {

            if (initialHash == -1) {
                initialHash = hash;
            }

            hash = (hash + 1) % table.length;
        }
        if (initialHash != hash) {
            if (table[hash] != null && table[hash].getKey() == key) {
                table[hash].setValue(value);
            } else {
                table[hash] = new HashEntry(key, value);
                size++;
            }
        }
    }

    public void displayTable() {
        System.out.println("Table: ");
        for (int j = 0; j < table.length; j++) {
            if (table[j] != null) {
                System.out.print(j);
                System.out.print(" Key is " + table[j].getKey() + " ");
                System.out.print("Value is " + table[j].getValue() + " ");
                System.out.println();
            } else {
            }
        }
    }
    //resize when meets threshold
    private void resize() {
        int tableSize = 2 * table.length;
        maxSize = (int) (tableSize * threshold);
        HashEntry[] oldTable = table;
        table = new HashEntry[tableSize];
        size = 0;
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null && oldTable[i] != DeletedEntry.getDelE()) {
                insertNewTable(oldTable[i].getKey(), oldTable[i].getValue());
            }
        }
    }

}


