/*
 * DDSProjectApp.java
 */

package com.guzzservices.easydomain.ui.frame;

import com.guzzservices.easydomain.logic.DomainManager;
import com.guzzservices.easydomain.ui.panel.URLLoadAndShowPanel;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class DDSProjectApp extends SingleFrameApplication {
    DDSProjectView mainFrameView = null ;
    public static TrayIcon trayIcon = null;
    public static Image image = null ;

    private long lastUpdateTime = System.currentTimeMillis() ;

    private boolean useSystemTray = false ;

    private static List<URLLoadAndShowPanel> adsPages = new LinkedList<URLLoadAndShowPanel>() ;

    public static URLLoadAndShowPanel registerURLLoadAndShowPanel(URLLoadAndShowPanel panel){
        adsPages.add(panel) ;
        return panel ;
    }

    public void reloadAddAds(){
        for(URLLoadAndShowPanel p : adsPages){
            p.reloadPage();
        }
    }
    
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        onlyOneInstanceCheck() ;
        
        mainFrameView = new DDSProjectView(this) ;
        show(mainFrameView);

        setupSystemTray() ;
    }

    protected void setupSystemTray(){
        final JFrame frame = mainFrameView.getFrame() ;
        
        try {
            image = ImageIO.read(DDSProjectApp.class.getResourceAsStream("tray.gif"));
            frame.setIconImage(image) ;
        } catch (IOException ex) {
            Logger.getLogger(DDSProjectApp.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   DomainManager.getDomainManager().quitSystem();
                }
            };
            
            ActionListener showFrameListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   if(frame.isVisible()){
                       frame.setVisible(false) ;
                   }else{
                       frame.setVisible(true) ;
                       frame.toFront() ;
                   }
                }
            };

            PopupMenu popup = new PopupMenu();
            
            MenuItem showItem = new MenuItem("显示主界面");
            showItem.addActionListener(showFrameListener);
            popup.add(showItem) ;
            
            MenuItem exitItem = new MenuItem("退出");
            exitItem.addActionListener(exitListener);
            popup.add(exitItem) ;

            trayIcon = new TrayIcon(image, "域名管理系统", popup);
            trayIcon.setImageAutoSize(true) ;

            trayIcon.addActionListener(showFrameListener);

            try {
                tray.add(trayIcon);

                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE) ;

                WindowListener[] ls = frame.getWindowListeners() ;
                
                for(WindowListener l : ls){
                    frame.removeWindowListener(l) ;
                }

                //有了SystemTray以后，修改frame的关闭方法。
                frame.addWindowListener(new WindowAdapter(){

                    public void windowClosing(WindowEvent e) {
                        frame.setVisible(false) ;
                    }

                    public void windowClosed(WindowEvent e) {
                        frame.setVisible(false) ;
                    }

                    public void windowIconified(WindowEvent e) {
                        frame.setVisible(false) ;
                    }

                });

                useSystemTray = true ;

            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }

        } else {
            //  System Tray is not supported
        }

        //注册事件，当界面对外显示时刷新广告页面。
        frame.addWindowListener(
            new WindowAdapter(){

                @Override
                public void windowActivated(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowGainedFocus(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowLostFocus(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowOpened(WindowEvent e) {
                    onWindowEvent() ;
                }

                @Override
                public void windowStateChanged(WindowEvent e) {
                    onWindowEvent() ;
                }

                protected void onWindowEvent(){
                    long now = System.currentTimeMillis() ;
                    long interval = now - lastUpdateTime ;
                        //如果上次刷新在10分钟之内，重新刷新广告页面。
                        if(interval > 1000 * 60 * 10){
                            lastUpdateTime = now ;
                            reloadAddAds();
                        }
                    }
                }
        ) ;
    }   

    public DDSProjectView getMainFrameView() {
        return mainFrameView;
    }   

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of DDSProjectApp
     */
    public static DDSProjectApp getApplication() {
        return Application.getInstance(DDSProjectApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(DDSProjectApp.class, args);
    }

    @Action
    public void miniToSystemTray() {
        if(useSystemTray){
            mainFrameView.getFrame().setVisible(false) ;
        }
    }

    ServerSocket server = null ;
    Thread checkOneInstaceThread = null ;

    protected void onlyOneInstanceCheck(){
        //绑定端口。
        try{
           server = new ServerSocket(26494);//创建socket
           server.setSoTimeout(5000) ;
        }catch (Exception e) {
            try {
                //我们当作实例已经存在，通知该实例显示出窗口。
                Socket sock = new Socket("127.0.0.1", 26494);
                sock.getOutputStream().write("showup".getBytes()) ;
                sock.close();
                
                System.exit(0) ;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "启动失败：请确认26494端口可用！", "启动失败提示", JOptionPane.ERROR_MESSAGE) ;
                Logger.getLogger(DDSProjectApp.class.getName()).log(Level.SEVERE, null, ex);
                
                System.exit(1) ;
            }
        }

        checkOneInstaceThread = new Thread(
            new Runnable(){

                public void run() {
                    while(DomainManager.keepRunning){
                        try {
                           Socket s = server.accept();
                            if(s != null){
                                JFrame frame = mainFrameView.getFrame() ;

                                frame.setVisible(true) ;
                                frame.toFront() ;
                           }
                        } catch (Exception ex) {}
                    }
                }
            }
         ) ;

         checkOneInstaceThread.start() ;
    }

}
