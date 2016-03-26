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

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


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
