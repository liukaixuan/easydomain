/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.recorda;

import com.guzzservices.easydomain.logic.domain.RecordA;
import com.guzzservices.easydomain.ui.frame.ARecordListFrame;
import javax.swing.AbstractAction;

/**
 *
 * @author Administrator
 */
public abstract class AbstractRecordAAction extends AbstractAction{
        protected ARecordListFrame frame ;

        public AbstractRecordAAction(ARecordListFrame frame){
            this.frame = frame ;
        }       

        @Override
        public boolean isEnabled() {
            RecordA a = frame.getSelectedRecordA() ;
            return  a != null ;
        }

}
