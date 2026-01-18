package oostap2.balls;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircleRender {
    private final Vector position;
    private final float radius;
    private Paint paint;

    public CircleRender(Vector position, float radius, Paint paint) {
        this.position = position;
        this.radius = radius;
        this.paint = paint;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, radius, paint);
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
