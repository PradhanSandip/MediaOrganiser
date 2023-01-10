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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * This class is the main window class. This class is responsible for user interface.
 * @author Sandip Pradhan
 *
 */
public final class MainWindow implements Initializable{
	
	
	@FXML
	AnchorPane window;
	@FXML
	HBox  parent_hbox;
	@FXML
	VBox nav_layout, content_layout, sidebar_layout, dummy;
	@FXML
	Stage stage;
	@FXML
	Button home, playlist;
	HomeView homeView;
	PlaylistView playlistView;
    SidebarView playlistOptions = null;
    /**
	 * This method initialises size of different GUI elements.
	 */
	@FXML
	private void initializeLayoutSize() {
		HBox.setHgrow(nav_layout, Priority.NEVER);
		HBox.setHgrow(content_layout, Priority.ALWAYS);
		HBox.setHgrow(sidebar_layout, Priority.ALWAYS);
		nav_layout.setMaxWidth(75);
		nav_layout.setMinWidth(75);
		content_layout.setMaxWidth(Integer.MAX_VALUE);
		content_layout.setMinWidth(250);
		sidebar_layout.setMaxWidth(220);
		sidebar_layout.setMinWidth(150);
		sidebar_layout.setPrefWidth(150);
	}
	
	/**
	 * 	This method initialises navigation pane of the user interface
	 */
	@FXML
	private void initializeNavBar() {
		Image logo = null;
		try {
			logo = new Image(new FileInputStream("assets/logo.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ImageView logoImageView = new ImageView(logo);
		logoImageView.setId("logoImageView");
		//set size of images
		int size = 45;
		logoImageView.setFitHeight(size);
		
		//preserve aspect ratio of  images
		logoImageView.preserveRatioProperty().set(true);
		//add spacing
		nav_layout.setSpacing(20);
		nav_layout.setMargin(logoImageView, new Insets(30, 0,0,15));
	
		//add padding
		
		VBox.setVgrow(logoImageView, Priority.ALWAYS);
		nav_layout.setAlignment(Pos.TOP_LEFT);
		//navigation buttons
		home = new Button();
		playlist = new Button();
		//set button id for referencing
		home.setId("home");
		playlist.setId("playlist");
		//set button size
		home.setPrefSize(150, 75);
		playlist.setPrefSize(150, 75);
		//set image to a button
		setImageToButton(home, "assets/home.png");
		setImageToButton(playlist, "assets/playlist.png");
		nav_layout.setMargin(home, new Insets(50, 0,0,0));
		//set event listner
		navigate(home);
		navigate(playlist);
		
		//homeImageView, playlistImageView, recentImageView, settingsImageView,
		
		nav_layout.getChildren().addAll(logoImageView, home, playlist);	
	}
		
		/**
		 * This method sets a image to a button
		 * @param button a button to apply image
		 * @param location image location
		 */
	private void setImageToButton(Button button, String location) {
		try {
			Image image = new Image(new FileInputStream(location));
			ImageView imageView = new ImageView(image);
			imageView.setFitWidth(55);
			imageView.preserveRatioProperty().set(true);
			button.setGraphic(imageView);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method initialises header section of the user interface this includes program name and search bar.
	 */
	@FXML
	public void initializeHeader(){
		Label title = new Label("Media Organiser");
		title.setId("title");
		Image line = null, magnifyGlass = null;
		try {
			line = new Image(new FileInputStream("assets/line.png"));
			magnifyGlass = new Image(new FileInputStream("assets/search.png"));
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ImageView lineView = new ImageView(line);
		ImageView magnifyGlassView = new ImageView(magnifyGlass);
		lineView.setFitWidth(0);
		magnifyGlassView.setFitHeight(20);
		lineView.setPreserveRatio(true);
		magnifyGlassView.setPreserveRatio(true);
		StackPane searchPane = new StackPane();
		TextField searchBox = new TextField();
		searchBox.setPromptText("Search");
		searchBox.setFocusTraversable(false);
		searchBox.setMinHeight(40);
		searchPane.setPadding(new Insets(20));
		searchPane.getChildren().addAll(searchBox, magnifyGlassView);
		searchPane.setAlignment(magnifyGlassView, Pos.CENTER_RIGHT);
		searchPane.setMargin(magnifyGlassView, new Insets(0,10,0,0));
		content_layout.getChildren().addAll(title,lineView,searchPane);
		
	}
	/**
	 * This method initialises sidebar section of the interface
	 */
	
	@FXML
	private void intializeSidebar() {
		
		playlistOptions = new SidebarView();
		VBox root = playlistOptions.getView();
		VBox.setVgrow(root, Priority.ALWAYS);
		sidebar_layout.getChildren().add(root);
			
		
	}
	
	/**
	 * This method attach mouse event handler to a button, and used to navigate main section of the program.
	 * @param b Button object, used for navigation
	 */
	@FXML 
	public void navigate(Button b){
		b.setOnMouseClicked(event -> {
			if(event.getSource().equals(home)){
				SidebarView.hideTopContent();
				content_layout.getChildren().clear();
				initializeHeader();
				removeBorder(new Button[] {playlist});
				addBorder(home);
				homeView.populate();
				content_layout.getChildren().add(homeView.getView());
				ProgramData.mediaPage = false;
			}else if(event.getSource().equals(playlist)) {
				SidebarView.hideTopContent();
				content_layout.getChildren().clear();
				removeBorder(new Button[] {home});
				addBorder(playlist);
				initializeHeader();
				//clearing cache, prevent loading playlist which is already deleted but saved in previous playlist view object
				playlistView = new PlaylistView();
				content_layout.getChildren().add(playlistView.getView());
				ProgramData.mediaPage = false;
			}
		});
	}
	
	
	/**
	 * This method adds side border (right) to a button
	 * @param b button to add border to
	 */
	@FXML
	private void addBorder(Button b) {
		b.setStyle("-fx-border-width: 0 6 0 0; -fx-border-color: blue;");
	}
	
	/**
	 * This method removes border from a button
	 * @param b button to remove border from
	 */
	@FXML 
	private void removeBorder(Button[] b) {
		for(int i = 0; i < b.length; i++) {
			b[i].setStyle("-fx-border-width: 0 0 0 0;");
		}
		
	}
	/**
	 * This method hides top content from the side bar (playlist options), if node element is clicked.
	 * @param n Node element 
	 * @see Node
	 */
	
	void hideSidebarOptions(Node n) {
		n.setOnMouseClicked(event ->{
			SidebarView.hideTopContent();
		});
	}
	/**
	 * This method initialises FXML elements, and allow the program to access elements once loaded. 
	 * Accessing elements outside this method will cause error as elements are loaded when this method is called.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeLayoutSize();
		initializeNavBar();
		initializeHeader();
		intializeSidebar();
		homeView = new HomeView();
		playlistView = new PlaylistView();
		addBorder(home);
		content_layout.getChildren().add(homeView.getView());
		//add event 
		nav_layout.setOnMouseClicked(event -> {
			if(!ProgramData.mediaPage) {
				SidebarView.hideTopContent();
			}
			
		});
		ProgramData.pc = this;
		
	}
	
	
}
