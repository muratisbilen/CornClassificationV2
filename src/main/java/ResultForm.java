import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.commons.compress.utils.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

public class ResultForm {
    private JPanel mainPanel;
    private JList sampleList;
    private JPanel westPanel;
    private JScrollPane listSP;
    private JPanel centerPanel;
    private JPanel tablePanel;
    private JPanel graphPanel;
    private JScrollPane tableSP;
    private JTable resultTable;
    private JPanel exportPanel;
    private JButton exportBut;
    private GraphPanel gp = new GraphPanel();
    private GenerateReportForm grf = new GenerateReportForm();
    private InputStream candara;
    private InputStream candarabold;
    private Project p;

    public ResultForm(Project p){
        this.p = p;
        initComponents();
    }

    public void initComponents(){
        MyListCellRenderer cr = new MyListCellRenderer();
        this.sampleList.setCellRenderer(cr);
        JScrollPane sp = new JScrollPane(this.gp);
        this.graphPanel.add(sp);
        exportBut.setEnabled(false);

        this.sampleList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(sampleList.getSelectedIndex()>-1 && e.getClickCount()==1){
                    Maize m = (Maize)sampleList.getSelectedValue();
                    gp.setM(m);
                    DefaultTableModel dtm = new DefaultTableModel(new String[]{"Probeset","Genotip"},0){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    LinkedHashMap geno = m.getGenotype();
                    ArrayList<String> keys = new ArrayList<>(geno.keySet());
                    for(int i=0;i<keys.size();i++){
                        String key = keys.get(i);
                        dtm.addRow(new String[]{key,(String)geno.get(key)});
                    }
                    resultTable.setModel(dtm);
                    resultTable.updateUI();
                    exportBut.setEnabled(true);
                    LineBorder linedBorder = new LineBorder(Color.darkGray);
                    TitledBorder titledBorder = BorderFactory.createTitledBorder(linedBorder, m.getName());
                    tablePanel.setBorder(titledBorder);
                }
            }
        });

        exportBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                Maize m = (Maize)sampleList.getSelectedValue();
                jfc.setSelectedFile(new File(m.getName()+"_Genotip_Verisi.txt"));
                int approve = jfc.showOpenDialog(null);

                if(approve == JFileChooser.APPROVE_OPTION){
                    File out = jfc.getSelectedFile();

                    int overwrite = JOptionPane.YES_OPTION;
                    if(out.exists()){
                        overwrite = JOptionPane.showConfirmDialog(mainPanel,"Bu dosya ismi ile bir dosya mevcut. Üzerine yazmak istiyor musunuz?","Dosya İsmi Mevcut",JOptionPane.YES_NO_OPTION);
                    }
                    if(overwrite == JOptionPane.YES_OPTION) {
                        BufferedWriter bwr = null;
                        try {
                            bwr = new BufferedWriter(new FileWriter(jfc.getSelectedFile()));
                            TableModel dtm = resultTable.getModel();
                            bwr.write("Probeset\tGenotip\n");
                            for (int i = 0; i < dtm.getRowCount(); i++) {
                                bwr.write((String) dtm.getValueAt(i, 0) + "\t" + (String) dtm.getValueAt(i, 1)+"\n");
                            }
                            bwr.close();
                            JOptionPane.showMessageDialog(mainPanel,"Genotip verisi kaydedildi.","Veri Kaydedildi",JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        });

        addALtoGenerateBut();

        try {
            this.candara = this.getClass().getResourceAsStream("Candara.ttf");
            this.candarabold = this.getClass().getResourceAsStream("Candara_Bold.ttf");
        }catch(Exception ex){
            System.out.println("Candara");
            ex.printStackTrace();
        }
    }

    public void addALtoGenerateBut(){
        grf.getGenerateBut().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    generateReport();
                    grf.getD().dispose();
                    JOptionPane.showMessageDialog(mainPanel,"Rapor kaydedildi.","",JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ex){
                    System.out.println("allALtoGenerateBut");
                    ex.printStackTrace();
                }
            }
        });
    }

    public void generateReport() throws Exception{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy:MM:dd:HH:mm:ss");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String date2 = dtf2.format(now);
        String[] dates = date.split(":");

        String reportID = dates[0]+dates[1]+dates[2]+dates[3]+dates[4]+dates[5];
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setSelectedFile(new File(reportID+".pdf"));
        int approve = jfc.showOpenDialog(null);

        if(approve == JFileChooser.APPROVE_OPTION) {
            File out = jfc.getSelectedFile();

            int overwrite = JOptionPane.YES_OPTION;
            if(out.exists()){
                overwrite = JOptionPane.showConfirmDialog(mainPanel,"Bu dosya ismi ile bir dosya mevcut. Üzerine yazmak istiyor musunuz?","Dosya İsmi Mevcut",JOptionPane.YES_NO_OPTION);
            }
            if(overwrite == JOptionPane.YES_OPTION) {
                com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(out));
                document.open();
                PdfContentByte cb = writer.getDirectContent();
                byte[] bytes = IOUtils.toByteArray(candara);
                BaseFont CANDARA_REGULAR = BaseFont.createFont("Candara.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes, null);

                // Load existing PDF

                PdfReader reader = new PdfReader(getClass().getResource("May Report.pdf"));
                PdfImportedPage page;
                page = writer.getImportedPage(reader, 1);
                document.newPage();
                cb.addTemplate(page, 0, 0);

                cb.beginText();
                cb.setFontAndSize(CANDARA_REGULAR, 10);
                cb.setColorFill(new Color(0, 0, 0));
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, reportID, 492f, 791f, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, date2, 492f, 777f, 0);
                cb.setFontAndSize(CANDARA_REGULAR, 8);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mısır Sınıflandırma Aracı v1.0", 117f, 681.5f, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, this.p.getFilename(), 117f, 666.5f, 0);
                cb.endText();
                float yinit = 490f;
                float yshift = 140f;
                int inc = 0;
                int pagecount = 2;
                int samplecount = 0;
                for (int i = 0; i < this.grf.getSamples().size(); i++) {
                    if(this.grf.getSamples().get(i).isSelected()) {
                        if (yinit - (inc / 3) * yshift < 65f) {
                            page = writer.getImportedPage(reader, 2);
                            document.newPage();
                            cb.addTemplate(page, 0, 0);

                            cb.beginText();
                            cb.setFontAndSize(CANDARA_REGULAR, 10);
                            cb.setColorFill(new Color(0, 0, 0));
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, reportID, 492f, 791f, 0);
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, date2, 492f, 777f, 0);
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "" + pagecount++, 553f, 43f, 0);
                            cb.endText();

                            yinit = 640f;
                            inc = 0;
                        }
                        pieChart(cb, this.p.getSamples().get(i), 40 + (inc % 3) * 200f, yinit - (inc / 3) * yshift, 20, 100);
                        inc++;
                        samplecount++;
                    }
                }

                cb.beginText();
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, samplecount+"/"+this.p.getSamples().size() + "", 117f, 651.5f, 0);
                cb.endText();

                document.close();
            }
        }
    }

    public void pieChart(PdfContentByte cb, Maize m, float x, float y,float w, float h) throws Exception{
        byte[] bytes = IOUtils.toByteArray(candara);
        BaseFont CANDARA_REGULAR = BaseFont.createFont("Candara.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes,null);

        byte[] bytes2 = IOUtils.toByteArray(candarabold);
        BaseFont CANDARA_BOLD = BaseFont.createFont("Candara_Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes2, null);
        LinkedHashMap<String,Double> res = m.getResult();
        ArrayList<String> keys = new ArrayList<>(res.keySet());
        //cb.setLineWidth(32f);
        double cumsc = 0;
        for(int i=0;i<keys.size();i++){
            double sc = res.get(keys.get(i));
            Color c = Color.getHSBColor(1f * (i + 1) / keys.size(), 1, 1);
            cb.setColorFill(c);
            if(sc>0) {
                cb.rectangle(x,y+(float)(cumsc*h),w,(float)sc*h);

                cb.setColorStroke(Color.black);
                cb.fillStroke();
                cumsc += sc;
            }

            float dist = h*((10f-keys.size())/(10f*keys.size()+10));
            float rsize = h/10f;

            cb.rectangle(x+1.25f*w, y+dist+i*(dist+rsize),w/2,rsize);
            cb.fillStroke();
            cb.setColorFill(Color.black);

            cb.beginText();
            cb.setFontAndSize(CANDARA_REGULAR, 8);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, keys.get(i)+": %"+(Math.round(1000*sc)/10.0), x+2*w, y+dist+i*(dist+rsize)+rsize/3f, 0);
            cb.endText();
        }
        cb.setFontAndSize(CANDARA_BOLD, 10);
        cb.beginText();
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,m.getName(),x,y+1.1f*h,0);
        cb.endText();

        cb.setFontAndSize(CANDARA_BOLD, 7);
        cb.beginText();
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,0+"",x-0.2f*w,y,0);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,50+"",x-0.2f*w,y+h/2,0);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,100+"",x-0.2f*w,y+h,0);
        cb.endText();

        cb.moveTo(x,y);
        cb.lineTo(x-0.1f*w,y);
        cb.moveTo(x,y+h/2);
        cb.lineTo(x-0.1f*w,y+h/2);
        cb.moveTo(x,y+h);
        cb.lineTo(x-0.1f*w,y+h);
        cb.setColorStroke(Color.black);
        cb.stroke();
    }

    public GenerateReportForm getGrf() {
        return grf;
    }

    public void setGrf(GenerateReportForm grf) {
        this.grf = grf;
        addALtoGenerateBut();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JList getSampleList() {
        return sampleList;
    }

    public void setSampleList(JList sampleList) {
        this.sampleList = sampleList;
    }

    public JPanel getWestPanel() {
        return westPanel;
    }

    public void setWestPanel(JPanel westPanel) {
        this.westPanel = westPanel;
    }

    public JScrollPane getListSP() {
        return listSP;
    }

    public void setListSP(JScrollPane listSP) {
        this.listSP = listSP;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel) {
        this.centerPanel = centerPanel;
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public void setTablePanel(JPanel tablePanel) {
        this.tablePanel = tablePanel;
    }

    public JPanel getGraphPanel() {
        return graphPanel;
    }

    public void setGraphPanel(JPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    public JScrollPane getTableSP() {
        return tableSP;
    }

    public void setTableSP(JScrollPane tableSP) {
        this.tableSP = tableSP;
    }

    public JTable getResultTable() {
        return resultTable;
    }

    public void setResultTable(JTable resultTable) {
        this.resultTable = resultTable;
    }

    public GraphPanel getGp() {
        return gp;
    }

    public void setGp(GraphPanel gp) {
        this.gp = gp;
    }
}
