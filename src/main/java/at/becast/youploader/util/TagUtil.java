package at.becast.youploader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagUtil {
	private static final Logger LOG = LoggerFactory.getLogger(TagUtil.class);
	public static String prepareTagsfromArray(String[] in) {
		String tags = "";
		for (int i = 0; i < in.length; i++) {
			if (i == 0) {
				tags = in[i];
			} else {
				tags += "," + in[i];
			}
		}
		return tags;
	}
	
	public static String[] trimTags(String in){
		String[] tags = in.split(",");
		String[] trimmedtags = new String[tags.length];
		for (int i = 0; i < tags.length; i++) {
			trimmedtags[i] = tags[i].trim();
		}
		return trimmedtags;
	}

	public static int calculateTagLenght(String text) {
		LOG.info("Calculating lenght for Tags. Text lenght: {}",text.length());
		String[] tags = trimTags(text);
		int lenght = 0;
		for(int i=0;i<tags.length;i++){
			if(tags[i].contains(" ")){
				lenght += tags[0].length()+2;
			} else {
				lenght += tags[0].length();
			}
		}
		LOG.info("Tags lenght: {}",lenght);
		return lenght;
	}
}
