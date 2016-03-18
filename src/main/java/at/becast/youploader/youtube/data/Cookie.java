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

