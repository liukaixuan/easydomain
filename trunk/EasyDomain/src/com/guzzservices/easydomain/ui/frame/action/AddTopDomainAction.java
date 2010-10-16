/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action;

import com.guzzservices.easydomain.ui.frame.DomainEditForm;
import com.guzzservices.easydomain.util.ComponentCenter;
import com.guzzservices.easydomain.util.HelpUtil;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Administrator
 */
public class AddTopDomainAction extends AbstractAction{

    public AddTopDomainAction(){
        this.putValue(Action.NAME, "添加待管理域名");
    }

    public void actionPerformed(ActionEvent e) {
        DomainEditForm f = new DomainEditForm(ComponentCenter.getInstance().getMainView(), false, null) ;
        f.setLocation(ComponentCenter.getInstance().getRightMainViewPopWindowLocation()) ;
        f.setVisible(true) ;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }



}
