package at.fhooe.mc.android.cakespromoteobesity.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.card.Response;

/**
 * UserGame is a special User type which gets saved in a Game Object (as a whole List of all Users)
 * it has specific variables in use like the Cards in the User's hand or his/her points.
 */
public class UserGame implements Serializable {

    private String mName;
    private int mCardCount;
    private List<Response> mCardsInHand;
    private List<Response> mSelectedCards;
    private int mPoints;

    //Firebase
    public UserGame(){}

    public UserGame(String _name){
        mName = _name;
        mCardCount = 0;
        mCardsInHand = new ArrayList<>();
        mPoints = 0;
    }

    /**
     * Adds a card to the Users Hand List
     * @param cardText String of the card the User gets in his Hand
     */
    public void addCardToHand(Response cardText) {
        if (mCardsInHand == null) mCardsInHand = new ArrayList<>();
            mCardsInHand.add(cardText);
    }

    /**
     * Iterates throught the User's Cards List and removes the card with the same text as the parameter
     * @param _resp String of card that should be removed
     */
    public void removeCardFromHand(Response _resp){
        Iterator<Response> iterator = mCardsInHand.iterator();
        while(iterator.hasNext()){
            Response s = iterator.next();
            if(s.getText().equals(_resp.getText())){
                iterator.remove();
                break;
            }
        }
    }

    public void addCardToSelected(Response cardText) {
        if (mSelectedCards == null) mSelectedCards = new ArrayList<>();
        mSelectedCards.add(cardText);
    }

    /**
     * returns the User's name
     * @return String UserName
     */
    public String getmName() {
        return mName;
    }

    /**
     * sets the User's name
     * @param mName String of User's new name
     */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * returns the Number of cards in a User's hand
     * @return int of CardCount
     */
    public int getmCardCount() {
        return mCardCount;
    }

    /**
     * sets the value of the number of cards in a user's hand
     * @param mCardCount new value of cards in user's hand
     */
    public void setmCardCount(int mCardCount) {
        this.mCardCount = mCardCount;
    }

    /**
     * returns the list of cards in a user's hand
     * @return List<String> cards in a user's hand
     */
    public List<Response> getmCardsInHand() {
        return mCardsInHand;
    }

    /**
     * sets a list of cards in a user's hand
     * @param mCardsInHand List<String> of cards of the user's new hand
     */
    public void setmCardsInHand(List<Response> mCardsInHand) {
        this.mCardsInHand = mCardsInHand;
    }

    /**
     * get the current points a user has gotten yet
     * @return int current points from a user
     */
    public int getmPoints() {
        return mPoints;
    }

    /**
     * sets the points a user has gotten
     * @param mPoints int new value of points from a user
     */
    public void setmPoints(int mPoints) {
        this.mPoints = mPoints;
    }

    public List<Response> getmSelectedCards() {
        return mSelectedCards;
    }

    public void setmSelectedCards(List<Response> mSelectedCards) {
        this.mSelectedCards = mSelectedCards;
    }

    /**
     * returns a String for the scoreboard-listview
     * @return String for listview
     */
    @Override
    public String toString() {
        return mName + "\n \t" + mPoints;
    }

}
