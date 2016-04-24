package at.becast.youploader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.becast.youploader.gui.FrmMain;

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
		if(FrmMain.debug)
			LOG.debug("Calculating lenght for Tags. Text lenght: {}",text.length());
		
		String[] tags = trimTags(text);
		int lenght = 0;
		for(int i=0;i<tags.length;i++){
			if(i<tags.length){
				lenght += 1;
			}
			if(tags[i].contains(" ")){
				lenght += tags[i].length()+2;
			} else {
				lenght += tags[i].length();
			}
		}
		if(FrmMain.debug)
			LOG.debug("Tags lenght: {}",lenght);
		
		return lenght;
	}
}
