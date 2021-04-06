import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProgressFrame extends JDialog {
    ProgressPanel pn = new ProgressPanel();
    JLabel lab = new JLabel();
    Timer timer;
    public ProgressFrame(){
        super(new JFrame(),true);
        initComponents();
    }

    public ProgressFrame(String s){
        super(new JFrame(),true);
        lab.setText(s);
        initComponents();
    }

    public void initComponents(){
        setLayout(new BorderLayout());
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600,200);
        super.setLocationRelativeTo(null);
        setBackground(new Color(0f,0f,0f,0.0f));
        ArrayList<Double> x = new ArrayList<>();
        for(double i = 0.0;i<5*Math.PI;i=i+1.0){
            x.add(i);
        }
        lab.setFont(new Font("SansSerif", Font.BOLD, 24));
        lab.setForeground(Color.BLACK);
        lab.setHorizontalAlignment(JLabel.CENTER);
        pn.setXs(x);
        add(pn,BorderLayout.CENTER);
        add(lab,BorderLayout.SOUTH);

        this.timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pn.moveXs(0.1);
            }
        });
    }

    public ProgressPanel getPn() {
        return pn;
    }

    public void setPn(ProgressPanel pn) {
        this.pn = pn;
    }

    public JLabel getLab() {
        return lab;
    }

    public void setLab(JLabel lab) {
        this.lab = lab;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
