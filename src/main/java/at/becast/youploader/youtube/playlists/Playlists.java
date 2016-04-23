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
