/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ARecordListFrame.java
 *
 * Created on 2009-1-18, 13:25:09
 */

package com.guzzservices.easydomain.ui.frame;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.RecordA;
import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.logic.domain.TopDomainChangeListener;
import com.guzzservices.easydomain.util.ComponentCenter;
import java.util.List;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;

/**
 *
 * @author Administrator
 */
public class ARecordListFrame extends javax.swing.JFrame implements TopDomainChangeListener {

    private TopDomain topDomain ;

    private List<RecordA> ras ;

    /** Creates new form ARecordListFrame */
    public ARecordListFrame() {
        initComponents();
    }

    @Override
    public void dispose() {
        DomainManager.getDomainManager().removeTopDomainChangeListener(this) ;
        super.dispose();
    }

    public ARecordListFrame(TopDomain topDomain) {
        this.topDomain = topDomain ;
        initComponents();
        initData() ;
    }

    public RecordA getSelectedRecordA(){
        int index = aRecordsTAble.getSelectedRow();

        if(index < 0 || index > ras.size() - 1){
            return null;
        }

        return ras.get(index) ;
    }

    public void refreshTable(){
        if(this.topDomain != null){
            this.topDomain = DomainManager.getDomainManager().getTopDomainByFullURL(this.topDomain.getFullDomain()) ;
            
            this.setTitle(this.topDomain.getFullDomain() + "域名解析管理");

            ras = this.topDomain.getRecordAs() ;

            DefaultTableModel model = (DefaultTableModel) aRecordsTAble.getModel() ;

            while(model.getRowCount() > 0){
                model.removeRow(0);
            }

            if(ras != null){
                for(int i = 0 ; i < ras.size(); i++){
                    RecordA a = ras.get(i) ;

                    Vector b = new Vector() ;
                    b.add(a.getVisitedDomain());
                    b.add(a.getIP());
                    b.add(a.getTtl());
                    b.add(a.getRegisterDomain());

                    model.addRow(b);
                }
            }

            jPanel1.repaint();
        }
    }

    private void initData(){
       refreshTable() ;
       DomainManager.getDomainManager().addTopDomainChangeListener(this) ;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        addTopDomain = new javax.swing.JButton();
        editTopDomain = new javax.swing.JButton();
        deleteTopDomain = new javax.swing.JButton();
        manageAName = new javax.swing.JButton();
        freshAFromServerButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        aRecordsTAble = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.guzzservices.easydomain.ui.frame.DDSProjectApp.class).getContext().getResourceMap(ARecordListFrame.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setIconImage(DDSProjectApp.image);
        setName("Form"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jToolBar1.setBackground(resourceMap.getColor("jToolBar1.background")); // NOI18N
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setMargin(new java.awt.Insets(0, 2, 0, 1));
        jToolBar1.setName("jToolBar1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N
        jToolBar1.add(jLabel2);

        addTopDomain.setAction(ComponentCenter.getInstance().registerSwingAction(new com.guzzservices.easydomain.ui.frame.action.recorda.AddRecordAAction(this)));
        addTopDomain.setBackground(resourceMap.getColor("addTopDomain.background")); // NOI18N
        addTopDomain.setText(resourceMap.getString("addTopDomain.text")); // NOI18N
        addTopDomain.setFocusable(false);
        addTopDomain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addTopDomain.setName("addTopDomain"); // NOI18N
        addTopDomain.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(addTopDomain);

        editTopDomain.setAction(ComponentCenter.getInstance().registerSwingAction(new com.guzzservices.easydomain.ui.frame.action.recorda.EditRecordAAction(this)));
        editTopDomain.setBackground(resourceMap.getColor("editTopDomain.background")); // NOI18N
        editTopDomain.setText(resourceMap.getString("editTopDomain.text")); // NOI18N
        editTopDomain.setFocusable(false);
        editTopDomain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        editTopDomain.setName("editTopDomain"); // NOI18N
        editTopDomain.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(editTopDomain);

        deleteTopDomain.setAction(ComponentCenter.getInstance().registerSwingAction(new com.guzzservices.easydomain.ui.frame.action.recorda.DeleteRecordAAction(this)));
        deleteTopDomain.setBackground(resourceMap.getColor("deleteTopDomain.background")); // NOI18N
        deleteTopDomain.setText(resourceMap.getString("deleteTopDomain.text")); // NOI18N
        deleteTopDomain.setFocusable(false);
        deleteTopDomain.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteTopDomain.setName("deleteTopDomain"); // NOI18N
        deleteTopDomain.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(deleteTopDomain);

        manageAName.setAction(ComponentCenter.getInstance().registerSwingAction(new com.guzzservices.easydomain.ui.frame.action.recorda.AddToDynamicDomainRecordAAction(this)));
        manageAName.setBackground(resourceMap.getColor("manageAName.background")); // NOI18N
        manageAName.setText(resourceMap.getString("manageAName.text")); // NOI18N
        manageAName.setFocusable(false);
        manageAName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        manageAName.setName("manageAName"); // NOI18N
        manageAName.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(manageAName);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.guzzservices.easydomain.ui.frame.DDSProjectApp.class).getContext().getActionMap(ARecordListFrame.class, this);
        freshAFromServerButton.setAction(actionMap.get("refreshAListFromTheServer")); // NOI18N
        freshAFromServerButton.setBackground(resourceMap.getColor("freshAFromServerButton.background")); // NOI18N
        freshAFromServerButton.setText(resourceMap.getString("freshAFromServerButton.text")); // NOI18N
        freshAFromServerButton.setFocusable(false);
        freshAFromServerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        freshAFromServerButton.setName("freshAFromServerButton"); // NOI18N
        freshAFromServerButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(freshAFromServerButton);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.setBackground(resourceMap.getColor("jScrollPane1.background")); // NOI18N
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        aRecordsTAble.setBackground(resourceMap.getColor("recordAListTable.background")); // NOI18N
        aRecordsTAble.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "子域名", "IP地址", "TTL", "主域名"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        aRecordsTAble.setColumnSelectionAllowed(true);
        aRecordsTAble.setName("recordAListTable"); // NOI18N
        aRecordsTAble.setRowHeight(25);
        aRecordsTAble.setRowMargin(2);
        aRecordsTAble.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        aRecordsTAble.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                aRecordsTAbleMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(aRecordsTAble);
        aRecordsTAble.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        aRecordsTAble.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("recordAListTable.columnModel.title0")); // NOI18N
        aRecordsTAble.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("recordAListTable.columnModel.title1")); // NOI18N
        aRecordsTAble.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("recordAListTable.columnModel.title2")); // NOI18N
        aRecordsTAble.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("recordAListTable.columnModel.title3")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void aRecordsTAbleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_aRecordsTAbleMouseClicked
       if(evt.getClickCount() == 2){
         ARecordEditForm f = new ARecordEditForm(this, false, true) ;
         f.setVisible(true);
       }
    }//GEN-LAST:event_aRecordsTAbleMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ARecordListFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable aRecordsTAble;
    private javax.swing.JButton addTopDomain;
    private javax.swing.JButton deleteTopDomain;
    private javax.swing.JButton editTopDomain;
    private javax.swing.JButton freshAFromServerButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton manageAName;
    // End of variables declaration//GEN-END:variables

    public TopDomain getTopDomain() {
        return topDomain;
    }

    public void notifyRecordAChanged(TopDomain domain) {
        this.refreshTable() ;
    }

    @Action
    public Task refreshAListFromTheServer() {
        return new RefreshAListFromTheServerTask(org.jdesktop.application.Application.getInstance(com.guzzservices.easydomain.ui.frame.DDSProjectApp.class));
    }

    private class RefreshAListFromTheServerTask extends org.jdesktop.application.Task<Object, Void> {
        RefreshAListFromTheServerTask(org.jdesktop.application.Application app) {
            super(app);
            freshAFromServerButton.setEnabled(false) ;
        }
        @Override protected Object doInBackground() {
            DomainManager dm = DomainManager.getDomainManager() ;

            return dm.refreshARcordsFromServer(topDomain) ;
        }
        @Override protected void succeeded(Object result) {
            boolean success = Boolean.TRUE.equals(result) ;

            refreshTable() ;
            freshAFromServerButton.setEnabled(true) ;

            if(success){
                ComponentCenter.getInstance().setTextToStatusPanel(topDomain.getFullDomain() + "子域名成功与服务器完成同步") ;
            }
        }
    }


}

