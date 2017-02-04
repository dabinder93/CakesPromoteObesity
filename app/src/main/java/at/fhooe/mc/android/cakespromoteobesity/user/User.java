package at.fhooe.mc.android.cakespromoteobesity.user;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

/**
 * User Object gets saved in the Users-Branch in the Firebase DB
 * In a Lobby there is only a UserName-List saved
 * In a Game the Users are saved as a (extended) UserGame Object in a List
 * The user itself is most commonly used for checking variables without having to go
 * into a deep level of the DB
 */
public class User {

    private String mName;
    private int mUserGameID;
    private boolean mIsHost;
    private String mUserKey;

    public User(String _name, String _userKey){
        mName = _name;
        mIsHost = false;
        mUserKey = _userKey;
    }

    /**
     * returns the Username
     * @return Username
     */
    public String getmName() {
        return mName;
    }

    /**
     * sets the Username
     * @param _mName string of the set name
     */
    public void setmName(String _mName) {
        mName = _mName;
    }

    /**
     * returns the userGameID
     * @return int userGameID
     */
    public int getmUserGameID() {
        return mUserGameID;
    }

    /**
     * sets the userGameID
     * @param mUserGameID int of the set userGameID
     */
    public void setmUserGameID(int mUserGameID) {
        this.mUserGameID = mUserGameID;
    }

    /**
     * returns if user is Host
     * @return boolean isHost
     */
    public boolean isHost() {
        return mIsHost;
    }

    /**
     * sets the isHost bool
     * @param _mIsHost boolean to set the User's Host status
     */
    public void setmIsHost(boolean _mIsHost) {
        mIsHost = _mIsHost;
    }

    /**
     * returns the Firebase UserKey
     * @return string Userkey
     */
    public String getmUserKey() {
        return mUserKey;
    }
}
