package de.alshikh.haw.tron.client.views.view_library;

import java.io.Serializable;
import java.util.Objects;

// TODO: write a wrapper to keep this virgin
public class Coordinate implements Serializable {
    private static final long serialVersionUID = 6232415172729433635L;

    public final int x;
    public final int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinate add(Coordinate other){
        return new Coordinate(x + other.x, y + other.y);
    }

    public Coordinate sub(Coordinate other){
        return new Coordinate(x - other.x, y - other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return x == coordinate.x &&
                y == coordinate.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
