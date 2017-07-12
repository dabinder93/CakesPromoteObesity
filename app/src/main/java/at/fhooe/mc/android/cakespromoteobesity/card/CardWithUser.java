package at.fhooe.mc.android.cakespromoteobesity.card;

/**
 * Object that includes a String from the cards a user has selected and the user's GameID.
 * This way the czar can easily find out, which user picked this card when the czar selects it to give the user a point.
 */
public class CardWithUser {

    private int mUserGameID;
    private String mCardText;

    //Firebase
    CardWithUser(){}

    /**
     * Konstruktor
     * @param _mUserGameID
     * @param _mCardText
     */
    public CardWithUser(int _mUserGameID, String _mCardText){
        mUserGameID = _mUserGameID;
        mCardText = _mCardText;
    }

    /**
     * returns the cards owner id
     * @return int userID
     */
    public int getmUserGameID() {
        return mUserGameID;
    }

    /**
     * sets the cards owners id
     * @param mUserGameID int new userID for card
     */
    public void setmUserGameID(int mUserGameID) {
        this.mUserGameID = mUserGameID;
    }

    /**
     * returns the cardtext from the object
     * @return String cardtext
     */
    public String getmCardText() {
        return mCardText;
    }

    /**
     * sets the cardtext from the object
     * @param mCardText String new cardtext
     */
    public void setmCardText(String mCardText) {
        this.mCardText = mCardText;
    }

    /**
     * returns the String for the czar's listView of selected responses
     * @return String only the cardText so the czar doesn't know who the owner of that card is
     */
    @Override
    public String toString() {
        return mCardText;
    }
}
