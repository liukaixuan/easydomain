/**
 * 
 */
package com.guzzservices.easydomain.logic;

import com.guzzservices.easydomain.logic.domain.DynamicDomain;
import com.guzzservices.easydomain.logic.domain.DynamicDomainChangeListener;
import com.guzzservices.easydomain.logic.domain.RecordA;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.guzzservices.easydomain.logic.domain.TopDomain;
import com.guzzservices.easydomain.logic.domain.TopDomainChangeListener;
import com.guzzservices.easydomain.logic.thirdpart.handler.IRecordAHandler;
import com.guzzservices.easydomain.logic.thirdpart.handler.IRecordAManager;
import com.guzzservices.easydomain.logic.thirdpart.handler.RecordAHandlerFactory;
import com.guzzservices.easydomain.ui.frame.DDSProjectApp;
import com.guzzservices.easydomain.util.CloseUtil;
import com.guzzservices.easydomain.util.ComponentCenter;
import com.guzzservices.easydomain.util.HelpUtil;
import com.guzzservices.easydomain.util.NetUtil;
import java.awt.SystemTray;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import javax.swing.table.AbstractTableModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * 域名加载与管理中心。
 * 
 * @date 2009-1-18 下午03:40:17
 */
public class DomainManager {
    public final String softwareVersion = "v1.0" ;
	
	private static DomainManager dm = new DomainManager() ;
	
	List<TopDomain> topDomains = new LinkedList<TopDomain>() ;
    private List<DynamicDomain> dynamicDomain = new LinkedList<DynamicDomain>() ;

    private List<TopDomainChangeListener> topDomainListeners = new LinkedList<TopDomainChangeListener>() ;

    private List<DynamicDomainChangeListener> dynamicDomainListeners = new LinkedList<DynamicDomainChangeListener>() ;

    private AbstractTableModel domainTableModel ;

    public static boolean keepRunning = true ;

    private Date onlineTime = new Date() ;

    private Thread updateDynamicDomainThread = null ;

    private Thread updateLanIPThread = null ;

    private boolean lanIPUsed = false ;

    private long lastLanCheckTime = 0 ;

    private String lanIP = null ;

    Properties systemProps = new Properties();
    
    private final void loadSystemConfig(){
        FileInputStream fis = null;

        try {
            File file = new File("config.ini") ;
            if(!file.exists()){
                file.createNewFile();
            }
            
            fis = new FileInputStream(file);
            systemProps = new Properties();
            systemProps.load(fis) ;
        }  catch (Exception ex) {
            ComponentCenter.getInstance().showPopupTip("EasyDomain", "错误：" + ex.getMessage(), ComponentCenter.MSG_TYPE_INFO) ;
        }finally{
            CloseUtil.closeInputStream(fis) ;
        }
    }

    public String getOnlineTime(){
        Date now = new Date() ;

        long ot_SS = onlineTime.getTime() ;
        long now_SS = System.currentTimeMillis() ;

        long SS = now_SS - ot_SS ;

        int hours = (int) SS/1000/3600 ;

        int lastHours = hours % 24 ;

        int days = (hours - lastHours) / 24 ;

        if(days > 0){
            return days + "天" + hours + "小时" ;
        }else{
            return hours + "小时" ;
        }
    }

    public int getActiveDynamicDomainCount(){
        int count = 0 ;

        for(DynamicDomain d : dynamicDomain){
            if(d.getActive() == 1){
                count++ ;
            }
        }

        return count ;
    }

    public String getLanIP(){
        return this.lanIP ;
    }

    public void addTopDomainChangeListener(TopDomainChangeListener listener){
        this.topDomainListeners.add(listener);
    }

     public void removeTopDomainChangeListener(TopDomainChangeListener listener){
        this.topDomainListeners.remove(listener);
    }

    public void addDynamicDomainChangeListener(DynamicDomainChangeListener listener){
        this.dynamicDomainListeners.add(listener) ;
    }

    public void removeDynamicDomainChangeListener(DynamicDomainChangeListener listener){
        this.dynamicDomainListeners.remove(listener) ;
    }

    public int getDynamicDomainCounts(TopDomain domain){
        int count = 0 ;
        for(DynamicDomain dd : this.dynamicDomain){
            if(dd.getRegisterDomain().equalsIgnoreCase(domain.getFullDomain())){
                count++ ;
            }
        }
        return count ;
    }

    public void addDynamicDomain(DynamicDomain domain){
        this.dynamicDomain.add(domain) ;
        notifyDynamicDomainChanged() ;
        this.save();
    }

    public void removeDynamicDomain(DynamicDomain domain){
        this.dynamicDomain.remove(domain) ;
        notifyDynamicDomainChanged() ;
        this.save();
    }

    public void updateDynamicDomain(DynamicDomain domain){
        this.dynamicDomain.remove(domain) ;
        this.dynamicDomain.add(domain) ;
        notifyDynamicDomainChanged() ;
        this.save();
    }

    public void updateDynamicIP(DynamicDomain domain, String newIP){
        String oldIP = domain.getIP() ;
        if(oldIP.equalsIgnoreCase(newIP)){
            return ;
        }

        domain.setIP(newIP) ;
        domain.setLastUpdateTime(new Date()) ;
        domain.setUpdateCount(domain.getUpdateCount() + 1) ;

        TopDomain td = this.getTopDomainByFullURL(domain.getRegisterDomain()) ;
        
        if(td != null){
            RecordA a =  td.getRecordAByAName(domain.getNameA()) ;
            
            if(a == null){
                a = new RecordA(td) ;
                a.setIP(domain.getIP());
                a.setNameA(domain.getNameA());
                a.setTtl(domain.getTtl());

                this.addRecordA(a);
            }else{
                oldIP = a.getIP() ;

                a.setIP(newIP) ;
                this.updateRecordA(domain.getNameA(), a);
            }

            notifyTopDomainChanged() ;
            notifyDynamicDomainChanged() ;
            ComponentCenter.getInstance().setTextToStatusPanel("更新动态域名[" + domain.getVisitedDomain() + "]IP从 " + oldIP + " 到：" + newIP) ;
        }else{
            ComponentCenter.getInstance().setTextToStatusPanel("顶级域名：" + domain.getRegisterDomain() + "不存在:无法更新其动态域名：" + domain.getVisitedDomain()) ;
        }
    }

    public AbstractTableModel getDomainTableModel() {
        return domainTableModel;
    }

    public void setDomainTableModel(AbstractTableModel domainTableModel) {
        this.domainTableModel = domainTableModel;
    }
	
	public static DomainManager getDomainManager(){
		return dm ;
	}
	
	private DomainManager(){
		init() ;
	}

    private void init(){
        loadSystemConfig() ;
        
        try {
            loadFromDisk() ;
        } catch (Exception ex) {
            ComponentCenter.getInstance().setTextToStatusPanel("错误：" + ex.getMessage());
        }

        this.updateDynamicDomainThread = new DynamicDomainUpdateThread() ;
        this.updateDynamicDomainThread.start();

        this.updateLanIPThread = new LanIPCheckThread() ;
        this.updateLanIPThread.start();
    }
	
	public List<TopDomain> getTopDomains(){
		return topDomains ;	
	}

    protected void notifyTopDomainChanged(){
        if(domainTableModel != null){
            domainTableModel.fireTableDataChanged();
        }

        try {
            saveToDisk();
        } catch (Exception ex) {
            ComponentCenter.getInstance().setTextToStatusPanel("错误：" + ex.getMessage());
        }
    }

    public void notifyDynamicDomainChanged(){
        for(int i = 0 ; i < dynamicDomainListeners.size() ; i++){
            dynamicDomainListeners.get(i).notifyDomainChanged();
        }
    }
	
	public void removeTopDomain(TopDomain domain){
		for(TopDomain d : topDomains){
			if(d.getFullDomain().equalsIgnoreCase(domain.getFullDomain())){
				topDomains.remove(d) ;

                notifyTopDomainChanged() ;
				return ;
			}
		}
	}
	
	public void addTopDomain(TopDomain domain){
        if(this.getTopDomainByFullURL(domain.getFullDomain()) != null){
            //update it
            updateTopDomain(domain) ;
            return ;
        }

        this.topDomains.add(domain);

        notifyTopDomainChanged() ;
	}
	
	public void updateTopDomain(TopDomain domain){
		for(TopDomain d : topDomains){
			if(d.getFullDomain().equalsIgnoreCase(domain.getFullDomain())){
				d.setPassword(domain.getPassword()) ;
				d.setProvider(domain.getProvider()) ;				

                notifyTopDomainChanged() ;
				return ;
			}
		}
	}

    public DynamicDomain getDynamicDomainByRecordA(RecordA ra){
        String vu = ra.getVisitedDomain().toLowerCase() ;

        for(DynamicDomain dd : this.dynamicDomain){
            if(vu.equalsIgnoreCase(dd.getVisitedDomain())){
                return dd ;
            }
        }

        return null ;
    }

    /**在每次和服务器通讯后，利用通讯得到的数据，对动态域名进行IP同步。完成在动态域名IP和服务器不同步时的自动纠错。*/
    public void synDynamicDomainIPsWithServerRecentInfo(List<RecordA> records){
        if(records == null) return ;

        for(RecordA ra : records){
            DynamicDomain dd = this.getDynamicDomainByRecordA(ra) ;

            if(dd != null){
                dd.setIP(ra.getIP());
                this.notifyDynamicDomainChanged() ;
            }
        }
    }

    public boolean addRecordA(final RecordA ra){
        final TopDomain td = getTopDomainByFullURL(ra.getRegisterDomain()) ;
        if(td == null) return false ;
        
        //connect to server and add it.
        Boolean success = (Boolean) fetchRecordAHandlerAndCallBack(td, new IRACallBack(){

            public Object exchangeWithTheServer(IRecordAHandler h) throws Exception {
                if(h.addRecordA(ra)){
                    List<RecordA> records = h.listAllRecordAs() ;
                    td.setRecordAs(records);
                    getTopDomainByFullURL(td.getFullDomain()).setRecordAs(records) ;
                    synDynamicDomainIPsWithServerRecentInfo(records) ;
                    return true ;
                }else{
                    return false ;
                }
            }

       }) ;

       boolean realSuccess = success == null ? false : success.booleanValue() ;

       if(realSuccess){
         td.addRecordA(ra) ;
         notifyTopDomainChangeListeners(td) ;
       }

       return realSuccess ;
    }

    public boolean updateRecordA(final String orginalNameA, final RecordA newRecordA){
        final TopDomain td = getTopDomainByFullURL(newRecordA.getRegisterDomain()) ;
        if(td == null) return false ;       
       
        //connect to server and update it.
         Boolean success = (Boolean) fetchRecordAHandlerAndCallBack(td, new IRACallBack(){

            public Object exchangeWithTheServer(IRecordAHandler h) throws Exception {
                if(h.updateRecordA(orginalNameA, newRecordA)){
                    List<RecordA> records = h.listAllRecordAs() ;
                    td.setRecordAs(records);
                    getTopDomainByFullURL(td.getFullDomain()).setRecordAs(records) ;
                    synDynamicDomainIPsWithServerRecentInfo(records) ;
                    return true ;
                }else{
                    return false ;
                }
            }

       }) ;
       
       boolean realSuccess = success == null ? false : success.booleanValue() ;
       
       if(realSuccess){
         td.updateRecordA(orginalNameA, newRecordA) ;
         notifyTopDomainChangeListeners(td) ;
       }

       return realSuccess ;
    }

    public boolean removeRecordA(final RecordA ra){
        final TopDomain td = getTopDomainByFullURL(ra.getRegisterDomain()) ;
        if(td == null) return false ;

        //connect to server and remove it.
        Boolean success = (Boolean) fetchRecordAHandlerAndCallBack(td, new IRACallBack(){

            public Object exchangeWithTheServer(IRecordAHandler h) throws Exception {
                if(h.removeRecordA(ra)){
                    List<RecordA> records = h.listAllRecordAs() ;
                    td.setRecordAs(records);
                    getTopDomainByFullURL(td.getFullDomain()).setRecordAs(records) ;
                    synDynamicDomainIPsWithServerRecentInfo(records) ;
                    return true ;
                }else{
                    return false ;
                }
            }

       }) ;

       boolean realSuccess = success == null ? false : success.booleanValue() ;

       if(realSuccess){
         td.removeRecordA(ra) ;
         notifyTopDomainChangeListeners(td) ;
       }

       return realSuccess ;
    }
	
	public TopDomain getTopDomainByFullURL(String url){
		for(TopDomain d : topDomains){
			if(d.getFullDomain().equalsIgnoreCase(url)){
				return d ;
			}
		}
		
		return null ;
	}
	
	public void save(){
       notifyTopDomainChanged() ;
	}
	
	protected void saveToDisk() throws DocumentException, IOException{
        Document document = DocumentHelper.createDocument();

        Element root = document.addElement( "domains" );

        Element tops = root.addElement( "tops" ) ;
        for(int i = 0 ; i < this.topDomains.size() ; i++){
            TopDomain td = this.topDomains.get(i) ;
            Element topdomain = tops.addElement("topdomain") ;

            topdomain.addElement("domainPrefix").setText(td.getDomainPrefix()) ;
            topdomain.addElement("domainSuffix").setText(td.getDomainSuffix()) ;
            topdomain.addElement("password").setText(td.getPassword()) ;
            topdomain.addElement("provider").setText(td.getProvider()) ;

            Element ras = topdomain.addElement( "ras" ) ;

            List<RecordA> records = td.getRecordAs() ;

            for(int j = 0 ; j < records.size() ; j++){
                RecordA a = records.get(j) ;
                Element e_ra = ras.addElement("ra") ;

                e_ra.addElement("id").setText(a.getId()) ;
                e_ra.addElement("nameA").setText(a.getNameA()) ;
                e_ra.addElement("IP").setText(a.getIP()) ;
                e_ra.addElement("TTL").setText(a.getTtl()) ;
                e_ra.addElement("registerDomain").setText(a.getRegisterDomain()) ;
            }
        }
        
        Element dynamics = root.addElement( "dynamics" ) ;
        for(int i = 0 ; i < this.dynamicDomain.size() ; i++){
            DynamicDomain dd = this.dynamicDomain.get(i) ;
            Element ddomain = dynamics.addElement("ddomain") ;

            ddomain.addElement("registerDomain").setText(dd.getRegisterDomain()) ;
            ddomain.addElement("nameA").setText(dd.getNameA()) ;
            ddomain.addElement("networkdevice").setText(dd.getNetworkDevice()) ;
            ddomain.addElement("IP").setText(dd.getIP()) ;
            ddomain.addElement("ttl").setText(dd.getTtl()) ;
            ddomain.addElement("lastUpdateTime").setText(dd.getLastUpdateTimeAsString()) ;
            ddomain.addElement("updateCount").setText(dd.getUpdateCount() + "") ;
            ddomain.addElement("active").setText(dd.getActive() + "") ;
        }

        String text = document.asXML();

        HelpUtil.saveSecureFile(new File("dds.data"), text, "UTF-8") ;
	}
	
	protected void loadFromDisk() throws DocumentException{
        String text = HelpUtil.readSecureFile(new File("dds.data"), "UTF-8") ;
        if(text == null) return ;

        List<TopDomain> tDomains = new LinkedList<TopDomain>() ;
        List<DynamicDomain> dds = new LinkedList<DynamicDomain>() ;

        Document document = DocumentHelper.parseText(text) ;

        Element root = document.getRootElement() ;
        Element tops = root.element("tops") ;
        for ( Iterator i = tops.elementIterator( "topdomain" ); i.hasNext(); ) {
            Element ele = (Element) i.next();
            TopDomain d = new TopDomain() ;

            d.setDomainPrefix(ele.element("domainPrefix").getTextTrim());
            d.setDomainSuffix(ele.element("domainSuffix").getTextTrim());
            d.setPassword(ele.element("password").getTextTrim());
            d.setProvider(ele.element("provider").getTextTrim());

            Element ras = ele.element( "ras" ) ;

            if(ras != null){
                for ( Iterator j = ras.elementIterator( "ra" ); j.hasNext(); ) {
                    Element e_ra = (Element) j.next();

                    RecordA a = new RecordA(e_ra.element("registerDomain").getTextTrim()) ;
                    a.setId(e_ra.element("id").getTextTrim());
                    a.setNameA(e_ra.element("nameA").getTextTrim());
                    a.setIP(e_ra.element("IP").getTextTrim());
                    a.setTtl(e_ra.element("TTL").getTextTrim()) ;

                    d.addRecordA(a) ;
                }
            }

            tDomains.add(d) ;
        }

        int activeCount = 0 ;

        Element dynamics = root.element("dynamics") ;
        for ( Iterator i = dynamics.elementIterator( "ddomain" ); i.hasNext(); ) {
            Element ele = (Element) i.next();
            DynamicDomain dd = new DynamicDomain() ;

            dd.setIP(ele.element("IP").getTextTrim()) ;
            dd.setLastUpdateTimeAsString(ele.element("lastUpdateTime").getTextTrim()) ;
            dd.setNameA(ele.element("nameA").getTextTrim()) ;
            dd.setNetworkDevice(ele.element("networkdevice").getTextTrim()) ;
            dd.setRegisterDomain(ele.element("registerDomain").getTextTrim()) ;
            dd.setTtl(ele.element("ttl").getTextTrim()) ;
            
            dd.setUpdateCount(HelpUtil.string2Int(ele.element("updateCount").getTextTrim(), 0)) ;

            int saveAsActive = HelpUtil.string2Int(ele.element("active").getTextTrim(), 0) ;
            if(saveAsActive == 1){
                dd.setActive(1) ;
                activeCount++ ;
            }else{
                dd.setActive(0) ;
            }
            
            dds.add(dd) ;
        }

        this.topDomains = tDomains ;
        this.dynamicDomain = dds ;
	}

    public List<DynamicDomain> getDynamicDomain() {
        return dynamicDomain;
    }

    protected void notifyTopDomainChangeListeners(TopDomain domain){
        for(int i = 0 ; i < topDomainListeners.size() ; i++){
            TopDomainChangeListener t = topDomainListeners.get(i) ;
            t.notifyRecordAChanged(domain) ;
        }

        this.notifyTopDomainChanged();
    }

    
    public boolean refreshARcordsFromServer(final TopDomain td){
       return fetchRecordAHandlerAndCallBack(td, new IRACallBack(){

            public Object exchangeWithTheServer(IRecordAHandler h) throws Exception {
                h.refreshFromServer();
                List<RecordA> records = h.listAllRecordAs() ;

                td.setRecordAs(records);
                getTopDomainByFullURL(td.getFullDomain()).setRecordAs(records) ;

                synDynamicDomainIPsWithServerRecentInfo(records) ;
                return null ;
            }

       }) ;
    }

    /**是否执行成功*/
    protected boolean fetchRecordAHandlerAndCallBack(TopDomain td, IRACallBack callback){
       IRecordAManager m = RecordAHandlerFactory.getRecordAManager() ;

       System.out.println("communication with the server...") ;

       IRecordAHandler h = m.getHandler(td) ;
       if(h != null){
            try {
                Object rv = callback.exchangeWithTheServer(h) ;

                this.notifyTopDomainChanged();

                return true ;
            } catch (Exception ex) {
                ComponentCenter.getInstance().setTextToStatusPanel("与服务器交互失败：" + ex.getMessage()) ;
            }
       }else{
            ComponentCenter.getInstance().setTextToStatusPanel("不支持的域名服务商，请升级软件到最新版本！") ;
       }

       return false ;
    }    

    public void quitSystem(){
        keepRunning = false ;
        
        try{
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                tray.remove(DDSProjectApp.trayIcon);
            }

            this.save();
            
        }finally{
            System.exit(0) ;
        }
    }

//    private class LoadARecordsThread extends Thread{
//        private TopDomain domain ;
//
//        public LoadARecordsThread(TopDomain domain){
//            this.domain = domain ;
//        }
//
//        public void run() {
//            try {
//                 for(TopDomain d : domains){
//                    LinkedList<RecordA> ras = new LinkedList<RecordA>() ;
//                    for(int i = 0 ; i < 12 ; i++){
//                        RecordA r = new RecordA(d.getFullDomain()) ;
//                        r.setNameA("www" + i);
//                        r.setIP("202.36.25." + i) ;
//                        ras.add(r) ;
//                    }
//
//                    d.setRecordAs(ras);
//                }
//
//                DomainManager.getDomainManager().notifyTopDomainChanged() ;
//            } catch (Exception ex) {
//                Logger.getLogger(LoadARecordsThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//    }

    private class DynamicDomainUpdateThread extends Thread{

        @Override
        public void run() {
            while(DomainManager.keepRunning){
                synchronized(this){
                    try{
                        List<DynamicDomain> dds = dynamicDomain ;
                        DynamicDomain dd = null ;

                        for(int i = 0 ; i < dds.size() ; i++){
                            try {
                                dd = dds.get(i);

                                if(dd.getActive() == 1){ //只有激活的域名才进行检测
                                    String device = dd.getNetworkDevice();
                                    String oldIP = dd.getIP();
                                    String IP = "" ;

                                    if("lan".equalsIgnoreCase(device)){
                                        lanIPUsed = true ;
                                        
                                        if(HelpUtil.notEmpty(DomainManager.this.lanIP)){
                                            IP = DomainManager.this.lanIP ;
                                        }
                                    }else{
                                        NetworkInterface net = NetworkInterface.getByName(device);
                                        Enumeration<InetAddress> ips = net.getInetAddresses() ;

                                        while(ips.hasMoreElements()){
                                           IP = ips.nextElement().getHostAddress() ;
                                        }
                                    }

                                    if(HelpUtil.notEmpty(IP)){
                                        IP = IP.trim() ;
                                        if (!IP.equalsIgnoreCase(oldIP)){
                                            //IP发生了变化，变更IP地址到服务器。
                                            DomainManager.this.updateDynamicIP(dd, IP) ;
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                if(dd != null){
                                    ComponentCenter.getInstance().setTextToStatusPanel("动态域名[" + dd.getVisitedDomain() + "]IP检测失败：" + ex.getMessage());
                                }
                            }
                        }

                        wait(1000 * 3) ;

                    }catch(Exception e){
                    }
                }
            }
        }
    }

    private interface IRACallBack{
        public Object exchangeWithTheServer(IRecordAHandler h) throws Exception ;
    }


    private class LanIPCheckThread extends Thread{

        @Override
        public void run() {
            while(DomainManager.keepRunning){
                synchronized(this){
                    try{
                        long timePeriod = 0 ;

                        if(DomainManager.this.lanIPUsed){ //如果有lan动态IP，1分钟检测一次。否则1个小时监测一次。
                            timePeriod = 1000 * 60 * 1 ;
                        }else{
                            timePeriod = 1000 * 60 * 60 ;
                        }

                        if(System.currentTimeMillis() - DomainManager.this.lastLanCheckTime > timePeriod){
                            //check again.
                            String page = NetUtil.getPageFromServer("http://dongtaiyuming.appspot.com/service/queryIP") ;

                            String IP = null ;

                            int start = page.indexOf("IP: ") ;
                            if(start != -1){
                                int end = page.indexOf("\r\n", start) ;
                                if(end == -1){
                                    end = page.indexOf("\n", start) ;
                                }

                                if(end == -1){
                                    IP = page.substring(start + "IP: ".length()) ;
                                }else{
                                    IP = page.substring(start + "IP: ".length(), end) ;
                                }
                            }

                             if(HelpUtil.notEmpty(IP)){
                                DomainManager.this.lanIP = IP ;
                                 DomainManager.this.lastLanCheckTime = System.currentTimeMillis() ;
                            }
                        }

                        wait(1000 * 3) ;

                    }catch(Exception e){
                    }
                }
            }
        }
    }

}
