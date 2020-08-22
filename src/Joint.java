import java.awt.*;

/** A joint must be either stationary or mobile but they share the same
 * functionality. */
public class Joint extends MovingBodyPart{
    private String name;
    public static final int radius = 13;
    public Joint(Vec2 loc) {
        super(loc);
    }
    public Joint(Vec2 loc, boolean isStationary) {
        super(loc, isStationary);
    }
    public Joint (Mat33 mat) {
        super(mat);
    }
    @Override
    /** Draws the current Joint and returns its position as a Vector. */
    public Vec2 draw (Graphics2D g, Vec2 parent) {
        Vec2 transformedLoc = loc(parent);
        g.drawOval((int)transformedLoc.getX() - radius, (int)transformedLoc.getY() - radius, radius*2, radius*2);
        return transformedLoc;
    }

    /** Draw based on previous transforms. */
    public void draw (Graphics2D g, Mat33 prev) {

    }
    String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String toString () {
        return super.getTransform().toString();
    }
    @Override
    public void drawFill(Graphics2D g) {
        // TODO Auto-generated method stub

    }

}
