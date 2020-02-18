package data;

public class YUser {
  public String  Name;
  public String  ImageUrl;
  public String  ChannelId;

  public YUser(String name, String Image, String Channel) {
    Name = name;
    ImageUrl = Image;
    ChannelId = Channel;
  }

  @Override
  public boolean equals(Object other)
  {
    if (other == this)
      return true;

    if (other == null || this.getClass() != other.getClass())
      return false;

    return YUser.class.cast(other).Name.equals(Name) &&
           YUser.class.cast(other).ChannelId.equals(ChannelId);
  }

  @Override
  public int hashCode()
  {
    return Name.hashCode();
  }
}
