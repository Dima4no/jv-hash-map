package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {

    private final double loadFactor;
    private int capacity;
    private int threshold;
    private int size;
    private MyNode<K, V>[] table;

    public MyHashMap() {
        this.capacity = 16;
        this.loadFactor = 0.75;
        this.threshold = (int) (capacity * loadFactor);
        this.size = 0;
        this.table = (MyNode<K, V>[]) new MyNode[capacity];
    }

    private static class MyNode<K, V> {
        private final K key;
        private final int hash;
        private V value;
        private MyNode<K, V> next;

        public MyNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.hash = (key == null ? 0 : key.hashCode());
            this.next = null;
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + (key == null ? 0 : key.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || this.getClass() != obj.getClass()) {
                return false;
            }
            MyNode<?, ?> other = (MyNode<?, ?>) obj;
            return (Objects.equals(key, other.key));
        }
    }

    public int calculateIndex(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode() % capacity);
    }

    public void resize() {
        // Update size, capacity and threshold
        size = 0;
        capacity = capacity * 2;
        threshold = (int) (capacity * loadFactor);
        // Create a new array
        MyNode<K, V>[] newTable = (MyNode<K, V>[]) new MyNode[capacity];
        // Move elements to the new array
        for (int i = 0; i < table.length; i++) {
            MyNode<K, V> current = table[i];
            // Move elements if there is more than one element in a bucket
            while (current != null) {
                int index = calculateIndex(current.key);
                MyNode<K, V> next = current.next;
                current.next = newTable[index];
                newTable[index] = current;
                size++;
                current = next;
            }
        }
        table = newTable;
    }

    @Override
    public void put(K key, V value) {

        // Calculate an index in array
        int index = calculateIndex(key);
        // Check is the same elements exists in the table
        MyNode<K, V> current = table[index];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        // Create new node with given key and value
        MyNode<K, V> node = new MyNode<>(key, value);
        node.next = table[index];
        table[index] = node;
        size++;
        // Check size
        if (size > threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int index = calculateIndex(key);
        MyNode<K, V> current = table[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }
}
