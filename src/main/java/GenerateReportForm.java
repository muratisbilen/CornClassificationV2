import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class GenerateReportForm {
    private JPanel mainPanel;
    private JPanel mainSouthPanel;
    private JButton cancelBut;
    private JButton generateBut;
    private JPanel mainNorthPanel;
    private JCheckBox selectUnselectAllSamplesCheckBox;
    private JScrollPane centerSP;
    private JPanel centerPanel;
    private ArrayList<MaizeCheckBox> samples = new ArrayList<>();
    private JDialog d = new JDialog(new JFrame(),true);

    public GenerateReportForm(){
        this.generateBut.setEnabled(false);
        this.d.add(this.mainPanel);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.setSize(300,600);
        d.setLocationRelativeTo(null);
        initComponents();
    }

    public GenerateReportForm(JList samples){
        this.samples = new ArrayList<>();
        ListModel dlm = samples.getModel();

        for(int i=0;i<dlm.getSize();i++){
            MaizeCheckBox mcb = new MaizeCheckBox((Maize)dlm.getElementAt(i));
            mcb.setSelected(true);
            this.samples.add(mcb);
        }
        putSamples(this.samples);
        if(dlm.getSize()==0){
            this.generateBut.setEnabled(false);
        }
        this.d.add(this.mainPanel);
        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        d.setSize(300,600);
        d.setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents(){
        this.selectUnselectAllSamplesCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                JCheckBox m = (JCheckBox) e.getItem();
                if(m.isSelected()){
                    for(int i=0;i<samples.size();i++){
                        samples.get(i).setSelected(true);
                    }
                }else{
                    for(int i=0;i<samples.size();i++){
                        samples.get(i).setSelected(false);
                    }
                }
            }
        });

        this.cancelBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                d.dispose();
            }
        });
    }

    public void putSamples(ArrayList<MaizeCheckBox> samples){
        centerPanel.removeAll();
        centerPanel.setLayout(new GridLayout(0,1));
        for(MaizeCheckBox m : samples){
            centerPanel.add(m);
        }
    }

    public JDialog getD() {
        return d;
    }

    public void setD(JDialog d) {
        this.d = d;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getMainSouthPanel() {
        return mainSouthPanel;
    }

    public void setMainSouthPanel(JPanel mainSouthPanel) {
        this.mainSouthPanel = mainSouthPanel;
    }

    public JButton getCancelBut() {
        return cancelBut;
    }

    public void setCancelBut(JButton cancelBut) {
        this.cancelBut = cancelBut;
    }

    public JButton getGenerateBut() {
        return generateBut;
    }

    public void setGenerateBut(JButton generateBut) {
        this.generateBut = generateBut;
    }

    public JPanel getMainNorthPanel() {
        return mainNorthPanel;
    }

    public void setMainNorthPanel(JPanel mainNorthPanel) {
        this.mainNorthPanel = mainNorthPanel;
    }

    public JCheckBox getSelectUnselectAllSamplesCheckBox() {
        return selectUnselectAllSamplesCheckBox;
    }

    public void setSelectUnselectAllSamplesCheckBox(JCheckBox selectUnselectAllSamplesCheckBox) {
        this.selectUnselectAllSamplesCheckBox = selectUnselectAllSamplesCheckBox;
    }

    public JScrollPane getCenterSP() {
        return centerSP;
    }

    public void setCenterSP(JScrollPane centerSP) {
        this.centerSP = centerSP;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel) {
        this.centerPanel = centerPanel;
    }

    public ArrayList<MaizeCheckBox> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<Maize> samples) {
        this.samples = new ArrayList<>();
        for(Maize m : samples){
            this.samples.add(new MaizeCheckBox(m));
        }
    }
}
