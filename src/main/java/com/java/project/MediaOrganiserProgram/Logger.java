package com.java.project.MediaOrganiserProgram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
/**
 * This class writes a log (error or messages) to a text file
 * @author Sandip Pradhan
 *
 */
public class Logger {
	/**
	 * contructor, checks if the text file exist if not it creates a log text file.
	 */
	Logger(){
		File f = new File("log.txt");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * Writes the string to a text file
	 * @param log string log
	 */
	public static void writeLog(String log) {
		LocalDateTime dateTime = LocalDateTime.now();
		String dt = dateTime.toString();
		File logFile = new File("log.txt");
		try {
			FileWriter fWriter = new FileWriter(logFile, true);
			fWriter.write(dt+": "+log+"\n");
			fWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
