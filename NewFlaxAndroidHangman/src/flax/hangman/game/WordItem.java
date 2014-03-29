/*
 * File: flax.collocationdominoes.game.WordItem
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds all of the information for an individual collocation word.
 */
package flax.hangman.game;

/**
 * WordItem Class
 * 
 * This file holds all of the information for an individual collocation word.
 * 
 * Note: This class has been used for managing collocations used in Collocation Dominoes,
 * it will not be necessary for activities that do not include collocations.
 * 
 * @author Jemma Konig
 */
public class WordItem {
	
	// Declare variables
	protected int id;
	protected String first;
	protected String middle;
	protected String last;
	protected String status;
	protected String statusFinal;
	
	/*
	 * Class constructor
	 * 
	 * @param f 	= first word in collocation
	 * @param m 	= middle words in collocation
	 * @param l 	= last word in collocation
	 * @param s 	= status 
	 * @param sf 	= final status
	 * @param i 	= collocation id
	 */
	public WordItem(String f, String m, String l, String s, String sf, int i){
		this.first = f;
		this.middle = m;
		this.last = l;
		this.status = s;
		this.statusFinal = sf;
		this.id = i;
	}
	
	/* getId method
	 * 
	 * @return collocation id
	 */
	public int getId(){
		return this.id;
	}
	
	/* setId method
	 * 
	 * @param i = collocation id
	 */
	public void setId(int i){
		this.id = i;
	}
	
	/* getFirst method
	 * 
	 * @return first word in collocation
	 */
	public String getFirst(){
		return this.first;
	}
	
	/* setFirst method
	 * 
	 * @param f = first word in collocation
	 */
	public void setFirst(String f){
		this.first = f;
	}
	
	/* getMiddle method
	 * 
	 * @return middle word in collocation
	 */
	public String getMiddle(){
		return this.middle;
	}
	
	/* setMiddle method
	 * 
	 * @param m = middle words in collocation
	 */
	public void setMiddle(String m){
		this.middle = m;
	}
	
	/* getLast method
	 * 
	 * @return last word in collocation
	 */
	public String getLast(){
		return this.last;
	}
	
	/* setLast method
	 * 
	 * @param l = last word in collocation
	 */
	public void setLast(String l){
		this.last = l;
	}
	
	/* getStatus method
	 * 
	 * @return collocation status 
	 */
	public String getStatus(){
		return this.status;
	}
	
	/* setStatus method
	 * 
	 * @param s = collocation status
	 */
	public void setStatus(String s){
		this.status = s;
	}
	
	/* getStatusFinal method
	 * 
	 * @return final status
	 */
	public String getStatusFinal(){
		return this.statusFinal;
	}
	
	/* setStatusFinal method
	 * 
	 * @param f = final status
	 */
	public void setStatusFinal(String f){
		this.statusFinal = f;
	}
} // end of WordItem class
