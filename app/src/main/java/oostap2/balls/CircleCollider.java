package oostap2.balls;

public class CircleCollider {

    private final RigidBody parrentRigidBody;
    private final Vector position;
    private final int radius;
    private final float elasticity;

    public CircleCollider(RigidBody parrentRigidBody, Vector position, int radius, float elasticity) {
        this.parrentRigidBody = parrentRigidBody;
        this.position = position;
        this.radius = radius;
        this.elasticity = elasticity;

    }

    public float getElasticity() {
        return elasticity;
    }

    public float getRadius() {
        return radius;
    }

    public Vector getPosition() {
        return position;
    }

    public RigidBody getRigidBody() {
        return parrentRigidBody;
    }
}
