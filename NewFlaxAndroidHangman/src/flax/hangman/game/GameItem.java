/*
 * File: flax.collocationdominoes.game.GameItem
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds all of the information for an individual game.
 * The data is taken from the activity, collocation and summary tables in the database.
 * GameItem objects are used to hold the data when loaded into and out of the database.
 */
package flax.hangman.game;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * GameItem Class
 * 
 * This class holds all of the information for an individual game.
 * 
 * Note: The existing information is for the general activity data. Further variables
 * and methods may need to be added depending on the specific activity.
 * 
 * @author Jemma Konig
 */
public class GameItem {
	
	// Declare variables for the GameItem
	protected static int score;
	protected static int attempts; 
	protected static int activityId;
	protected static int possibleScore;
	protected static int pageNumber;
	
	protected static String gameTitle;
	protected static String activityStatus;
	protected static String startTime;
	protected static String endTime;
	
	protected static ArrayList<WordItem> collocationWords = new ArrayList<WordItem>();


	/*
	 * GameItem class constructor
	 */
	public GameItem(){
		
	}
	
	/*
	 * getActivityId Method
	 * @return activity id
	 */
	public static int getActivityId(){
		return activityId;
	}
	
	/*
	 * setActivityId method
	 * @param activity id
	 */
	public static void setActivityId(int aid){
		activityId = aid;
	}
	
	/*
	 * getActivityStatus method
	 * @return activity status (ie new, incomplete etc.)
	 */
	public static String getActivityStatus(){
		return activityStatus;
	}
	
	/*
	 * setActivityStatus method
	 * @param status of activity (ie new, incomplete etc.)
	 */
	public static void setActivityStatus(String ast){
		activityStatus = ast;
	}
	
	/*
	 * getAttempts method
	 * @return current number of attempts
	 */
	public static int getAttempts(){
		return attempts;
	}
	
	/*
	 * setAttempts method
	 * @param attempts
	 */
	public static void setAttempts(int a){
		attempts = a;
	}
	
	/*
	 * getScore method
	 * @return users current score
	 */
	public static int getScore(){
		return score;
	}
	
	/*
	 * setScore method
	 * @param score
	 */
	public static void setScore(int s){
		score = s;
	}
	
	/*
	 * getPossibleScore method
	 * @return possible score
	 */
	public static int getPossibleScore(){
		return possibleScore;
	}
	
	/*
	 * setPossibleScore method
	 * @param possible score
	 */
	public static void setPossibleScore(int p){
		possibleScore = p;
	}
	
	/*
	 * getGameTitle method
	 * @return activity title
	 */
	public static String getGameTitle(){
		return gameTitle;
	}
	
	/*
	 * setGameTitle method
	 * @param activity title
	 */
	public static void setGameTitle(String g){
		gameTitle = g;
	}
	
	/*
	 * getDominoeWords method
	 * @return ArrayList of WordItems
	 */
	public static ArrayList<WordItem> getCollocationWords(){
		return collocationWords;
	}
	
	/*
	 * setCorrectWords method
	 * @param ArrayList of WordItems
	 */
	public static void setCollocationWords(ArrayList<WordItem> d){
		collocationWords = d;
	}
	
	/*
	 * addCorrectWord method
	 * @param WordItem (a collocation split into first, mid, last etc)
	 */
	public static void addCollocationWord(WordItem w){
		collocationWords.add(w);
	}
	
	/*
	 * clearCorrectWords method
	 * Clears all the WordItems from the ArrayList
	 */
	public static void clearCollocationWords(){
		collocationWords.clear();
	}
		
	/*
	 * setStartTime method
	 * Sets startTime to given string
	 * @param String start time
	 */
	public static void setStartTime(String s){
		startTime = s;
	}
	
	/*
	 * setStartTime method
	 * Sets startTime to the current time
	 */
	public static void setStartTime(){
		startTime = "";

		Calendar current = Calendar.getInstance();
		String tmp = current.getTime().toString();
		
		String[] arr = tmp.split(" ");
		for(int i = 0; i < arr.length - 2; i++){
			startTime += (arr[i] + " ");
		}
	}
	
	/*
	 * getStartTime method
	 * @return start time
	 */
	public static String getStartTime(){
		return startTime;
	}
	
	/*
	 * setEndTime method
	 * @param endTime
	 */
	public static void setEndTime(String e){
		endTime = e;
	}
	
	/* 
	 * setEndTime method
	 * Sets endTime to the current time
	 */
	public static void setEndTime(){
		endTime = "";

		Calendar current = Calendar.getInstance();
		String tmp = current.getTime().toString();
		
		String[] arr = tmp.split(" ");
		for(int i = 0; i < arr.length - 2; i++){
			endTime += (arr[i] + " ");
		}
	}
	
	/*
	 * getEndTime method
	 * @return endTime
	 */
	public static String getEndTime(){
		if (endTime == null) {
			setEndTime();
		}
		return endTime;
	}

	public static int getPageNumber() {
		return pageNumber;
	}

	public static void setPageNumber(int pageNumber) {
		GameItem.pageNumber = pageNumber;
	}
	
	
	
} // end of class
