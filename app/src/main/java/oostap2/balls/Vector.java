package oostap2.balls;

import java.util.Random;

public class Vector {

    public float x, y;

    private static final Random rng = new Random();

    Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    Vector() {
        this(0, 0);
    }

    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector set(Vector other) {
        x = other.x;
        y = other.y;
        return this;
    }

    public Vector reset() {
        set(0, 0);
        return this;
    }

    public Vector add(Vector other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector subtract(Vector other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Vector multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public Vector reverse() {
        return multiply(-1.0f);
    }

    private Vector divide(float scalar) {
        float res = 1 / scalar;
        x *= res;
        y *= res;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return x * x + y * y;
    }

    public Vector normalize() {
        float vectorLength = length();
        if (vectorLength == 0) {
            return setRandomDirection();
        }
        divide(vectorLength);
        return this;
    }

    private Vector setRandomDirection() {
        double angle = rng.nextFloat() * 2 * Math.PI;
        this.x = (float) Math.cos(angle);
        this.y = (float) Math.sin(angle);
        return this;
    }

    public static float dotProduct(Vector vector1, Vector vector2) {
        return vector1.x * vector2.x + vector1.y * vector2.y;
    }
}
