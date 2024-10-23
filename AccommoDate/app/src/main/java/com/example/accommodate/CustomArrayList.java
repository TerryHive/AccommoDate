package com.example.accommodate;
import com.example.accommodate.global.*;

import java.util.ArrayList;

public class CustomArrayList<E> extends ArrayList<E> {

    public E getFirst() {
        if (this.isEmpty()) {
            throw new IndexOutOfBoundsException("The list is empty.");
        }
        return this.get(0);
    }
}