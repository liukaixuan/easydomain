/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.frame.action.recorda;

import com.guzzservices.easydomain.ui.frame.ARecordEditForm;
import com.guzzservices.easydomain.ui.frame.ARecordListFrame;
import java.awt.event.ActionEvent;

/**
 *
 * @author Administrator
 */
public class EditRecordAAction extends AbstractRecordAAction{

    public EditRecordAAction(ARecordListFrame frame){
        super(frame) ;
    }

    public void actionPerformed(ActionEvent e) {
         ARecordEditForm f = new ARecordEditForm(frame, false, true) ;
         f.setVisible(true);
    }
       
}
