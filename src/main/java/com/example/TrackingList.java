package com.example;

import java.util.*;

public class TrackingList<E> implements List<E> {
    private final List<E> innerList;
    private final List<String> modificationLog = new ArrayList<>();

    public TrackingList(List<E> list) {
        this.innerList = list;
    }

    private void logModification(String operation, Object element) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String callerMethod = stackTrace.length > 3 ? stackTrace[3].toString() : "Unknown";

        String log = String.format("Operation: %s, Element: %s, Thread: %s, Time: %s, Caller: %s",
                operation,
                element,
                Thread.currentThread().getName(),
                new Date(),
                callerMethod);
        modificationLog.add(log);
        System.out.println(log);  // 또는 logger를 사용
    }

    public List<String> getModificationLog() {
        return new ArrayList<>(modificationLog);
    }

    @Override
    public boolean add(E e) {
        logModification("add", e);
        return innerList.add(e);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        logModification("addAll", c);
        return innerList.addAll(c);
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return innerList.iterator();
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return innerList.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        logModification("remove", o);
        return innerList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return innerList.containsAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        logModification("addAll at index " + index, c);
        return innerList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        logModification("removeAll", c);
        return innerList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        logModification("retainAll", c);
        return innerList.retainAll(c);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        logModification("sort", c);
        innerList.sort(c);
    }

    @Override
    public void clear() {
        logModification("clear", "");
        innerList.clear();
    }

    @Override
    public E get(int index) {
        return innerList.get(index);
    }

    @Override
    public E set(int index, E element) {
        logModification("set at index " + index, element);
        return innerList.set(index, element);
    }

    @Override
    public void add(int index, E element) {
        logModification("add at index " + index, element);
        innerList.add(index, element);
    }

    @Override
    public E remove(int index) {
        logModification("remove at index", index);
        return innerList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerList.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return innerList.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return innerList.listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return innerList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<E> spliterator() {
        return innerList.spliterator();
    }
}