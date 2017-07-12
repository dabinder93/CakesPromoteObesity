package at.fhooe.mc.android.cakespromoteobesity.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * gets locally created in the host's gameactivity, stores all ids of prompts and responses that have
 * been used in a game
 */
public class DeckGame implements Serializable {
    private String mDeckName;
    private List<Integer> mCardPromptsID;
    private List<Integer> mCardResponsesID;

    //Firebase
    public DeckGame() {}

    /**
     * default constructor
     * @param _deckName
     */
    public DeckGame(String _deckName) {
        mDeckName = _deckName;
        mCardPromptsID = new ArrayList<>();
        mCardResponsesID = new ArrayList<>();
    }

    /**
     * adds a value to the list of prompts
     * @param id int new id to add to list
     */
    public void addCardToPrompts(int id) {
        if (mCardPromptsID == null) mCardPromptsID = new ArrayList<>();
        mCardPromptsID.add(id);
    }

    /**
     * adds a value to the list of responses
     * @param id int new id to add to list
     */
    public void addCardToResponses(int id) {
        if (mCardResponsesID == null) mCardResponsesID = new ArrayList<>();
        mCardResponsesID.add(id);
    }

    /**
     * returns the whole list of responseIDs
     * @return List<Integer> of all ids in use in a game
     */
    public List<Integer> getmCardResponsesID() {
        return mCardResponsesID;
    }

    /**
     * sets a list of responseIDS
     * @param mCardResponsesID List<Integer> new list of ids
     */
    public void setmCardResponsesID(List<Integer> mCardResponsesID) {
        this.mCardResponsesID = mCardResponsesID;
    }

    /**
     * returns the objects deckname
     * @return String deckname
     */
    public String getmDeckName() {
        return mDeckName;
    }

    /**
     * sets the objects deckname
     * @param mDeckName String new deckname
     */
    public void setmDeckName(String mDeckName) {
        this.mDeckName = mDeckName;
    }

    /**
     * returns the whole list of promptIDs
     * @return List<Integer> of all ids in use in a game
     */
    public List<Integer> getmCardPromptsID() {
        return mCardPromptsID;
    }

    /**
     * sets a list of promptIDs
     * @param mCardPromptsID List<Integer> new list of ids
     */
    public void setmCardPromptsID(List<Integer> mCardPromptsID) {
        this.mCardPromptsID = mCardPromptsID;
    }
}
