import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class CreateProject {
    private JPanel mainPanel;
    private JPanel mainPanel2;
    private JTextField projectNameTF;
    private JTextField rawDataFileTF;
    private JButton uploadRawDataFileBut;
    private JButton cancelBut;
    private JButton createBut;
    private JFrame fr = new JFrame("Proje Oluştur");
    private JProgressBar pb = new JProgressBar();
    private JTextPane tp = new JTextPane();

    public CreateProject(){
        this.fr.add(this.mainPanel);
        this.fr.setSize(800,600);
        this.fr.setLocationRelativeTo(null);
        this.fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.createBut.setEnabled(false);
        this.projectNameTF.getDocument().addDocumentListener(new DocumentListener() {
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

    public void refresh(){
        projectNameTF.setText("");
        rawDataFileTF.setText("");
    }

    public static ArrayList<Maize> getGenotypes(String filename){
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

    public JProgressBar getPb() {
        return pb;
    }

    public void setPb(JProgressBar pb) {
        this.pb = pb;
    }

    public JTextPane getTp() {
        return tp;
    }

    public void setTp(JTextPane tp) {
        this.tp = tp;
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
