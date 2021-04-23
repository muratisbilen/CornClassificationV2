import javax.swing.*;

public class ProjectInfoPanel {
    private JPanel mainPanel;
    private JLabel projectNameLab;
    private JLabel dateLab;
    private JLabel uploadedFileLab;
    private JLabel numberOfSamplesLab;
    private JLabel lab1;
    private JLabel lab2;
    private JLabel lab3;
    private JLabel lab4;
    private JLabel pnLab;
    private JLabel creLab;
    private JLabel ufLab;
    private JLabel snLab;

    public ProjectInfoPanel(){

    }
    public ProjectInfoPanel(String projectNameLab, String dateLab, String uploadedFileLab, int numberOfSamplesLab) {
        this.projectNameLab.setText(projectNameLab);
        this.dateLab.setText(dateLab);
        this.uploadedFileLab.setText(uploadedFileLab);
        this.numberOfSamplesLab.setText(""+numberOfSamplesLab);
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

    public void setProjectNameLab(String projectNameLab) {
        this.projectNameLab.setText(projectNameLab);
    }

    public JLabel getDateLab() {
        return dateLab;
    }

    public void setDateLab(String dateLab) {
        this.dateLab.setText(dateLab);
    }

    public JLabel getUploadedFileLab() {
        return uploadedFileLab;
    }

    public void setUploadedFileLab(String uploadedFileLab) {
        this.uploadedFileLab.setText(uploadedFileLab);
    }

    public JLabel getNumberOfSamplesLab() {
        return numberOfSamplesLab;
    }

    public void setNumberOfSamplesLab(String numberOfSamplesLab) {
        this.numberOfSamplesLab.setText(numberOfSamplesLab);
    }

    public void setValues(String projectNameLab, String dateLab, String uploadedFileLab, int numberOfSamplesLab) {
        this.projectNameLab.setText(projectNameLab);
        this.dateLab.setText(dateLab);
        this.uploadedFileLab.setText(uploadedFileLab);
        this.numberOfSamplesLab.setText(""+numberOfSamplesLab);
    }
}
