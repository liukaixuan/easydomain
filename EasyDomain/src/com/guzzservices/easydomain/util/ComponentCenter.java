/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.util;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.NavPanelManager;
import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.ui.frame.DDSProjectApp;
import com.guzzservices.easydomain.ui.frame.DDSProjectView;
import com.guzzservices.easydomain.ui.panel.DomainListPanel;
import java.awt.Point;
import java.awt.TrayIcon;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTable;

/**
 *
 * @author Administrator
 */
public class ComponentCenter {

    private final List<Action> actions = new LinkedList<Action>() ;

    private ActionEnableStatusLookThread lookup ;

    private class ActionEnableStatusLookThread extends Thread{

        @Override
        public void run() {           
            while(DomainManager.keepRunning){
            try {

                 for(Action a : actions){
                    a.setEnabled(a.isEnabled()) ;
                 }
                 
                 synchronized(this){
                  wait(300);
                 } ;
               
            } catch (Exception ex) {
                Logger.getLogger(ComponentCenter.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

        }
    }

    private ComponentCenter(){
          lookup = new ActionEnableStatusLookThread() ;
          lookup.start();
    }

    private static ComponentCenter cc = new ComponentCenter() ;

    public static ComponentCenter getInstance(){
        return cc ;
    }

    private static JList navList ;

    private static JTable topDomainTable  ;

    private static JTable dynamicDomainTable ;

    public static TopDomain getSelectedTopDomain(){
        if(!isTopDomainViewOnTop()) return null ;

        int row = ComponentCenter.getTopDomainTable().getSelectedRow() ;

        if(row < 0) return null;

        TopDomain d = DomainManager.getDomainManager().getTopDomains().get(row) ;

        return d ;
    }


    public static boolean isTopDomainViewOnTop(){
        return navList.getSelectedIndex() == 1 ;
    }

     public static boolean isDynamicDomainViewOnTop(){
        return navList.getSelectedIndex() == 2 ;
    }

    public static JTable getDynamicDomainTable() {
        return dynamicDomainTable;
    }

    public static void setDynamicDomainTable(JTable dynamicDomainTable) {
        ComponentCenter.dynamicDomainTable = dynamicDomainTable;
    }

    public static JList getNavList() {
        return navList;
    }

    public static void setNavList(JList navList) {
        ComponentCenter.navList = navList;

        DomainListPanel dp = (DomainListPanel) NavPanelManager.getInstance().getPanel(1) ;
        ComponentCenter.topDomainTable = dp.getTopDomainTable() ;
    }

    public static JTable getTopDomainTable() {
        return topDomainTable;
    }

    public static void setTopDomainTable(JTable topDomainTable) {
        ComponentCenter.topDomainTable = topDomainTable;
    }

    public Action registerSwingAction(Action action){
        this.actions.add(action) ;

        return action ;
    }

    public JFrame getMainView(){
        return DDSProjectApp.getApplication().getMainFrame() ;
    }

    public Point getRightMainViewPopWindowLocation(){
        JFrame f = getMainView() ;
        int x = f.getX() + 245 ;
        int y = f.getY() + 150 ;

        return new Point(x, y) ;
    }

    public void setTextToStatusPanel(String text){
        DDSProjectView ddv = DDSProjectApp.getApplication().getMainFrameView() ;

        if(ddv != null && ddv.getStatusMessageLabel() != null){
            ddv.getStatusMessageLabel().setText(text) ;
        }
    }

    public static final int MSG_TYPE_INFO = 1 ;
    public static final int MSG_TYPE_WARNING = 2 ;
    public static final int MSG_TYPE_ERROR = 3 ;


    public void showPopupTip(String title, String msg, int msgType){
        TrayIcon trayIcon = DDSProjectApp.trayIcon ;

        if(trayIcon != null){
            if(msgType == MSG_TYPE_INFO){
                trayIcon.displayMessage(title, msg, TrayIcon.MessageType.INFO) ;
            }else if(msgType == MSG_TYPE_WARNING){
                trayIcon.displayMessage(title, msg, TrayIcon.MessageType.WARNING) ;
            }else if(msgType == MSG_TYPE_ERROR){
                trayIcon.displayMessage(title, msg, TrayIcon.MessageType.ERROR) ;
            }
        }

    }


}
