package flax.hangman.utils;

import flax.utils.FlaxUtil;
import flax.utils.IURLConverter;
public class HangmanURLConverter implements IURLConverter{

	@Override
	public String convert(String url) {
		// This is the new beginning of the url
		String start = "?a=pr&rt=r&ro=1&o=xml&s=Hangman&c=password&s1.collname=password&s1.service=100&s1.params=";

		// Get original url and split at the first ";"
		String badUrl = url;
		String[] arr = badUrl.split("&s1.params=", 2);
		String end = arr[1];

		// Use the second half of the array only plus the 'start' url and
		// the server path
		return FlaxUtil.getServerPath() + start + end;
	}
	
}
