package at.becast.youploader.youtube.data;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"uri"})
public class CookieJar implements CookieStore{

	private final CookieStore store;

	public CookieJar() {
		store = new CookieManager().getCookieStore();
	}

	public void setSerializeableCookies(final List<Cookie> cookies) {
		for (final Cookie cookie : cookies) {
			add(cookie.getURI(), cookie.getCookie());
		}
	}

	public List<Cookie> getSerializeableCookies() {
		final List<Cookie> cookies = new ArrayList<>(store.getCookies().size());

		for (final HttpCookie cookie : store.getCookies()) {
			final Cookie serializableCookie = new Cookie(cookie);
			cookies.add(serializableCookie);
		}
		return cookies;
	}

	@Override
	public void add(final URI uri, final HttpCookie cookie) {
		store.remove(uri, cookie);
		store.add(uri, cookie);
	}

	@Override
	public List<HttpCookie> get(final URI uri) {
		return store.get(uri);
	}

	@Override
	public List<HttpCookie> getCookies() {
		return store.getCookies();
	}

	@Override
	public List<URI> getURIs() {
		return store.getURIs();
	}

	@Override
	public boolean remove(final URI uri, final HttpCookie cookie) {
		return store.remove(uri, cookie);
	}

	@Override
	public boolean removeAll() {
		return store.removeAll();
	}
}
