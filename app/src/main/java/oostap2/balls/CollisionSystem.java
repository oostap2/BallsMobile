package oostap2.balls;

public class CollisionSystem {

    private static class ContactPool {

        private final ContactPoint[] pool;
        private int index;
        ContactPool(int quantity) {
            int poolSize = (quantity * (quantity - 1)) / 2;

            pool = new ContactPoint[poolSize];
            for (int i = 0; i < poolSize; i++) {
                pool[i] = new ContactPoint();
            }
        }
        public ContactPoint get() {
            return pool[index++];
        }
        
        public void flush() {
            index = 0;
        }

        public int getIndex() {
            return index;
        }
    }

    private final Vector temporalVector;
    private final ContactPool contactPool;
    private final int size;
    private final CircleCollider[] colliders;

    CollisionSystem(CircleCollider[] colliders) {
        this.colliders = colliders;
        size = colliders.length;
        temporalVector = new Vector();
        contactPool = new ContactPool(size);
    }

    private void checkCollision(CircleCollider collider1, CircleCollider collider2) {
        float radiusSum = collider1.getRadius() + collider2.getRadius();
        float radiusSumSquared = radiusSum * radiusSum;
        temporalVector
                .set(collider2.getPosition())
                .subtract(collider1.getPosition());
        float distanceSquared = temporalVector.lengthSquared();

        if (radiusSumSquared < distanceSquared) return;

        ContactPoint contactPoint = contactPool.get();
        float distance = (float) Math.sqrt(distanceSquared);
        float depth = radiusSum - distance;
        contactPoint
                .set(depth, collider1, collider2)
                .setNormal(temporalVector.normalize());
    }

    private void correctColliders(ContactPoint contactPoint) {
        temporalVector
                .set(contactPoint.normal)
                .multiply(contactPoint.depth);
        contactPoint.collider2
                .getPosition()
                .add(temporalVector.multiply(0.5f));
        contactPoint.collider1
                .getPosition()
                .add(temporalVector.reverse());
    }

    private void applyImpactForce(ContactPoint contactPoint) {
        CircleCollider collider1 = contactPoint.collider1;
        CircleCollider collider2 = contactPoint.collider2;
        RigidBody rigidBody1 = collider1.getRigidBody();
        RigidBody rigidBody2 = collider2.getRigidBody();
        temporalVector
                .set(rigidBody2.getVelocity())
                .subtract(rigidBody1.getVelocity());
        float effectiveRelativeVelocity = Vector.dotProduct(temporalVector, contactPoint.normal);

        if (effectiveRelativeVelocity > 0) return;

        float e = Math.min(collider1.getElasticity(), collider2.getElasticity());
        float j = (-(1 + e)) * effectiveRelativeVelocity / (rigidBody1.getInvertedMass() + rigidBody2.getInvertedMass());
        temporalVector
                .set(contactPoint.normal)
                .multiply(j);
        rigidBody2.addImpulse(temporalVector);
        temporalVector.reverse();
        rigidBody1.addImpulse(temporalVector);
    }

    private void resolveCircleCollision(ContactPoint contactPoint) {
        applyImpactForce(contactPoint);
        correctColliders(contactPoint);
    }

    public void resolveCollisions(int screenWidth, int screenHeight) {
        contactPool.flush();
        for (int i = 0; i < size; i++) {
            CircleCollider collider1 = colliders[i];
            wallsReflection(collider1, screenWidth, screenHeight);

            for (int j = i + 1; j < size; j++) {
                CircleCollider collider2 = colliders[j];

                checkCollision(collider1, collider2);
            }
        }

        int contactPointNumber = contactPool.getIndex();
        contactPool.flush();
        for (int i = 0; i < contactPointNumber; i++) {
            ContactPoint contactPoint = contactPool.get();
            resolveCircleCollision(contactPoint);
        }
    }

    private void wallsReflection(CircleCollider circleCollider, int screenWidth, int screenHeight) {
        Vector position = circleCollider.getPosition();
        Vector velocity = circleCollider.getRigidBody().getVelocity();
        float radius = circleCollider.getRadius();

        if (position.x < radius) { position.x = radius; velocity.x = Math.abs(velocity.x); }
        if (position.y < radius) { position.y = radius; velocity.y = Math.abs(velocity.y); }
        if (position.x > screenWidth - radius) { position.x = screenWidth - radius; velocity.x = -Math.abs(velocity.x); }
        if (position.y > screenHeight - radius) { position.y = screenHeight - radius; velocity.y = -Math.abs(velocity.y); }
    }
}
