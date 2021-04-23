import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
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
    private JButton cancelButton;
    private JScrollPane projectListSP;
    private JScrollPane mainPanelSP;
    private JPanel projectButPanel;
    private JPanel cancelPanel;
    private ProjectInfoPanel infoContentPanel = new ProjectInfoPanel();

    public OpenProject() {
        initComponents();
    }

    public void initComponents(){
        JScrollPane projectInfoSP = new JScrollPane(infoContentPanel.getMainPanel());
        this.projectInfoPanel.add(projectInfoSP,BorderLayout.CENTER);
        updateProjectList();
        setActions();

        projectList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(projectList.getSelectedIndex()>-1){
                    openProjectBut.setEnabled(true);
                    deleteProjectBut.setEnabled(true);
                }else{
                    openProjectBut.setEnabled(false);
                    deleteProjectBut.setEnabled(false);
                }
                ProjectItem pi = (ProjectItem)projectList.getSelectedValue();
                infoContentPanel.setValues(pi.getProjectName(),pi.getDate(),pi.getFilename(),pi.getSampleNumber());
                infoContentPanel.getMainPanel().updateUI();
            }
        });

        deleteProjectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resp = JOptionPane.showConfirmDialog(mainPanel,((ProjectItem)projectList.getSelectedValue()).getProjectName()+" projesini silmek istiyor musunuz?","Proje Sil",JOptionPane.YES_NO_OPTION);
                if(resp == JOptionPane.YES_OPTION) {
                    Connection c = null;
                    try {
                        Class.forName("org.sqlite.JDBC");
                        c = DriverManager.getConnection("jdbc:sqlite:test.db");
                        Statement st = c.createStatement();
                        ProjectItem pi = ((ProjectItem) projectList.getSelectedValue());
                        String com = "DELETE FROM Projects WHERE serialized_id=" + pi.getSerialized_id();
                        st.executeUpdate(com);
                        ((DefaultListModel) projectList.getModel()).remove(projectList.getSelectedIndex());
                        openProjectBut.setEnabled(false);
                        deleteProjectBut.setEnabled(false);
                        projectList.clearSelection();
                        c.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    public OpenProject(ProjectInfoPanel infoContentPanel) {
        this.infoContentPanel = infoContentPanel;
        JScrollPane projectInfoSP = new JScrollPane(infoContentPanel.getMainPanel());
        projectInfoPanel.add(projectInfoSP, BorderLayout.CENTER);
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
            String getdbtable = "Select serialized_id, object_name, file_name, project_date, number_of_samples from Projects";
            ResultSet rs = st.executeQuery(getdbtable);

            DefaultListModel dlm = new DefaultListModel();

            while (rs.next()) {
                long serialized_id = rs.getLong("serialized_id");
                String object_name = rs.getString("object_name");
                String file_name = rs.getString("file_name");
                String project_date = rs.getString("project_date");
                int number_of_samples = rs.getInt("number_of_samples");

                ProjectItem pi = new ProjectItem(object_name, serialized_id,file_name,project_date,number_of_samples);
                dlm.addElement(pi);
            }
            projectList.setModel(dlm);
            c.close();
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
        projectInfoPanel.removeAll();
        JScrollPane projectInfoSP = new JScrollPane(this.infoContentPanel.getMainPanel());
        projectInfoPanel.add(projectInfoSP,BorderLayout.CENTER);
    }
}
