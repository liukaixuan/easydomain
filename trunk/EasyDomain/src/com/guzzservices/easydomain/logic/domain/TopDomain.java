package com.guzzservices.easydomain.logic.domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//顶级域名
public class TopDomain {

	//如：baidu
	private String domainPrefix ;
	
	//如：com.cn
	private String domainSuffix ;
	
	private String password ;
	
	private String provider ;
	
    private Map<String, RecordA> aNameMap = new HashMap<String, RecordA>() ;
	
	public String getFullDomain(){
		return domainPrefix + "." + domainSuffix ;
	}

	public String getDomainPrefix() {
		return domainPrefix;
	}

	public void setDomainPrefix(String domainPrefix) {
		this.domainPrefix = domainPrefix;
	}

	public String getDomainSuffix() {
		return domainSuffix;
	}

	public void setDomainSuffix(String domainSuffix) {
		this.domainSuffix = domainSuffix;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public List<RecordA> getRecordAs() {
		List<RecordA> ra = new LinkedList<RecordA>() ;
        ra.addAll(aNameMap.values()) ;

        return ra ;
	}

    public int getRecordACount(){
        if(this.aNameMap == null) return 0 ;

        return this.aNameMap.size() ;
    }

    public void addRecordA(RecordA ra){
        if(ra == null) return ;

        aNameMap.put(ra.getNameA().toLowerCase(), ra) ;
    }

    public void updateRecordA(String orginalNameA,RecordA ra){
        if(ra == null) return ;

        if(orginalNameA != null){
            aNameMap.remove(orginalNameA.toLowerCase()) ;
        }
        
        aNameMap.put(ra.getNameA().toLowerCase(), ra) ;
    }

    public void removeRecordA(RecordA ra){
        if(ra == null) return ;

        aNameMap.remove(ra.getNameA().toLowerCase()) ;
    }

	public void setRecordAs(List<RecordA> recordAs) {
        aNameMap.clear();
        for(RecordA a : recordAs){
			addRecordA(a) ;
		}
	}
	
	public RecordA getRecordAByAName(String aname){
        if(aname == null) return null ;
        aname = aname.toLowerCase() ;

        return aNameMap.get(aname) ;
	}
	
}
