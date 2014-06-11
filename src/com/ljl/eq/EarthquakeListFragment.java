/**
 * 
 */
package com.ljl.eq;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.earthquake.R;


/**
 * @author laijunlong
 *
 */
public class EarthquakeListFragment extends ListFragment {
	ArrayAdapter<Quake> aa;
	ArrayList<Quake> earthquakes = new ArrayList<Quake>();
	private static final String TAG = "EARTHQUAKE";
	private Handler handler = new Handler();
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		int layoutID = android.R.layout.simple_list_item_1;
		aa = new ArrayAdapter<Quake>(getActivity(), layoutID,earthquakes);
		setListAdapter(aa);
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				refreshEarthquakes();
				
			}
		});
		t.start();
	}
	
	public void refreshEarthquakes(){
		URL url;
		try{
			String quakeFeed = getString(R.string.quake_feed);
			url = new URL(quakeFeed);
			
			URLConnection connection;
			connection = url.openConnection();
			
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCOde = httpConnection.getResponseCode();
			
			if(responseCOde == HttpURLConnection.HTTP_OK){
				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				
				earthquakes.clear();
				
				NodeList nl = docEle.getElementsByTagName("entry");
				if(nl != null && nl.getLength() > 0){
					for(int i=0 ; i<nl.getLength() ; i++){
						Element entry=(Element)nl.item(i);
						Element title = (Element)entry.getElementsByTagName("title").item(0);
						Element g = (Element)entry.getElementsByTagName("georss:point").item(0);
						Element when = (Element)entry.getElementsByTagName("updated").item(0);
						Element link = (Element)entry.getElementsByTagName("link").item(0);
						
						String details = title.getFirstChild().getNodeValue();
						String hostname = "http://earthquake.usgs.gov";
						String linkString = hostname + link.getAttribute("href");
						
						String point = g.getFirstChild().getNodeValue();
						String dt = when.getFirstChild().getNodeValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
						Date qdate = new GregorianCalendar(0,0,0).getTime();
						try{
							qdate = sdf.parse(dt);
						}catch(ParseException e){
							Log.d(TAG, "Date parsing exception.",e);
						}
						String[] location = point.split(" ");
						Location l = new Location("dummyGPS");
						l.setLatitude(Double.parseDouble(location[0]));
						l.setAltitude(Double.parseDouble(location[1]));
						
						String magnitudeString = details.split(" ")[1];
						int end = magnitudeString.length()-1;
						double magnitude = Double.parseDouble(magnitudeString.substring(0,end));
						
						details = details.split(",")[1].trim();
						
						final Quake quake = new Quake(qdate,details,l,magnitude,linkString);
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								addNewQuake(quake);
								
							}
						});
					}
				}
			}		
		}catch(Exception e){
			Log.d(TAG, "refreash error.", e);
		}finally{
			
		}
	}
	private void addNewQuake(Quake _quake){
		earthquakes.add(_quake);
		aa.notifyDataSetChanged();
	}
}
