/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.render;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.logic.domain.DynamicDomain;

/**
 *
 * @author Administrator
 */
public class DynamicDomainTableDataModel extends AbstractTableModel{

    String[] columns = new String[]{"动态域名", "当前IP", "绑定设备", "更新时间", "状态"} ;

    public DynamicDomainTableDataModel() {
        super() ;
    }

	public int getColumnCount() {
		return columns.length ;
	}

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false ;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column] ;
    }

	public int getRowCount() {
		List<DynamicDomain> dd = DomainManager.getDomainManager().getDynamicDomain() ;
		return dd.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
        DomainManager dm = DomainManager.getDomainManager() ;
		List<DynamicDomain> dd = dm.getDynamicDomain() ;
		
		DynamicDomain d = dd.get(rowIndex) ;
		
		switch(columnIndex){
			case 0 :
				return d.getVisitedDomain() ;
			case 1 :
				return d.getIP() ;
			case 2 :
                return d.getNetworkDevice() ;
            case 3 :
                return d.getLastUpdateTimeAsString() ;
			default :
				return d.getActive() == 1 ? "启用中" : "暂停使用" ;
		}
	}
	
}

