package org.mistu.android.exam;

import java.io.Serializable;

/**
 * Created by kedee on 30/3/17.
 */

public class Pair implements Serializable{
    private static final long serialVersionUID = 34L;

    public String first;
    public String second;

    public Pair() {
    }

    public Pair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public boolean areFieldsEqual(){
        if (first.equals(second)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first='" + first + '\'' +
                ", second='" + second + '\'' +
                '}';
    }
}
