package com.gebrais.cordova.autoserver;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * A simple class for serve directory www at localhost:8080
 *
 * It's a variant of https://github.com/floatinghotpot/cordova-httpd.git
 *
 * <p> Autoserver version 1.0,
 * Copyright &copy; 2018 Alfonso Guerrero (aguerreromier@gmail.com)
 *
 * <p><b>Features + limitations: </b><ul>
 *
 *    <li> Only for Android </li>
 *    <li> Released as open source, MIT licence </li>
 *    <li> No fixed config files, logging, authorization etc. (Implement yourself if you need them.) </li>
 *</ul>
 *
 * This Cordova plugin is built based on following 3 projects, and thanks to the authors.<ul>
 *    <li> [Httpd](https://github.com/floatinghotpot/cordova-httpd.git), a Cordova plugin
 *    <li> [cordova-local-webserve](https://github.com/oakwood/cordova-local-webserver.git), other Cordova plugin, only for ios
 *    <li> [NanoHTTPD](https://github.com/NanoHttpd/nanohttpd), written in java, for java / android, by psh.</li>
 * </ul>
 *
 * (MIT licence)
 */

 public class Autoserver extends CordovaPlugin {

      /**
       * Singleton class instance
       */
    public static Autoserver instance = null;

    /** Common tag used for logging statements. */
    private static final String LOGTAG = "Autoserver";

    private String www_root = "www";
  	private int port = 8888;
  	private boolean localhost_only = true;

  	private String localPath = "";
  	private WebServer server = null;
  	private String	url = "";

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        // your init code here
        instance = this;
        www_root = "www";
        port = 8888;
        localPath = www_root;
        localhost_only = true;
        __startServer();
    }

    private String __getLocalIpAddress() {
    	try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (! inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) {
                            String ip = inetAddress.getHostAddress();
                    		return ip;
                    	}
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(LOGTAG, ex.toString());
        }

		    return "127.0.0.1";
    }

    private String __startServer() {
    	String errmsg = "";
    	try {
    		AndroidFile f = new AndroidFile(localPath);

	        Context ctx = cordova.getActivity().getApplicationContext();
			  AssetManager am = ctx.getResources().getAssets();
    		f.setAssetManager( am );

    		if(localhost_only) {
    			InetSocketAddress localAddr = new InetSocketAddress(InetAddress.getByAddress(new byte[]{127,0,0,1}), port);
    			server = new WebServer(localAddr, f);
    		} else {
    			server = new WebServer(port, f);
    		}
        Log.i(LOGTAG, "Started server at " + localPath + "!!");
		} catch (IOException e) {
			errmsg = String.format("IO Exception: %s", e.getMessage());
			Log.w(LOGTAG, errmsg);
		}
    	return errmsg;
    }

    private void __stopServer() {
		if (server != null) {
			server.stop();
			server = null;
		}
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy() {
    	__stopServer();
    }
}
