/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.ui.render;

import com.guzzservices.easydomain.util.HelpUtil;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.text.JTextComponent;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.w3c.dom.html.HTMLLinkElement;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html2.HTMLElement;

/**
 *
 * @author Administrator
 */
public class OnePageHtmlRenderContext extends SimpleHtmlRendererContext {
    private HtmlPanel contextComponent ;

     JPopupMenu menu = new JPopupMenu() ;

    public OnePageHtmlRenderContext(HtmlPanel contextComponent, UserAgentContext ucontext) {
        super(contextComponent, ucontext) ;
        this.contextComponent = contextComponent ;

        menu.add(TransferHandler.getCutAction()) ;
        menu.add(TransferHandler.getCopyAction()) ;
        menu.add(TransferHandler.getPasteAction()) ;
        
        contextComponent.setComponentPopupMenu(menu) ;
    }

    @Override
    public void linkClicked(HTMLElement linkNode, URL url, String target) {
        try {
            if (!HelpUtil.openURLInSystemBroswer(url.toURI())) {
                super.linkClicked(linkNode, url, target);
            }else{
                return ;
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(OnePageHtmlRenderContext.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.linkClicked(linkNode, url, target);
    }

    @Override
    public boolean onContextMenu(HTMLElement element, MouseEvent event) {
        Object src = event.getSource() ;

        System.out.println(src.getClass()) ;

        if(src instanceof JTextComponent){
            ((JTextComponent) src).setComponentPopupMenu(menu) ;
             return true ;
        }

        return super.onContextMenu(element, event) ;
    }

    @Override
    public boolean onMouseClick(HTMLElement element, MouseEvent event) {
        return super.onMouseClick(element, event);
    }

    protected boolean isElementClickable(HTMLElement element){
        //System.err.println(element.getClass()) ;

        boolean clickable = element instanceof HTMLLinkElement ;

        clickable = clickable || (element instanceof HTMLAnchorElement) ;
        clickable = clickable || (element instanceof org.w3c.dom.html2.HTMLLinkElement) ;
        clickable = clickable || (element instanceof org.w3c.dom.html2.HTMLAnchorElement) ;


        return clickable ;
    }

    @Override
    public void onMouseOut(HTMLElement element, MouseEvent event) {
        if(isElementClickable(element)){
            contextComponent.setCursor(cursor) ;
        }else{
            super.onMouseOver(element, event);
        }
    }

    @Override
    public void onMouseOver(HTMLElement element, MouseEvent event) {        
        if(isElementClickable(element)){
            cursor = contextComponent.getCursor() ;

            contextComponent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)) ;
        }else{
            super.onMouseOver(element, event);
        }
    }

    private Cursor cursor ;

}
