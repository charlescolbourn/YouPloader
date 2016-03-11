package at.becast.youploader.youtube;

public enum Categories {
	FILM(1,"Film", "Film & Animation"), AUTOS(2,"Autos", "Autos & Vehicles"), MUSIC(10,"Music", "Music"), ANIMALS(15, "Animals", "Pets & Animals"), SPORTS(17, "Sports", "Sports"), TRAVEL(19,"Travel", "Travel & Events"), GAMES(20,"Games", "Gaming"), PEOPLE(22,"People", "People & Blogs"), COMEDY(23,"Comedy", "Comedy"), ENTERTAINMENT(24,"Entertainment", "Entertainment"), NEWS(25,"News", "News & Politics"), HOWTO(26,"Howto", "Howto & Style"), EDUCATION(27,"Education", "Education"), TECH(28,"Tech", "Science & Technology");

	private final int number;
	private final String term;
	private final String label;

	Categories(final int number, final String term, final String label) {
		this.number = number;
		this.term = term;
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
