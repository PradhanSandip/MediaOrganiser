package com.java.project.MediaOrganiserProgram;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * This is the main class and starting point of the program
 * @author Sandip Pradhan
 *
 */
public class Main extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		//create temp playlist files
		new FileHandler().getInstance().createTempFiles();
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("primary" + ".fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1200,700);
		stage.setTitle("Media Organiser");
		String mainCss = Main.class.getResource("main.css").toExternalForm();
		scene.getStylesheets().add(mainCss);
		stage.setScene(scene);
		//remove focus from search box if it clicked outside of the search box
		scene.setOnMouseClicked(event -> {
			if(scene.getFocusOwner().getParent() != null) {
				scene.getFocusOwner().getParent().requestFocus();
			}
			
		});
		
		stage.show();
		//prevent window close
		Platform.setImplicitExit(false);
		//if window close is requested
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        //if user made any changes
		    	if(ProgramData.changes) {
		    		//ask the user if they want to save changes
		        	new DialogManager().getInstance().closeWindowDialog(stage);
		        }else {
		        	
		        }
		    	//delete temp files
		    	new FileHandler().deleteTempFiles();
		    }
		});
	}

		public static void main(String[] args) {
		launch(args);
	}

	

}
