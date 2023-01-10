package com.java.project.MediaOrganiserProgram;

import java.io.File;
import java.io.IOException;

/**
 * This class defines a playlist, this class contain attributes and methods that describes a playlist
 * @author Sandip Pradhan
 * @see IPlaylist
 *
 */
public class Playlist implements IPlaylist{
	private String playlistName;
	private String folderColour;
	private int fileCount;
	private String[] fileTypes;
	private String dateModified;
	private boolean favourite;
	private String dateAdded;
	private FileHandler fh = new FileHandler().getInstance();

	
	/**
	 * This methods accepts playlist attributes and assigns it.
	 */
	@Override
	public void createPlaylist(String name, String folderColour, int fileCount, String dateModified, String[] fileTypes, boolean favourite, String dateAdded) {
		this.playlistName = name;
		this.folderColour = folderColour;
		this.fileCount = fileCount;
		this.dateModified = dateModified;
		this.fileTypes = new String[3];
		this.favourite = favourite;
		this.dateAdded = dateAdded;
		//create playlist
		fh.createPlaylistFile(name);
		//write metadata
		fh.writePlaylistMetaData(this);
		//load the laylist
		fh.loadPlaylist();
		//populate homeView
		ProgramData.homeView.populate();
	}


	

	//getters
	@Override
	public String getPlaylistName() {
		return this.playlistName;
	}


	@Override
	public String getFolderColour() {
		return this.folderColour;
	}


	@Override
	public int getFileCount() {
		return this.fileCount;
	}


	@Override
	public String[] getFileTypes() {
		return this.fileTypes;
	}


	@Override
	public String getDateModified() {
		return this.dateModified;
	}

	//setters
	@Override
	public void setPlaylistName(String name) {
		this.playlistName = playlistName;
		
	}


	@Override
	public void setFolderColour(String folderColour) {
		this.folderColour = folderColour;
		
	}


	@Override
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
		
	}


	@Override
	public void setFileTypes(String[] fileTypes) {
		this.fileTypes = fileTypes;
		
	}


	@Override
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
		
	}




	/**
	 * This method sets values to the playlist attributes
	 */
	@Override
	public void setPlaylist(String playlistName, String folderColour, int fileCount, String dateModified,String[] fileTypes, boolean favourite, String dateAdded) {
		this.playlistName = playlistName;
		this.folderColour = folderColour;
		this.fileCount = fileCount;
		this.fileTypes = fileTypes;
		this.dateModified = dateModified;
		this.favourite = favourite;
		this.dateAdded = dateAdded;
		
	}




	@Override
	public boolean getIsFavourite() {
		
		return favourite;
	}





	@Override
	public void setIsFavourite(boolean favorite) {
		this.favourite = favorite;
		
	}





	@Override
	public boolean isExist(String name) {
		return fh.isPlaylistExist(name);
	}



	@Override
	public String getDateAdded() {
		return dateAdded;
	}



	
	
}
