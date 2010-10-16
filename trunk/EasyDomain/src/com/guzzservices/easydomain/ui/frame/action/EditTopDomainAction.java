/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action;

import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.ui.frame.DomainEditForm;
import com.guzzservices.easydomain.util.ComponentCenter;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Administrator
 */
public class EditTopDomainAction extends AbstractAction{

    public EditTopDomainAction(){
        this.putValue(Action.NAME, "修改主域名");
        this.putValue(Action.LONG_DESCRIPTION, "设置域名密码与注册服务上密码一致。只有密码一致才能进行域名管理，修改此密码并不会修改域名服务器上的域名管理密码。");
    }

    public void actionPerformed(ActionEvent e) {
        TopDomain d = ComponentCenter.getSelectedTopDomain() ;

        if(d != null){
            DomainEditForm f = new DomainEditForm(ComponentCenter.getInstance().getMainView(), false, d) ;
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
