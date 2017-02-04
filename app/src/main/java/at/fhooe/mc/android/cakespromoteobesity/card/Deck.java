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

    //Only gets called when a developer wants to add a deck to the database
    public Deck(String _name, String _deckID, int _blackCardCount, int _whiteCardCount){
        mDeckName = _name;
        mDeckID = _deckID;
        mBlackCardCount = _blackCardCount;
        mWhiteCardCount = _whiteCardCount;
    }

    //Firebase
    public Deck() {}

    /**
     * gets the amount of responses in a deck
     * @return int responsecount
     */
    public int getmWhiteCardCount() {
        return mWhiteCardCount;
    }

    /**
     * sets the responsecount of a deck
     * @param mCardCount int new responsecount
     */
    public void setmWhiteCardCount(int mCardCount) {
        this.mWhiteCardCount = mCardCount;
    }

    /**
     * gets the amount of prompts in a deck
     * @return int promptcount
     */
    public int getmBlackCardCount() {
        return mBlackCardCount;
    }

    /**
     * sets the promptcount of a deck
     * @param mCardCount int new promptcount
     */
    public void setmBlackCardCount(int mCardCount) {
        this.mBlackCardCount = mCardCount;
    }

    /**
     * returns the Decks name
     * @return String decks name
     */
    public String getmDeckName() {
        return mDeckName;
    }

    /**
     * sets the decks name
     * @param mDeckName String decks new name
     */
    public void setmDeckName(String mDeckName) {
        this.mDeckName = mDeckName;
    }

    /**
     * returns the decks id
     * @return string decks id
     */
    public String getmDeckID() {
        return mDeckID;
    }

    /**
     * sets the decks id
     * @param mDeckID String new decks id
     */
    public void setmDeckID(String mDeckID) {
        this.mDeckID = mDeckID;
    }
}
