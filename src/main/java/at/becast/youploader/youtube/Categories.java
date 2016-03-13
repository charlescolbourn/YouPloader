/* 
 * YouPloader Copyright (c) 2016 genuineparts (itsme@genuineparts.org)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
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
	
	public int getID() {
		return number;
	}
}
