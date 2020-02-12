package http;

//import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
//import java.io.DataOutputStream;
import java.net.HttpURLConnection;

    /*
     * HttpRequest r = new HttpRequest(); String response =
     * r.sendGet("http://youtube.com");
     *
     * System.out.println(response);
     */

public class HttpRequest {

  public HttpRequest() {
    HttpURLConnection.setFollowRedirects(true);
  }

  public String sendGet(String url) throws Exception {
    HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

    con.setRequestMethod("GET");
    con.setRequestProperty("Content-Type", "application/json");
    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);

    int status = con.getResponseCode();
    StringBuilder response = new StringBuilder();

    try {
      Reader streamReader = new InputStreamReader(con.getInputStream());
      BufferedReader buf = new BufferedReader(streamReader);
      String line;

      while ((line = buf.readLine()) != null) {
        response.append(line);
      }
      buf.close();
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
    }
    finally {
      response.append(con.getResponseCode())
      .append(" ")
      .append(con.getResponseMessage())
      .append("\n");

      con.disconnect();
    }

    return response.toString();
  }
}
