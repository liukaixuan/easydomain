/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.logic.thirdpart.handler;

/**
 *
 * @author Administrator
 */
public class RecordAHandlerFactory {

    static DefaultRecordAManager m = new DefaultRecordAManager() ;

    public static IRecordAManager getRecordAManager(){
        return m ;
    }

}
