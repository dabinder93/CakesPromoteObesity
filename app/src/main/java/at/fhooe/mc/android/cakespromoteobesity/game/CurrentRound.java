package at.fhooe.mc.android.cakespromoteobesity.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.card.CardWithUser;

/**
 * CurrentRound Objects get created when there is a new game created.
 * In every game there is only 1 instance of CurrentRound
 * The czar will decrement the Countdown
 */
public class CurrentRound implements Serializable {

    private List<CardWithUser> mCardWithUserList;
    private int mPickCount;
    private String mPromptInPlay;
    private int mCzarPickID;
    //private CardWithUser mSelectedCard;

    //Firebase
    public CurrentRound(){}

    public CurrentRound(int _pickcount){
        mCardWithUserList = new ArrayList<CardWithUser>();
        mPickCount = _pickcount;
        mPromptInPlay = "";
        mCzarPickID = 0;
    }

    /**
     * Adds a card to the CardWithUser List
     * @param _cwu CardWithUser object to the list
     */
    public void addCardToCardWithUserList(CardWithUser _cwu){
        mCardWithUserList.add(_cwu);
    }

    /**
     * Gets the CardWithUser list
     * @return List<CardWithUser> list with all cardWithUser
     */
    public List<CardWithUser> getmCardWithUserList() {
        return mCardWithUserList;
    }

    /**
     * Sets a new List to CardWithUserList
     * @param mCardWithUserList new List<CardWithUser> list
     */
    public void setmCardWithUserList(List<CardWithUser> mCardWithUserList) {
        this.mCardWithUserList = mCardWithUserList;
    }

    /**
     * returns the amount of cards which have been choosen as a response from the users
     * @return int of pickCount
     */
    public int getmPickCount() {
        return mPickCount;
    }

    /**
     * sets a new value for the pickCount
     * @param mPickCount int of new pickCount
     */
    public void setmPickCount(int mPickCount) {
        this.mPickCount = mPickCount;
    }

    /**
     * return the String of the Prompt which is currently Displayed to the users
     * @return String of promptInPlay
     */
    public String getmPromptInPlay() {
        return mPromptInPlay;
    }

    /**
     * set a new Card as the prompt in Play
     * @param mPromptInPlay String of new card which should be displayed to the users
     */
    public void setmPromptInPlay(String mPromptInPlay) {
        this.mPromptInPlay = mPromptInPlay;
    }

    /**
     * returns the id of the card the czar has picked
     * @return int id of card
     */
    public int getmCzarPickID() {
        return mCzarPickID;
    }

    /**
     * sets a new id of the card the czar has picked
     * @param mCzarPickID int of new id of card
     */
    public void setmCzarPickID(int mCzarPickID) {
        this.mCzarPickID = mCzarPickID;
    }
}
