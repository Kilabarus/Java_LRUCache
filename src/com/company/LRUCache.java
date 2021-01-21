package com.company;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Коллекция, реализующая LRU кеш
 * За основу взят двусвязный список
 *
 * @param <K> Тип ключей
 * @param <V> Тип значений
 */
public class LRUCache<K, V> implements ILRUCache<K, V>, Iterable<Node<K, V>> {
    private final int cacheLimit;

    private Node<K, V> head = null, tail = null;
    private int size = 0;

    public LRUCache(int cacheLimit) {
        this.cacheLimit = cacheLimit;
    }

    /**
     * Создает узел с элементом и его ключом и добавляет его в начало списка
     *
     * @param key   ключ элемента
     * @param value сам элемент
     */
    private void addAsFirst(K key, V value) {
        Node<K, V> newNode = new Node<>(key, value);

        if (head == null) {
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
        }

        head = newNode;
        ++size;
    }

    /**
     * Удаляет последний узел в списке
     */
    private void removeLast() {
        switch (size) {
            case 0 -> throw new NoSuchElementException("Попытка удаления элемента из пустой очереди");
            case 1 -> {
                head = tail = null;
                size = 0;
            }
            default -> {
                tail = tail.prev;
                tail.next = null;
                --size;
            }
        }
    }

    /**
     * Перемещает уже существующий узел в начало списка
     *
     * @param node
     */
    private void moveToFront(Node<K, V> node) {
        if (node.prev != null) {
            if (node.next == null) {
                head.prev = tail;
                tail.next = head;
                tail = tail.prev;
                tail.next = null;
                head = head.prev;
                head.prev = null;
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                node.prev = null;
                node.next = head;
                head.prev = node;
                head = node;
            }
        }
    }

    /**
     * Возвращает значение, соответствующее указанному ключу
     * При этом элемент (пара ключ-значение) помечается
     * как последний использованный.
     *
     * @param key Ключ
     * @return Значение или {@code null},
     * если значение не найдено
     */
    @Override
    public V get(K key) {
        for (Node<K, V> node : this) {
            if (node.key.equals(key)) {
                moveToFront(node);
                return node.value;
            }
        }

        return null;
    }

    /**
     * Добавляет элемент (пару ключ-значение) в коллекцию
     * В случае, если элемент с таким ключом уже был
     * в коллекции, он заменяется
     * При этом элемент помечается как последний использованный
     * <p>
     * В случае, если до вставки размер коллекции был равен
     * максимальному, из нее удаляется элемент,
     * неиспользованный дольше всех
     *
     * @param key Ключ
     * @param value Значение
     */
    @Override
    public void set(K key, V value) {
        if (head != null) {
            for (Node<K, V> node : this) {
                if (node.key.equals(key)) {
                    node.value = value;
                    moveToFront(node);
                    return;
                }
            }

            if (size == cacheLimit) {
                removeLast();
            }
        }

        addAsFirst(key, value);
    }

    /**
     * Возвращает текущий размер коллекции
     *
     * @return Текущий размер
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Возвращает максимальный размер коллекции
     *
     * @return Максимальный размер
     */
    @Override
    public int getLimit() {
        return cacheLimit;
    }

    /**
     * Реализация интерфейса Iterable<Node<K, V>>
     *
     * @return  итератор для возможности использования буфера в for-each циклах
     */
    @Override
    public Iterator<Node<K, V>> iterator() {
        return new Iterator<>() {
            private Node<K, V> node = head;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Node<K, V> next() {
                Node<K, V> returnNode = node;
                node = node.next;

                return returnNode;
            }
        };
    }
}
