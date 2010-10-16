/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.util.ComponentCenter;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class DeleteTopDomainAction extends AbstractAction{

    public DeleteTopDomainAction(){
        this.putValue(Action.NAME, "删除主域名");
        this.putValue(Action.LONG_DESCRIPTION, "从本软件管理域名列表中删除选择的域名，并不会真正的在域名服务器中删除。");
    }

    public void actionPerformed(ActionEvent e) {
        TopDomain d = ComponentCenter.getSelectedTopDomain() ;

        if(d != null){
            if(JOptionPane.showConfirmDialog(ComponentCenter.getInstance().getMainView(), "确认从本软件中删除对域名：" + d.getFullDomain() + "的管理？") == JOptionPane.OK_OPTION){
                DomainManager.getDomainManager().removeTopDomain(d) ;
                ComponentCenter.getTopDomainTable().repaint();
            }
        }
    }

    @Override
    public boolean isEnabled() {
        TopDomain d = ComponentCenter.getSelectedTopDomain() ;
        return  d != null ;
    }



}
