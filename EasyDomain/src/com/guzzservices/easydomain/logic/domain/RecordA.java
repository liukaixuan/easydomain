package com.guzzservices.easydomain.logic.domain;

import com.guzzservices.easydomain.logic.exception.InvalidParamException;
import com.guzzservices.easydomain.util.HelpUtil;

public class RecordA implements Cloneable{
		
	private String id = "" ;
	
	private String registerDomain ;
	
	/**自域名头部。如cache.baidu.com，此处的domain值为cache*/
	private String nameA ;
	
	private String IP ;
	
	private String ttl = "3600" ;
	
	public RecordA(String registerDomain){
		this.registerDomain = registerDomain ;
	}

    public RecordA(TopDomain domain){
		this(domain.getFullDomain()) ;
	}

    public RecordA(RecordA a){
        this.id = a.id ;
        this.registerDomain = a.registerDomain ;
        this.nameA = a.nameA ;
        this.IP = a.IP ;
        this.ttl = a.ttl ;
	}

	public String getNameA() {
		return nameA;
	}

	public void setNameA(String domain) {
		this.nameA = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
        if(id == null) id = "" ;
		this.id = id;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String ip) {
        if(ip != null){
            ip = ip.trim() ;
        }
		IP = ip;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getRegisterDomain() {
		return registerDomain;
	}

	public void setRegisterDomain(String registerDomain) {
		this.registerDomain = registerDomain;
	}
	
	public String getVisitedDomain(){
		if(nameA == null || nameA.length() == 0){
			return this.registerDomain ;
		}
		return nameA + "." + this.registerDomain ;
	}
	
	public void setVisitedDomain(String domain) throws InvalidParamException{
		if(domain.endsWith(this.registerDomain)){
			int pos = domain.lastIndexOf(this.registerDomain) ;
			
			if(pos > 0){
				this.nameA = domain.substring(0, pos - 1) ;
			}else{
				this.nameA = "" ; //如：baidu.com，没有前缀。
			}
		}else{
			throw new InvalidParamException("域名[" + domain + "]不属于[" + this.registerDomain + "]的子域名。") ;
		}
	}

	public boolean equals(Object obj) {
		if(obj == null) return false ;
		if(! (obj instanceof RecordA))return false ;
		
		RecordA a = (RecordA) obj ;
		
		return (HelpUtil.isSame(id, a.getId()) 
				&& HelpUtil.isSame(registerDomain, a.getRegisterDomain()) 
				&& HelpUtil.isSame(nameA, a.getNameA()) 
				&& HelpUtil.isSame(IP, a.getIP()) 
				&& HelpUtil.isSame(ttl, a.getTtl())) ;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null ;
	}

}
