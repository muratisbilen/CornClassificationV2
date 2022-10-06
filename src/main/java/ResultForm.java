import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.*;
import org.apache.commons.compress.utils.IOUtils;

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
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
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
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);
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
                cb.setColorFill(new BaseColor(0, 0, 0));
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, reportID, 492f, 791f, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, date2, 492f, 777f, 0);
                cb.setFontAndSize(CANDARA_REGULAR, 8);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mısır Sınıflandırma Aracı v2.0", 117f, 681.5f, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, this.p.getFilename(), 117f, 666.5f, 0);

                cb.endText();
                float yinit = 600f;
                float yshift = 300f;
                int inc = 0;
                int pagecount = 2;
                int samplecount = 0;

                for (int i = 0; i < this.grf.getSamples().size(); i++) {
                    if (this.grf.getSamples().get(i).isSelected()) {
                        samplecount++;
                    }
                }

                cb.beginText();
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, samplecount+"/"+this.p.getSamples().size() + "", 117f, 651.5f, 0);
                cb.endText();

                for (int i = 0; i < this.grf.getSamples().size(); i++) {
                    MaizeCheckBox mcb = grf.getSamples().get(i);
                    Maize m = mcb.getSample();

                    if(mcb.isSelected()) {
                        if (yinit - inc * yshift < 170f) {
                            page = writer.getImportedPage(reader, 2);
                            document.newPage();
                            cb.addTemplate(page, 0, 0);

                            cb.beginText();
                            cb.setFontAndSize(CANDARA_REGULAR, 10);
                            cb.setColorFill(new BaseColor(0, 0, 0));
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, reportID, 492f, 791f, 0);
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, date2, 492f, 777f, 0);
                            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "" + pagecount++, 553f, 43f, 0);
                            cb.endText();

                            yinit = 750f;
                            inc = 0;
                        }

                        if(m.getResult().size()>0) {
                            float x = 40f;
                            float y = yinit - inc*yshift;

                            piechartHG(cb,m,40f, y,30f);
                            piechartLine(cb,m,40f, y,20f);
                        }

                        inc++;
                    }
                }

                document.close();
            }
        }
    }

    public void piechartLine(PdfContentByte pb, Maize m,float x, float y,float r) throws Exception{
        LinkedHashMap<String,LinkedHashMap<String,Double>> res = m.getLineResults();
        ArrayList<String> keys = new ArrayList<>(res.keySet());

        for(int i=0;i<keys.size();i++){
            LinkedHashMap<String,Double> res2 = res.get(keys.get(i));
            piechart(pb,res2,x + 20f + (i%3)*(2*r+110f),y-((i/3)+1)*(2*r+50f),r,keys.get(i));
        }
    }

    public void piechartHG(PdfContentByte pb, Maize m,float x, float y,float r) throws Exception{
        piechart(pb,m.getResult(),x,y,r,m.getName());
    }


    public void piechart(PdfContentByte pb, LinkedHashMap<String,Double> res,float x, float y,float r,String name) throws Exception{
        byte[] bytes = IOUtils.toByteArray(candara);
        BaseFont CANDARA_REGULAR = BaseFont.createFont("Candara.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes,null);

        byte[] bytes2 = IOUtils.toByteArray(candarabold);
        BaseFont CANDARA_BOLD = BaseFont.createFont("Candara_Bold.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, bytes2, null);

        ArrayList<String> keys = new ArrayList<>(res.keySet());

        float rx = x + r;
        float ry = y - r - 5f;

        float enddeg = 0;

        for(int i=0;i<keys.size();i++){
            float score = (float)(1f*res.get(keys.get(i)));
            Color c = Color.getHSBColor(1f * (i + 1) / keys.size(), 1, 1);

            if(score!=0) {
                float deg = score * 360f;

                float firstx = (float) (rx + r * Math.cos(2 * Math.PI * enddeg / 360));
                float firsty = (float) (ry + r * Math.sin(2 * Math.PI * enddeg / 360));

                enddeg += deg;

                pb.moveTo(rx, ry);
                pb.lineTo(firstx, firsty);
                pb.arc(rx-r,ry+r,rx-r+2*r,ry+r-2*r,enddeg-deg,deg);
                pb.lineTo(rx, ry);

                pb.closePath();
            }

            pb.rectangle(rx+r+20f,ry+r-i*10f,8f,-8f);
            pb.setColorFill(new BaseColor(c.getRGB()));
            pb.fill();

            pb.setColorFill(new BaseColor(Color.BLACK.getRGB()));

            pb.setFontAndSize(CANDARA_REGULAR,7f);
            pb.beginText();
            pb.showTextAligned(PdfContentByte.ALIGN_LEFT,keys.get(i)+": "+Math.round(10000f*score)/100f+" %",rx+r+30f,ry+r-i*10f-6f,0f);
            pb.endText();
        }

        pb.setFontAndSize(CANDARA_BOLD, 10f);
        pb.beginText();
        pb.showTextAligned(PdfContentByte.ALIGN_LEFT,name,x,y+5f,0f);
        pb.endText();
    }

    /*
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

     */

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
