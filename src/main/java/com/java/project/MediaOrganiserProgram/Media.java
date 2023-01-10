package com.java.project.MediaOrganiserProgram;

public class Media implements IMedia{
	private String playlistName;
	private String name;
	private String location;
	private String mediaType;
	private String mediaResolution;
	private String dateAdded;
	private String mediaSize;
	private String duration;
	@Override
	public void setMedia(String name, String location, String mediaType, String mediaResolution, String dateAdded, String mediaSize,String duration) {
		this.name = name;
		this.location = location;
		this.mediaType = mediaType;
		this.mediaResolution = mediaResolution;
		this.dateAdded = dateAdded;
		this.mediaSize = mediaSize;
		this.duration = duration;
		
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	@Override
	public String getLocation() {
		// TODO Auto-generated method stub
		return this.location;
	}
	@Override
	public String getMediaType() {
		// TODO Auto-generated method stub
		return this.mediaType;
	}
	@Override
	public String getMediaResolution() {
		// TODO Auto-generated method stub
		return this.mediaResolution;
	}
	@Override
	public String getDateAdded() {
		// TODO Auto-generated method stub
		return this.dateAdded;
	}
	@Override
	public String getMediaSize() {
		// TODO Auto-generated method stub
		return this.mediaSize;
	}
	@Override
	public String getDuration() {
		// TODO Auto-generated method stub
		return this.duration;
	}
	@Override
	public void setName(String name) {
		this.name = name;
		
		
	}
	@Override
	public void setLocation(String location) {
		this.location = location;
		
		
	}
	@Override
	public void setMediaType(String type) {
		this.mediaType = type;
		
		
	}
	@Override
	public void setMediaResolution(String resolution) {
		this.mediaResolution = resolution;
		
		
	}
	@Override
	public void setDateAdded(String date) {
		this.dateAdded = date;
		
		
	}
	@Override
	public void setMediaSize(String size) {
		this.mediaSize = size;
		
		
	}
	@Override
	public void setDuration(String duration) {
		this.duration = duration;
		
	}
	@Override
	public String getPlaylistName() {
		
		return playlistName;
	}
	@Override
	public void setPlaylistName(String name) {
		playlistName = name;
		
	}
}
