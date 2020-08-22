import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLOutput;
import java.util.function.Consumer;
import java.util.function.Function;

public class Display extends JPanel implements MouseListener,
        MouseMotionListener {
    private static int timePerFrame = 1;
    private static final int framesPerSecond = 60;
    private static boolean needsRepaint = true;
    private static int tokens = 4;
    private Node body;
    private Vec2 center;

    private Vec2 mouseLoc = new Vec2(-1, -1);
    private static boolean pressed = false, released = false;
    private static Vec2 start, end;

    private Container cp;

    private Link link;
    /** Allows for buttons, listening, and organization of GUI. */
    public Display(JFrame frame) {
        super(new FlowLayout());
        addMouseListener(this);
        addMouseMotionListener(this);
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton moveBut = new JButton("Fixed Joint");
        btnPanel.add(moveBut);
        moveBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Note: if we want each action to be performed in a second, we must give framesPerSecond number of tokens
                //link.head().translate(new Vec2(20, 30));
                tokens = 8;
                update();
            }
        });

        JButton mobileMove = new JButton("Mobile Joint");
        btnPanel.add(mobileMove);
        mobileMove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Note: if we want each action to be performed in a second, we must give framesPerSecond number of tokens
                tokens = 1;
                update();
            }
        });

        JButton resetBut = new JButton("Reset");
        btnPanel.add(resetBut);
        resetBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setup();
                update();
                repaint();
            }
        });
        //container cp allows us to two JPanels: canvas and buttons
        cp = frame.getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(this, BorderLayout.CENTER);
        cp.add(btnPanel, BorderLayout.SOUTH);
        setup();
    }
    /** First create the fixed torso. Then add on the Links that let the torso
     move. For now I'm ignoring the head which is a moving body part which
     means I'd have to revamp Link a little.
     */
    public void setup() {
        center = new Vec2(300, 400);
        //link = new Link();
        Joint root = new Joint(new Vec2(center.getX(), center.getY()), true);
        Link pelvis = new Link(root, root);
        Link hipRight = new Link(new Joint(new Vec2(-40, 0), true), root);
        Link hipLeft = new Link(new Joint(new Vec2(40, 0), true), root);
        body = new Node(pelvis);
        body.addChild(new Node(hipLeft));
        body.addChild(new Node(hipRight));

        Link neck = new Link(new Joint(new Vec2(0, -100), true), root);
        Link shoulderLeft = new Link(new Joint(new Vec2(40, 0), true), root);
        Link shoulderRight = new Link(new Joint(new Vec2(-40, 0), true), root);
        Node neckNode = new Node(neck);
        body.addChild(neckNode);
        neckNode.addChild(new Node(shoulderLeft));
        neckNode.addChild(new Node(shoulderRight));

        Joint kneeRight = new Joint(new Vec2(-10, 80));
        Joint kneeLeft = new Joint(new Vec2(10, 80));
        Joint heelRight = new Joint(new Vec2(10, 90));
        Joint heelLeft = new Joint(new Vec2(-10, 90));
        hipRight.add(kneeRight);
        hipRight.add(heelRight);
        hipLeft.add(kneeLeft);
        hipLeft.add(heelLeft);

        Joint elbowRight = new Joint(new Vec2(-40, 20));
        Joint elbowLeft = new Joint(new Vec2(40, 20));
        Joint wristRight = new Joint(new Vec2(-40, -40));
        Joint wristLeft = new Joint(new Vec2(40, -40));
        shoulderRight.add(elbowRight);
        shoulderRight.add(wristRight);
        shoulderLeft.add(elbowLeft);
        shoulderLeft.add(wristLeft);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        //link.draw(g2d);
        body.traverseDraw(g2d, new Vec2());

    }

    /** Updates the joints to their new positions and determines whether
     * repainting is needed.
     */
    public void update() {
        if (pressed && end != null) {
            //link.move(start, end, tokens);
            //need a better technique to find the link that i need to search;
            // this is just brute force
            Consumer<Link> func = (link) -> link.move(start, end, tokens,
                    body.findParentLinkLoc(link, new Vec2()));
            body.traverseApply(func);
            repaint();
        }
        if (needsRepaint) {
            repaint();
        }
        needsRepaint = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("pressed");
        start = new Vec2(e.getX(), e.getY());
        end = start;
        pressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("release");
        pressed = false;
        start = new Vec2(e.getX(), e.getY());
        end = start;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        end = new Vec2(e.getX(), e.getY());
        needsRepaint = true;
        update();

        start = end;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }


    public static void main(String[] args) throws InterruptedException {
        timePerFrame = 1000/framesPerSecond;
        System.out.println("START");
        JFrame frame = new JFrame();

        Display display = new Display(frame);
        frame.add(display);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 800));
        frame.pack();
        frame.setVisible(true);
        int counter = 0;
        boolean run = true;
        while(run) {
            //not sure why this sleep is needed...
            Thread.sleep(1);
            while(true) {
                if (needsRepaint)
                    display.repaint();
                display.update();
                Thread.sleep(timePerFrame);
            }
        }
    }
}
