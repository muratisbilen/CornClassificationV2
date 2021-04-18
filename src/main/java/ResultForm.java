import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
    private GraphPanel gp = new GraphPanel();
    private GenerateReportForm grf = new GenerateReportForm();
    private String candara;
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

        this.sampleList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(sampleList.getSelectedIndex()>-1 && e.getClickCount()==1){
                    gp.setM((Maize)sampleList.getSelectedValue());
                }
            }
        });

        addALtoGenerateBut();

        try {
            candara = new File(this.getClass().getResource("Candara.ttf").toURI()).getAbsolutePath();
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

            com.lowagie.text.Document document = new com.lowagie.text.Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(jfc.getSelectedFile()));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont CANDARA_REGULAR = BaseFont.createFont(candara, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            // Load existing PDF

            PdfReader reader = new PdfReader(getClass().getResource("May Report.pdf"));
            PdfImportedPage page;
            page = writer.getImportedPage(reader, 1);
            document.newPage();
            cb.addTemplate(page, 0, 0);

            cb.beginText();
            cb.setFontAndSize(CANDARA_REGULAR, 10);
            cb.setColorFill(new Color(0, 0, 0));
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, reportID, 507f, 791f, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, date2, 507f, 777f, 0);
            cb.setFontAndSize(CANDARA_REGULAR, 8);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Mısır Sınıflandırma Aracı v1.0", 117f, 681.5f, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, this.p.getFilename(), 117f, 666.5f, 0);
            cb.showTextAligned(PdfContentByte.ALIGN_LEFT, this.p.getSamples().size()+"", 117f, 651.5f, 0);
            cb.endText();

            document.close();
        }

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
