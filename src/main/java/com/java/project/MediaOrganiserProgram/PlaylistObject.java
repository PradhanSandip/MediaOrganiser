package com.java.project.MediaOrganiserProgram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class defines how a playlist should look in the user interface.
 * @author Sandip Pradhan
 *
 */

public class PlaylistObject implements Initializable{
	@FXML
	HBox layout = null, rightContent = null;
	@FXML
	VBox leftContent = null;
	Label title = null, fileCount = null;
	ImageView fileLogoView = null, musicIndicatorView = null, videoIndicatorView = null, imageIndicatorView = null;
	SidebarView playlistOptions = null;
	String pName, fCount, folderColour = null;
	MainWindow pc;
	/**
	 * Construction initialises class attributes
	 * @param title title of the playlist
	 * @param fileCount number of files in the playlist
	 * @param fileTypes file types this playlist contains 
	 * @param colour colour of the folder
	 */
	public PlaylistObject(String title, String fileCount, String[] fileTypes, String colour) {
		//initialise objects and layouts
		pName = title;
		pc = new MainWindow();
		layout = new HBox();
		leftContent = new VBox();
		rightContent = new HBox();
		//set title of the playlist
		this.title = new Label(title);
		//display file count in playlist
		this.fileCount = new Label(fileCount);
		//set id for reference
		layout.setId("playlist_layout");
		leftContent.setId("playlist_left_content");
		rightContent.setId("playlist_right_content");
		this.title.setId("playlist_title");
		this.fileCount.setId("playlist_file_count");
		this.fileLogoView = new ImageView();
		try {
			//load an folder image so it can set to a image view
			Image logo = new Image(new FileInputStream("assets/"+colour+"_folder.png"));
			folderColour = colour;
			//set the image to the image view
			this.fileLogoView.setImage(logo);
		} catch (FileNotFoundException e) {
			//write log
			Logger.writeLog("PlaylistObject "+ e.getMessage());
		}
		//check what kind of file type this playlist contains and show appropriate indicators
		for(int i =0; i < fileTypes.length; i++) {
			//if the playlist contains image file
			if(fileTypes[i].equals("image")) {
				//add image indicator to imave view
				HandleImage("assets/image_indicator.png", imageIndicatorView);
			}else if(fileTypes[i].equals("video")){ // if the playlist contains video file type
				//add video indicator to image view
				HandleImage("assets/video_indicator.png", videoIndicatorView);
				
			}else if(fileTypes[i].equals("audio")) { // if the playlist contains audio file type
				//add music indicator to image view
				HandleImage("assets/music_indicator.png", musicIndicatorView);
			}
		}
		//add margin to the right content 
		layout.setMargin(rightContent, new Insets(0, 0, 0, 30));
		
		//add title, filecount and file logo to left side of playlist
		leftContent.getChildren().addAll(fileLogoView, this.title, this.fileCount);
		
		//add the elements to main layout 
		layout.getChildren().addAll(leftContent,rightContent);
		//initialises UI elements
		initialize(null, null);
	}
	/**
	 * This method adds a image to a image view object.
	 * @param location location of the image
	 * @param iv ImageView  object
	 * @see ImageView
	 */
	public void HandleImage(String location, ImageView iv) {
		//initialise image view
		iv = new ImageView();
		try {
			//load image
			Image indicator = new Image(new FileInputStream(location));
			//set image
			iv.setImage(indicator);
			//add image to rightContent layout
			rightContent.getChildren().add(iv);
		} catch (FileNotFoundException e) {
			//write log
			Logger.writeLog("PlaylistObject: "+ e.getMessage());
		}
	}
	
	/**
	 * This methods initialises size of various ui elements
	 */
	private void InitialiseSize() {
		layout.setMinWidth(400);
		layout.setMaxWidth(400);
		layout.setMinHeight(150);
		layout.setMaxHeight(150);
		leftContent.setPrefHeight(150);
		leftContent.setPrefWidth(200);
		rightContent.setPrefHeight(150);
		rightContent.setPrefWidth(200);
		
		
		
	}
	/**
	 * This function return a parent VBox layout
	 * @return parent HBox containing all the children
	 * @see HBox
	 */
	@FXML
	public HBox getView() {
		
		return layout;
	}
	
	
	
	/**
	 * Initialiser method that initialises UI elements and allow the class to access them. 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//if a playlist is clicked
		layout.setOnMouseClicked(event ->{
			//initialise side bar with playlist information
			SidebarView.setPlaylistInfo(title.getText(), fileCount.getText(), folderColour);
			SidebarView.showTopContent();
			//boolean that keep tracks of if media view is active.
			ProgramData.mediaPage = true;
			//if the content layout from main window is null exit the function.
		    if(ProgramData.pc.content_layout == null) {
		    	//write log
		    	Logger.writeLog("PlaylistObject: content layout is null, exiting function");
		    	return;
		    }else {
		    	//clear the content page
		    	ProgramData.pc.content_layout.getChildren().clear();
		    	//add the header
		    	ProgramData.pc.initializeHeader();
		    	//file handler object
		    	FileHandler fileHandler = new FileHandler().getInstance();
		    	//if the playlist exist.
		    	if(fileHandler.isPlaylistExist(pName)) {
		    		//load the playlist
		    		Playlist playlist = fileHandler.readPlaylistMetaData(pName);
			    	//add the media items from playlist file to the content page
		    		ProgramData.pc.content_layout.getChildren().add(new MediaView(playlist.getPlaylistName(), Integer.toString(playlist.getFileCount()), playlist.getDateAdded()).getView());
		    	}else {
		    		//write log
		    		Logger.writeLog("PlaylistObject: playlist doesnt exist");
		    	}
		    	
		    }
			
		});
		
	}

	
	

	
	

}
