import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class SupplyAndDemand extends JPanel implements MouseListener, MouseMotionListener {
    private JFrame frame;
    static SupplyAndDemand demo = new SupplyAndDemand();
    static SupplyCurve s = new SupplyCurve();
    static DemandCurve d = new DemandCurve();

    JRadioButton supplyButton;
    JRadioButton demandButton;

    JRadioButton shiftButton;
    JRadioButton rotateButton;

    ButtonGroup curves;
    ButtonGroup modes;

    JCheckBox surplusCheckBox;

    public int x = 500;
    public int y = 500;

    public static int equilibriumX;
    public static int equilibriumY;
    public static final int BORDER_OFFSET = 150;
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 1000;

    public SupplyAndDemand() {
        frame = new JFrame("Supply and Demand Demonstration");
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setVisible(true);

        supplyButton = new JRadioButton();
        supplyButton.setText("Supply");

        demandButton = new JRadioButton();
        demandButton.setText("Demand");

        shiftButton = new JRadioButton();
        shiftButton.setText("Shift");

        rotateButton = new JRadioButton();
        rotateButton.setText("Rotate");

        curves = new ButtonGroup();
        curves.add(supplyButton);
        curves.add(demandButton);

        this.add(supplyButton);
        this.add(demandButton);

        modes = new ButtonGroup();
        modes.add(shiftButton);
        modes.add(rotateButton);

        this.add(shiftButton);
        this.add(rotateButton);

        surplusCheckBox = new JCheckBox("Surplus");
        this.add(surplusCheckBox);

        frame.add(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void drawAxes(Graphics g) {
        g.drawLine(BORDER_OFFSET, BORDER_OFFSET, BORDER_OFFSET, WINDOW_HEIGHT - BORDER_OFFSET);
        g.drawLine(BORDER_OFFSET, WINDOW_HEIGHT - BORDER_OFFSET, WINDOW_WIDTH - BORDER_OFFSET, WINDOW_HEIGHT - BORDER_OFFSET);

        g.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        g.drawString("Price", 10, WINDOW_HEIGHT / 2);
        g.drawString("Quantity", WINDOW_WIDTH / 2, WINDOW_HEIGHT - 25);
    }

    public void drawCurves(Graphics g) {
        //supply curve
        g.setColor(Color.CYAN);
        g.drawLine(s.getX1(), s.getY1(), s.getX2(), s.getY2());
        g.drawString("S", s.getX2() + 5, s.getY2() + 5);
        //demand curve
        g.setColor(Color.RED);
        g.drawLine(d.getX1(), d.getY1(), d.getX2(), d.getY2());
        g.drawString("D", d.getX2() + 5, d.getY2() + 5);

        g.setColor(Color.BLACK);
    }
    public void calculateEquilibrium() {
        double supplySlope = (double) (WINDOW_HEIGHT - s.getY2() - (WINDOW_HEIGHT - s.getY1())) / (s.getX2() - s.getX1());
        double demandSlope = (double) (WINDOW_HEIGHT - d.getY2() - (WINDOW_HEIGHT - d.getY1())) / (d.getX2() - d.getX1());
        /*double supplySlope = s.getM();
        double demandSlope = d.getM();*/

        double equilibriumX = (supplySlope * s.getX() - s.getY() - demandSlope * d.getX() + d.getY()) / (supplySlope - demandSlope);
        SupplyAndDemand.equilibriumX = (int) equilibriumX;
        // y = m ( x - x1 ) + y1
        double equilibriumY = supplySlope * (equilibriumX - s.getX()) + s.getY();
        SupplyAndDemand.equilibriumY = (int) equilibriumY;
    }
    public void drawEquilibrium(Graphics g) {
        /*double supplySlope = (double) (WINDOW_HEIGHT - s.getY2() - (WINDOW_HEIGHT - s.getY1())) / (s.getX2() - s.getX1());
        double demandSlope = (double) (WINDOW_HEIGHT - d.getY2() - (WINDOW_HEIGHT - d.getY1())) / (d.getX2() - d.getX1());


        double equilibriumX = (supplySlope * s.getX() - s.getY() - demandSlope * d.getX() + d.getY()) / (supplySlope - demandSlope);
        SupplyAndDemand.equilibriumX = (int) equilibriumX;
        // y = m ( x - x1 ) + y1
        double equilibriumY = supplySlope * (equilibriumX - s.getX()) + s.getY();
        SupplyAndDemand.equilibriumY = (int) equilibriumY;*/
        g.setColor(new Color(25, 120, 0));
        if (BORDER_OFFSET <= equilibriumX && equilibriumX <= WINDOW_WIDTH - BORDER_OFFSET &&
            BORDER_OFFSET <= equilibriumY && equilibriumY <= WINDOW_HEIGHT - BORDER_OFFSET) {
            int currentX = BORDER_OFFSET;
            int currentY = BORDER_OFFSET;

            int increment = 10;
            while (currentY <= equilibriumY - increment) {
                int start = currentY + increment / 4;
                int end = currentY + 3 * increment / 4;
                g.drawLine((int) equilibriumX, WINDOW_HEIGHT - start, (int) equilibriumX, WINDOW_HEIGHT - end);
                currentY += increment;
            }
            g.drawString("Q", (int) equilibriumX - 5, WINDOW_HEIGHT - BORDER_OFFSET + 15);
            while (currentX <= equilibriumX - increment) {
                int start = currentX + increment / 4;
                int end = currentX + 3 * increment / 4;
                g.drawLine(start, WINDOW_HEIGHT - (int) equilibriumY, end, WINDOW_HEIGHT - (int) equilibriumY);
                currentX += increment;
            }
            g.drawString("P", BORDER_OFFSET - 15, WINDOW_HEIGHT - (int) equilibriumY + 7);

        }
        System.out.println("(" + equilibriumX + ", " + equilibriumY + ")");
        g.setColor(Color.BLACK);
    }
    public void drawProducerSurplus(Graphics g) {
        //left bound of producer surplus is the end of the leftmost curve
        int leftBound = Math.max(s.getX1(), d.getX1());
        //left corner, left corner up to equilibrium, equilibrium
        g.setColor(new Color(10, 145, 130));
        int[] xValues = new int[] {leftBound, leftBound, equilibriumX};
        int[] yValues = new int[] {WINDOW_HEIGHT - s.getYValue(leftBound), WINDOW_HEIGHT - equilibriumY, WINDOW_HEIGHT - equilibriumY};
        g.drawPolygon(xValues, yValues, 3);

        double lineSlope = -1;
        int currentX = leftBound;
        int currentY = s.getYValue(leftBound);

        double supplySlope = s.getM();
        int increment = 20;

        while (currentX <= equilibriumX || currentY <= equilibriumY) {
            if (currentY <= equilibriumY) { //lines that start from the left of producer surplus

                double intersectionX = (supplySlope * s.getX() - s.getY() - lineSlope * leftBound + currentY) / (supplySlope - lineSlope);
                // y = m ( x - x1 ) + y1
                double intersectionY = supplySlope * (intersectionX - s.getX()) + s.getY();
                g.drawLine(leftBound, WINDOW_HEIGHT - currentY, (int) intersectionX, WINDOW_HEIGHT - (int) intersectionY);
                currentY += increment;
                if (currentY > equilibriumY) { //the x position gets what "hangs over" the y positions
                    currentX += currentY - equilibriumY;
                }
            } else { //lines that start from the top of producer surplus
                double intersectionX = (supplySlope * s.getX() - s.getY() - lineSlope * currentX + equilibriumY) / (supplySlope - lineSlope);
                // y = m ( x - x1 ) + y1
                double intersectionY = supplySlope * (intersectionX - s.getX()) + s.getY();
                g.drawLine(currentX, WINDOW_HEIGHT - equilibriumY, (int) intersectionX, WINDOW_HEIGHT - (int) intersectionY);
                currentX += increment;
            }
        }
        if (s.getM() > 0) {
            //drawing the "PS" label, located in the centroid of the triangle
            g.setColor(new Color(10, 95, 75));
            //using the middles of leftmost and topmost borders
            //first (left border) line
            int lx = leftBound;
            int ly = (equilibriumY + s.getYValue(leftBound)) / 2;
            double lm = (double) (equilibriumY - ly) / (equilibriumX - lx);
            //second (top border) line
            int tx = (leftBound + equilibriumX) / 2;
            int ty = equilibriumY;
            double tm = (double) (s.getYValue(leftBound) - ty) / (leftBound - tx);

            double intersectionX = (lm * lx - ly - tm * tx + ty) / (lm - tm);
            // y = m ( x - x1 ) + y1
            double intersectionY = lm * (intersectionX - lx) + ly;
            g.drawString("PS", (int) intersectionX - 8, WINDOW_HEIGHT - ((int) intersectionY - 5));

        }
        g.setColor(Color.BLACK);
    }

    public void drawConsumerSurplus(Graphics g) {
        //left bound of consumer surplus is the end of the leftmost curve
        int leftBound = Math.max(s.getX1(), d.getX1());
        //left corner, left corner down to equilibrium, equilibrium
        g.setColor(new Color(210, 80, 0));
        int[] xValues = new int[] {leftBound, leftBound, equilibriumX};
        int[] yValues = new int[] {WINDOW_HEIGHT - d.getYValue(leftBound), WINDOW_HEIGHT - equilibriumY, WINDOW_HEIGHT - equilibriumY};
        g.drawPolygon(xValues, yValues, 3);

        double lineSlope = 1;
        int currentX = leftBound;
        int currentY = d.getYValue(leftBound);

        double demandSlope = d.getM();
        int increment = 20;

        while (currentX <= equilibriumX || currentY >= equilibriumY) {
            if (currentY >= equilibriumY) { //lines that start from the left of consumer surplus

                double intersectionX = (lineSlope * leftBound - currentY - demandSlope * d.getX() + d.getY()) / (lineSlope - demandSlope);
                // y = m ( x - x1 ) + y1
                double intersectionY = demandSlope * (intersectionX - d.getX()) + d.getY();
                g.drawLine(leftBound, WINDOW_HEIGHT - currentY, (int) intersectionX, WINDOW_HEIGHT - (int) intersectionY);
                currentY -= increment;
                if (currentY < equilibriumY) { //the x position gets what "hangs over" the y positions
                    currentX += equilibriumY - currentY;
                }
            } else { //lines that start from the bottom of consumer surplus
                double intersectionX = (lineSlope * currentX - equilibriumY - demandSlope * d.getX() + d.getY()) / (lineSlope - demandSlope);
                // y = m ( x - x1 ) + y1
                double intersectionY = demandSlope * (intersectionX - d.getX()) + d.getY();
                g.drawLine(currentX, WINDOW_HEIGHT - equilibriumY, (int) intersectionX, WINDOW_HEIGHT - (int) intersectionY);
                currentX += increment;
            }
        }
        if (d.getM() < 0) {
            //drawing the "CS" label, located in the centroid of the triangle
            g.setColor(new Color(155, 65, 0));
            //using the middles of leftmost and bottommost borders
            //first (left border) line
            int lx = leftBound;
            int ly = (equilibriumY + d.getYValue(leftBound)) / 2;
            double lm = (double) (equilibriumY - ly) / (equilibriumX - lx);
            //second (bottom border) line
            int bx = (leftBound + equilibriumX) / 2;
            int by = equilibriumY;
            double bm = (double) (d.getYValue(leftBound) - by) / (leftBound - bx);

            double intersectionX = (lm * lx - ly - bm * bx + by) / (lm - bm);
            // y = m ( x - x1 ) + y1
            double intersectionY = lm * (intersectionX - lx) + ly;
            g.drawString("CS", (int) intersectionX - 8, WINDOW_HEIGHT - ((int) intersectionY - 5));
        }
        g.setColor(Color.BLACK);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAxes(g);
        drawCurves(g);
        //the equilibrium point should only change when there is a shift
        if ((!shiftButton.isSelected() && !rotateButton.isSelected()) || shiftButton.isSelected()) {
            calculateEquilibrium();
        }
        drawEquilibrium(g);
        if (surplusCheckBox.isSelected()) {
            drawProducerSurplus(g);
            drawConsumerSurplus(g);
        }
        repaint();
    }

    public static void main(String[] args) {
        demo.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX() - 8;
        y = e.getY() - 31;
        if (shiftButton.isSelected()) {
            if (supplyButton.isSelected()) {
                s.shiftCurve(x, y);
            } else if (demandButton.isSelected()) {
                d.shiftCurve(x, y);
            }
        } else if (rotateButton.isSelected()) { //TODO: problem with rotate: turning curve in the alternative quadrants until m = 0 gives weird lines
            if (supplyButton.isSelected()) {
                s.rotateCurve(x, y);
            } else if (demandButton.isSelected()) {
                d.rotateCurve(x, y);
            }
        }


        //System.out.println("(" + x + ", " + y + ")");
        //s.setPointsInput(x, y);
        demo.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX() - 8;
        y = e.getY() - 31;
        if (shiftButton.isSelected()) {
            if (supplyButton.isSelected()) {
                s.shiftCurve(x, y);
            } else if (demandButton.isSelected()) {
                d.shiftCurve(x, y);
            }
        } else if (rotateButton.isSelected()) {
            if (supplyButton.isSelected()) {
                s.rotateCurve(x, y);
            } else if (demandButton.isSelected()) {
                d.rotateCurve(x, y);
            }
        }
        demo.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
