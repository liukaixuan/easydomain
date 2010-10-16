
package com.guzzservices.easydomain.logic.thirdpart.handler.impl ;

import com.guzzservices.easydomain.logic.domain.DomainProvider;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.guzzservices.easydomain.logic.domain.RecordA;
import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.logic.exception.LoginException;
import com.guzzservices.easydomain.logic.thirdpart.handler.IRecordAHandler;
import com.guzzservices.easydomain.util.HelpUtil;
import com.guzzservices.easydomain.util.ComponentCenter ;
import com.guzzservices.easydomain.util.StringUtil;
import com.guzzservices.easydomain.util.VerfiyUtil;


public class WanWangRecordAHandler implements IRecordAHandler{
	
	DefaultHttpClient httpclient = new DefaultHttpClient();
	DomainProvider wanWangProvider ;

    //10分钟过期
    private int sessionTimeOut = 1000 * 60 * 10 ;

    private long lastLoginTime = 0L ;

    //还可以重试登录的次数
    private int retryTimeRemain = 0 ;
    
	{
		httpclient.getParams().setParameter("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)") ;
        wanWangProvider = new DomainProvider() ;
        wanWangProvider.setKeyword("wanwang") ;
        wanWangProvider.setProviderName("万网");
        wanWangProvider.setDescription("http://www.net.cn/");
        wanWangProvider.setMaxARecords(10) ;
    }
		
	private List<RecordA> records ;
	
	private String mydnsURL ;
	
	private String domainName ;

    private TopDomain topDomain ;
    
    private boolean isInited = false ;

    public void bindWithTopDomain(TopDomain td){
        this.topDomain = td ;
    }

    protected synchronized boolean isSessionTimeOut(){
        long now = System.currentTimeMillis() ;

        //已经超时了。
        if(now - lastLoginTime > sessionTimeOut){
            retryTimeRemain = 1 ; //允许重试1次。
            return true ;
        }

        return false ;
    }

    protected synchronized void setSessionValidNow(){
        this.retryTimeRemain = 0 ;
        this.lastLoginTime = System.currentTimeMillis() ;
    }

    protected synchronized void makeSureWeAreLogin() throws Exception{
        while(isSessionTimeOut() && retryTimeRemain > 0){
            loginAndPrepare(this.topDomain.getFullDomain(), this.topDomain.getPassword()) ;
            retryTimeRemain-- ;
        }

        if(isSessionTimeOut()){
            throw new LoginException("登录域名：" + this.topDomain.getFullDomain() + "服务器失败，请检查帐号与网络连接。") ;
        }
    }
    
    protected synchronized void makeShowWeAreInited() throws Exception{
    	if(!isInited){
        	this.refreshFromServer() ;
        }
    }

    public DomainProvider getDomainProvider(){
        return wanWangProvider ;
    }
	
	public synchronized void loginAndPrepare(String domainName, String password) throws Exception {
		this.domainName = domainName ;

        int rand = 1 ;
        String scode = null ;

        for(int i = 0 ; i < 5 ; i++){ //如果验证码分析错误，有5次机会重新分析。
            rand = (int) (System.currentTimeMillis() % 998) + 1 ;
            scode = VerfiyUtil.getWanCode(rand) ;

            if(StringUtil.notEmpty(scode)){
                break ;
            }
        }

//Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; CIBA; .NET CLR 1.1.4322; .NET CLR 2.0.50727)
		HttpPost httpost = new HttpPost("http://diy.hichina.com/cgi-bin/login");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("Rand", "" + rand));
		nvps.add(new BasicNameValuePair("Domain", domainName));
		nvps.add(new BasicNameValuePair("Password", password));
		nvps.add(new BasicNameValuePair("Scode", scode));
		nvps.add(new BasicNameValuePair("x", "30"));
		nvps.add(new BasicNameValuePair("y", "15"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		
		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		String content = "" ;
//		System.out.println("Login form get: " + response.getStatusLine());
		if (entity != null) {
			content = HelpUtil.readStream(entity.getContent(), HelpUtil.ENCODE_UTF8) ;
			entity.consumeContent();
		}
		
		if(content.contains("解析服务器")){
			this.mydnsURL = "http://diy.hichina.com/cgi-bin/showResolv?User=" + domainName ;

            //登录成功了清楚标记。
            setSessionValidNow() ;
            
		}else{
            throw new LoginException("登录域名：" + domainName + "服务器失败，请检查帐号与网络连接。") ;
		}
	}
	
	public synchronized void logout(){
		this.lastLoginTime = 0 ;
	}
	
	public List<RecordA> parseServerPageContentToRecordAs(String content){
		int start = content.indexOf("A (IPv4主机)") ;
		int end = content.indexOf("CNAME (别名)", start) ;
		
		List<RecordA> as = new LinkedList<RecordA>() ;
		
		while(start >= 0 && start <= end){
			RecordA ra = new RecordA(this.domainName) ;
			
			//id
			int m_start = content.indexOf("<input type=hidden name=id value=", start) ;
			int m_end = content.indexOf(">", m_start) ;
			String id = content.substring(m_start + "<input type=hidden name=id value=".length(), m_end) ;
			if("0".equalsIgnoreCase(id)){
				start = content.indexOf("<form method=post action=\"updateResolv\">", m_end) ;
				continue ;
			}
			
			ra.setId(id) ;
			
			//visited domain
			int m_start2 = content.indexOf("<td align=\"left\">", m_end) ;
			int m_end2 = content.indexOf("</td>", m_start2) ;
			ra.setVisitedDomain(content.substring(m_start2 + "<td align=\"left\">".length(), m_end2)) ;
			
			
			//TTL
			int m_start3 = content.indexOf("<td align=\"left\"><input type=text name=ttl value=\"", m_end2) ;
			m_start3 = m_start3 + "<td align=\"left\"><input type=text name=ttl value=\"".length() ;
			
			int m_end3 = content.indexOf("\"", m_start3) ;
			ra.setTtl(content.substring(m_start3, m_end3)) ;
			
			//IP
			int m_start4 = content.indexOf("<td align=\"left\"><input type=text name=content value=\"", m_end3) ;
			m_start4 = m_start4 + "<td align=\"left\"><input type=text name=content value=\"".length() ;
			
			int m_end4 = content.indexOf("\"", m_start4) ;
			ra.setIP(content.substring(m_start4, m_end4)) ;
			
			as.add(ra) ;
			
			start = content.indexOf("<form method=post action=\"updateResolv\">", m_end4) ;
		}
		
		this.isInited = true ;
		
		return as ;
	}

    protected boolean isListContentRightForParser(String content){
        if(content == null) return false ;

        if(!content.contains("A (IPv4主机)")) return false ;

        return true ;
    }

	public List<RecordA> listAllRecordAs() throws Exception {
		if(records == null){
			refreshFromServer() ;
		}
		
		if(records != null){
			List<RecordA> copied = new LinkedList<RecordA>() ;
			
			for(RecordA ra : this.records){
				copied.add((RecordA) ra.clone()) ;
			}
			
			return copied ;
		}
		
		return null ;
	}

	public synchronized void refreshFromServer() throws Exception {
        makeSureWeAreLogin() ;
//		System.out.println("jump to mydns :" + this.mydnsURL);
		
		//success
		HttpGet get = new HttpGet(this.mydnsURL) ;
				
		HttpResponse response = httpclient.execute(get);
		HttpEntity entity = response.getEntity();
		
		String content = HelpUtil.readStream(entity.getContent(), HelpUtil.ENCODE_UTF8) ;
		entity.consumeContent();

        if(!isListContentRightForParser(content)){
//            System.out.println(content) ;
            return ;
        }
        
		this.records = parseServerPageContentToRecordAs(content) ;
		
		ComponentCenter.getInstance().setTextToStatusPanel("域名[" + this.domainName + "]的二级域名成功与服务器同步") ;
	}
	
	public RecordA getRecordByNameA(String nameA){
        if(nameA == null) return null ;
        
		for(RecordA r : this.records){
			if(nameA.equalsIgnoreCase(r.getNameA())){
				return r ;
			}
		}
		
		return null ;
	}

	public synchronized boolean addRecordA(RecordA record) throws Exception {        
		if(record == null) return false ;
		makeShowWeAreInited() ;
				
		RecordA r = getRecordByNameA(record.getNameA()) ;

        if(r != null){ //已经存在了。
            ComponentCenter.getInstance().showPopupTip("错误", "域名：" + record.getVisitedDomain() + "已经存在！", ComponentCenter.MSG_TYPE_ERROR);
            return false ;
        }
        
        makeSureWeAreLogin() ;
		
		//增加新域名
		HttpPost httpost = new HttpPost("http://diy.hichina.com/cgi-bin/updateResolv");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("Domain", this.domainName));
		nvps.add(new BasicNameValuePair("type", "A"));
		nvps.add(new BasicNameValuePair("id", "0"));
		nvps.add(new BasicNameValuePair("name", record.getNameA()));
		nvps.add(new BasicNameValuePair("ttl", record.getTtl()));
		nvps.add(new BasicNameValuePair("content", record.getIP()));
		nvps.add(new BasicNameValuePair("create", "创建"));
			
		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			
		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
			
		String content = HelpUtil.readStream(entity.getContent(), HelpUtil.ENCODE_UTF8) ;
			
		entity.consumeContent() ;

        if(!isListContentRightForParser(content)){
//            System.out.println(content) ;
            return false ;
        }
			
		int oldSize = this.records.size() ;
                
        List<RecordA> ras = parseServerPageContentToRecordAs(content) ;

        if(ras.size() == 0){
            //error found.
            
            return false ;
        }

        this.records = ras ;
			
		if(this.records.size() == oldSize + 1){
			RecordA a = this.getRecordByNameA(record.getNameA()) ;
				
			if(a != null){ //更新ID回去。
				record.setId(a.getId()) ;
				return true ;
			}
				
			return false ;
		}
			
		return false ;
	}

    public synchronized boolean updateRecordA(String oldNameA,RecordA record) throws Exception {
		if(record == null) return false ;
		makeShowWeAreInited() ;

        RecordA r = getRecordByNameA(oldNameA) ;

        if(r == null){
            ComponentCenter.getInstance().showPopupTip("错误", "域名：" + record.getVisitedDomain() + "不存在。可能是本地列表和服务器不同步引起，请刷新子域名列表。", ComponentCenter.MSG_TYPE_ERROR);
            return false ;
        }

        makeSureWeAreLogin() ;

		record.setId(r.getId()) ;

		HttpPost httpost = new HttpPost("http://diy.hichina.com/cgi-bin/updateResolv");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		nvps.add(new BasicNameValuePair("Domain", this.domainName));
		nvps.add(new BasicNameValuePair("type", "A"));
		nvps.add(new BasicNameValuePair("id", r.getId()));
		nvps.add(new BasicNameValuePair("ttl", record.getTtl()));
		nvps.add(new BasicNameValuePair("content", record.getIP()));
		nvps.add(new BasicNameValuePair("update", "改"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		String content = HelpUtil.readStream(entity.getContent(), HelpUtil.ENCODE_UTF8) ;

		entity.consumeContent() ;

        if(!isListContentRightForParser(content)){
//            System.out.println(content) ;
            return false ;
        }
        
		int oldSize = this.records.size() ;

        List<RecordA> ras = parseServerPageContentToRecordAs(content) ;

        if(ras.size() == 0){
            //error found.
            return false ;
        }

		this.records = ras ;

		if(this.records.size() == oldSize){
			RecordA a = this.getRecordByNameA(record.getNameA()) ;
//			更新成功了
			if(record.equals(a)){
				return true ;
			}
		}

		return false ;
	}

	public synchronized boolean removeRecordA(RecordA record) throws Exception {
		RecordA r = this.getRecordByNameA(record.getNameA()) ;
		makeShowWeAreInited() ;
		
		if(r == null){
			return false ;
		}

        if(!HelpUtil.notEmpty(r.getId())){
            return false ;
        }

        makeSureWeAreLogin() ;
			
        HttpPost httpost = new HttpPost("http://diy.hichina.com/cgi-bin/updateResolv");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		nvps.add(new BasicNameValuePair("Domain", this.domainName));
		nvps.add(new BasicNameValuePair("type", "A"));
		nvps.add(new BasicNameValuePair("id", r.getId()));
		nvps.add(new BasicNameValuePair("ttl", record.getTtl()));
		nvps.add(new BasicNameValuePair("content", record.getIP()));
		nvps.add(new BasicNameValuePair("delete", "删"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();

		String content = HelpUtil.readStream(entity.getContent(), HelpUtil.ENCODE_UTF8) ;

		entity.consumeContent() ;
		

        if(!isListContentRightForParser(content)){
//            System.out.println(content) ;
            return false ;
        }
        
		int oldSize = this.records.size() ;

        List<RecordA> ras = parseServerPageContentToRecordAs(content) ;

        if(ras.size() == 0 && oldSize > 1){
            //error found.
            return false ;
        }

		this.records = ras ;
		
		if(this.records.size() == oldSize - 1){
			//删除成功了
			return true ;
		}
		
		return false ;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	
}
