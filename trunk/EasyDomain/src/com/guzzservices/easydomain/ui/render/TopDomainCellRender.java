/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.render;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.ui.frame.DDSProjectApp;
import com.guzzservices.easydomain.ui.frame.DomainEditForm;

/**
 *
 * @author Administrator
 */
public class TopDomainCellRender extends DefaultTableCellRenderer{

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		TopDomain domain = DomainManager.getDomainManager().getTopDomains().get(row) ;
		
		if(column >= 4){
			JPanel p = new JPanel() ;
			
			JButton changePsw = new JButton(new ChangeDomainPassword(table, domain)) ;
			
			JButton deleteDomain = new JButton(new DeleteTopDomainAction(domain)) ;
			
			p.add(changePsw) ;
			p.add(deleteDomain) ;
			return p;
		}
		
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) ;
	}  
}

class ChangeDomainPassword extends AbstractAction{
	TopDomain domain ;
	JComponent comp ;
	
	public ChangeDomainPassword(JComponent comp, TopDomain domain){
		super() ;
		this.putValue(Action.NAME, "更改密码") ;
		this.domain = domain ;
		this.comp = comp ;
	}

	public void actionPerformed(ActionEvent e) {
		DomainEditForm form = new DomainEditForm(DDSProjectApp.getApplication().getMainFrame(), true, this.domain);
		
		form.setVisible(true) ;
	}
	
}

class DeleteTopDomainAction extends AbstractAction{
	TopDomain domain ;
	
	public DeleteTopDomainAction(TopDomain domain){
		super() ;
		this.putValue(Action.NAME, "撤销域名") ;
		this.domain = domain ;
	}

	public void actionPerformed(ActionEvent e) {
		DomainManager.getDomainManager().removeTopDomain(domain) ;
	}
	
}

