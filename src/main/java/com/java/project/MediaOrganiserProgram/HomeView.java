package com.java.project.MediaOrganiserProgram;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
/**
 * This class responsible for showing content on the home page.
 * @author Sandip Pradhan
 *
 */
 
public class HomeView {
	//label to display text
	Label favourite = null, recent = null;
	//scroll pane to enable scrolling on overflow
	ScrollPane scrollPane = null;
	//parent scroll
	ScrollPane parentScroll;
	//Tile pane to display playlist in tiles
	TilePane favouriteTilePane = null, recentTilePane = null;
	//root layout that act as a parent
	VBox contentHolder = null;
	//file handler object to read/write playlist files.
	private FileHandler fileHandler;
	/**
	 * Constructor initialises objects 
	 */
	public HomeView() {
		//initialising labels with text
		favourite = new Label("favourite");
		recent = new Label("Recents");
		//initialise fileHandler
		fileHandler = new FileHandler().getInstance();
		//initialising layouts
		parentScroll = new ScrollPane();
		scrollPane = new ScrollPane();
		favouriteTilePane = new TilePane();
		recentTilePane = new TilePane();
		contentHolder = new VBox();
		//set id for referencing
		favourite.setId("favourite_text");
		recent.setId("recent_text");
		//define size of controls
		scrollPane.setMaxHeight(1000);
		scrollPane.setMinHeight(500);
		favouriteTilePane.setMaxWidth(Integer.MAX_VALUE);
		recentTilePane.setMaxHeight(500);
		recentTilePane.setPrefHeight(200);
		//setting spacing property
		contentHolder.setPadding(new Insets(30));
		favouriteTilePane.setVgap(20);
		favouriteTilePane.setHgap(20);
		recentTilePane.setVgap(20);
		recentTilePane.setHgap(20);
		scrollPane.setFitToWidth(true);
		contentHolder.setMargin(scrollPane, new Insets(25, 0,25,0));
		contentHolder.setMargin(recentTilePane, new Insets(25, 0,0,0));
		//setting vgrow to make the layout flexible
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
		VBox.setVgrow(recentTilePane, Priority.ALWAYS);
		VBox v = new VBox();
		scrollPane.setContent(favouriteTilePane);
		v.getChildren().addAll(recent, recentTilePane, favourite, scrollPane);
		parentScroll.setContent(v);
		parentScroll.setFitToWidth(true);
		contentHolder.getChildren().addAll(parentScroll);
		ProgramData.homeView = this;
	}
	/**
	 * This method return the parent layout with all its children
	 * @return the parent layout so it can be used in other class as module.
	 */
	public VBox getView() {
		populate();
		return contentHolder;
	}
	/**
	 * This method populate the home view with contents (Playlists)
	 */
	public void populate() {
		//clear favourite tile pane
		favouriteTilePane.getChildren().clear();
		//clear recent tile pane
		recentTilePane.getChildren().clear();
		//get the list of playlist file
		List<Playlist> playlists = fileHandler.loadPlaylist();
		//for each playlist
		for(Playlist playlist: playlists) {
			//if its favourite
			if(playlist.getIsFavourite()) {
				//create playlist object
				PlaylistObject playlistObject = new PlaylistObject(playlist.getPlaylistName(), Integer.toString(playlist.getFileCount())+" files", playlist.getFileTypes(), playlist.getFolderColour());
				//add it to favourite section of the home page.
				favouriteTilePane.getChildren().add(playlistObject.getView());
				
			}
			/*add playlist to recent view if it was edited recently (5 days)
			https://www.javatpoint.com/how-to-calculate-date-difference-in-java
			This code was slightly modified from above source to meet the requirement */
			// Create an instance of the SimpleDateFormat class  
			SimpleDateFormat currentDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat modifiedDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			try {
				/**
				 * create a date object and parse the date stored in playlist file, 
				 * in playlist dates are stored as dd/mm/yyyy the replace function converts this to dd-mm-yyy so parse can undersatnd it.
				 */
				Date currentDate = currentDateFormat.parse(fileHandler.getDateAdded().replace("/", "-"));
				//parse current date and create date object 
				Date modifiedDate = modifiedDateFormat.parse(playlist.getDateModified().replace("/","-"));
				//calculate time difference in milliseconds
				long timeDifference = modifiedDate.getTime() - currentDate.getTime();
				//calculate days by dividing the time difference by 1000 as the time difference is in milliseconds, 60 = seconds, 60 = minutes, 24 = hours.  
				long daysDifference = (timeDifference/(1000*60*60*24))%365;
				
				//if the day difference is less than 5
				if(daysDifference < 5) {
					//create playlist object and initialise it 
					PlaylistObject playlistObject = new PlaylistObject(playlist.getPlaylistName(), Integer.toString(playlist.getFileCount())+" files", playlist.getFileTypes(), playlist.getFolderColour());
					//add the playlist object to resents section of the home page.
					recentTilePane.getChildren().add(playlistObject.getView());
				}
			} catch (ParseException e) {
				//write log
				Logger.writeLog("HomeView: "+e.getMessage());
			}
			
			
	
			
		}
	}
	
	
	


}
