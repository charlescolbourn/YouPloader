package at.becast.youploader.youtube;

public enum LicenseType {
	YOUTUBE("youtube", "Youtube Licence"), CC("creativeCommon", "Creative Commons");

	private final String term;
	private final String label;

	LicenseType(final String term, final String label) {
		this.term = term;
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
	
	public String getData() {
		return term;
	}
}

