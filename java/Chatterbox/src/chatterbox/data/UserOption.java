package chatterbox.data;

public class UserOption
{
    public final String userId;
    public String nickname;

    public UserOption( String userId, String nickname )
    {
        this.userId = userId;
        this.nickname = nickname;
    }

    public UserOption( UserOption uo )
    {
        this.userId = uo.userId;
        this.nickname = uo.nickname;
    }
}
