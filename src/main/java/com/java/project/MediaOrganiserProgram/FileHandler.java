package com.java.project.MediaOrganiserProgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/*
 * This class enable program to select files and directory. This class also read and writes to a playlist file.
 */
public final class FileHandler extends Metadata{
	//creating singleton
	private static final FileHandler fileHandler = new FileHandler();
	//this string is used to separate attributes when saving into file
	private static String pattern;
	//DialogManager object to display message
	private static DialogManager dialogManager = new DialogManager().getInstance();
	//constructor
	public FileHandler(){
		//set string pattern 
		pattern = "x0000001";
	}
	
	/**hTis method opens a file manager so user can select files and return those files
	 * 
	 * @return List of files selected by the user
	 */
	public List<File> chooseFile() {
		//creating filter list so that the user can only select supported file
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Media Files", supportedFiles);
		//creating FileChooser object so the program can open file manager
		FileChooser fc = new FileChooser();
		//adding the filter list to file manager
		fc.getExtensionFilters().add(filter);
		//save the selected file in a list
		List<File> file = fc.showOpenMultipleDialog(null);
		//return the list of files selected by user
		return file;
		
	}
	/**This method opens a file manager where user can select a directory and return that directory as file
	 * 
	 * @return directory selected by a user.
	 */
	public  File chooseDirectory() {
		//directory chooser object to open file manager 
		DirectoryChooser directoryChooser = new DirectoryChooser();
		//setting the title of the directory chooser to display
		directoryChooser.setTitle("Choose a directory");
		//open the file manager and save the user selection to a file
		File directory = directoryChooser.showDialog(null);
		//return the directory
		return directory;
	}
	
	/**
	 * Method takes a file name and creates a playlist
	 * @param name is the name of the file or a playlist
	 */
	public void createPlaylistFile(String name) {
		//logging if user made any modification that needs saving, so it can we checked when user try to exit the application
		ProgramData.changes = true;
		//create playlist directory if does not exist
		if(!new File("playlists").exists()) {
			new File("playlists").mkdir();
		}
		//create file object
//		File playlist = new File("playlists/"+name+".plst"); old code
		File playlist = new File("playlists/temp_"+name+".plst"); //new code
		//create playlist if it does not exit
		if(!playlist.exists()) {
			try {
				playlist.createNewFile();
				
			} catch (IOException e) {
				//log any io exception
				Logger.writeLog("FileHandler: "+e.getMessage());
			}
		}else {
			//show message
			dialogManager.showMessage("Error", "couldn't create "+name+".plst file. Possibly the file already exist");
			//log any errors
			Logger.writeLog("FileHandler: couldn't create "+name+".plst file. Possibly the file already exist");
		}

	}
	/**
	 * Method takes a playlist object and write its metadata to its playlist file
	 * @param playlist is a playlist object
	 * @see Playlist
	 */
	public void writePlaylistMetaData(Playlist playlist) {
		//logging if user made any modification that needs saving, so it can we checked when user try to exit the application
		ProgramData.changes = true;
		//load the playlist file
//		File file = new File("playlists/"+playlist.getPlaylistName()+".plst"); old code
		File file = new File("playlists/temp_"+playlist.getPlaylistName()+".plst");
		String meta = playlist.getPlaylistName() + pattern+playlist.getFolderColour()+pattern+Integer.toString(playlist.getFileCount())+pattern+playlist.getDateModified()+pattern+playlist.getFileTypes()[0]+pattern+playlist.getFileTypes()[1]+pattern+playlist.getFileTypes()[2]+pattern+Boolean.toString(playlist.getIsFavourite())+pattern+playlist.getDateAdded();
		writeToFile(file, meta);
			
		
	};
	/**
	 * Method deletes temp playlist file
	 */
	public void deleteTempFiles() {
		File directory = new File("playlists/");
		File[] files = directory.listFiles();
		if(files != null) {
			for(File file: files) {
				if(file.getName().startsWith("temp_")) {
					file.delete();
				}
			}
		}
		
	}
	/**
	 * Method renames temp playlist files to the original playlist name
	 */
	public void renameTempFiles() {
		File directory = new File("playlists/");
		File[] files = directory.listFiles();
		for(File file: files) {
			if(file.getName().startsWith("temp_")) {
				//non temp file name
				String fileName = file.getName().split("temp_")[1];
				//delete the old file if exist
				File old = new File("playlists/"+fileName);
				if(old.exists()) {
					boolean res = old.delete();
				}
				
				file.renameTo(new File("playlists/"+fileName));
			}
		}
	}
	/**
	 * Method to generate temp file for each playlist file. Temp files are used during 
	 * the application life cycle, 
	 * and the main playlist file is updated using temp file if user approves it.
	 * @throws IOException 
	 */
	public void createTempFiles() throws IOException {
		File directory = new File("playlists/");
		if(directory.exists()) {
			File[] files = directory.listFiles();
			if(files.length > 0) {
				for(File file: files) {
					String nonTempName;
					if(file.getName().startsWith("temp_")) {
						nonTempName = file.getName().split("temp_")[1];
					}else {
						nonTempName = file.getName();
					}
					
					File tempFile = new File("playlists/temp_"+nonTempName);
					if(!tempFile.exists()) {
						try {
							tempFile.createNewFile();
						} catch (IOException e) {
							Logger.writeLog("FileHandler: "+ e.getMessage());
						}
					}
					//copy content from old file to temp file, Source https://www.geeksforgeeks.org/different-ways-to-copy-files-in-java/
					// Creating two channels one input and other output
					// by creating two objects of FileChannel Class
					FileChannel oldFile = null;
					FileChannel temp = null;
					try {
						oldFile = new FileInputStream(file.getAbsoluteFile()).getChannel();
						temp = new FileOutputStream(tempFile.getAbsoluteFile()).getChannel();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
					

					// Try block to check for exceptions
					try {

						// Transferring files in one go from source to
						// destination using transferFrom() method
						temp.transferFrom(oldFile, 0, oldFile.size());
						// we can also use transferTo
						// src.transferTo(0,src.size(),dest);
					}

					// finally keyword is good practice to save space in
					// memory by closing files, connections, streams
					finally {

						// Closing the channels this makes the space
						// free

						// Closing the source channel
						oldFile.close();

						// Closing the destination channel
						temp.close();
					}
				}
			}
		}
		
		
	}
	/**
	 * This method takes a file name and exports it to user specified location
	 * @param name is the file name
	 */
	public void exportPlaylist(String name) {
		DirectoryChooser fileChooser = new DirectoryChooser();
		fileChooser.setTitle("Export playlist file");
		File directory = fileChooser.showDialog(null);
		File playlist = new File("playlists/temp_"+name+".plst");
		if(directory != null) {
			File export = new File(directory.getAbsoluteFile()+"/"+name+".plst");
			if(!export.exists()) {
				try {
					export.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileChannel playlistChannel = null;
			FileChannel exportChannel = null;
			try {
				playlistChannel = new FileInputStream(playlist.getAbsoluteFile()).getChannel();
				exportChannel = new FileOutputStream(export.getAbsoluteFile()).getChannel();
				exportChannel.transferFrom(playlistChannel, 0, playlistChannel.size());
				
			} catch (FileNotFoundException e) {
				Logger.writeLog("FileHandler: "+ e.getMessage());
			} catch (IOException e) {
				Logger.writeLog("FileHandler: " + e.getMessage());
			}
			
			finally {
				try {
					playlistChannel.close();
					exportChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		
	}
	/**
	 * This Method imports a playlist file from user selected location
	 */
	public  void importPlaylist() {
		FileChooser files = new FileChooser();
		files.getExtensionFilters().add(new ExtensionFilter("Playlist file","*.plst"));
		List<File> playlists = files.showOpenMultipleDialog(null);
		if(playlists != null) {
			for(File file: playlists) {
				try {
					FileChannel playlistFile = new FileInputStream(file.getAbsolutePath()).getChannel();
					//create the playlist file in playlists directory
					File playlist = new File("playlists/"+file.getName());
					FileChannel copyFile = new FileOutputStream(playlist.getAbsolutePath()).getChannel();
					copyFile.transferFrom(playlistFile, 0, playlistFile.size());
					playlistFile.close();
					copyFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	/**
	 * This method writes string to a file
	 * @param file is the file to write string
	 * @param content is the string to write into a file
	 */
	private  void writeToFile(File file, String content) {
		try {
			//file writer object to write 
			FileWriter fw = new FileWriter(file);
			//buffered writer object to write efficiently
			BufferedWriter bw = new BufferedWriter(fw);
			//writing the metadata
			bw.write(content+"\n");
			//closing the file
			bw.close();
			
			
		} catch (IOException e) {
			//write any errors
			Logger.writeLog("FileHandler "+ e.getMessage());
		}
	}
	
	/**
	 * Function takes a playlist name and list of medias to save into a playlist file
	 * @param playlist is the name of the playist
	 * @param media is the list of media array list
	 */
	public void writeMeidaItems(String playlist, List<Media> media) {
		String write = "";
		//create playlist object of current playlist
		Playlist plst = readPlaylistMetaData(playlist);
		//update the file count in metadata
		plst.setFileCount(media.size()+plst.getFileCount());
		//add already saved medias list in playlist file to new medias list
		media.addAll(readMediaList(playlist));
		//media types in playlist
		String[] types = new String[3];
		plst.setFileTypes(types);
		//opening the playlist file
		File f = new File("playlists/"+playlist+".plst");
		File temp = new File("playlists/temp_"+playlist+".plst");
		if(!temp.exists()) {
			try {
				temp.createNewFile();
			} catch (IOException e) {
				Logger.writeLog("FileHandler: "+e.getMessage());
			}
		}
		//copy content from old file to temp file, Source https://www.geeksforgeeks.org/different-ways-to-copy-files-in-java/
		try {
			Files.copy(f.toPath(), temp.toPath());
		} catch (IOException e1) {
			Logger.writeLog("FileHandler: "+e1.getMessage());
		}
		//if the file doesn't exist
		if(!temp.exists()) {
			//show message
			dialogManager.showMessage("Error", "temp_"+playlist+".plst doesnt exist. Unable to write the media item");
			//log the error
			Logger.writeLog("FileHandler: temp_"+playlist+".plst doesnt exist. Unable to write the media item");
			//exit the function
			return;
		}
		//filewriter object to write to file
		FileWriter fw = null;
		try {
			//initialise file writer, with a file and in append mode
			fw = new FileWriter(temp, true);
		} catch (IOException er) {
			//log io exception
			Logger.writeLog("FileHandler: "+er.getMessage());
		}
		//creating buffered writer to write efficiently
		BufferedWriter bw = new BufferedWriter(fw);
		//if the list of medias not null or empty
		if(media != null || media.size() > 0) {
			//for each media m
			for(Media m: media) {
				//get the attributes of each media object
				String mediaItem = m.getName()+pattern+m.getLocation()+pattern+ m.getMediaType() + pattern + m.getMediaResolution() + pattern + m.getDateAdded() + pattern+m.getMediaSize()+pattern+m.getDuration();
				write += mediaItem+"\n";
				if(m.getMediaType().equals("png") || m.getMediaType().equals("jpg") || m.getMediaType().equals("jpeg")) {
					types[0] = "image";
				}else if(m.getMediaType().equals("mp4") || m.getMediaType().equals("mov") || m.getMediaType().equals("mkv")) {
					types[1] = "video";
				}else if(m.getMediaType().equals("mp3")) {
					types[2] = "audio";
				}
			
			}
			
			try {
				plst.setFileTypes(types);
				//update the date Modified
				plst.setDateModified(getDateAdded());
				//first writing the metadata
				writePlaylistMetaData(plst);
				//write the media item attributes 
				bw.write(write);
				//close the file 
				bw.close();
			} catch (IOException e) {
				//log any io exceptions
				Logger.writeLog("FileHandler: "+e.getMessage());
			}
		}
		//update media view
		ProgramData.mediaView.update();
		PlaylistView.populate();
		//update side bar
		ProgramData.sidebarView.setPlaylistInfo(plst.getPlaylistName(), plst.getFileCount()+" file(s)",plst.getFolderColour());
		
	}
	/**
	 * Function takes playlist name and returns a list of playlist saved on that file
	 * @param playlist is the name of the playlist file
	 * @return list of media files in a playlist
	 */
	public List<Media> readMediaList(String playlist){
		//creating empty list of medias 
		List<Media> medias = new ArrayList<Media>();
		//keeping track of line numbers the program reading
		int currentLine = 0;
		//saving the current line into this string
		String currentLineContent = "";
		//opening the playlist file
		File playlistFile = new File("playlists/temp_"+playlist+".plst");
		//creating file reader object
		FileReader fileReader;
		//creating buffered reader object
		BufferedReader bufferedReader = null;
		try {
			//initialising file reader with playlist file
			fileReader = new FileReader(playlistFile);
			//initialising buffered reader with file reader as a parameter
			 bufferedReader = new BufferedReader(fileReader);
			 //while the current line is not null
			 while((currentLineContent = bufferedReader.readLine())!=null) {
				 //ignore the first line as its the metadata and not a medias object
				 if(currentLine == 0) {
					 //increase the line count
					 currentLine += 1;
					 //continue the while loop to next iteration
					 continue;
				 }else {
					 //creating medias object
					 Media media = new Media();
					 //creating array of attributes by splitting the String using pattern 
					 String[] metadataArray = currentLineContent.split(pattern);
					 //setting the values of medias object
					 media.setMedia(metadataArray[0], metadataArray[1], metadataArray[2], metadataArray[3], metadataArray[4], metadataArray[5], metadataArray[6]);
					 //add the media object to array list
					 medias.add(media);
					 //increment the line number
					 currentLine += 1;
				 }
			 }
			 try {
				 //close the playlist file
					bufferedReader.close();
				} catch (IOException e) {
					//log any io exceptions
					Logger.writeLog("FileHandler: "+e.getMessage());
				}
		} catch (IOException e) {
			//log any io exceptions
			Logger.writeLog("FileHandler: "+e.getMessage());
		}
		
		
		//return the array list containing medias
		return medias;
		
	}
	/**
	 * Method takes a playlist name and deletes it
	 * @param name is the playlist name to delete
	 */
	public void deletePlaylist(String name) {
		//loading the playlist file
		File playlist = new File("playlists/"+name+".plst");
		//temp file
		File temp = new File("playlists/temp_"+name+".plst");
		if(temp.exists()) {
			//delete the file
			boolean res =  temp.delete();
			//if the res is false, means deleting was unsuccessful
			if(res == false) {
				//show message
				dialogManager.showMessage("Error", "could not delete temp_"+name+".plst possibly opened by some other process");
				//log error
				Logger.writeLog("FileHandler: could not delete temp_"+name+".plst"+" possible open");
			}
		}
		
		//if the playlist file exist
		if(playlist.exists()) {
			//delete the file
			boolean res =  playlist.delete();
			//if the res is false, means deleting was unsuccessful
			if(res == false) {
				//show message
				dialogManager.showMessage("Error", "could not delete "+name+".plst possibly opened by some other process");
				//log error
				Logger.writeLog("FileHandler: could not delete "+name+".plst"+" possible open");
			}
		}
	}
	
	/**
	 * This methods takes a media object and playlist name, and removes the media file from the playlist
	 * @param playlist is the playlist name
	 * @param m is the Media object
	 * @see Media
	 */
	public void deleteMediaItem(String playlist, Media m) {
		File plst = new File("playlists/temp_"+playlist+".plst");
		Playlist metadata = readPlaylistMetaData(playlist);
		metadata.setFileCount(0);
		List<Media> medias = readMediaList(playlist);
		writePlaylistMetaData(metadata);
		for(int i =0; i < medias.size(); i++) {
			if(medias.get(i).getLocation().equals(m.getLocation()) && medias.get(i).getName().equals(m.getName())) {
				medias.remove(i);
				break;
			}else {
				//show message
				dialogManager.showMessage("Error", " media item ont found, unable to delete from playlist "+playlist);
				Logger.writeLog("FileHandler: media item ont found, unable to delete from playlist "+playlist);
			}
		}
		writeMeidaItems(playlist,medias);
	}
	/** Method takes a playlist name and returns playlist object with its metadata
	 * 
	 * @param playlist is the name of the playlist
	 * @return Playlist object
	 * @see Playlist
	 */
	public Playlist readPlaylistMetaData(String playlist) {
		//creating a playlist object
		Playlist p =  new Playlist();
		//creating file object of the playlist
		File file = new File("playlists/temp_"+playlist+".plst");
		//creating buffered reader object to read the file
		BufferedReader bReader = null;
		try {
			//initialising file reader with playlist file
			FileReader reader = new FileReader(file);
			//initialising buffered reader
			 bReader = new BufferedReader(reader);
			//read the first line and save it in string
			 String s = bReader.readLine();
			 //split the string using the pattern and save it in array
			String[] attributes = s.split(pattern,0);
			//creating file type indicator, this is an array of length 3 keeping track what kind of file this playlist contains (img, vid, mus)
			String[] fileTypeIndicator = {attributes[4],attributes[5],attributes[6]};
			//set the metadata to a playlist object
			p.setPlaylist(attributes[0], attributes[1], Integer.parseInt(attributes[2]), attributes[3], fileTypeIndicator, Boolean.parseBoolean(attributes[7]), attributes[8]);
			bReader.close();
		} catch (FileNotFoundException e) {
			//log any file not found exceptions
			Logger.writeLog("FileHandler: "+e.getMessage());
		} catch (IOException e) {
			//log any io exceptions
			Logger.writeLog("FileHandler: "+e.getMessage());
		}
		
		//return the playlist object
		return p;
	}
	/** Method takes playlist name and return boolean if the playlist exist
	 * 
	 * @param name name of the playlist
	 * @return boolean 
	 */
	public boolean isPlaylistExist(String name) {
		//if the playlist exist
		if(new File("playlists/temp_"+name+".plst").exists()) {
			//return true
			return true;
		}
		//return false if does not exist
		return false;
	}
	
	//Function loads all the playlist file in playlists directory and returns list of playlists
	public ArrayList<Playlist> loadPlaylist() {
		//root directory where playlist files exists
		File playlistPath = new File("playlists/");
		//get all the files in playlists directory
		File[] playlistNames = playlistPath.listFiles();
		//creating empty playlist array
		ArrayList<Playlist> playlists = new ArrayList<Playlist>();
		//if playlist file exists in playlists directory (not null)
		if(playlistNames != null) {
			//for each playlist file 
			for(int i =0; i < playlistNames.length; i++) {
				if(playlistNames[i].getName().startsWith("temp_")) {
					//read the metadata, which returns a playlist object and save it into playlist array. Splitting the string with . to get rid of the file extension.
					playlists.add(readPlaylistMetaData(playlistNames[i].getName().split("temp_")[1].split("[.]")[0]));
				}
				
			}
		}
		
		//return the playlist array
		return playlists;
	}

	/**
	 * This method returns an instance of the class
	 * @return FileHandler instance
	 */
	public FileHandler getInstance() {
		return fileHandler;
	}

	
	
}
