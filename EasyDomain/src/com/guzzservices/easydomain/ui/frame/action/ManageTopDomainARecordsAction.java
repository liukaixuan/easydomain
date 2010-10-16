/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action;

import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.ui.frame.ARecordListFrame;
import com.guzzservices.easydomain.util.ComponentCenter;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Administrator
 */
public class ManageTopDomainARecordsAction extends AbstractAction{

    public ManageTopDomainARecordsAction(){
        this.putValue(Action.NAME, "管理子域名");
        this.putValue(Action.LONG_DESCRIPTION, "管理选择域名的子域名。子域名在增删改时，自动与域名注册服务器同步操作。");
    }

    public void actionPerformed(ActionEvent e) {
        TopDomain d = ComponentCenter.getSelectedTopDomain() ;

        if(d != null){
            ARecordListFrame f = new ARecordListFrame(d) ;
            f.setLocation(ComponentCenter.getInstance().getRightMainViewPopWindowLocation()) ;
            f.setVisible(true) ;
        }
    }

    @Override
    public boolean isEnabled() {
        TopDomain d = ComponentCenter.getSelectedTopDomain() ;
        return  d != null ;
    }

}
