package at.fhooe.mc.android.cakespromoteobesity.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastian on 25.01.2017.
 */

public class CurrentRound implements Serializable {

    private List<CardWithUser> mCardWithUserList;
    private int mCountdown;
    private int mPickCount;
    private String mPromptInPlay;
    //private CardWithUser mSelectedCard;

    public CurrentRound(){}


    public CurrentRound(int _countdown){
        mCountdown = _countdown;
        mCardWithUserList = new ArrayList<CardWithUser>();
        mPickCount = 0;
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

    public int getmCountdown() {
        return mCountdown;
    }

    public void setmCountdown(int mCountdown) {
        this.mCountdown = mCountdown;
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
