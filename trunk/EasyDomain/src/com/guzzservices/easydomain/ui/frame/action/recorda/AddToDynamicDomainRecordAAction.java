/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.recorda;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.RecordA;
import com.guzzservices.easydomain.ui.frame.ARecordListFrame;
import com.guzzservices.easydomain.ui.frame.AddToDynamicDomainForm;
import java.awt.event.ActionEvent;

/**
 *
 * @author Administrator
 */
public class AddToDynamicDomainRecordAAction extends AbstractRecordAAction{

    public AddToDynamicDomainRecordAAction(ARecordListFrame frame){
        super(frame) ;
    }

    public void actionPerformed(ActionEvent e) {
        //TODO: 接下来处理和真正域名管理系统的联调(test)， 增加手动刷新域名列表功能，数据验证。
         AddToDynamicDomainForm f = new AddToDynamicDomainForm(frame, frame.getSelectedRecordA(), true) ;
         f.setVisible(true);
    }

     @Override
     public boolean isEnabled() {
        RecordA a = frame.getSelectedRecordA() ;
        if(a == null) return false ;

         //已经加入到了动态域名中的判断，决定是否enable.
        return DomainManager.getDomainManager().getDynamicDomainByRecordA(a) == null ;
     }
       
}
