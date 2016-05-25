package com.example.kimjs.sample.infoparser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.util.Log;

class ReceiveXml extends Thread {
    

	private URL url;
	private String output;
	
	
	public ReceiveXml(URL url) {
		this.url = url;
	}
	
	public String getXml() {
		return output;
	}
	
	public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    url.openStream(), "UTF-8"));
            output = in.readLine();
            while(true){
                String temp;
                temp = in.readLine();
                if(temp==null)
                    break;
                output += temp;
            }
        } catch (Exception e) {
        	// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(getName(), e.toString());
        }
    }
}