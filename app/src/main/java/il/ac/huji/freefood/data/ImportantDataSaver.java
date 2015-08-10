package il.ac.huji.freefood.data;

import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by reem on 8/7/15.
 */
public class ImportantDataSaver {
    private static ImportantDataSaver instance;
    private ParseUser user;
    private Date lastUserLogin;

    private ImportantDataSaver() {

    }

    public static synchronized ImportantDataSaver getInstance() {
        if (instance == null)
            instance = new ImportantDataSaver();
        return instance;
    }

    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        this.user = user;
    }

    public Date getLastUserLogin() {
        return lastUserLogin;
    }

    public void setLastUserLogin(Date lastUserLogin) {
        this.lastUserLogin = lastUserLogin;
    }
}
