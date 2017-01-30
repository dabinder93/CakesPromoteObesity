package at.fhooe.mc.android.cakespromoteobesity.card;

import java.io.Serializable;

/**
 * Deck Objects are used when creating a new game.
 * The spinner calls the Firebase DB in the "Decks" Branch to
 * showcase all available Decks a User can choose for his/her game
 */
public class Deck implements Serializable{

    private String mDeckName;
    private String mDeckID;
    private int mBlackCardCount;
    private int mWhiteCardCount;

    public Deck(String _name, String _deckID, int _blackCardCount, int _whiteCardCount){
        mDeckName = _name;
        mDeckID = _deckID;
        mBlackCardCount = _blackCardCount;
        mWhiteCardCount = _whiteCardCount;
    }

    /**
     * Default Constructor is only for Firebase use
     */
    public Deck() {
    }


    public int getmWhiteCardCount() {
        return mWhiteCardCount;
    }

    public void setmWhiteCardCount(int mCardCount) {
        this.mWhiteCardCount = mCardCount;
    }

    public int getmBlackCardCount() {
        return mBlackCardCount;
    }

    public void setmBlackCardCount(int mCardCount) {
        this.mBlackCardCount = mCardCount;
    }

    public String getmDeckName() {
        return mDeckName;
    }

    public void setmDeckName(String mDeckName) {
        this.mDeckName = mDeckName;
    }

    public String getmDeckID() {
        return mDeckID;
    }

    public void setmDeckID(String mDeckID) {
        this.mDeckID = mDeckID;
    }

}
