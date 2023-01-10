package com.java.project.MediaOrganiserProgram;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * This class display notification/error to users + gets inputs from users
 */
public final class DialogManager {
/**
 * This Method displays a prompt the user whether they want to save changes or not
 *  when trying to exit the program. 
 * @param mainStage stage from main window
 */
	//create FileHandler object
	private static FileHandler fileHandler;
	//creating singleton
	private static final DialogManager dialogManager = new DialogManager();
	public DialogManager() {
		 fileHandler = new FileHandler().getInstance();
	}
	public  void closeWindowDialog(Stage mainStage) {
		//creating stage object (window)
		Stage stage = new Stage();
		//parent control that holds all the child elements 
		VBox parent = new VBox();
		//creating scene to display layout (parent) with window size of 400x150
		Scene scene = new Scene(parent, 400,150);
		//control that holds buttons
		HBox buttonHolder = new HBox();
		//label
		Label description = new Label("Save changes?");
		//buttons
		Button save = new Button("Save");
		Button dontSave = new Button("Don't save");
		//setting id for reference in css
		dontSave.setId("cancel");
		save.setId("ok");
		//adding the button to the button holder
		buttonHolder.getChildren().addAll(save, dontSave);
		//adding main controls to parent layout
		parent.getChildren().addAll(description, buttonHolder);
		//seting hgrow, hgrow allows an element to take as much as space is available.
		buttonHolder.setHgrow(dontSave, Priority.ALWAYS);
		buttonHolder.setHgrow(save, Priority.ALWAYS);
		//setting spacing between the buttons
		buttonHolder.setSpacing(100);
		//setting margins
		parent.setMargin(buttonHolder, new Insets(20,20,20,60));
		parent.setMargin(description, new Insets(20));
		//setting alignment to centre
		parent.setAlignment(Pos.CENTER);
		//setting font size to a label
		description.setStyle("-fx-font-size:20px;");
		//if save button is clicked
		save.setOnMouseClicked(event ->{
			/*rename the temp files (saving), NOTE: temp files act as a playlist file during 
			* program life cycle and all the changes are made to temp files instead of original playlist 
			* files.
			*/
			fileHandler.renameTempFiles();
			//close this window
			stage.close();
			//close the main window
			mainStage.close();
		});
		//if do not save button is pressed 
		dontSave.setOnMouseClicked(event -> {
			/*delete the temp files NOTE: temp files act as a playlist file during 
			* program life cycle and all the changes are made to temp files instead of original playlist 
			* files. Thus getting rid of any save.
			*/
			fileHandler.deleteTempFiles();
			//close main window
			mainStage.close();
			//close this window
			stage.close();
		});
		//load css file
		String mainCss = Main.class.getResource("main.css").toExternalForm();
		//set the css stylesheet to this window
		scene.getStylesheets().add(mainCss);
		//set the title of the window
		stage.setTitle("Save changes?");
		//set always on top to true
		stage.setAlwaysOnTop(true);
		//disable resizing
		stage.setResizable(false);
		//set the scene to a stage
		stage.setScene(scene);
		//disable interaction in main window
		stage.initModality(Modality.APPLICATION_MODAL);
		//shwo this window
		stage.showAndWait();
		
		
	}
	
	/**
	 * This methods show pop up message to the user
	 * @param errorType, type of error as string
	 * @param message, message as string
	 */
	public void showMessage(String errorType, String message) {
		//create a stage
		Stage stage = new Stage();
		//label
		Label text = new Label(message);
		//button
		Button close = new Button("Close");
		//main layout holder
		VBox contentHolder = new VBox();
		//set id
		close.setId("cancel");
		//set text size
		text.setStyle("-fx-font-size:1.2em;");
		//show whole text
		text.setWrapText(true);
		//padding
		text.setPadding(new Insets(0,10,0,10));
		//add children
		contentHolder.getChildren().addAll(text, close);
		//set spacing
		contentHolder.setSpacing(40);
		//disable resizing 
		stage.setResizable(false);
		//if close button is clicked
		close.setOnMouseClicked(EVENT ->{
			//close stage
			stage.close();
		});
		//align content to center
		contentHolder.setAlignment(Pos.CENTER);
		//create window 300 by 150
		Scene scene = new Scene(contentHolder, 300,150);
		//load css
		String css = Main.class.getResource("main.css").toExternalForm();
		//set css
		scene.getStylesheets().add(css);
		//set scene
		stage.setScene(scene);
		//set title
		stage.setTitle(errorType);
		//disable interaction with main window
		stage.initModality(Modality.APPLICATION_MODAL);
		//show this window always on top
		stage.setAlwaysOnTop(true);
		//show window
		stage.showAndWait();
		
	}
	/**
	 * This method return instance of this class
	 * @return DialogManager instance
	 */
	public DialogManager getInstance() {
		return dialogManager;
	}
	
}
