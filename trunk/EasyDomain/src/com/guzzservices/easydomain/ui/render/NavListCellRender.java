/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.render;

import com.guzzservices.easydomain.ui.frame.DDSProjectAboutBox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Administrator
 */
public class NavListCellRender implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)) ;

        JPanel p0 = new JPanel() ;
        p0.setBackground(Color.BLUE);
        p0.setSize(p0.getWidth(), 35);

        final JPanel p = new JPanel() ;
        p.setSize(p0.getWidth() - 4, p0.getHeight() - 4);       

        if (isSelected) {
            p.setBackground(new Color(244, 182, 72)) ;
        }else {
            p.setBackground(new Color(157, 190, 236)) ;
        }

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.guzzservices.easydomain.ui.frame.DDSProjectApp.class).getContext().getResourceMap(DDSProjectAboutBox.class);

        Icon icon = resourceMap.getIcon(index + ".gif") ;

        JLabel l = new JLabel(icon) ;
        JLabel t = new JLabel(value.toString()) ;

//        p.add(l) ;
        p.add(t) ;

        return p ;
    }  
}
