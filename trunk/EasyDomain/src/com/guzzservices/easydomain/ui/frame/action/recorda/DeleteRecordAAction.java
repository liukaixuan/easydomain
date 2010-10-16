/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.recorda;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.RecordA;
import com.guzzservices.easydomain.ui.frame.ARecordListFrame;
import com.guzzservices.easydomain.util.ComponentCenter;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class DeleteRecordAAction extends AbstractRecordAAction{

    public DeleteRecordAAction(ARecordListFrame frame){
        super(frame) ;
    }

    public void actionPerformed(ActionEvent e) {
        RecordA ra = frame.getSelectedRecordA() ;

        if(ra != null){
            //TODO: 改造成按照TASK方式执行。
            if(JOptionPane.showConfirmDialog(frame, "确认从服务器中删除二级域名：" + ra.getVisitedDomain() + "？") == JOptionPane.OK_OPTION){
                ComponentCenter cc = ComponentCenter.getInstance() ;
                if(DomainManager.getDomainManager().removeRecordA(ra)){
                    cc.setTextToStatusPanel("二级域名 " + ra.getVisitedDomain() + " 成功删除。") ;
                }else{
                    cc.setTextToStatusPanel("二级域名 " + ra.getVisitedDomain() + " 删除失败！") ;
                }
            }
        }
    }
       
}
