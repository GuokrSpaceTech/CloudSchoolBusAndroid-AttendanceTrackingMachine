package com.cytx.timecardmobile.lbs;

public class BusStopDto {

	private int    geofenceid;
	private String name;
	private double longitude;
	private double latitude;


	public int getGeofenceid()
	{
		return geofenceid;
	}
	
	public void setGeofenceid(int id)
	{
		geofenceid = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}