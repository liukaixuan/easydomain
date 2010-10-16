/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.dynamicdomain;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.DynamicDomain;
import java.awt.event.ActionEvent;
import javax.swing.Action;

/**
 *
 * @author Administrator
 */
public class PauseDDAction extends AbstractDDAction{
        @Override
        protected void initData(){
            super.initData();
            super.putValue(Action.NAME, "禁用动态域名");
            super.putValue(Action.LONG_DESCRIPTION, "暂停动态域名解析。暂停后，可以为其他动态域名节省1个开启许可证。");
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && super.getSelectedDynamicDomain().getActive() == 1 ;
        }

        public void actionPerformed(ActionEvent e) {
            DynamicDomain dd = getSelectedDynamicDomain() ;
            dd.setActive(0) ;
            DomainManager.getDomainManager().updateDynamicDomain(dd) ;
        }

}
