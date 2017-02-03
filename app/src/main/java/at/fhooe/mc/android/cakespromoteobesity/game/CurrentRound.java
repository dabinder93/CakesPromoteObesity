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
    //private CardWithUser mSelectedCard;

    public CurrentRound(){}


    public CurrentRound(int _pickcount){
        mCardWithUserList = new ArrayList<CardWithUser>();
        mPickCount = _pickcount;
        mPromptInPlay = "";
    }

    public void addCardToCardWithUserList(CardWithUser _cwu){
        mCardWithUserList.add(_cwu);
    }

    public List<CardWithUser> getmCardWithUserList() {
        return mCardWithUserList;
    }

    public void setmCardWithUserList(List<CardWithUser> mCardWithUserList) {
        this.mCardWithUserList = mCardWithUserList;
    }

    public int getmPickCount() {
        return mPickCount;
    }

    public void setmPickCount(int mPickCount) {
        this.mPickCount = mPickCount;
    }

    public String getmPromptInPlay() {
        return mPromptInPlay;
    }

    public void setmPromptInPlay(String mPromptInPlay) {
        this.mPromptInPlay = mPromptInPlay;
    }

    /*public CardWithUser getmSelectedCard() {
        return mSelectedCard;
    }

    public void setmSelectedCard(CardWithUser mSelectedCard) {
        this.mSelectedCard = mSelectedCard;
    }*/

}
