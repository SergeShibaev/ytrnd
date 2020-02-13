package http;

import java.util.HashSet;

import org.json.*;

import data.YUser;

public class RequestParser {
  public static HashSet<YUser> collectUsers(String str) {

    HashSet<YUser> users = new HashSet<YUser>();
    JSONObject jObj = new JSONObject(str);
    JSONArray items = jObj.getJSONArray("items");
    HashSet<String> names = new HashSet<String>();    // names should be unique

    for (int i = 0; i < items.length(); ++i)
    {
      JSONObject arr = items.getJSONObject(i);
      JSONObject snippet = arr.getJSONObject("snippet").getJSONObject("topLevelComment").getJSONObject("snippet");

      if (names.add(snippet.getString("authorDisplayName"))) {
        users.add(new YUser(snippet.getString("authorDisplayName"),
                            snippet.getString("authorProfileImageUrl"),
                            snippet.getJSONObject("authorChannelId").getString("value")));
      }
    }

    return users;
  }
}
