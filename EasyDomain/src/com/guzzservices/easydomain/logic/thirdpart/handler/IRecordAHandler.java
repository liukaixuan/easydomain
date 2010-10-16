package com.guzzservices.easydomain.logic.thirdpart.handler;

import com.guzzservices.easydomain.logic.domain.DomainProvider;
import java.util.List;

import com.guzzservices.easydomain.logic.domain.RecordA;
import com.guzzservices.easydomain.logic.domain.TopDomain;

public interface IRecordAHandler {

    public DomainProvider getDomainProvider() ;

    public void bindWithTopDomain(TopDomain td) ;
	
	public void loginAndPrepare(String domainName, String password) throws Exception ;
	
	public void logout() throws Exception;
	
	public List<RecordA> parseServerPageContentToRecordAs(String content);
	
	public boolean removeRecordA(RecordA record) throws Exception ;
	
	public boolean addRecordA(RecordA record) throws Exception ;

    public boolean updateRecordA(String oldNameA, RecordA record) throws Exception ;
	
	/**获取最新的A记录；如果有缓存，获取缓存内容；否则从服务器读取。*/
	public List<RecordA> listAllRecordAs() throws Exception ;
	
	public void refreshFromServer() throws Exception ;
	
	public RecordA getRecordByNameA(String nameA) ;
	
	public String getDomainName() ;

	public void setDomainName(String domainName) ;

}
