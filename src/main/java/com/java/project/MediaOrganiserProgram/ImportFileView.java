package com.java.project.MediaOrganiserProgram;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * This class created a new window, that act as a form when importing files.
 * @author Sandip Pradhan
 *
 */
public class ImportFileView {
	//parent Vbox holds all the child 
	private static VBox parent;
	//Buttons for different functions
	private static Button fileOnly, directoryOnly, cancel;
	//Stage to create window
	private static Stage stage;
	//playlist name
	static String playlist;
	//FileHandler object to create or modify files
	private static FileHandler fileHandler;
	ImportFileView(String playlist){
		this.playlist = playlist;
	}
	//initialise the layouts.
	private static void setLayout() {
		fileHandler = new FileHandler().getInstance();
		parent = new VBox();
		fileOnly = new Button("File Only");
		directoryOnly = new Button("Directory only");
		cancel = new Button("Cancel");
		parent.getChildren().addAll(fileOnly, directoryOnly, cancel);
		parent.setSpacing(40);
		parent.setAlignment(Pos.CENTER);
		//set id for styling
		cancel.setId("cancel");
		fileOnly.setId("fileOnlyButton");
		directoryOnly.setId("directoryOnlyButton");
		setEvents();
	}
	/**
	 * This method show the window
	 */
	public static void show() {
		setLayout();
		Scene scene = new Scene(parent, 300,400);
		stage = new Stage();
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setAlwaysOnTop(true);
		//add stylesheet
		String mainCss = Main.class.getResource("main.css").toExternalForm();
		scene.getStylesheets().add(mainCss);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
		
	}
	/**
	 * This method closes the window
	 */
	public static void close() {
		stage.close();
	}
	/**
	 * This method sets events to buttons
	 */
	private static void setEvents() {
		//if cancel button is clicked
		cancel.setOnMouseClicked(event ->{
			//close the stage
			close();
		});
		//if file only button is clicked
		fileOnly.setOnMouseClicked(event ->{
			//close the window
			close();
			try {
				//write media metadata to playlist file
				fileHandler.writeMeidaItems(playlist, fileHandler.returnMetadata(fileHandler.chooseFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		//if directory only button is clicked 
		directoryOnly.setOnMouseClicked(event -> {
			//close the stage
			close();
			//open directory chooser
			File dir = fileHandler.chooseDirectory();
			//if directory is not null
			if(dir != null) {
				//get list of files
				File[] files = dir.listFiles();
				//empty List of file
				List<File> fileList = new ArrayList<File>();
				//for each file
				for(File file: files) {	
						//if the file is valid and file is not directory
						if((!file.isDirectory() && fileHandler.isValidFile(file))){
							//add file to file list
							fileList.add(file);
						}
					} 
				try {
					//write media to playlist
					fileHandler.writeMeidaItems(playlist, fileHandler.returnMetadata(fileList));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			
		});
	}
	
}
