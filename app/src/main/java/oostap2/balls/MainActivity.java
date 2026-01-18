package oostap2.balls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Choreographer;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }

    private static class GameView extends View implements Choreographer.FrameCallback {

        private final Ball[] balls;
        private final CollisionSystem collisionSystem;
        private final Vector force;

        private long lastFrameNanoSeconds = 0;

        public GameView(Context context) {
            super(context);
            force = new Vector();

            Paint whitePaint = new Paint();
            whitePaint.setColor(0xffffffff);
            Paint greenPaint = new Paint();
            greenPaint.setColor(0xff96ff96);

            int quantity = 8;

            balls = new Ball[quantity];
            CircleCollider[] colliders = new CircleCollider[quantity];
            for (int i = 0; i < quantity; i++) {
                Ball ball = new Ball(new Vector(), 25, 1.0f, 0.9f, 1.0f, whitePaint);
                balls[i] = ball;
                colliders[i] = balls[i].getCollider();
            }
            balls[0].setPaint(greenPaint);

            collisionSystem = new CollisionSystem(colliders);

            Choreographer.getInstance().postFrameCallback(this);
        }

        @Override
        public void doFrame(long frameNanoSeconds) {
            if (lastFrameNanoSeconds != 0) {
                float delta = (frameNanoSeconds - lastFrameNanoSeconds) / 1_000_000_000f;
                updatePhysics(delta);
            }
            lastFrameNanoSeconds = frameNanoSeconds;

            invalidate();

            Choreographer.getInstance().postFrameCallback(this);
        }

        private void updatePhysics(float delta) {
            for (Ball ball : balls) {
                ball.update(delta);
            }
            collisionSystem.resolveCollisions(getWidth(), getHeight());
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.BLACK);
            for (Ball ball : balls) {
                ball.draw(canvas);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                pushBall(balls[0], event.getX(), event.getY());
            }
            return true;
        }

        @Override
        protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
            super.onSizeChanged(width, height, oldWidth, oldHeight);
            resetBallsPosition(width, height);
        }

        private void pushBall(Ball ball, float x, float y) {
            force
                    .set(x, y)
                    .subtract(ball.getPosition())
                    .normalize()
                    .multiply(80000f);
            ball
                    .getCollider()
                    .getRigidBody()
                    .addForce(force);
        }

        private void resetBallsPosition(int width, int height) {
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            for (Ball ball : balls) {
                Vector position = ball.getPosition();
                position.set(halfWidth, halfHeight);
            }
            for (int i = 0; i < 100; i++) {
                collisionSystem.resolveCollisions(width, height);
            }
        }
    }
}