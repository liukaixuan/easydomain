/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.render;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.TopDomain;

/**
 *
 * @author Administrator
 */
public class TopDomainTableDataModel extends AbstractTableModel{

    String[] columns = new String[]{"域名", "一级注册商", "子域名个数", "动态域名数"} ;

    public TopDomainTableDataModel() {
        super() ;
        DomainManager.getDomainManager().setDomainTableModel(this) ;
    }

	public int getColumnCount() {
		return columns.length ;
	}

    @Override
    public String getColumnName(int column) {
        return columns[column] ;
    }

	public int getRowCount() {
		List<TopDomain> ds = DomainManager.getDomainManager().getTopDomains() ;
		return ds.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        DomainManager dm = DomainManager.getDomainManager() ;
		List<TopDomain> ds = dm.getTopDomains() ;
		
		TopDomain d = ds.get(rowIndex) ;
		
		switch(columnIndex){
			case 0 :
				return d.getFullDomain() ;
			case 1 :
				return d.getProvider() ;
			case 2 :
                if(d.getRecordAs() == null) return "读取中……" ;
                else return "" + d.getRecordAs().size() ;
            case 3 :
                return dm.getDynamicDomainCounts(d) + "" ;
			default :
				return d.getFullDomain() ;
		}
	}
	
}

