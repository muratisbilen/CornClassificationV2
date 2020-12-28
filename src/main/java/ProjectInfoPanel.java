import javax.swing.*;

public class ProjectInfoPanel {
    private JPanel mainPanel;
    private JLabel projectNameLab;
    private JLabel dateLab;
    private JLabel uploadedFileLab;
    private JLabel numberOfSamplesLab;

    public ProjectInfoPanel(){

    }
    public ProjectInfoPanel(JLabel projectNameLab, JLabel dateLab, JLabel uploadedFileLab, JLabel numberOfSamplesLab) {
        this.projectNameLab = projectNameLab;
        this.dateLab = dateLab;
        this.uploadedFileLab = uploadedFileLab;
        this.numberOfSamplesLab = numberOfSamplesLab;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JLabel getProjectNameLab() {
        return projectNameLab;
    }

    public void setProjectNameLab(JLabel projectNameLab) {
        this.projectNameLab = projectNameLab;
    }

    public JLabel getDateLab() {
        return dateLab;
    }

    public void setDateLab(JLabel dateLab) {
        this.dateLab = dateLab;
    }

    public JLabel getUploadedFileLab() {
        return uploadedFileLab;
    }

    public void setUploadedFileLab(JLabel uploadedFileLab) {
        this.uploadedFileLab = uploadedFileLab;
    }

    public JLabel getNumberOfSamplesLab() {
        return numberOfSamplesLab;
    }

    public void setNumberOfSamplesLab(JLabel numberOfSamplesLab) {
        this.numberOfSamplesLab = numberOfSamplesLab;
    }
}
