package at.fhooe.mc.android.cakespromoteobesity.game;

/**
 * Created by David on 26.01.2017.
 */
public class CardWithUser {

    private int mUserGameID;
    private String mCardText;

    CardWithUser(){}

    CardWithUser(int _mUserGameID, String _mCardText){
        mUserGameID = _mUserGameID;
        mCardText = _mCardText;
    }
    public int getmUserGameID() {
        return mUserGameID;
    }

    public void setmUserGameID(int mUserGameID) {
        this.mUserGameID = mUserGameID;
    }

    public String getmCardText() {
        return mCardText;
    }

    public void setmCardText(String mCardText) {
        this.mCardText = mCardText;
    }




}
