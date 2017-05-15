package at.fhooe.mc.android.cakespromoteobesity.card;

import java.io.Serializable;

/**
 * DeckInfo Objects are used when creating a new game.
 * The spinner calls the Firebase DB in the "Decks" Branch to
 * showcase all available Decks a User can choose for his/her game
 */
public class DeckInfo implements Serializable, Comparable<DeckInfo> {

    private String mDeckName;
    private String mDeckID;
    private int mBlackCardCount;
    private int mWhiteCardCount;

    //Only gets called when a developer wants to add a deck to the database
    public DeckInfo(String _name, String _deckID, int _blackCardCount, int _whiteCardCount){
        mDeckName = _name;
        mDeckID = _deckID;
        mBlackCardCount = _blackCardCount;
        mWhiteCardCount = _whiteCardCount;
    }

    //Only gets called when a dev wants to add a deck to the database
    public DeckInfo(Deck _deck) {
        mDeckName = _deck.getName();
        mDeckID = _deck.getId();
        if (_deck.getPrompts() != null) mBlackCardCount = _deck.getPrompts().size();
        else mBlackCardCount = 0;
        if (_deck.getResponses() != null) mWhiteCardCount = _deck.getResponses().size();
        else mWhiteCardCount = 0;
    }

    //Firebase
    public DeckInfo() {}

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

    /**
     * Comparable compareTo Method for DeckInfo
     * @param _deckInfo other DeckInfo
     * @return comparison of the names
     */
    @Override
    public int compareTo(DeckInfo _deckInfo) {
        return getmDeckName().compareTo(_deckInfo.getmDeckName());
    }
}
