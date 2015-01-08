package com.cytx.timecardmobile.utility;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.cytx.timecardmobile.TimeCardApplicatoin;
import com.cytx.timecardmobile.constants.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * AsyncHttpClient：异步HttpClient请求类，引入了jar包android-async-http-1.4.3.jar
 * @author ben
 *
 */
public class TimeCardClient{  
	
	public static final String TAG = "TimeCardClient";
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	static{
		// 设置超时时间
	   client.setTimeout(30000);

	   // 通过https验证
	   SSLSocketFactory sslsocketfactory = SSLSocketFactory.getSocketFactory();
	   sslsocketfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	   client.setSSLSocketFactory(sslsocketfactory);
	     
       // 修复在post的时候重定向问题 
	   client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	   
	   // 设置header参数
	   client.addHeader(Constants.APIKEY, "mactopattendance");
	   client.addHeader(Constants.VERSION, TimeCardApplicatoin.getInstance().getVersionName());
	}
	
	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.get(url, params, responseHandler);
	  }

	  public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  client.post(url, params, responseHandler);
	  }
	  
	  public static void post(Context context, String url, StringEntity stringEntity, String contentType, AsyncHttpResponseHandler responseHandler){
		  client.post(context, url, stringEntity, contentType, responseHandler);
	  }
	  
}
