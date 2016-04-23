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
package at.becast.youploader.youtube.playlists;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Playlists {
	public String kind;
	public String nextPageToken;
	public String prevPageToken;
	public String etag;
	public PageInfo pageInfo;
	@JsonDeserialize(as=ArrayList.class, contentAs=Item.class)
	public List<Item> items;
	
	public Playlists() {
		this.kind = "";
		this.nextPageToken = "";
		this.prevPageToken = "";
		this.etag = "";
		this.pageInfo = new PageInfo();
	}
	
	public static class PageInfo{
		public int totalResults;
		public int resultsPerPage;
		
		public PageInfo(){
			
		}
	}
	
	public static class Item{
		@JsonIgnore(true)
		public String kind;
		public String etag;
		public String id;
		public Snippet snippet;
		
		public Item(){

		}
		
		public static class Snippet{
			public String publishedAt;
			public String channelId;
			public String title;
			public String description;
			public String channelTitle;
			public ThumbnailDetails thumbnails;
			@JsonIgnore
			public String localized;
			
			public static class ThumbnailDetails{
				@JsonProperty("default")
				public Thumbnail default__;
				@JsonIgnore
				public Thumbnail medium;
				@JsonIgnore
				public Thumbnail high;
				@JsonIgnore
				public Thumbnail standard;
				@JsonIgnore
				public Thumbnail maxres;
				
				public static class Thumbnail{
					public String url;
					public int width;
					public int height;
					
					public Thumbnail(){
						
					}
				}
				
			}
			public Snippet(){
				
			}
			
		}
		/*
   "kind": "youtube#playlist",
   "etag": "\"CuSCwMPVmgi8taDtE2LV6HdgkN0/IGbxGrjhFPxxsPFLNA4o03YVde0\"",
   "id": "PLpgcsjzutNdyabOR-eaSBZtGnuqNq7w4m",
   "snippet": {
    "publishedAt": "2016-04-03T07:36:46.000Z",
    "channelId": "UCnoTmaT3LmE9Aq5gQ7B1ziw",
    "title": "Move Or Die",
    "description": "",
    "thumbnails": {
     "default": {
      "url": "https://i.ytimg.com/vi/JwdoyZIE4uE/default.jpg",
      "width": 120,
      "height": 90
     },
     "medium": {
      "url": "https://i.ytimg.com/vi/JwdoyZIE4uE/mqdefault.jpg",
      "width": 320,
      "height": 180
     },
     "high": {
      "url": "https://i.ytimg.com/vi/JwdoyZIE4uE/hqdefault.jpg",
      "width": 480,
      "height": 360
     },
     "standard": {
      "url": "https://i.ytimg.com/vi/JwdoyZIE4uE/sddefault.jpg",
      "width": 640,
      "height": 480
     },
     "maxres": {
      "url": "https://i.ytimg.com/vi/JwdoyZIE4uE/maxresdefault.jpg",
      "width": 1280,
      "height": 720
     }
    },
    "channelTitle": "GenuineParts",
    "localized": {
     "title": "Move Or Die",
     "description": ""
    }
   }
  }
		 */
	}
}
