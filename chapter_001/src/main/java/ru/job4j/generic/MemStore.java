package ru.job4j.generic;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public final class MemStore<T extends Base> implements Store<T> {
    private final List<T> mem = new ArrayList<>();

    @Override
    public void add(T model) {
        mem.add(model);
    }

    @Override
    public boolean replace(String id, T model) {
        boolean done = false;
        int index = this.findIndexById(id);
        if (index >= 0) {
            mem.set(index, model);
            done = true;
        }
        return done;
    }

    @Override
    public boolean delete(String id) {
        T model = this.findById(id);
        if (model != null) {
            return mem.remove(model);
        } else {
            return false;
        }
    }

    @Override
    public T findById(String id) {
        return mem.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst().orElse(null);
    }

    public int findIndexById(String id) {
        int index = -1;
        for (T t: mem) {
            index++;
            if (t.getId().equals(id)) {
                break;
            }
        }
        return index;
    }
}