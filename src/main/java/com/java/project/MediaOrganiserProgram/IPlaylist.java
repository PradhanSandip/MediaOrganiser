package com.java.project.MediaOrganiserProgram;
/**
 * This interface defines methods for Playlist class.
 * @see Playlist
 * @author Sandip Pradhan
 *
 */
public interface IPlaylist {
	public void createPlaylist(String name, String folderColor, int fileCount, String dateModified, String[] fileTypes, boolean favorite, String dateAdded);
	public void setPlaylist(String playlistName,String folderColour,int fileCount,String dateModified, String[] fileTypes, boolean favorite, String dateAdded);
	public String getPlaylistName();
	public String getFolderColour();
	public int getFileCount();
	public String[] getFileTypes();
	public String getDateModified();
	public String getDateAdded();
	public boolean isExist(String name);
	public boolean getIsFavourite();
	public void setPlaylistName(String name);
	public void setFolderColour(String folderColour);
	public void setFileCount(int fileCount);
	public void setFileTypes(String[] fileTypes);
	public void setDateModified(String dateModified);
	public void setIsFavourite(boolean favorite);
	
	
}
