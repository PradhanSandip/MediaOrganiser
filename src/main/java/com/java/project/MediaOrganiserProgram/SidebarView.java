package com.java.project.MediaOrganiserProgram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
/**
 * This class is responsible for handling and showing information on the side bar of the graphical user interface
 * @author Sandip Pradhan
 *
 */
public final class SidebarView {
	
	//layout objects, used to place child elements inside.
	private static VBox topContent = null, bottomContent = null, parent = null;
	//Button objects
	private static Button importButton = null , importFileButton = null, deleteButton = null, createButton = null, exportButton = null;
	//Labels to show text on screen
	private static Label playlist_name = new Label(":("), file_count = null;
	//ImageView to show folder colour
	private static ImageView folder_logo = null;
	//String holding information about a playlist (playlist name, file count and the colour of the folder
	private static String pName, count, folderColour;
	//Playlist object to display information or create new playlist
	private static Playlist playlist;
	//FileHandler object
	FileHandler fileHandler = new FileHandler().getInstance();
	/**
	 * Constructor initialises layouts and objects
	 */
	SidebarView(){
		//initialise playlist object
		playlist = new Playlist();
		//initialise layout
		initializeLayout();
		//set event to create playlist button
		createButton.setOnMouseClicked(event ->{
			//open a window to get user input
			new UserInput();
			
		});
		//when the delete button is clicked
		deleteButton.setOnMouseClicked(event ->{
			//delete the playlist 
			fileHandler.deletePlaylist(playlist_name.getText());
			//reload the playlist lists
			PlaylistView.populate();
			//clear the main content page so it can be updated with new information
			ProgramData.pc.content_layout.getChildren().clear();
			//add the header back to the content area
			ProgramData.pc.initializeHeader();
			//re add the playlistObject to the content area, this acts as a refresh
			ProgramData.pc.content_layout.getChildren().add(new PlaylistView().getView());
			//hide the top content in the side bar (playlist options) as it should not exist when the playlist is deleted
			hideTopContent(); 
		});
		//when import file button is pressed
		importFileButton.setOnMouseClicked(event -> {
			//show a import file window with the options, where user can select files or directory
			ImportFileView importFileView = new ImportFileView(playlist_name.getText());
			importFileView.show();
			
		});
		//when export button is clicked 
		exportButton.setOnMouseClicked(event ->{
			//export the playlist
			fileHandler.exportPlaylist(pName);
		});
		//when import button is clicked
		importButton.setOnMouseClicked(event -> {
			//copy the playlist to playlists folder.
			fileHandler.importPlaylist();
		});
		
		ProgramData.sidebarView = this;
		
	}
	/**
	 * This method initialises UI elements
	 */
	private void initializeLayout() {
		//Initialise layouts and controls
		parent = new VBox();
		topContent = new VBox();
		bottomContent = new VBox();
		importButton = new Button("Import Playlist");
		importFileButton = new Button("Import File");
		exportButton = new Button("Export Playlist");
		deleteButton = new Button("Delete");
		createButton = new Button("Create playlist");
		file_count = new Label();
		folder_logo = new ImageView();
		
		//set id for referencing
		importButton.setId("import_button");
		importFileButton.setId("import_file_button");
		exportButton.setId("export_button");
		deleteButton.setId("delete_button");
		createButton.setId("create_button");
		
		//folder image property
		//maintain aspect ratio
		folder_logo.setPreserveRatio(true);
		//set image height
		folder_logo.setFitWidth(70);
		
		//specify VBox layout property
		VBox.setVgrow(bottomContent, Priority.ALWAYS);
		VBox.setVgrow(topContent, Priority.ALWAYS);
		parent.setMaxHeight(Integer.MAX_VALUE);
		
		
		//define control's size
		topContent.setMaxHeight(1000);
		bottomContent.setMaxHeight(500);
		importFileButton.setMaxWidth(200);
		importFileButton.setMinHeight(45);
		importButton.setMaxWidth(200);
		importButton.setMinHeight(45);
		deleteButton.setMaxWidth(200);
		deleteButton.setMinHeight(45);
		exportButton.setMaxWidth(200);
		exportButton.setMinHeight(45);
		createButton.setMaxWidth(200);
		createButton.setMinHeight(45);
		
		//add spacing between elements
		topContent.setSpacing(25);
		bottomContent.setSpacing(25);
		topContent.setMargin(folder_logo, new Insets(40,0,0,0));
		//set alignment
		topContent.setAlignment(Pos.TOP_CENTER);
		bottomContent.setAlignment(Pos.CENTER);
		hideTopContent();
		//add the Child elements to parent
		topContent.getChildren().addAll(folder_logo, playlist_name, file_count, importFileButton, exportButton, deleteButton);
		bottomContent.getChildren().addAll(importButton, createButton);
		parent.getChildren().addAll(topContent, bottomContent);
		
		
	}
	
	
	/**
	 * This method unhides the top content of the sidebar 
	 */
	public static void showTopContent() {
		topContent.setVisible(true);
		
	}
	/**
	 * This method hides the top content of the sidebar
	 */
	public static void hideTopContent() {
		topContent.setVisible(false);
	}
	/**
	 * This method sets media metadata to the sidebar and shows it when a media item is clicked
	 * @param media is the media object
	 */
	public static void showMediaMetadata(Media media) {
		//string defines colour name, used to define text of the colour
		String colour = "black";
		//font size
		String fontSize = "1.2em";
		//font weight
		String bold = "bold";
		//font weight
		String normal = "normal";
		//initialises labels
		Label nameLabel = new Label("Name");
		Label typeLabel = new Label("Media Type");
		Label locationLabel = new Label("Location");
		Label sizeLabel = new Label("Size");
		Label durationLabel = new Label("Duration");
		Label dateLabel = new Label("Date added");
		Label resolutionLabel = new Label("Resolution");
		//set styles to labels
		setTextStyle(nameLabel,colour, fontSize, bold);
		setTextStyle(typeLabel,colour, fontSize, bold);
		setTextStyle(locationLabel,colour, fontSize, bold);
		setTextStyle(sizeLabel,colour, fontSize, bold);
		setTextStyle(durationLabel,colour, fontSize, bold);
		setTextStyle(dateLabel,colour, fontSize, bold);
		setTextStyle(resolutionLabel,colour, fontSize, bold);
		//initialises labels with values
		Label name = new Label(media.getName());
		Label type = new Label(media.getMediaType());
		Label location = new Label(media.getLocation());
		//show all text even if the string is too long
		location.setWrapText(true);
		//padding
		location.setPadding(new Insets(0,10,0,10));
		//initialises labels with a value
		Label size = new Label (media.getMediaSize());
		Label duration = new Label (media.getDuration());
		Label dateAdded = new Label(media.getDateAdded());
		Label resolution = new Label(media.getMediaResolution());
		//set styles for the labels 
		setTextStyle(name,colour, fontSize, normal);
		setTextStyle(type,colour, fontSize, normal);
		setTextStyle(location,colour, fontSize, normal);
		setTextStyle(size,colour, fontSize, normal);
		setTextStyle(duration,colour, fontSize, normal);
		setTextStyle(dateAdded,colour, fontSize, normal);
		setTextStyle(resolution,colour, fontSize, normal);
		//button to close the media metadata informations
		Button close = new Button("Close");
		//this layout holds all the label elements 
		VBox dataHolder = new VBox();
		//add the labels as children
		dataHolder.getChildren().addAll(nameLabel, name, typeLabel, type, locationLabel, location,sizeLabel, size, durationLabel, duration, dateLabel, dateAdded, resolutionLabel, resolution);
		//set the alignment of the dataHolder to top centre
		dataHolder.setAlignment(Pos.TOP_CENTER);
		//define space between each children
		dataHolder.setSpacing(10);
		//set margin
		parent.setMargin(dataHolder, new Insets(30,0,0,0));
		//remove any previous children
		parent.getChildren().clear();
		//set the alignment of child elements
		parent.setAlignment(Pos.CENTER);
		//make the elements take as much space available
		parent.setVgrow(dataHolder, Priority.ALWAYS);
		//set margin to close button
		parent.setMargin(close, new Insets(0,0,40,0));
		//set id to close button, this only used to style the button in css file.
		close.setId("cancel");
		//add the child elements to the parent
		parent.getChildren().addAll(dataHolder,close);
		//when the close button is clicked
		close.setOnMouseClicked(event -> {
			//remove the highlight from the media item
			MediaView.removePreviousHighlight(MediaView.currentHighlight);
			//clear the side bar
			parent.getChildren().clear();
			//add top and bottom content (buttons and labels)
			parent.getChildren().addAll(topContent, bottomContent);
			//set the side bar with playlist data.
			setPlaylistInfo(pName, count, folderColour);
		});
		
	}
	/**
	 * This method sets a style to a label 
	 * @param label is the label object
	 * @param colour colour of the text
	 * @param size size of the text
	 * @param bold font weight
	 */
	private static void setTextStyle(Label label, String colour, String size, String bold) {
		label.setStyle("-fx-text-fill: "+colour+ "; -fx-font-size:"+size+"; -fx-font-weight:"+bold+";");
	}
	/**
	 * This method initialise the side bar with the playlist information
	 * @param name is the name of the playlist
	 * @param fileCount number of files the playlist contains 
	 * @param folderLogo folder colour
	 */
	public static void setPlaylistInfo(String name, String fileCount, String folderLogo) {
		//initialise the playlist attributes
		pName = name;
		count = fileCount;
		folderColour = folderLogo;
		//set a label with playlist name
		playlist_name.setText(name);
		//set a label with file count
		file_count.setText(fileCount);
		try {
			//set image view with folder colour
			folder_logo.setImage(new Image(new FileInputStream("assets/"+folderLogo+"_folder.png")));
		} catch (FileNotFoundException e) {
			//write log
			Logger.writeLog("SidebarView: "+ e.getMessage());
		}
	}
	/**
	 * return this layout
	 * @return the parent layout with all its children
	 */
	public VBox getView() {
		return parent;
	}
	

}
