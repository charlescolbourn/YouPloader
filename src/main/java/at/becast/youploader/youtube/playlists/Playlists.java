package at.becast.youploader.youtube.playlists;

import java.util.ArrayList;

public class Playlists {
	public String kind;
	public String nextPageToken;
	public String prevPageToken;
	public String etag;
	public PageInfo pageInfo;
	public ArrayList<Items> items = new ArrayList<Items>();
	
	public class PageInfo{
		int totalResults;
		int resultsPerPage;
	}
	
	public class Items{
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
