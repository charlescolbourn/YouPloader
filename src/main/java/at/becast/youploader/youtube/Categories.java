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

import java.util.Locale;
import java.util.ResourceBundle;

public enum Categories {
	FILM(1, "Category.film"), AUTOS(2, "Category.autos"), MUSIC(10, "Category.music"), ANIMALS(15, "Category.animals"), SPORTS(17, "Category.sports"), TRAVEL(19, "Category.travel"), GAMES(20, "Category.games"), PEOPLE(22, "Category.people"), COMEDY(23, "Category.comedy"), ENTERTAINMENT(24, "Category.entertainment"), NEWS(25, "Category.news"), HOWTO(26, "Category.howto"), EDUCATION(27, "Category.education"), TECH(28, "Category.tech");
	private final ResourceBundle LANG = ResourceBundle.getBundle("lang", Locale.getDefault());
	private final int number;
	private final String label;

	Categories(final int number, final String label) {
		this.number = number;
		this.label = LANG.getString(label);
	}

	@Override
	public String toString() {
		return label;
	}
	
	public int getID() {
		return number;
	}
}
