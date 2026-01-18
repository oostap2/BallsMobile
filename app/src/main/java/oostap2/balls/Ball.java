package oostap2.balls;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball {

    private final CircleRender render;
    private final CircleCollider collider;
    private final RigidBody rigidBody;

    private final Vector position;

    Ball(Vector position, int radius, float mass, float dampening, float elasticity, Paint paint) {
        render = new CircleRender(position, radius, paint);
        rigidBody = new RigidBody(position, mass, dampening);
        collider = new CircleCollider(rigidBody, position, radius, elasticity);

        this.position = position;
    }

    public void draw(Canvas canvas) {
        render.draw(canvas);
    }

    public void update(float delta) {
        rigidBody.update(delta);
    }

    public CircleCollider getCollider() {
        return collider;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPaint(Paint paint) {
        render.setPaint(paint);
    }
}