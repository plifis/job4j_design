package ru.job4j.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ForwardLinked<T> implements Iterable<T> {
    private Node<T> head;

    public void add(T value) {
        Node<T> node = new Node<>(value, null);
        if (head == null) {
            head = node;
            return;
        }
        Node<T> tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = node;
    }

    public T deleteFirst() {
        Node<T> next;
        T model;
        if (head == null) {
            throw new NoSuchElementException();
        }
        model = head.value;
        next = head.next;
        head = next;
        return model;
    }

    public T deleteLast() {
        Node<T> next = head;
        Node<T> prev = null;
        T model;
        if (head == null) {
            throw new NoSuchElementException();
        }
        while (next.next != null) {
            prev = next;
            next = next.next;
        }
        model = next.value;
        if (prev != null) {
            prev.next = null;
        } else {
          head = null;
        }
        return model;
    }

    public void revert() {
        Node<T> prev = null;
        Node<T> current = head;
        Node<T> next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        this.head = prev;

    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> node = head;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = node.value;
                node = node.next;
                return value;
            }
        };
    }

    private static class Node<T> {
        T value;
        Node<T> next;

        public Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }
}