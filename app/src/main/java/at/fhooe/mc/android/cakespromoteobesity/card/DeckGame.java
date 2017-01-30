package at.fhooe.mc.android.cakespromoteobesity.card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastian on 30.01.2017.
 */
public class DeckGame implements Serializable {
    private String mDeckName;
    private List<Integer> mCardPromptsID;
    private List<Integer> mCardResponsesID;

    public DeckGame() {}

    public DeckGame(String _deckName) {
        mDeckName = _deckName;
        mCardPromptsID = new ArrayList<>();
        mCardResponsesID = new ArrayList<>();
    }

    public void addCardToPrompts(int id) {
        if (mCardPromptsID == null) mCardPromptsID = new ArrayList<>();
        mCardPromptsID.add(id);
    }

    public void addCardToResponses(int id) {
        if (mCardResponsesID == null) mCardResponsesID = new ArrayList<>();
        mCardResponsesID.add(id);
    }

    public List<Integer> getmCardResponsesID() {
        return mCardResponsesID;
    }

    public void setmCardResponsesID(List<Integer> mCardResponsesID) {
        this.mCardResponsesID = mCardResponsesID;
    }

    public String getmDeckName() {
        return mDeckName;
    }

    public void setmDeckName(String mDeckName) {
        this.mDeckName = mDeckName;
    }

    public List<Integer> getmCardPromptsID() {
        return mCardPromptsID;
    }

    public void setmCardPromptsID(List<Integer> mCardPromptsID) {
        this.mCardPromptsID = mCardPromptsID;
    }
}
