/**
 * 
 */
package com.guzzservices.easydomain.logic.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DynamicDomain {
		
	private String registerDomain ;
	
	/**自域名头部。如cache.baidu.com，此处的domain值为cache*/
	private String nameA ;
	
	private String IP ;
	
	private String ttl = "3600" ;
	
	private Date lastUpdateTime = new Date() ;

    private int updateCount ;

    private String networkDevice = "eth0" ;

    private static String dateFormat = "yyyy-MM-dd HH:mm:ss" ;

    private int active = 0 ;

    public DynamicDomain(){

    }

    public DynamicDomain(RecordA ra, String networkDevice){
        this.registerDomain = ra.getRegisterDomain() ;
        this.nameA = ra.getNameA() ;
        this.IP = ra.getIP() ;
        this.ttl = ra.getTtl() ;
        this.networkDevice = networkDevice ;
    }

    public String getVisitedDomain(){
		if(nameA == null || nameA.length() == 0){
			return this.registerDomain ;
		}
		return nameA + "." + this.registerDomain ;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

    public String getLastUpdateTimeAsString() {
        if(this.lastUpdateTime == null) return "" ;
        
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat) ;
		return sdf.format(lastUpdateTime) ;
	}

	public void setLastUpdateTimeAsString(String lastUpdateTime) {
        if(lastUpdateTime == null){
            this.lastUpdateTime = null ;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat) ;
        try {
            this.lastUpdateTime = sdf.parse(lastUpdateTime);
        } catch (ParseException ex) {
            Logger.getLogger(DynamicDomain.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String ip) {
		IP = ip;
	}

	public String getNameA() {
		return nameA;
	}

	public void setNameA(String nameA) {
		this.nameA = nameA;
	}

	public String getRegisterDomain() {
		return registerDomain;
	}

	public void setRegisterDomain(String registerDomain) {
		this.registerDomain = registerDomain;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public String getNetworkDevice() {
        return networkDevice;
    }

    public void setNetworkDevice(String networkDevice) {
        this.networkDevice = networkDevice;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
    
}
