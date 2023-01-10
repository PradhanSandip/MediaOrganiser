package com.java.project.MediaOrganiserProgram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * This class is a Graphical  user window, that is responsible for getting user input to create playlist files.
 * @author Sandip Pradhan
 *
 */
public class UserInput {
	private Label selectFolderColour, error;
	private TextField textBox;
	private ImageView blueFolder, redFolder, greenFolder;
	private String folderSelected = "blue", playlistName = "";
	private HBox folderHolder, r, g,b;
	private VBox view;
	private Button ok, cancel;
	private Stage stage;
	//use this class to create playlist
	Playlist playlist;
	/**
	 * Constructor that initialises the layouts and objects 
	 */
	public UserInput(){
		//initialise layut
		initialize();
		
	}
	/**
	 * This method initialises objects and UI elements
	 */
	private void initialize() {
		//each of these three controlls hold folder image with different colour, where the user can select folder colour 
		r = new HBox(); // holds red folder
		g = new HBox(); // holds green folder
		b = new HBox(); // holds blue folder
		highlightFolder(b); // by default highlight blue folder
		//text filed to enter playlist name
		textBox = new TextField();
		//label
		selectFolderColour = new Label("Select folder Colour");
		error = new Label();
		
		view = new VBox();
		folderHolder = new HBox();
		ok = new Button("Create playlist");
		cancel = new Button("Cancel");
		playlist = new Playlist();
		//set id for reference
		ok.setId("ok");
		cancel.setId("cancel");
		error.setId("error");
		try {
			blueFolder = new ImageView(new Image(new FileInputStream("assets/blue_folder.png")));
			redFolder = new ImageView(new Image(new FileInputStream("assets/red_folder.png")));
			greenFolder = new ImageView(new Image(new FileInputStream("assets/green_folder.png")));
		} catch (FileNotFoundException e) {
			new Logger().writeLog(e.getMessage());
		}
		//set placeholder for text field;
		textBox.setAccessibleText("Playlist name");
		//increase font size
		selectFolderColour.setStyle("-fx-font-size:17px;");
		//set spacing and padding for controls
		view.setSpacing(30);
		folderHolder.setSpacing(30);
		view.setMargin(folderHolder, new Insets(0,10,0,10));
		view.setMargin(textBox, new Insets(30,10,0,10));
		view.setMargin(selectFolderColour, new Insets(0,10,0,10));
		//set element alignment
		view.setAlignment(Pos.TOP_CENTER);
		folderHolder.setAlignment(Pos.TOP_CENTER);
		//Initialise events
		initialiseEvent();
		//add childrens
		r.getChildren().add(redFolder);
		g.getChildren().add(greenFolder);
		b.getChildren().add(blueFolder);
		folderHolder.getChildren().addAll(r,g,b);
		view.getChildren().addAll(textBox,error,selectFolderColour,folderHolder,ok,cancel );
		stage = new Stage();
		Scene scene = new Scene(view,250,380);
		//add stylesheet
		String mainCss = Main.class.getResource("main.css").toExternalForm();
		scene.getStylesheets().add(mainCss);
		stage.setTitle("Create Playlist");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
	
	/**
	 * This methods adds mouse event listener to various elements
	 */
	private void initialiseEvent() {
		//add events to detect which folder colour is selected
				blueFolder.setOnMouseClicked(event ->{
					setFolderColour("blue");
					removeHighlight(r);
					removeHighlight(g);
					highlightFolder(b);
				});
				redFolder.setOnMouseClicked(event ->{
					setFolderColour("red");
					highlightFolder(r);
					removeHighlight(g);
					removeHighlight(b);
				});
				greenFolder.setOnMouseClicked(event ->{
					setFolderColour("green");
					removeHighlight(r);
					highlightFolder(g);
					removeHighlight(b);
				});
				cancel.setOnMouseClicked(event ->{
					close(stage);
					folderSelected = "blue";
					playlistName = "";
					error.setText("");
				});
				ok.setOnMouseClicked(event ->{
					playlistName = textBox.getText();
					if(playlistName.length() == 0) {
						error.setText("Please enter playlist name");
					}else if(playlist.isExist(playlistName)) {
						error.setText("Playlist already exists");
						
					}else {
					close(stage);
					error.setText("");
					
					//TODO: create playlist here;
					LocalDate dt = LocalDate.now();
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
					String formattedDate = dtf.format(dt).toString();
					playlist.createPlaylist(playlistName, folderSelected, 0, formattedDate, null, false, formattedDate);
					PlaylistView.populate();
					
					}
				});
	}
	
	/**
	 * this method sets value to folderSelected attribute.
	 * @param color colour of the folder.
	 */
	private void setFolderColour(String color) {
		this.folderSelected = color;
	}
	
	/**
	 * This method highlights folder when clicked
	 * @param folder is the HBox which contain folder
	 */
	private void highlightFolder(HBox folder) {
		folder.setStyle("-fx-border-color: #7694FF; -fx-border-width: 2;");
		
	}
	/**
	 * This method removes any highlight from folder
	 * @param folder
	 */
	public void removeHighlight(HBox folder) {
		folder.setStyle("-fx-border-width: 0;");
	}
	/**
	 * This method takes a stage and closes it.
	 * @param stage, is the stage to close
	 * @see Stage
	 */
	private void close(Stage stage) {
		stage.close();
	}
}
