/*
 * File: flax.collocation.CollocationSampleXmlParser
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * CollocationSampleXmlParser Class
 *
 * This class is used to parse a given input stream (containing xml data)
 * and extract collocation information.
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationSampleXmlParser {
	
	// Declare constants for parser
    private static final String ns 			= null;  
	
	// Declare list for parser
	protected ArrayList<String> sampleList 	= new ArrayList<String>();

	/* NetworkXmlParser class constructor */
    public CollocationSampleXmlParser(){
    }
    
    /*
     * parse method
     * 
     * Parses the given input stream (xml content) and
     * saves in a list
     * 
     * @param in, the input stream containing the xml content
     * @return colloList, the list that holds the sample sentences
     */
    public ArrayList<String> parse(InputStream in) throws XmlPullParserException, IOException {
        try {

        	// Create xml pull parser
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
                        
            // Find first tag
            parser.nextTag();
        	       	
            // Call readXml() to being parsing 
            readXml(parser);
            return sampleList;
            
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
     */
    private void readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        
    	// Need tag "page"
    	parser.require(XmlPullParser.START_TAG, ns, "page");
        
        // Move through the input stream, processing tags as needed
    	while (parser.next() != XmlPullParser.END_TAG) {
            
        	// If the current tag is not a start tag, continue through
    		if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
    		
            // Else if it is a start tag, get name and process
            String name = parser.getName();
            
            // If the tag name is "pageResponse", call readResponse()
            if (name.equals("pageResponse")) {
            	readPageResponse(parser);
            // If it's any other tag, skip it
            } else {
                skip(parser);
            }
        }
    }
    
    /*
     * readPageResponse method
     * 
     * @param parser, xmlPullParser
     */
    public void readPageResponse(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pageResponse");
    	
    	// Read through tags ...
        while (parser.next() != XmlPullParser.END_TAG) {
        	
        	// If the current tag is not a start tag, continue through
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            // Else if it is a start tag, get name and process
            String name = parser.getName();
            
            // If the tag is "sampleSentences", call readSampleSentences()
            if (name.equals("sampleSentences")) {
                readSampleSentences(parser);
                
            // Skip any other tag
            } else {
                skip(parser);
            }
        }
    }
    
    /*
     * readSampleSentences method
     * 
     * @param parser, xmlPullParser
     */
    private void readSampleSentences(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "sampleSentences");
        
    	// Read through tags ...
        while (parser.next() != XmlPullParser.END_TAG) {
        	
        	// If the current tag is not a start tag, continue through
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            // Else if it is a start tag, get name and process
            String name = parser.getName();

            // If the tag is "sample", call readSample()
            if (name.equals("sample")) {
                readSample(parser);
                
            // Skip any other tag
            } else {
                skip(parser);
            }
        }
    }
    
    /*
     * readSample method
     * 
     * @param parser, xmlPullParser
     */
    private void readSample(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "sample");   	
        
    	// Call readText to retrieve text content
        String word = readText(parser);
        
        // Check that this is the end of the sample tag
        parser.require(XmlPullParser.END_TAG, ns, "sample");

        // Add the sample sentence to the list
        sampleList.add(word);
    }
    
    /*
     * readText method
     * 
     * Extracts the text from the xml tag
     * 
     * @param parser, xmlPullParser
     * @return result
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

