/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.dynamicdomain;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.DynamicDomain;
import com.guzzservices.easydomain.util.ComponentCenter;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class DeleteDDAction extends AbstractDDAction{
    private Component frame;

        @Override
        protected void initData(){
            super.initData();
            super.putValue(Action.NAME, "删除动态域名");
            super.putValue(Action.LONG_DESCRIPTION, "删除动态域名只是将动态域名绑定取消，并不会真正的把域名从服务器上删除。");
        }

    public void actionPerformed(ActionEvent e) {
        DynamicDomain dd = getSelectedDynamicDomain() ;
        if(dd != null){
            if(JOptionPane.showConfirmDialog(ComponentCenter.getInstance().getMainView(), "确认取消动态域名：" + dd.getVisitedDomain() + "？\n（注：并不会从服务器中真正删除域名记录。）") == JOptionPane.OK_OPTION){
                DomainManager.getDomainManager().removeDynamicDomain(dd) ;
            }
        }
    }

}
