/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.dynamicdomain;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.NavPanelManager;
import com.guzzservices.easydomain.util.ComponentCenter;
import com.guzzservices.easydomain.logic.domain.DynamicDomain;
import java.util.List;
import javax.swing.AbstractAction;

/**
 *
 * @author Administrator
 */
public abstract class AbstractDDAction extends AbstractAction{
        public AbstractDDAction(){
            initData() ;
        }

        protected void initData(){

        }

        protected DynamicDomain getSelectedDynamicDomain(){
            if(!ComponentCenter.isDynamicDomainViewOnTop()){
                return null ;
            }

            int index = NavPanelManager.getInstance().getDynamicDomainsTable().getSelectedRow();

            List<DynamicDomain> dd = DomainManager.getDomainManager().getDynamicDomain() ;

            if(index < 0 || index > dd.size() - 1){
                return null;
            }

            return dd.get(index) ;
        }

        @Override
        public boolean isEnabled() {
            return getSelectedDynamicDomain() != null ;
        }

}
