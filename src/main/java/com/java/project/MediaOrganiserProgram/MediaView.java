package com.java.project.MediaOrganiserProgram;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MediaView {
	Image playlistIconImage, favoriteIconEnable, favoriteIconDisable;
	ImageView playlistIcon, musicIcon, favoriteButtonIcon;
	VBox container, playlistHeaderInfo, tNameColumn, tTypeColumn, tLocationColumn,tableBody;
	HBox playlistHeader, tableHead;
	ScrollPane scroll;
	Label playlistName, fileCount, dateCreated, name, type, location;
	Button favorite;
	static HBox currentHighlight;
	FileHandler fileHandler;
	String pName, fCount, date;
	boolean isFavorite;
	Playlist playlist;
	//log messages/errors
	Logger log = new Logger();
	public MediaView(String playlistName, String fileCount, String date) {
		this.pName = playlistName;
		this.fCount = fileCount;
		this.date = date;
		fileHandler = new FileHandler().getInstance();
		playlist = fileHandler.readPlaylistMetaData(playlistName);
		isFavorite = playlist.getIsFavourite();
		initialize();
		setStyle();
		addTableHead();
		addTableBody();
		addMedia(fileHandler.readMediaList(this.pName));
		ProgramData.mediaView = this;
	}
	
	public void updateFileCount(int files) {
		this.fCount = Integer.toString(files);
		fileCount.setText(this.fCount);
	}
	//update the mediaview when new media items added to playlist
	public void update() {
		tableBody.getChildren().clear();
		addMedia(fileHandler.readMediaList(this.pName));
	}
	
	private void initialize() {
		playlistIcon = new ImageView();
		container = new VBox();
		tableHead = new HBox();
		tableBody = new VBox();
		playlistHeaderInfo = new VBox();
		playlistHeader = new HBox();
		playlistName = new Label(this.pName);
		fileCount = new Label(this.fCount);
		dateCreated = new Label(this.date);
		tNameColumn = new VBox();
		tLocationColumn = new VBox();
		tTypeColumn = new VBox();
		scroll = new ScrollPane();
		name = new Label("Name");
		type = new Label("Size");
		location = new Label("Type");
		name.setId("media_view_name");
		type.setId("media_view_type");
		location.setId("media_view_location");
		
		try {
			playlistIconImage = new Image(new FileInputStream("assets/playlist_icon.png"));
			playlistIcon.setImage(playlistIconImage);
			favoriteIconEnable = new Image(new FileInputStream("assets/heart_toggle.png"));
			favoriteIconDisable = new Image(new FileInputStream("assets/heart.png"));
			if(isFavorite) {
				System.out.println("is favorite "+isFavorite);
				favoriteButtonIcon = new ImageView(favoriteIconEnable);
			}else {
				System.out.println("is favorite "+isFavorite);
				favoriteButtonIcon = new ImageView(favoriteIconDisable);
			}
			
			
			favoriteButtonIcon.setPreserveRatio(true);
			favoriteButtonIcon.setFitWidth(40);
			favorite = new Button();
			favorite.setStyle("-fx-background-color: transparent;");
			favorite.setGraphic(favoriteButtonIcon);
			playlistHeader.setMargin(favorite, new Insets(45,30,0,0));
			playlistHeader.setHgrow(playlistIcon, Priority.ALWAYS);
			playlistHeader.setHgrow(playlistHeaderInfo, Priority.ALWAYS);
			playlistHeader.setHgrow(favorite, Priority.ALWAYS);
			
		} catch (FileNotFoundException e) {
			log.writeLog(e.getLocalizedMessage());
		}
		
		//layout elements
		playlistHeaderInfo.getChildren().addAll(playlistName,fileCount,dateCreated);
		playlistHeader.getChildren().addAll(playlistIcon,playlistHeaderInfo,favorite);
		container.getChildren().addAll(playlistHeader, tableHead,scroll);
		
		container.setOnMouseClicked(event -> {
			
			SidebarView.setPlaylistInfo(pName, fCount, fileHandler.readPlaylistMetaData(pName).getFolderColour());
		});
		favorite.setOnMouseClicked(event -> {
			if(isFavorite) {
				System.out.println("Playlist was fav, changing to not fav");
				isFavorite = false;
				favoriteButtonIcon.setImage(favoriteIconDisable);
				playlist.setIsFavourite(isFavorite);
			}else {
				System.out.println("Playlist was not fav, changing to fav");
				isFavorite = true;
				favoriteButtonIcon.setImage(favoriteIconEnable);
				playlist.setIsFavourite(isFavorite);
			}
			List<Media> medias = fileHandler.readMediaList(playlist.getPlaylistName());
			fileHandler.writePlaylistMetaData(playlist);
			System.out.println("Current statuc: "+playlist.getIsFavourite());
			fileHandler.writeMeidaItems(playlist.getPlaylistName(), medias);
		});
		
	}
	
		private void setStyle() {
			HBox.setMargin(playlistIcon, new Insets(20,10,10,20));
			HBox.setMargin(playlistHeaderInfo, new Insets(40,10,10,10));
			playlistHeaderInfo.setSpacing(10);
			container.setMargin(tableHead, new Insets(60,20,10,40));
			container.setMargin(scroll, new Insets(25,20,10,40));
			tNameColumn.setSpacing(40);
			tLocationColumn.setSpacing(40);
			tTypeColumn.setSpacing(40);
			
		}
		
		private void addTableHead() {
			tNameColumn.getChildren().add(name);
			tLocationColumn.getChildren().add(location);
			tTypeColumn.getChildren().add(type);
			tableHead.setSpacing(300);
			tableHead.setHgrow(tNameColumn, Priority.ALWAYS);
			tableHead.setHgrow(tTypeColumn, Priority.ALWAYS);
			tableHead.setHgrow(tLocationColumn, Priority.ALWAYS);
			tableHead.getChildren().addAll(tNameColumn, tLocationColumn, tTypeColumn);
//			tableHead.setStyle("-fx-background-color:red;");
			
		}
		private void addTableBody() {
			tableBody.setAlignment(Pos.TOP_CENTER);
			scroll.setContent(tableBody);
			scroll.setFitToWidth(true);
		}
		public void addMedia(List<Media> medias) {
			for(Media media: medias) {
				HBox parentLine = new HBox();
				VBox nameColumn = new VBox();
				VBox sizeColumn = new VBox();
				VBox typeColumn = new VBox();
				Label name = new Label(media.getName());
				Label type = new Label(media.getMediaType());
				Label size = new Label(media.getMediaSize());
				parentLine.setPadding(new Insets(10));
				nameColumn.getChildren().add(name);
				sizeColumn.getChildren().add(size);
				typeColumn.getChildren().add(type);
				setTextSize(name,type,location);
				parentLine.getChildren().addAll(nameColumn,typeColumn,sizeColumn);
				parentLine.setHgrow(nameColumn, Priority.ALWAYS);
				parentLine.setHgrow(typeColumn, Priority.ALWAYS);
				parentLine.setHgrow(sizeColumn, Priority.NEVER);
				sizeColumn.setMaxWidth(250);
				Button delete = new Button();
				try {
					ImageView image = new ImageView(new Image(new FileInputStream("assets/trash.png")));
					image.setPreserveRatio(true);
					image.setFitWidth(20);
					delete.setGraphic(image);
					delete.setStyle("-fx-background-color: transparent; -fx-padding: 0 0 10 20;");
					parentLine.setMargin(delete, new Insets(0,0,0,20));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parentLine.getChildren().add(delete);
				tableBody.setSpacing(10);
				tableBody.getChildren().add(parentLine);
				parentLine.setOnMouseClicked(event ->{
					removePreviousHighlight(currentHighlight);
					highlight(parentLine);
					SidebarView.showMediaMetadata(media);
				});
				delete.setOnMouseClicked(event ->{
					fileHandler.deleteMediaItem(this.pName, media);
				});
				
			}
			updateFileCount(medias.size());
			
		}
		
		
		private void highlight(HBox box) {
			currentHighlight = box;
			box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(28, 100, 2, 0.8), 10, 0, 5, 5); -fx-border-width:0 0 1 0; -fx-border-color: #7694FF; -fx-background-color: #FFE175");
		}
		public static  void removePreviousHighlight(HBox box) {
			if(box != null) {
				box.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(28, 100, 2, 0), 10, 0, 5, 5); -fx-border-width:0 0 0 0; -fx-border-color: #7694FF; -fx-background-color: transparent");
			}
			
		}
		private void setTextSize(Label name, Label type, Label location) {
			name.setStyle("-fx-font-size: 1.2em");
			type.setStyle("-fx-font-size: 1.2em");
			location.setStyle("-fx-font-size: 1.2em");
		}
	
	public VBox getView() {
		return container;
	}
}
