import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class OpenProject {
    private JPanel mainPanel;
    private JButton openProjectBut;
    private JButton deleteProjectBut;
    private JList projectList;
    private JPanel mainPanel2;
    private JPanel savedProjects;
    private JPanel projectInfoPanel;
    private JScrollPane projectInfoSP;
    private JButton cancelButton;
    private ProjectInfoPanel infoContentPanel = new ProjectInfoPanel();

    public OpenProject() {
        projectInfoSP.add(infoContentPanel.getMainPanel());
        updateProjectList();
        setActions();

    }
    public OpenProject(ProjectInfoPanel infoContentPanel) {
        this.infoContentPanel = infoContentPanel;
        projectInfoSP.add(infoContentPanel.getMainPanel());
        updateProjectList();
        setActions();
    }

    public void setActions(){
        openProjectBut.setEnabled(false);
        deleteProjectBut.setEnabled(false);
    }

    public void updateProjectList(){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");

            Statement st = c.createStatement();
            String getdbtable = "Select serialized_id, object_name from Projects";
            ResultSet rs = st.executeQuery(getdbtable);

            DefaultListModel dlm = new DefaultListModel();

            while (rs.next()) {
                long serialized_id = rs.getLong("serialized_id");
                String object_name = rs.getString("object_name");

                ProjectItem pi = new ProjectItem(object_name, serialized_id);
                dlm.addElement(pi);
            }
            projectList.setModel(dlm);

        }catch(Exception ex){
            System.out.println("Projeler veritabanından çekilirken hata oluştu.");
            ex.printStackTrace();
        }
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
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

    public JButton getDeleteProjectBut() {
        return deleteProjectBut;
    }

    public void setDeleteProjectBut(JButton deleteProjectBut) {
        this.deleteProjectBut = deleteProjectBut;
    }

    public JList getProjectList() {
        return projectList;
    }

    public void setProjectList(JList projectList) {
        this.projectList = projectList;
    }

    public JPanel getMainPanel2() {
        return mainPanel2;
    }

    public void setMainPanel2(JPanel mainPanel2) {
        this.mainPanel2 = mainPanel2;
    }

    public JPanel getSavedProjects() {
        return savedProjects;
    }

    public void setSavedProjects(JPanel savedProjects) {
        this.savedProjects = savedProjects;
    }

    public JPanel getProjectInfoPanel() {
        return projectInfoPanel;
    }

    public void setProjectInfoPanel(JPanel projectInfoPanel) {
        this.projectInfoPanel = projectInfoPanel;
    }

    public ProjectInfoPanel getInfoContentPanel() {
        return infoContentPanel;
    }

    public void setInfoContentPanel(ProjectInfoPanel infoContentPanel) {
        this.infoContentPanel = infoContentPanel;
        projectInfoSP.removeAll();
        projectInfoSP.add(this.infoContentPanel.getMainPanel());
    }
}
