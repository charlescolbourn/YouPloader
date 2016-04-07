package at.becast.youploader.util;

public class TagUtil {
	
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
}
