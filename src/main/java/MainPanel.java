import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
    private JScrollPane mainPanelSP;
    private JPanel southPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel projectButtonPanel;
    private JPanel centerPanel2;
    private JPanel centerPanel1;
    private JTextField logTF;
    private JPanel buttonPanel;
    private OpenProject op = new OpenProject();
    private CreateProject cp = new CreateProject();
    private JFrame openProjectJF;
    private ResultForm rf;
    private JDialog dia = new JDialog(new JFrame(),true);
    private JTextPane tp = new JTextPane();
    private JProgressBar pb = new JProgressBar();
    private ProgressFrame pbfr = new ProgressFrame();


    public MainPanel() {
        initComponents();
    }

    public void initComponents(){
        nextButton.setEnabled(false);
        backButton.setEnabled(false);
        openProjectBut.setFocusPainted(false);
        createProjectBut.setFocusPainted(false);
        tp.setEditable(false);
        cp.getCreateBut().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f1 = new File(cp.getRawDataFileTF().getText());
                if(!f1.exists()){
                    cp.getCreateBut().setEnabled(false);
                    JOptionPane.showMessageDialog(null,"The sample file you specified does not exist. Please specify an existing file with its absolute path.");
                }else{
                    try {
                        SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
                            @Override
                            protected Boolean doInBackground() throws Exception {
                                publish(0);
                                cp.getFr().dispose();

                                tp.setText("Lütfen bekleyiniz...");

                                tp.setText(tp.getText()+"\n"+f1.getName()+" dosyası analiz ediliyor");

                                Class.forName("org.sqlite.JDBC");
                                Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");

                                tp.setText(tp.getText()+"\n"+"Veritabanına başarılı bir şekilde bağlanıldı.");

                                ArrayList<Maize> samples = CreateProject.getGenotypes(cp.getRawDataFileTF().getText());
                                tp.setText(tp.getText()+"\n"+samples.size()+" adet örnek tespit edildi.");
                                String biomarkerFile = "Biomarkers.txt";
                                LinkedHashMap<String, ArrayList<String>> biomarkers = Analysis.getBiomarkers(biomarkerFile);
                                tp.setText(tp.getText()+"\n"+"Biyobelirteçler okundu. Toplamda "+biomarkers.size()+" adet biyobelirteç tespit edildi.");

                                tp.setText(tp.getText()+"\n"+"Örnekler analiz ediliyor");
                                String tx = tp.getText();

                                int count = 1;
                                for(Maize m : samples){
                                    Analysis.analyze(m,biomarkers);
                                    tp.setText(tx+"\n"+(count++)+"/"+samples.size()+" adet örnek analiz edildi.");
                                }

                                Project p = new Project(f1.getName(),cp.getProjectNameTF().getText(),samples);

                                Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
                                Type doubleDataListType = new TypeToken<Double>() {}.getType();
                                String pgson = gson.toJson(p);

                                long serialized_id = SerializeToDatabase.serializeJavaObjectToDB(c,pgson,p);
                                tp.setText(tp.getText()+"\n"+"Proje veritabanına başarılı bir şekilde kaydedildi.");

                                rf = new ResultForm(p);

                                DefaultListModel dlm = new DefaultListModel();
                                for(int i=0;i<samples.size();i++){
                                    dlm.addElement(samples.get(i));
                                }
                                rf.getSampleList().setModel(dlm);

                                c.close();

                                centerPanel1.removeAll();
                                centerPanel1.add(rf.getMainPanel(), BorderLayout.CENTER);
                                centerPanel1.updateUI();

                                return(true);
                            }

                            protected void process(List<Integer> i){
                                System.out.println("Progress Started");
                                showUpProgressBar();
                            }

                            protected void done(){
                                tp.setText(tp.getText()+"\n"+"Veritabanı başarılı bir kapatıldı ve analiz tamamlandı.");
                                try{
                                    boolean a = (boolean)get();

                                    disposeProgressBar();
                                    nextButton.setEnabled(true);
                                    backButton.setEnabled(true);

                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
                            }
                        };
                        worker.addPropertyChangeListener(new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent evt) {
                                SwingWorker task = (SwingWorker) evt.getSource();

                                if ("progress".equals(evt.getPropertyName())) {
                                    int progress = task.getProgress();
                                }

                                if(evt.getNewValue() == SwingWorker.StateValue.DONE){
                                    System.out.println("Task done.");
                                    try{
                                        task.get();
                                    }catch(Exception ex){

                                    }
                                }
                            }
                        });
                        worker.execute();
                    }catch(Exception ex){
                        System.out.println("Problem in serialization, saving to database or reading the raw data file.");
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.logTF.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JDialog d = new JDialog(new JFrame(), true);
                    d.setSize(800, 600);
                    d.setLocationRelativeTo(null);
                    d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JTextPane tp2 = new JTextPane();
                    tp2.setEditable(false);
                    tp2.setText(tp.getText());
                    JScrollPane sp2 = new JScrollPane(tp2);
                    d.add(sp2);
                    d.setVisible(true);
                }
            }
        });

        openProjectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProjectJF = new JFrame("Kayıtlı Proje Aç");
                op.updateProjectList();
                op.getOpenProjectBut().setEnabled(false);
                op.getDeleteProjectBut().setEnabled(false);
                op.getProjectList().clearSelection();
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

        op.getOpenProjectBut().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ///////////////////////////

                try {
                    SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
                        @Override
                        protected Boolean doInBackground() throws Exception {
                            publish(0);

                            Connection c = null;
                            try {
                                Class.forName("org.sqlite.JDBC");
                                c = DriverManager.getConnection("jdbc:sqlite:test.db");

                                Statement st = c.createStatement();
                                ProjectItem pi = (ProjectItem)(op.getProjectList().getSelectedValue());
                                String getdbtable = "Select serialized_object from Projects where serialized_id="+pi.getSerialized_id();

                                ResultSet rs = st.executeQuery(getdbtable);
                                Project p = null;
                                if(rs.next()) {
                                    String pgson = SerializeToDatabase.deSerializeJavaObjectFromDB(c,pi.getSerialized_id());
                                    Gson gson = new Gson();
                                    Type mytype = new TypeToken<Project>(){}.getType();
                                    p = gson.fromJson(pgson, mytype);
                                }

                                if(p!=null){
                                    openProject(p);
                                    openProjectJF.dispose();
                                }else{
                                    JOptionPane.showMessageDialog(centerPanel,"Proje veritabanında bulunamamıştır.");
                                }
                                c.close();
                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                            Thread.sleep(1000);
                            return(true);
                        }

                        protected void process(List<Integer> i){
                            System.out.println("Progress Started");
                            showUpProgressBar("Projeyi yüklüyor. Lütfen Bekleyiniz");
                        }

                        protected void done(){
                            tp.setText(tp.getText()+"\n"+"Veritabanı başarılı bir kapatıldı ve analiz tamamlandı.");
                            try{
                                boolean a = (boolean)get();
                                pbfr.getTimer().stop();
                                pbfr.dispose();
                                backButton.setEnabled(true);
                                nextButton.setEnabled(true);

                            }catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    };
                    worker.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            SwingWorker task = (SwingWorker) evt.getSource();

                            if ("progress".equals(evt.getPropertyName())) {
                                int progress = task.getProgress();
                            }

                            if(evt.getNewValue() == SwingWorker.StateValue.DONE){
                                System.out.println("Task done.");
                                try{
                                    task.get();
                                }catch(Exception ex){

                                }
                            }
                        }
                    });
                    worker.execute();
                }catch(Exception ex){
                    System.out.println("Problem in serialization, retrieving database or the project.");
                    ex.printStackTrace();
                }
            }
        });

        createProjectBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.refresh();
                cp.getFr().setVisible(true);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel1.removeAll();
                centerPanel1.add(centerPanel2, BorderLayout.CENTER);
                centerPanel1.updateUI();
                backButton.setEnabled(false);
                nextButton.setEnabled(false);
            }
        });

        this.nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rf.setGrf(new GenerateReportForm(rf.getSampleList()));
                rf.getGrf().getD().setVisible(true);
            }
        });
    }

    public void showUpProgressBar(){
        this.dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.dia.setUndecorated(true);
        this.dia.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        JPanel pn = new JPanel();
        pn.setLayout(new BorderLayout());
        JScrollPane jsp = new JScrollPane(this.tp);
        pn.add(jsp,BorderLayout.CENTER);
        this.pb.setIndeterminate(true);
        pn.add(this.pb,BorderLayout.SOUTH);
        this.dia.add(pn);
        this.dia.setSize(800,600);
        this.dia.setLocationRelativeTo(null);
        this.dia.setVisible(true);
        this.dia.validate();
    }

    public void openProject(Project p){
        ArrayList<Maize> samples = p.getSamples();

        rf = new ResultForm(p);
        DefaultListModel dlm = new DefaultListModel();
        for(int i=0;i<samples.size();i++){
            dlm.addElement(samples.get(i));
        }
        rf.getSampleList().setModel(dlm);

        centerPanel1.removeAll();
        centerPanel1.add(rf.getMainPanel(), BorderLayout.CENTER);
        centerPanel1.updateUI();
        nextButton.setEnabled(true);
        backButton.setEnabled(true);
    }

    public void showUpProgressBar(String text){
        pbfr = new ProgressFrame(text);
        pbfr.getTimer().start();
        pbfr.setVisible(true);
    }

    public void disposeProgressBar(){
        this.dia.dispose();
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

    public JProgressBar getProgressBar1() {
        return progressBar1;
    }

    public void setProgressBar1(JProgressBar progressBar1) {
        this.progressBar1 = progressBar1;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setBackButton(JButton backButton) {
        this.backButton = backButton;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public void setNextButton(JButton nextButton) {
        this.nextButton = nextButton;
    }

    public JScrollPane getMainPanelSP() {
        return mainPanelSP;
    }

    public void setMainPanelSP(JScrollPane mainPanelSP) {
        this.mainPanelSP = mainPanelSP;
    }

    public JPanel getSouthPanel() {
        return southPanel;
    }

    public void setSouthPanel(JPanel southPanel) {
        this.southPanel = southPanel;
    }

    public JPanel getNorthPanel() {
        return northPanel;
    }

    public void setNorthPanel(JPanel northPanel) {
        this.northPanel = northPanel;
    }

    public JPanel getWestPanel() {
        return westPanel;
    }

    public void setWestPanel(JPanel westPanel) {
        this.westPanel = westPanel;
    }

    public JPanel getEastPanel() {
        return eastPanel;
    }

    public void setEastPanel(JPanel eastPanel) {
        this.eastPanel = eastPanel;
    }

    public JPanel getProjectButtonPanel() {
        return projectButtonPanel;
    }

    public void setProjectButtonPanel(JPanel projectButtonPanel) {
        this.projectButtonPanel = projectButtonPanel;
    }

    public JPanel getCenterPanel2() {
        return centerPanel2;
    }

    public void setCenterPanel2(JPanel centerPanel2) {
        this.centerPanel2 = centerPanel2;
    }

    public JPanel getCenterPanel1() {
        return centerPanel1;
    }

    public void setCenterPanel1(JPanel centerPanel1) {
        this.centerPanel1 = centerPanel1;
    }

    public JTextField getLogTF() {
        return logTF;
    }

    public void setLogTF(JTextField logTF) {
        this.logTF = logTF;
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public OpenProject getOp() {
        return op;
    }

    public void setOp(OpenProject op) {
        this.op = op;
    }

    public CreateProject getCp() {
        return cp;
    }

    public void setCp(CreateProject cp) {
        this.cp = cp;
    }

    public JFrame getOpenProjectJF() {
        return openProjectJF;
    }

    public void setOpenProjectJF(JFrame openProjectJF) {
        this.openProjectJF = openProjectJF;
    }

    public ResultForm getRf() {
        return rf;
    }

    public void setRf(ResultForm rf) {
        this.rf = rf;
    }

    public JDialog getDia() {
        return dia;
    }

    public void setDia(JDialog dia) {
        this.dia = dia;
    }
}
