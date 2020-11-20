import java.awt.Graphics2D;
/*
 * MovingParts are body objects like joints as well as head or torso, named after their moving functionality
 */
public abstract class MovingBodyPart {
    //describes the transformation mat to be applied to previous parent to find current part loc
    private Mat33 transform;
    //Stationary MovingBodyParts cause the entire group to translate. 
    private boolean isStationary;
    //describes the amount a given part should change after each drawing update
    private Vec2 change;
    public MovingBodyPart(Vec2 change, boolean isStationary) {
        this.isStationary = isStationary;
        this.setTransform(Mat33.findTranslationMat(change));
    }
    public MovingBodyPart(Mat33 trans) {
        this.transform = trans;
    }
    public MovingBodyPart(Vec2 change) {
        this.setTransform(Mat33.findTranslationMat(change));
    }
    //changed to return Vec2 instead of void
    public abstract Vec2 draw (Graphics2D g, Vec2 parent);
    public abstract void drawFill(Graphics2D g);
    //public abstract boolean contains(Vec2 point);
    public void translate (Vec2 trans) {
        float x = transform.getValAtPosition(0, 2);
        float y = transform.getValAtPosition(1, 2);

        transform.setValAtPosition(0, 2, x+trans.getX());
        transform.setValAtPosition(1, 2, x + trans.getY());
    }
    public Vec2 getChange() {
        return change;
    }
    public void setChange(Vec2 change) {
        this.change = change;
    }

    public Mat33 getTransform() {
        return transform;
    }
    public void setTransform(Mat33 transform) {
        this.transform = transform;
    }
    public void setTransform(Vec2 change) {
        this.setTransform(Mat33.findTranslationMat(change));
    }
    public boolean isStationary() {
        return isStationary;
    }

    public void setStationary(boolean stationary) {
        isStationary = stationary;
    }
}
