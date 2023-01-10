package com.java.project.MediaOrganiserProgram;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
/**
 * This class is responisble for showing content on playlist page
 * @author Sandip Pradhan
 *
 */

public class PlaylistView {
	VBox contentHolder = null;
	Label playlist = null;
	ScrollPane scrollPane = null;
	static TilePane playlistPane = null;
	static FileHandler fileHandler;
	public PlaylistView() {
		contentHolder = new VBox();
		playlist = new Label("Playlist");
		playlist.setId("playlist_text");
		scrollPane = new ScrollPane();
		playlistPane = new TilePane();
		//set spacing
		VBox.setMargin(playlist,  new Insets(25));
		VBox.setMargin(scrollPane,  new Insets(0,25,0,25));
		playlistPane.setVgap(20);
		playlistPane.setHgap(20);
		scrollPane.setFitToWidth(true);
		scrollPane.setMaxHeight(Integer.MAX_VALUE);
		playlistPane.setMaxHeight(Integer.MAX_VALUE);
		scrollPane.setPrefHeight(900);
		playlistPane.setPrefHeight(400);
		//initialise file handler to loads playlists
		fileHandler = new FileHandler().getInstance();
		populate();
		scrollPane.setContent(playlistPane);
		contentHolder.getChildren().addAll(playlist, scrollPane);
		
	}
	/**
	 * This methods populates the page with playlists
	 */
	public static void populate() {
		//load playlist files
		ArrayList<Playlist> p = fileHandler.loadPlaylist();
		//clear the content area
		playlistPane.getChildren().clear();
		//if playlist eixst
		if(p.size() > 0 && p != null) {
			//for each playlist
			for(int i =0; i < p.size(); i++) {
				//insert a playlistViewObject to playlist pane.
				playlistPane.getChildren().add(new PlaylistObject(p.get(i).getPlaylistName(), p.get(i).getFileCount()+" files",p.get(i).getFileTypes(),p.get(i).getFolderColour()).getView());
			}
			
		}
		
	}
	/**
	 * This method return a layout
	 * @return return VBox that contain all child elements.
	 */
	public VBox getView() {
		return contentHolder;
	};
	

}
