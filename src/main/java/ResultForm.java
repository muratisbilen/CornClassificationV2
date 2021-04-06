import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    public ResultForm(){
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
