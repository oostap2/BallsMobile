package oostap2.balls;

public class RigidBody {


    private static final Vector temporaryVector = new Vector();
    private float dampening;
    private float invertedMass;
    private final Vector position;
    private final Vector velocity;
    private final Vector acceleration;
    private final Vector temporaryTotalForce;
    private float mass;


    public RigidBody(Vector position, float mass, float dampening) {
        this.position = position;
        this.dampening = dampening;
        invertedMass = 1 / mass;
        velocity = new Vector();
        acceleration =  new Vector();
        temporaryTotalForce = new Vector();
    }

    public void update(float deltaTime) {
        acceleration
                .reset()
                .add(temporaryTotalForce)
                .multiply(invertedMass);
        temporaryTotalForce.reset();
        temporaryVector.set(acceleration);
        velocity
                .add(temporaryVector.multiply(deltaTime))
                .multiply(1.0f - dampening * deltaTime);
        temporaryVector.set(velocity);
        position.add(temporaryVector.multiply(deltaTime));
    }

    public void addForce(Vector force) {
        temporaryTotalForce.add(force);
    }

    public void addImpulse(Vector impulse) {
        temporaryVector.set(impulse);
        velocity.add(
                temporaryVector.multiply(invertedMass)
        );
    }

    public void setMass(float mass) {
        this.mass = mass;
        invertedMass = 1 / mass;
    }

    public void setDampening(float dampening) {
        this.dampening = dampening;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public float getMass() {
        return mass;
    }

    public float getDampening() {
        return dampening;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public float getInvertedMass() {
        return invertedMass;
    }
}
