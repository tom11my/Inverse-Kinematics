import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
public class Link{
    private Joint head;
    private Link tail;
    /** Default Link of 5 nodes for testing. */
    public Link() {
        this.head = new Joint(new Vec2(100, 100));
        this.head.setStationary(true);
        this.add(new Joint(new Vec2(40, 60)));
        this.add(new Joint(new Vec2(30, 10)));
        this.add(new Joint(new Vec2(80, -40)));
        this.add(new Joint(new Vec2(100, 30)));
        this.add(new Joint(new Vec2(40, -40)));
    }

    public Link(Joint j) {
        this.head = j;
    }

    /** Draws the Joints of this group by traversing through them, starting
     * at the root. */
    public void draw(Graphics2D g2d) {
        Link pointer = this;
        Vec2 prev = pointer.head.draw(g2d, new Vec2(0, 0));
        pointer = pointer.tail;
        while(pointer != null) {
            Vec2 temp = prev;
            prev = pointer.head.draw(g2d, prev);
            g2d.drawLine((int)temp.getX(), (int)temp.getY(), (int)prev.getX(),
                    (int)prev.getY());
            pointer = pointer.tail;
        }
    }

    /** Moves the Joint at start to goal and adjusts the rest of the Link
     * accordingly. Also determines which joint is at start. Applies fabrik
     * to mobile joints. If TOKENS != 1, then move back and forth TOKENS
     * number of times.
     */
    public void move(Vec2 start, Vec2 goal, int tokens) {
        Link pointer = this;
        Vec2 prev = new Vec2();
        while (pointer != null) {
            prev = pointer.head.loc(prev);
            if (prev.minus(start).findMag2() < Joint.radius* Joint.radius) {
                break;
            }
            pointer = pointer.tail;
        }
        if (pointer != null) {
            Joint j = pointer.head;
            if (j.isStationary()) {
                this.head.setTransform(goal);
            } else {
                fabrik(j, goal, tokens);


            }
        } else {
            System.out.println("No joint at that location. Try again.");
        }
    }

    /** FABRIK. */
    public void fabrik(Joint j, Vec2 goal, int tokens) {
        Vec2 START = this.head.loc(new Vec2());
        int len = depth(j) + 1;
        Joint[] joints = getJoints(j);
        if (!isPossible(j, joints, goal)) {
            System.out.println("Not possible. Try again.");
        }
        else {
            boolean alternate = false;
            while(tokens != 0) {
                Vec2[] desiredLocs = alternate ?
                        forwardPass(joints[0], START, j) : backwardsPass(j,
                        goal);
                Vec2 parent = desiredLocs[0];
                joints[0].setTransform(desiredLocs[0]);
                for (int i = 1; i < depth(j) + 1; i++) {
                    joints[i].setTransform(desiredLocs[i].minus(parent));
                    parent = desiredLocs[i];
                }
                alternate = !alternate;
                tokens--;
            }
        }
    }
    /** Backwards pass. */
    public Vec2[] backwardsPass (Joint j, Vec2 goal) {
        Joint[] joints = getJoints(j);
        Vec2[] locs = getLocs(j);
        int len = depth(j) + 1;
        Vec2[] desiredLocs = new Vec2[128];
        desiredLocs[len - 1] = goal;
        for (int i = 1; i < len; i++) {
            Joint next = joints[len - i];
            Vec2 dir = locs[len - i - 1].minus(goal).toUnit();
            Vec2 delta =
                    dir.scaledBy(next.getTransform().findTransVec().findMag());
            desiredLocs[len - 1 - i] = goal.plus(delta);
            goal = desiredLocs[len - 1 - i];
        }
        return desiredLocs;
    }
    /** Forward pass. TAKE OUT END TO MAKE WORK. USE J IN PLACE OF END. */
    public Vec2[] forwardPass(Joint j, Vec2 start, Joint end) {
        Joint[] joints = getJoints(end);
        Vec2[] locs = getLocs(end);
        int len = depth(end) + 1;
        Vec2[] desiredLocs = new Vec2[128];
        desiredLocs[0] = start;
        for (int i = 1; i < len ; i++) {
            Joint next = joints[i];
            Vec2 dir = locs[i].minus(start).toUnit();
            Vec2 delta =
                    dir.scaledBy(next.getTransform().findTransVec().findMag());
            desiredLocs[i] = start.plus(delta);
            start = desiredLocs[i];
        }
        return desiredLocs;
    }
    /** Forward pass. Moves the mobile Joint j to the Vec2 goal.
     * Invariant: Length of joint connections are preserved. */
    public Vec2[] moveForward (Joint j, Vec2 goal, Joint end) {
        return pass(j, goal, true, end);
    }

    /** Backward pass. Moves the root of the link back to its original position.
     */
    public Vec2[] moveBackward (Joint j, Vec2 initial, Joint end) {
        return pass(j, initial, false, end);
    }

    /** Pass outline. Can be made backwards or forwards by ISFORWARDS. */
    public Vec2[] pass(Joint j, Vec2 goal, boolean isForwards, Joint end) {
        int len = depth(end) + 1;
        int increment = isForwards ? len : 0;
        int factor = isForwards ? -1 : 1;
        Joint[] joints = getJoints(end);
        Vec2[] locs = getLocs(end);
        Vec2[] desiredLocs = new Vec2[128];
        desiredLocs[isForwards ? 0: len - 1] = goal;
        for (int i = 1 ; i < len ; i++) {
            int nextInd = len - i*factor - increment;
            Joint next = joints[nextInd];
            Vec2 dir = locs[nextInd - 1].minus(goal).toUnit();
            Vec2 delta =
                    dir.scaledBy(next.getTransform().findTransVec().findMag());
            desiredLocs[nextInd - 1*factor] = goal.plus(delta);
            goal = desiredLocs[nextInd - 1*factor];
        }
        return desiredLocs;
    }

    /** Returns whether the chosen destination LOC is possible for JOINT in
     * JOINTS to reach. Only a loose check. */
    public boolean isPossible(Joint joint, Joint[] joints, Vec2 loc) {
        Vec2 center = this.head.loc(new Vec2());

        float distSquared = center.minus(loc).findMag();
        float radiusSquared = 0;
        for (int i = 1; joints[i - 1] != joint; i++) {
            radiusSquared += joints[i].getTransform().findTransVec().findMag();
        }
        return distSquared < radiusSquared;
    }

    /** Returns an array of the Joints in this Link up to but including
     * Joint j. Assumes j is somewhere in this Link. */
    public Joint[] getJoints(Joint j) {
        Link pointer = this;
        Joint[] joints = new Joint[128];
        int counter = 0;
        while (pointer.head != j) {
            joints[counter] = pointer.head;
            pointer = pointer.tail;
            counter++;
        }
        joints[counter] = pointer.head;
        return joints;
    }

    /** Returns how far Joint j is from the root of this link. Assumes the
     * joint is somewhere in this Link. */
    public int depth(Joint j) {
        int counter = 0;
        Link pointer = this;
        while (pointer.head != j) {
            counter++;
            pointer = pointer.tail;
        }
        return counter;
    }

    /** Returns an array of the locations of the Joints in this Link up
     * to but including Joint j. */
    public Vec2[] getLocs(Joint j) {
        Link pointer = this;
        Vec2 parent = new Vec2();
        Vec2[] locs = new Vec2[128];
        int counter = 0;
        while (pointer.head != j) {
            parent = pointer.head.loc(parent);
            locs[counter] = parent;
            pointer = pointer.tail;
            counter++;
        }
        locs[counter] = pointer.head.loc(parent);
        return locs;
    }

    /** Adds a Joint to the end of the Linked List. */
    public void add (Joint j) {
        Link pointer = this;
        while (pointer.tail != null) {
            pointer = pointer.tail;
        }
        pointer.tail = new Link(j);
    }

    /** Finds the Link whose head is Joint j starting from this Link. */
    public Link find(Joint j) {
        Link pointer = this;
        while (pointer != null) {
            if (pointer.head == j) {
                return pointer;
            }
            pointer = pointer.tail;
        }
        return null;
    }

    /** Returns the head of this Link. */
    public Joint head() {
        return this.head;
    }

    /** Returns the tail of this Link. */
    public Link tail() {
        return this.tail;
    }
}