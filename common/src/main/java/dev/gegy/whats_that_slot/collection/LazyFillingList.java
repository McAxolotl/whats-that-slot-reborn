package dev.gegy.whats_that_slot.collection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class LazyFillingList<T> extends AbstractList<T> {
    private final Iterator<T> source;
    private final int size;

    private final List<T> backing = new ArrayList<>();

    private LazyFillingList(Iterator<T> source, int size) {
        this.source = source;
        this.size = size;
    }

    public static <T> LazyFillingList<T> ofIterator(Iterator<T> source, int size) {
        return new LazyFillingList<>(source, size);
    }

    @Override
    public T get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + index);
        }

        var list = this.fillUpTo(index);
        return list.get(index);
    }

    private List<T> fillUpTo(int index) {
        var source = this.source;
        var backing = this.backing;
        while (backing.size() <= index) {
            if (!source.hasNext()) {
                throw new IllegalStateException("Could not fill list up to index=" + index);
            }
            backing.add(source.next());
        }

        return backing;
    }

    @Override
    public int size() {
        return this.size;
    }
}
