/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.logic;

import com.guzzservices.easydomain.ui.frame.DDSProjectApp;
import com.guzzservices.easydomain.ui.panel.DomainListPanel;
import com.guzzservices.easydomain.ui.panel.DynamicDomainPanel;
import com.guzzservices.easydomain.ui.panel.RunningStatusPanel;
import com.guzzservices.easydomain.ui.panel.SettingsPanel;
import com.guzzservices.easydomain.ui.panel.URLLoadAndShowPanel;
import java.awt.Component;
import javax.swing.JTable;

/**
 *
 * @author Administrator
 */
public class NavPanelManager {   

    private final Component[] panels ;

    private static NavPanelManager instance = new NavPanelManager() ;

    private javax.swing.JTable dynamicDomainsTable;

    public static NavPanelManager getInstance(){
        return instance ;
    }
    
    private NavPanelManager(){
        DynamicDomainPanel ddp = new DynamicDomainPanel() ;
        this.dynamicDomainsTable = ddp.getDynamicDomainsTable() ;

        SettingsPanel sp = new SettingsPanel() ;
        
        panels = new Component[]{
            new RunningStatusPanel(),
            new DomainListPanel(),
            ddp,
            DDSProjectApp.registerURLLoadAndShowPanel(new URLLoadAndShowPanel("http://share.guzzservices.com/easydomain/hostshare/?source=client")),
            DDSProjectApp.registerURLLoadAndShowPanel(new URLLoadAndShowPanel("http://share.guzzservices.com/easydomain/sell/?source=client")),
            sp
        } ;
        init() ;
    }

    private void init(){        
    }

    public Component getPanel(int index){
        if(index >= panels.length){
            return panels[0] ;
        }

        return panels[index] ;
    }

    public JTable getDynamicDomainsTable() {
        return dynamicDomainsTable;
    }

}
