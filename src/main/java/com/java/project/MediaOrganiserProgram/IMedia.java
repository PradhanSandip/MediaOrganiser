package com.java.project.MediaOrganiserProgram;
/*
 * This interface defines list of function which will be implemented by medias class
 */
public interface IMedia {
	public void setMedia(String name,String location,String mediaType,String mediaResolution,String dateAdded,String mediaSize,String duration);
	public String getName();
	public String getLocation();
	public String getMediaType();
	public String getMediaResolution();
	public String getDateAdded();
	public String getMediaSize();
	public String getDuration();
	public String getPlaylistName();
	public void setName(String name);
	public void setLocation(String location);
	public void setMediaType(String type);
	public void setMediaResolution(String resolution);
	public void setDateAdded(String date);
	public void setMediaSize(String size);
	public void setDuration(String duration);
	public void setPlaylistName(String name);
	
}
