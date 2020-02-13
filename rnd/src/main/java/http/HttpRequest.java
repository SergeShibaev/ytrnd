package http;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;


public class HttpRequest {

  private static String m_Key = "AIzaSyBTYY47NBuSMFtHGhDg3zj5Dr5iaKwScy0";

  public HttpRequest() {
    HttpURLConnection.setFollowRedirects(true);
  }

  public static String sendGet(String url) throws Exception {
    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

    con.setRequestMethod("GET");
    con.setRequestProperty("Content-Type", "application/json");
    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);

    int status = con.getResponseCode();
    StringBuilder response = new StringBuilder();

    try {
      Reader streamReader = status != 400 ? new InputStreamReader(con.getInputStream())
                                          : new InputStreamReader(con.getErrorStream());
      BufferedReader buf = new BufferedReader(streamReader);
      String line;

      while ((line = buf.readLine()) != null) {
        response.append(line);
      }
      buf.close();
    }
    catch(Exception e) {
      response.append(e.getMessage());
    }
    finally {
      response.append(con.getResponseCode())
      .append(" status=")
      .append(status)
      .append(" ")
      .append(con.getResponseMessage())
      .append("\n");

      con.disconnect();
    }

    return response.toString();
  }

  public static String getMessages(String videoId) throws Exception {
    String pageToken = "";
    JSONArray res = new JSONArray();

    while (res.length() == 0 || pageToken != "")
    {
      String request = "https://www.googleapis.com/youtube/v3/commentThreads?key=" + m_Key +
                       "&textFormat=plainText&part=snippet&videoId=" + videoId + "&maxResults=50";
      if ( pageToken != "" )
        request += "&pageToken=" + pageToken;

      JSONObject jObj = new JSONObject(sendGet(request));

      try {
        JSONArray arr = jObj.optJSONArray("items");
        for (int i = 0; i < arr.length(); ++i)
          res.put(arr.getJSONObject(i));
        pageToken = jObj.getString("nextPageToken");
      }
      catch (JSONException ex) {
        break;
      }
      catch (Exception ex) {
        System.out.println(ex.getStackTrace());
        break;
      }
    }

    return new JSONObject().put("items", (JSONArray)res)
                           .toString();
  }
}
