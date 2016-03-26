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
package at.becast.youploader.youtube.data;

import java.net.HttpCookie;
import java.net.URI;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE) 
public class Cookie {

	private final String  name;
	private final String  value;
	private final String  comment;
	private final String  commentUrl;
	private final String  domain;
	private final boolean discard;
	private final String  path;
	private final String  portList;
	private final long    maxAge;
	private final boolean secure;
	private final int     version;

	@SuppressWarnings("unused")
	private Cookie() {
		name = null;
		value = null;
		comment = null;
		commentUrl = null;
		domain = null;
		discard = false;
		path = null;
		portList = null;
		maxAge = 0;
		secure = false;
		version = 0;
	}

	public Cookie(final HttpCookie cookie) {
		name = cookie.getName();
		value = cookie.getValue();
		comment = cookie.getComment();
		commentUrl = cookie.getCommentURL();
		domain = cookie.getDomain();
		discard = cookie.getDiscard();
		maxAge = cookie.getMaxAge();
		path = cookie.getPath();
		portList = cookie.getPortlist();
		secure = cookie.getSecure();
		version = cookie.getVersion();
	}
	public URI getURI() {
		return URI.create(domain);
	}

	public HttpCookie getCookie() {
		final HttpCookie cookie = new HttpCookie(name, value);
		cookie.setComment(comment);
		cookie.setCommentURL(commentUrl);
		cookie.setDiscard(discard);
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setPortlist(portList);
		cookie.setMaxAge(maxAge);
		cookie.setSecure(secure);
		cookie.setVersion(version);
		return cookie;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", name, value);
	}
}

