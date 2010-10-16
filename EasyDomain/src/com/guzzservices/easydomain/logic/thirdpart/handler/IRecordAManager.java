/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guzzservices.easydomain.logic.thirdpart.handler;

import com.guzzservices.easydomain.logic.domain.DomainProvider;
import com.guzzservices.easydomain.logic.domain.TopDomain;

/**
 *
 * @author Administrator
 */
public interface IRecordAManager {

    public String[] getAllProviderNames() ;

    public DomainProvider getDomainProvider(String providerName) ;

    public IRecordAHandler getHandler(TopDomain td) ;

}
