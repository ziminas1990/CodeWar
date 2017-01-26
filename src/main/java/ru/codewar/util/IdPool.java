package ru.codewar.util;

import java.util.LinkedList;
import java.util.List;

public class IdPool {

    private int nextId = 1;
    private LinkedList<Integer> reusableId = new LinkedList<>();

    public int getNextId() {
        if(reusableId.isEmpty()) {
            return nextId++;
        }
        Integer id = reusableId.getFirst();
        reusableId.removeFirst();
        return id;
    }

    public void releaseId(int id) {
        reusableId.addLast(id);
    }

}
