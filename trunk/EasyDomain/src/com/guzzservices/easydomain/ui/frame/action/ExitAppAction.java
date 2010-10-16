/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action;

import com.guzzservices.easydomain.logic.DomainManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Administrator
 */
public class ExitAppAction extends AbstractAction{

    public ExitAppAction(){
        this.putValue(Action.NAME, "退出");
    }

    public void actionPerformed(ActionEvent e) {
        DomainManager.getDomainManager().quitSystem() ;
    }

    @Override
    public boolean isEnabled() {
        return true ;
    }

}
