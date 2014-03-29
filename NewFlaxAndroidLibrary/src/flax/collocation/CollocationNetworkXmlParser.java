/*
 * File: flax.collocation.CollocationNetworkXmlParser
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to parse a given input stream (containing xml data)
 * and extract collocation information.
 */
package flax.collocation;

import android.util.Xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * CollocationNetworkXmlParser Class
 *
 * This class is used to parse a given input stream (containing xml data)
 * and extract collocation information.
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationNetworkXmlParser {
	
	// Declare variables and constants for parser
    protected static String baseWord				= null;
    protected static int wordCount					= 0;
    protected static final String ns 				= null;  
    
    // Declare CollocationActivityExercise object
    protected CollocationItem collocation;
	protected List<CollocationItem> collocations 	= new ArrayList<CollocationItem>();

	/* NetworkXmlParser class constructor */
    public CollocationNetworkXmlParser(){
    }
    
    /* getWordCount method, @return wordCount */
    public int getWordCount(){
    	return wordCount;
    }
    
    /*
     * parse method
     * 
     * Parses the given input stream (xml content) and
     * saves in a list
     * 
     * @param in, the input stream containing the xml content
     * @return exercises, the list that holds the activity details
     */
    public List<CollocationItem> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
        	
        	// Start word count at zero for each xml file
        	wordCount = 0;

        	// Create xml pull parser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);          

            // Find first tag
            parser.nextTag();

            // Call readXml() to being parsing 
            return readXml(parser);
            
        } finally {
            in.close();
        }
    }

    /*
     * readXml method
     * 
     * Runs through the xml content, reading the
     * Required tags as it goes.
     * 
     * @param parser, xmlPullParser
     * @return exercises, the list that hold the activity details
     */
    private List<CollocationItem> readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
                
    	// Need tag "response"
    	parser.require(XmlPullParser.START_TAG, ns, "response");
        
        // Move through the input stream, processing tags as needed
    	while (parser.next() != XmlPullParser.END_TAG) {
            
        	// If the current tag is not a start tag, continue through
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
    		
            // Else if it is a start tag, get name and process
            String name = parser.getName();
            
            // If the tag name is "word", call readWord()
            if (name.equals("word")) {
                readWord(parser);
                
            // skip the player tag
            } else if (name.equals("player")) {
            	parser.next();
            
            // If it's any other tag, skip it
            } else {
                skip(parser);
            }
        }
        return collocations;
    }
    
    /*
     * readWord method
     * 
     * Runs through the content in each word tag, 
     * Reading the required tags as it goes.
     * 
     * @param parser, xmlPullParser
     * @return exercises, the list that holds the collocation details
     */
    private void readWord(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "word");
        
        // Increment word count 
        wordCount ++;
    	baseWord 	= parser.getAttributeValue(null, "word");
    	
    	//TODO: Quick fix for hangman by Nan Wu
    	if(baseWord == null){
    		baseWord = parser.getText();
    		if(baseWord == null && parser.next() == XmlPullParser.TEXT) { 
    			baseWord = parser.getText();
    		}
    		// TODO: Create CollocationItem and store. by Nan Wu
            CollocationItem collo = new CollocationItem(0, baseWord, baseWord, 0, 
            		baseWord, baseWord, "none", "Hangman", "none", "none", baseWord, 0);
            collocations.add(collo);
    	}
                
    	// Read through tags ...
        while (parser.next() != XmlPullParser.END_TAG) {
        	
        	// If the current tag is not a start tag, continue through
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            // Else if it is a start tag, get name and process
            String name = parser.getName();
            
            // If the tag is "collo", call readCollo()
            if (name.equals("collo")) {
                readCollo(parser);
                
            // Skip any other tag
            } else {
                skip(parser);
            }
        }
    }

    /*
     * readCollo method
     * 
     * Processes the nested collo tag
     */
    private void readCollo(XmlPullParser parser) throws IOException, XmlPullParserException {
        
    	parser.require(XmlPullParser.START_TAG, ns, "collo");
        
        // Declare variables for collocation and set attribute values
    	int    index 			= 0;
    	String collocationId 	= parser.getAttributeValue(null, "ID");
    	String sendId 			= parser.getAttributeValue(null, "sentID");
    	String fre 				= parser.getAttributeValue(null, "fre");
    	String type				= parser.getAttributeValue(null, "colloType");    	
    	
    	// Call readText to retrieve text content
        String word = readText(parser);
        
        // Check that this is the end of the collo tag
        parser.require(XmlPullParser.END_TAG, ns, "collo");
        
        // Return newly created CollocationActivityExercise
        CollocationItem collo = new CollocationItem(0, collocationId, sendId, index, 
        		fre, word, "none", type, "none", "none", baseWord, 0);
        collocations.add(collo);
    }

    /*
     * readText method
     * 
     * Extracts the text from the collo tag
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    /*
     * skip method
     * 
     * Skips any tags that are not required
     */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    	
    	// Expecting start tag. Throw exception if not start tag.
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        
    	// Skip tag including any nested tags
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
} // end of class