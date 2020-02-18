package data;

public class YComment {
  public YUser     User;
  public String    Text;
  public String    Published;
  public Integer   Likes;

  public YComment(YUser user, String comm, String time, Integer likes) {
    User = user;
    Text = comm;
    Published = time;
    Likes = likes;
  }
}
