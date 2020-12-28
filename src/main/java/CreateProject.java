import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class CreateProject {
    private JPanel mainPanel;
    private JPanel mainPanel2;
    private JTextField projectNameTF;
    private JTextField rawDataFileTF;
    private JButton uploadRawDataFileBut;
    private JButton cancelBut;
    private JButton createBut;
    private JFrame fr = new JFrame("Proje Oluştur");

    public CreateProject(){
        fr.add(mainPanel);
        fr.setSize(800,600);
        fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createBut.setEnabled(false);
        projectNameTF.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(rawDataFileTF.getText().equals("") || projectNameTF.getText().equals("")){
                    createBut.setEnabled(false);
                }else{
                    createBut.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(rawDataFileTF.getText().equals("") || projectNameTF.getText().equals("")){
                    createBut.setEnabled(false);
                }else{
                    createBut.setEnabled(true);
                }

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(rawDataFileTF.getText().equals("") || projectNameTF.getText().equals("")){
                    createBut.setEnabled(false);
                }else{
                    createBut.setEnabled(true);
                }
            }
        });

        rawDataFileTF.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(rawDataFileTF.getText().equals("") || projectNameTF.getText().equals("")){
                    createBut.setEnabled(false);
                }else{
                    createBut.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(rawDataFileTF.getText().equals("") || projectNameTF.getText().equals("")){
                    createBut.setEnabled(false);
                }else{
                    createBut.setEnabled(true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if(rawDataFileTF.getText().equals("") || projectNameTF.getText().equals("")){
                    createBut.setEnabled(false);
                }else{
                    createBut.setEnabled(true);
                }
            }
        });

        createBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f1 = new File(rawDataFileTF.getText());
                if(!f1.exists()){
                    createBut.setEnabled(false);
                    JOptionPane.showMessageDialog(null,"The sample file you specified does not exist. Please specify an existing file with its absolute path.");
                }else{

                    try {
                        Class.forName("org.sqlite.JDBC");
                        Connection c = DriverManager.getConnection("jdbc:sqlite:test.db");

                        ArrayList<Maize> samples = getGenotypes(rawDataFileTF.getText());
                        String biomarkerFile = "C:\\Users\\PC7\\Desktop\\Personal_Work\\Hizmetler\\May_Tohumculuk\\Corn_Heterotic_Groups\\Biomarkers.txt";
                        LinkedHashMap<String, ArrayList<String>> biomarkers = Analysis.getBiomarkers(biomarkerFile);

                        for(Maize m : samples){
                            Analysis.analyze(m,biomarkers);
                        }

                        Project p = new Project(projectNameTF.getText(),samples);

                        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
                        Type doubleDataListType = new TypeToken<Double>() {}.getType();
                        String pgson = gson.toJson(p);

                        long serialized_id = SerializeToDatabase.serializeJavaObjectToDB(c,pgson,p);

                        c.close();
                        fr.dispose();
                    }catch(Exception ex){
                        System.out.println("Problem in serialization, saving to database or reading the raw data file.");
                        ex.printStackTrace();
                    }
                }
            }
        });
        cancelBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.dispose();
            }
        });
        uploadRawDataFileBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File("src/main/resources"));
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnVal = fc.showOpenDialog(getMainPanel());

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    rawDataFileTF.setText(file.getPath());
                }
            }
        });
    }

    public ArrayList<Maize> getGenotypes(String filename){
        ArrayList<Maize> samples = new ArrayList<>();
        try{
            Workbook wb = WorkbookFactory.create(new File(filename));
            Sheet sheet = wb.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            if(rowIterator.hasNext()){
                Row row = rowIterator.next(); // Header Row
                Iterator<Cell> cellIterator = row.cellIterator();

                int i;
                for(i=0;(i<3 && cellIterator.hasNext());i++){
                    Cell cell = cellIterator.next(); // Headers 'Name', 'Chr' and 'Position'
                }

                if(i<3){
                    System.out.println("Input file you specified is not appropriate!");
                }

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    samples.add(new Maize(cell.getStringCellValue()));
                }
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next(); // Probe set Rows
                Iterator<Cell> cellIterator = row.cellIterator();

                String ps = cellIterator.next().getStringCellValue(); // Probeset Name
                DataFormatter formatter = new DataFormatter();
                String chr = formatter.formatCellValue(cellIterator.next()); // Chromosome
                String pos = formatter.formatCellValue(cellIterator.next()); // Position

                int s = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = cell.getStringCellValue();
                    Maize m = samples.get(s++);
                    m.getGenotype().put(ps,cellValue);
                }
                wb.close();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return samples;
    }

    public JFrame getFr() {
        return fr;
    }

    public void setFr(JFrame fr) {
        this.fr = fr;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JPanel getMainPanel2() {
        return mainPanel2;
    }

    public void setMainPanel2(JPanel mainPanel2) {
        this.mainPanel2 = mainPanel2;
    }

    public JTextField getProjectNameTF() {
        return projectNameTF;
    }

    public void setProjectNameTF(JTextField projectNameTF) {
        this.projectNameTF = projectNameTF;
    }

    public JTextField getRawDataFileTF() {
        return rawDataFileTF;
    }

    public void setRawDataFileTF(JTextField rawDataFileTF) {
        this.rawDataFileTF = rawDataFileTF;
    }

    public JButton getUploadRawDataFileBut() {
        return uploadRawDataFileBut;
    }

    public void setUploadRawDataFileBut(JButton uploadRawDataFileBut) {
        this.uploadRawDataFileBut = uploadRawDataFileBut;
    }

    public JButton getCancelBut() {
        return cancelBut;
    }

    public void setCancelBut(JButton cancelBut) {
        this.cancelBut = cancelBut;
    }

    public JButton getCreateBut() {
        return createBut;
    }

    public void setCreateBut(JButton createBut) {
        this.createBut = createBut;
    }
}
