package oostap2.balls;

public class ContactPoint {

    public float depth;
    public Vector normal;
    public CircleCollider collider1, collider2;

    ContactPoint() {
        normal = new Vector();
    }

    public ContactPoint set(float depth, CircleCollider collider1, CircleCollider collider2) {
        this.depth = depth;
        this.collider1 = collider1;
        this.collider2 = collider2;
        return this;
    }

    public ContactPoint setNormal(Vector normal) {
        this.normal.set(normal);
        return this;
    }
}
