package at.becast.youploader.youtube;

public enum VisibilityType {
	PUBLIC("public", "Public"), PRIVATE("private", "Private"), UNLISTED("unlisted", "Unlisted"), SCHEDULED("private", "Scheduled");

	private final String term;
	private final String label;

	VisibilityType(final String term, final String label) {
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

