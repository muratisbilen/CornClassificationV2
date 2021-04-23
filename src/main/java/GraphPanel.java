import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GraphPanel extends JPanel {
    private Maize m;

    public GraphPanel(){
        this.m = new Maize("");
        initComponents();
    }

    public GraphPanel(String s){
        this.m = new Maize(s);
        initComponents();
    }

    public GraphPanel(Maize m){
        this.m = m;
        initComponents();
    }

    public void initComponents(){
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(750,428));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2.getFontMetrics();

        if(this.m.getResult().size()>0){
            LinkedHashMap<String,Double> res = this.m.getResult();
            ArrayList<String> keys = new ArrayList<>(res.keySet());

            Line2D.Float ly = new Line2D.Float(100,100,100,500);
            Line2D.Float lx = new Line2D.Float(100,500,700,500);

            g2.draw(lx);
            g2.draw(ly);

            for(int i=0;i<=100;i=i+10){
                Line2D.Float lty = new Line2D.Float(95,500-(i*4),100,500-(i*4));
                Line2D.Float ltyi = new Line2D.Float(100,500-(i*4),700,500-(i*4));
                g2.draw(lty);
                g2.setColor(new Color(220,220,220,255));
                g2.draw(ltyi);
                g2.setColor(Color.BLACK);
                g2.drawString(""+i,93-fm.stringWidth(""+i),500-(i*4)+fm.getHeight()/2f);
            }

            for(int i=0;i<keys.size();i++){
                float score = Math.round(1000f*res.get(keys.get(i)))/10f;
                Rectangle2D.Float rect = new Rectangle2D.Float((i+1)*100+25,500-4*score,50,4*score);
                Line2D.Float ltx = new Line2D.Float((i+1)*100+50,500,(i+1)*100+50,505);
                g2.draw(ltx);
                g2.drawString(score+" %",(i+1)*100+50-1f*fm.stringWidth(score+" %")/2f,500-4*score-5);
                g2.drawString(keys.get(i),(i+1)*100+50-1f*fm.stringWidth(keys.get(i))/2f,520);

                Color c = Color.getHSBColor(1f*(i+1)/keys.size(),1,1);
                g2.setColor(c);
                g2.fill(rect);
                g2.setColor(Color.BLACK);
            }
        }
    }

    public Maize getM() {
        return m;
    }

    public void setM(Maize m) {
        this.m = m;
        setSize((m.getResult().size()+2)*100,Math.max(getHeight(),600));
        repaint();
    }
}
