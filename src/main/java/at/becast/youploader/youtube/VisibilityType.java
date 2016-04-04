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

public enum VisibilityType {
	PUBLIC("public", "Visibility.Public"), PRIVATE("private", "Visibility.Private"), UNLISTED("unlisted", "Visibility.Unlisted"), SCHEDULED("private", "Visibility.Scheduled");
	private final ResourceBundle LANG = ResourceBundle.getBundle("lang", Locale.getDefault());
	private final String term;
	private final String label;

	VisibilityType(final String term, final String label) {
		this.term = term;
		this.label = LANG.getString(label);
	}

	@Override
	public String toString() {
		return label;
	}
	
	public String getData() {
		return term;
	}
}

