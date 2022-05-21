package common.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Coordinates is a class that has two fields: x and y
 */
public class Coordinates implements Serializable {
    private int x;
    private float y;

    public Coordinates(int x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the organization
     *
     * @return The x coordinate of the organization.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the organization
     *
     * @return The y-coordinate of the organization.
     */
    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;

        Coordinates that = (Coordinates) o;

        if (getX() != that.getX()) return false;
        return Float.compare(that.getY(), getY()) == 0;
    }
}
