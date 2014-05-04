package flax.template.converter;

import flax.utils.FlaxUtil;
import flax.utils.IUrlConverter;
public class TemplateUrlConverter implements IUrlConverter{

	/**
	 * Implement this method, convert urls (html page url) 
	 * in the exercise list into exercise's xml url
	 */
	@Override
	public String convert(String url) {
		//TODO: Convert urls (html page url) in the exercise list into exercise's xml url
		
		/** The code below is an example of converting for hangman activity.*/ 
		// This is the new beginning of the url
		String start = "?a=pr&rt=r&ro=1&o=xml&s=Hangman&c=password&s1.collname=password&s1.service=100";

		// Get original url and split at the first ";"
		String end = url.substring(url.indexOf("&s1.params="));

		// Use the second half of the array only plus the 'start' url and
		// the server path
		return FlaxUtil.getServerPath() + start + end;
	}
}
