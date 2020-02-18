package http;

import java.util.HashSet;
import java.util.Set;

import org.json.*;

import data.YUser;
import data.YComment;

public class RequestParser {

  public static Set<YUser> collectUsers(String str) {

    Set<YUser> users = new HashSet<YUser>();
    JSONArray items = new JSONObject(str).getJSONArray("items");

    for (int i = 0; i < items.length(); ++i)
    {
      JSONObject snippet = items.getJSONObject(i)
                                .getJSONObject("snippet")
                                .getJSONObject("topLevelComment")
                                .getJSONObject("snippet");

      users.add(new YUser(snippet.getString("authorDisplayName"),
                          snippet.getString("authorProfileImageUrl"),
                          snippet.getJSONObject("authorChannelId").getString("value")));
    }

    return users;
  }

  public static YComment getComment(String comms, int n) {

    JSONArray items = new JSONObject(comms).getJSONArray("items");
    JSONObject snippet = items.getJSONObject(n)
                              .getJSONObject("snippet")
                              .getJSONObject("topLevelComment")
                              .getJSONObject("snippet");
    YUser user = new YUser(snippet.getString("authorDisplayName"),
                           snippet.getString("authorProfileImageUrl"),
                           snippet.getJSONObject("authorChannelId").getString("value"));

    return new YComment(user,
                        snippet.getString("textDisplay"),
                        snippet.getString("publishedAt"),
                        snippet.getInt("likeCount"));
  }
}
