import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {
    private JPanel mainPanel;
    private JButton openProjectBut;
    private JButton createProjectBut;
    private JPanel mainPanel2;
    private JPanel centerPanel;
    private JLabel logoLabel;
    private JLabel dnafectLogo;
    private JProgressBar progressBar1;
    private JButton backButton;
    private JButton nextButton;
    private OpenProject op = new OpenProject();
    private CreateProject cp = new CreateProject();
    JFrame openProjectJF;

    public MainPanel() {
        openProjectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProjectJF = new JFrame("Kayıtlı Proje Aç");
                op.updateProjectList();
                openProjectJF.add(op.getMainPanel());
                openProjectJF.setSize(800,600);
                openProjectJF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                openProjectJF.setVisible(true);
            }
        });

        op.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProjectJF.dispose();
            }
        });
        createProjectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.getFr().setVisible(true);
            }
        });
    }

    public JLabel getDnafectLogo() {
        return dnafectLogo;
    }

    public void setDnafectLogo(JLabel dnafectLogo) {
        this.dnafectLogo = dnafectLogo;
    }

    public JLabel getLogoLabel() {
        return logoLabel;
    }

    public void setLogoLabel(JLabel logoLabel) {
        this.logoLabel = logoLabel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JButton getOpenProjectBut() {
        return openProjectBut;
    }

    public void setOpenProjectBut(JButton openProjectBut) {
        this.openProjectBut = openProjectBut;
    }

    public JButton getCreateProjectBut() {
        return createProjectBut;
    }

    public void setCreateProjectBut(JButton createProjectBut) {
        this.createProjectBut = createProjectBut;
    }

    public JPanel getMainPanel2() {
        return mainPanel2;
    }

    public void setMainPanel2(JPanel mainPanel2) {
        this.mainPanel2 = mainPanel2;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel) {
        this.centerPanel = centerPanel;
    }
}
