package com.java.project.MediaOrganiserProgram;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import javafx.scene.image.Image;
/**
 * This class retrieves meta data such as size, resolution, track length from a media file.
 * @author Sandip Pradhan
 *
 */
public abstract class Metadata{
	//supported file types
	//public static String[] supportedFiles = {"*.png", "*.PNG", "*.jpg","*.JPG","*.jpeg","*.JPEG","*.mp3","*.MP3","*.mov","*.MOV","*.ogg","*.OGG,","*.mp4","*.MP4","*.mkv","*.mkv","*.flv","*.3gg"};
	public static String[] supportedFiles = {"*.png", "*.jpg","*.jpeg","*.mp3","*.mov","*.ogg","*.mp4","*.mkv","*.3gg"};
	/**
	 * This method takes a list of media files and set its metadata.
	 * @param files list of media files
	 * @return list of media files with metadata
	 * @throws IOException
	 * @see Media
	 */
	public List<Media> returnMetadata(List<File> files) throws IOException {
		//create a list of Media objects.
		List<Media> medias = new ArrayList<Media>();
		if(files != null) {
			//for each file
			for(File file : files) {
				//make a new media object
				Media media = new Media();
				//set media metadata (Attributes)
				media.setMedia(getName(file), getLocation(file), getFileType(file), getResolution(file), getDateAdded(), getFileSize(file), getDuration(file));
				//add the media object to the list
				medias.add(media);
				
			}
		}
		
		//return the list
		return medias;
	}
	/**
	 * This method takes a file as an argument and return its absolute path
	 * @param file file object
	 * @return location of the file
	 */
	private String getLocation(File file) {
		return file.getAbsolutePath();
	}
	/*
	 * This method generates data in the format of day/month/year and returns it as a string
	 */
	public String getDateAdded() {
		//Date time formatter object to format date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		//get current data
		LocalDate date = LocalDate.now();
		//return the date as a string after format
		return formatter.format(date).toString();
	}

	/**
	 * This method takes a file and returns its size
	 * @param file file object
	 * @return file size as a string
	 */
	private String getFileSize(File file) {
		//get the length of file in bytes
		long size = file.length();
		//String represents size 
		String sizeOption = "";
		//if size is less than 1000 bytes
		if(size < 1000) {
			//set size option as B
			sizeOption = "B";
			//if size is more than 1KB and less than 1000KB
		}else if(size > 1000 && size < 1000000) {
			//set size option as KB
			sizeOption = "KB";
			//get size in KB by dividing bytes by 1000
			size = size/1000;
		}
		//if the size is more than 1000MB
		else if(size > 1000000000) {
			//set size option to GB
			sizeOption = "GB";
			//get size in GB by dividing bytes/1000*1000*1000 (bytes/B*KB*MB)
			size = size/1000000000;
			//if the size is more than 1000KB
		}else if (size > 1000000) {
			//set size options to MB
			sizeOption = "MB";
			//get size in MB by dividing bytes/1000*1000 (Bytes/B*KB)
			size = size / 1000000;
		}
		//return the size as a string
		return size+sizeOption;
	}
	
	/**
	 * This method takes a file object as an argument and returns its file extension
	 * @param file file object
	 * @return return file extension
	 */
	private String getFileType(File file) {
		//split the file name by ., and return the 2nd element of the array. for example test.mp4 = [test, mp4] 2nd element = mp4.
//		String[] s = file.getName().toString().split("[.]",0);
		int index = file.getName().lastIndexOf(".");
		if(index > 0) {
			return file.getName().substring(index+1);
		}
		return "";
	}
	/**
	 * This method takes a file object and returns its name, without its extension
	 * @param file file object
	 * @return file name without the extension
	 */
	private String getName(File file) {
		//split the file name by . and return 1st element. For example test.mp4 =[test, mp4] 1st element = test.
		String[] s = file.getName().toString().split("[.]",0);
		return s[0];
	}
	/**
	 * This method takes a file object and return its resolution if it a image or video file.
	 * @param file file object
	 * @return resolution of the media
	 * @throws FileNotFoundException
	 */
	private String getResolution(File file) throws FileNotFoundException {
		String resolution = "";
		if(getFileType(file).equals("png")  || getFileType(file).equals("jpeg") || getFileType(file).equals("jpg")) {
			try {
				Image i = new Image(new FileInputStream(file.getAbsoluteFile()));
				resolution = (int)i.getWidth() + "px x " + (int)i.getHeight()+"px";
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if((ProgramData.enableXuggler) && (getFileType(file).equals("mp4") || getFileType(file).equals("mkv"))){
			/*
			 * https://examples.javacodegeeks.com/desktop-java/xuggler/inspect-a-video-file-with-xuggler/.
			 * This part of the code was used from above source, modified as needed.
			 */
			
			//xuggler container object
			IContainer container = IContainer.make();
			//loading the media file into the container object to read.
			int result = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
			//if the result is less than 0, xuggler container unable to open the file.
			if(result < 0 ) {
				//write the error to log file.
				Logger.writeLog("Metadata: unable to open media file");
			}else {
				int numStreams = container.getNumStreams();
					// find the stream object, only using the first stream object to retrieve metadata
					IStream stream = container.getStream(0);
					// get the pre-configured decoder that can decode this stream;
					IStreamCoder coder = stream.getStreamCoder();
					//get video resolution
					resolution += coder.getWidth()+"px X "+coder.getHeight()+"px";
					//closing the container, stop reading file
					container.close();
				
			}
		    
		}
		
		//return the resolution.
		return resolution;
	}
	
	
	/**
	 * This method takes a file object and returns its track length if its a video or audio file.
	 * @param file media file
	 * @return track length as a string
	 * @throws IOException
	 */
	private String getDuration(File file) throws IOException {
		String duration = "-";
		//xuggler container object
		IContainer container = IContainer.make();
		if((ProgramData.enableXuggler) && (getFileType(file).equals("mp4") || getFileType(file).equals("mkv"))) {
			
			//loading the media file into the container object to read.
			int result = container.open(file.getAbsolutePath(), IContainer.Type.READ, null);
			//if the result is less than 0, xuggler container unable to open the file.
			if(result < 0 ) {
				//write the error to log file.
				Logger.writeLog("Metadata: unable to open media file");
			}else {
					//duration length in milliseconds
					long length = container.getDuration();
					//converting milliseconds to more readable form HH:MM:SS 
					duration = TimeUnit.MICROSECONDS.toHours(length) + ":"+TimeUnit.MICROSECONDS.toMinutes(length) + ":"+TimeUnit.MICROSECONDS.toSeconds(length);
					//closing the container, stop reading file
//					container.close();
					container.delete();
				
			}
		}
		
		
		
		return duration;
	}
	
	public boolean isValidFile(File name) {
		for(String ext: supportedFiles) {
			int index = name.getName().lastIndexOf(".");
			if(index > 0) {
				if(ext.equals("*"+name.getName().substring(index))) {
					return true;
				}
			}
			
		}
		return false;
	}
	
	
}
