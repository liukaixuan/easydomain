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
public class ActiveDDAction extends AbstractDDAction{

        @Override
        protected void initData(){
            super.initData();
            super.putValue(Action.NAME, "启用动态域名");
            super.putValue(Action.LONG_DESCRIPTION, "启动域名，自动绑定域名IP到动态变化的设备IP");
        }

        @Override
        public boolean isEnabled() {
            return super.isEnabled() && super.getSelectedDynamicDomain().getActive() == 0 ;
        }

        public void actionPerformed(ActionEvent e) {
            DynamicDomain dd = getSelectedDynamicDomain() ;
            dd.setActive(1) ;
            DomainManager.getDomainManager().updateDynamicDomain(dd) ;
        }

}
