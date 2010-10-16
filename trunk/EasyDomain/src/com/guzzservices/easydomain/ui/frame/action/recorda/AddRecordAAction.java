/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.recorda;

import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.logic.thirdpart.handler.IRecordAHandler;
import com.guzzservices.easydomain.logic.thirdpart.handler.IRecordAManager;
import com.guzzservices.easydomain.logic.thirdpart.handler.RecordAHandlerFactory;
import com.guzzservices.easydomain.ui.frame.ARecordEditForm;
import com.guzzservices.easydomain.ui.frame.ARecordListFrame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author Administrator
 */
public class AddRecordAAction extends AbstractAction{

        protected ARecordListFrame frame ;

        public AddRecordAAction(ARecordListFrame frame){
            this.frame = frame ;
            this.putValue(Action.NAME, "添加子域名");
            this.putValue(Action.LONG_DESCRIPTION, "添加一个新的子域名解析记录，操作将同步到域名服务器中。");
        }

        public void actionPerformed(ActionEvent e) {
            ARecordEditForm f = new ARecordEditForm(frame, true, true) ;
            f.setVisible(true);
        }

        @Override
        public boolean isEnabled() {
            //根据provider判断允许的最大子域名数目。
            TopDomain td = frame.getTopDomain() ;
            IRecordAManager m = RecordAHandlerFactory.getRecordAManager() ;
            IRecordAHandler h = m.getHandler(td) ;

            if(h != null){
                return h.getDomainProvider().getMaxARecords() > td.getRecordACount() ;
            }else{ //不支持
                return false ;
            }
        }
        
}
