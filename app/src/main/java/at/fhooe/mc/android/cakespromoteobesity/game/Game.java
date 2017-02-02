package at.fhooe.mc.android.cakespromoteobesity.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.card.DeckGame;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;
import at.fhooe.mc.android.cakespromoteobesity.user.UserGame;

/**
 * A Game Objects includes all needed variables and lists for a game to work
 * The Host of a Lobby will create a new Game Object and upload it to the Firebase DB,
 * which the other Users then will retrieve from the DB
 */
public class Game implements Serializable{


    private String mName;
    private int mWinpoints;
    private String mGameKey;
    private int mResourcesCount;
    private List<Deck> mSelectedDecks;
    private int mUsersInLobby;
    private int mUsersInGame;
    //private boolean mRunGame;
    private int mGameStatus;
    private int mCzarID;
    private int mTimerForCzar;
    private int mTimerForPlayer;
    private CurrentRound mCurrentRound;
    private List<UserGame> mUserGameList;
    private List<DeckGame> mCardsInUse;

    public Game(){}

    public void setmGameStatus(int mGameStatus) {
        this.mGameStatus = mGameStatus;
    }

    public Game(Lobby _lobby) {
        mName = _lobby.getmName();
        mWinpoints = _lobby.getmWinpoints();
        mGameKey = _lobby.getmLobbyKey();
        mSelectedDecks = _lobby.getmDecks();
        mResourcesCount = _lobby.getmDecks().size();
        mUsersInLobby = _lobby.getmUsersInLobby();
        mUsersInGame = 0;
        //mRunGame = false;
        mGameStatus = 0;
        mCzarID = (int) (Math.random()*mUsersInLobby);
        //mCzarID = 0;
        mTimerForCzar = 60;
        mTimerForPlayer = 60;

        mUserGameList = new ArrayList<>();
        for (String user:_lobby.getmUserList()){
            mUserGameList.add(new UserGame(user));
        }
        mCurrentRound = new CurrentRound(60);

        mCardsInUse = new ArrayList<>();
        for (Deck deck : _lobby.getmDecks()) {
            mCardsInUse.add(new DeckGame(deck.getmDeckName()));
        }
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmWinpoints() {
        return mWinpoints;
    }

    public void setmWinpoints(int mWinpoints) {
        this.mWinpoints = mWinpoints;
    }

    public String getmGameKey() {
        return mGameKey;
    }

    public void setmGameKey(String mGameKey) {
        this.mGameKey = mGameKey;
    }

    public int getmResourcesCount() {
        return mResourcesCount;
    }

    public void setmResourcesCount(int mResourcesCount) {
        this.mResourcesCount = mResourcesCount;
    }

    public List<Deck> getmSelectedDecks() {
        return mSelectedDecks;
    }

    public void setmSelectedDecks(List<Deck> mSelectedDecks) {
        this.mSelectedDecks = mSelectedDecks;
    }

    public int getmUsersInLobby() {
        return mUsersInLobby;
    }

    public void setmUsersInLobby(int mUsersInLobby) {
        this.mUsersInLobby = mUsersInLobby;
    }

    public int getmUsersInGame() {
        return mUsersInGame;
    }

    public void setmUsersInGame(int mUsersInGame) {
        this.mUsersInGame = mUsersInGame;
    }

    /*public boolean ismRunGame() {
        return mRunGame;
    }*/

    /*public void setmRunGame(boolean mRunGame) {
        this.mRunGame = mRunGame;
    }*/

    public int getmCzarID() {
        return mCzarID;
    }

    public void setmCzarID(int mCzarID) {
        this.mCzarID = mCzarID;
    }

    public int getmTimerForCzar() {
        return mTimerForCzar;
    }

    public void setmTimerForCzar(int mTimerForCzar) {
        this.mTimerForCzar = mTimerForCzar;
    }

    public int getmTimerForPlayer() {
        return mTimerForPlayer;
    }

    public void setmTimerForPlayer(int mTimerForPlayer) {
        this.mTimerForPlayer = mTimerForPlayer;
    }

    public CurrentRound getmCurrentRound() {
        return mCurrentRound;
    }

    public void setmCurrentRound(CurrentRound mCurrentRound) {
        this.mCurrentRound = mCurrentRound;
    }

    public List<UserGame> getmUserGameList() {
        return mUserGameList;
    }

    public void setmUserGameList(List<UserGame> mUserGameList) {
        this.mUserGameList = mUserGameList;
    }

    public List<DeckGame> getmCardsInUse() {
        return mCardsInUse;
    }

    public void setmCardsInUse(List<DeckGame> mCardsInUse) {
        this.mCardsInUse = mCardsInUse;
    }

    public int getmGameStatus() {
        return mGameStatus;
    }

}
