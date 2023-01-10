package com.java.project.MediaOrganiserProgram;
/**
 * This class holds few static objects that references other class objects 
 * @author Sandip Pradhan
 *@see MainWindow
 *@see MediaView
 *@see HomeView
 */
public  class ProgramData {
	//xuggler library is broken on 64bit jre, enable xuggler if 32bit jre is installed and configured in eclipse.
	public static  boolean enableXuggler = false; //false = turned off, true = turned on.
	public static MainWindow pc;
	public static boolean mediaPage = false;
	public static MediaView mediaView;
	public static HomeView homeView;
	public static boolean changes = false;
	public static SidebarView sidebarView;
}
