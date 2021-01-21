package com.company;

/**
 * Узел для двусвязного списка
 *
 * @param <K>   тип для ключей
 * @param <V>   тип для хранящихся элементов
 */
public class Node<K, V> {
    K key;
    V value;
    Node<K, V> next = null, prev = null;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
