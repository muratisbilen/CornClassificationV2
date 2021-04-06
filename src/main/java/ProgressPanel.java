import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class ProgressPanel extends JPanel {
    private ArrayList<Double> xs = new ArrayList<>();

    public ProgressPanel(){
        super();
        //setBackground(new Color(0.0f,0.0f,0.0f,0f));
        setOpaque(false);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        double coef = 40;
        double coefx = getWidth();
        double shifty = getHeight()/2.0;
        double shiftx = getWidth()/2.0;

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        double size = 8;

        for(int i=0;i<xs.size();i++){

            Complex c = new Complex(xs.get(i));
            Complex c2 = new Complex(xs.get(i)+Math.PI);

            double x1 = coefx * i / xs.size();
            double y1 = coef * c.getImag() + shifty;
            double z1 = size * (c.getReal() + 2);

            double x2 = coefx * i / xs.size();
            double y2 = coef * c2.getImag() + shifty;
            double z2 = size * (c2.getReal() + 2);


            if(c.getReal()<c2.getReal()) {
                g2.setColor(new Color(0xEAC600));
                Ellipse2D.Double e = new Ellipse2D.Double(x1, y1, z1, z1);
                g2.fill(e);

                g2.setColor(new Color(0x43AF00));
                Ellipse2D.Double e2 = new Ellipse2D.Double(x2, y2, z2, z2);
                g2.fill(e2);

            }else{
                g2.setColor(new Color(0x43AF00));
                Ellipse2D.Double e2 = new Ellipse2D.Double(x2, y2, z2, z2);
                g2.fill(e2);

                g2.setColor(new Color(0xEAC600));
                Ellipse2D.Double e = new Ellipse2D.Double(x1, y1, z1, z1);
                g2.fill(e);
            }
        }
    }

    public ArrayList<Double> getXs() {
        return xs;
    }

    public void setXs(ArrayList<Double> xs) {
        this.xs = xs;
        repaint();
    }

    public void moveXs(double x){
        for(int i=0;i<xs.size();i++){
            xs.set(i,xs.get(i)+x);
        }
        repaint();
    }
}
