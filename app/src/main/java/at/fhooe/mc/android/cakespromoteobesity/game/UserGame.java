package at.fhooe.mc.android.cakespromoteobesity.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bastian on 25.01.2017.
 */

public class UserGame implements Serializable{

    private String mName;
    private int mCardCount;
    private List<String> mCardsInHand;
    private int mPoints;
    private String mSelectedCard;

    public UserGame(){}

    public UserGame(String _name){
        mName = _name;
        mCardCount = 0;
        mCardsInHand = new ArrayList<String>();
        mPoints = 0;
        mSelectedCard = "";
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmCardCount() {
        return mCardCount;
    }

    public void setmCardCount(int mCardCount) {
        this.mCardCount = mCardCount;
    }

    public List<String> getmCardsInHand() {
        return mCardsInHand;
    }

    public void setmCardsInHand(List<String> mCardsInHand) {
        this.mCardsInHand = mCardsInHand;
    }

    public int getmPoints() {
        return mPoints;
    }

    public void setmPoints(int mPoints) {
        this.mPoints = mPoints;
    }

    public String getmSelectedCard() {
        return mSelectedCard;
    }

    public void setmSelectedCard(String mSelectedCard) {
        this.mSelectedCard = mSelectedCard;
    }


}
