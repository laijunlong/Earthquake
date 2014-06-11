package com.ljl.eq;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.location.Location;

public class Quake {
	private Date date;
	private String details;
	private Location location;
	private double magnitudu;
	private String link;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public double getMagnitudu() {
		return magnitudu;
	}
	public void setMagnitudu(double magnitudu) {
		this.magnitudu = magnitudu;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Override
	public String toString() {
		SimpleDateFormat sdf=new SimpleDateFormat("HH.mm");
		String dateString = sdf.format(date);
		return dateString + ":" + magnitudu +" "+details;
	}
	public Quake(Date date, String details, Location location,
			double magnitudu, String link) {
		super();
		this.date = date;
		this.details = details;
		this.location = location;
		this.magnitudu = magnitudu;
		this.link = link;
	}
	
}
