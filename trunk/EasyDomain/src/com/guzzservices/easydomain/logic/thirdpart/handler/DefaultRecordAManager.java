/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.logic.thirdpart.handler;

import com.guzzservices.easydomain.logic.domain.DomainProvider;
import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.logic.thirdpart.handler.impl.WanWangRecordAHandler;
import com.guzzservices.easydomain.logic.thirdpart.handler.impl.XinnetRecordAHandler;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class DefaultRecordAManager implements IRecordAManager {

    private Map<String , Class> providers = new HashMap<String , Class>() ;

    private Map<String , DomainProvider> domainProviders = new HashMap<String , DomainProvider>() ;

    private Map<String, IRecordAHandler> handersMap = new HashMap<String, IRecordAHandler>() ;

    protected DefaultRecordAManager(){
        XinnetRecordAHandler handler = new XinnetRecordAHandler() ;
        providers.put(handler.getDomainProvider().getProviderName(), handler.getClass()) ;
        domainProviders.put(handler.getDomainProvider().getProviderName(), handler.getDomainProvider()) ;
        
        WanWangRecordAHandler wh = new WanWangRecordAHandler() ;
        providers.put(wh.getDomainProvider().getProviderName(), wh.getClass()) ;
        domainProviders.put(wh.getDomainProvider().getProviderName(), wh.getDomainProvider()) ;
    }

    public String[] getAllProviderNames() {
        return providers.keySet().toArray(new String[0]) ;
    }

    public DomainProvider getDomainProvider(String providerName){
        return this.domainProviders.get(providerName) ;
    }

    public IRecordAHandler getHandler(TopDomain td) {
        IRecordAHandler handler = handersMap.get(td.getFullDomain()) ;

        if(handler != null) return handler ;

        Class cls = providers.get(td.getProvider()) ;
        if(cls == null) return null ;
        
        try {
            handler =  (IRecordAHandler) cls.newInstance();
            handler.bindWithTopDomain(td) ;

            handersMap.put(td.getFullDomain(), handler) ;

            return handler ;
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        }

        return null ;
    }

}
