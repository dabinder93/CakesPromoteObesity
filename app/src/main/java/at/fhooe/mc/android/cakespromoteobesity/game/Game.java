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
    private int mGameStatus;
    private int mCzarID;
    private CurrentRound mCurrentRound;
    private List<UserGame> mUserGameList;

    //Firebase
    public Game(){}

    public Game(Lobby _lobby) {
        mName = _lobby.getmName();
        mWinpoints = _lobby.getmWinpoints();
        mGameKey = _lobby.getmLobbyKey();
        mSelectedDecks = _lobby.getmDecks();
        mResourcesCount = _lobby.getmDecks().size();
        mUsersInLobby = _lobby.getmUsersInLobby();
        mUsersInGame = 0;
        mGameStatus = 0;
        mCzarID = (int) (Math.random()*mUsersInLobby);
        mUserGameList = new ArrayList<>();
        for (String user:_lobby.getmUserList()){
            mUserGameList.add(new UserGame(user));
        }
        mCurrentRound = new CurrentRound(0);
    }

    /**
     * Get the current Game name
     * @return String of current game name
     */
    public String getmName() {
        return mName;
    }

    /**
     * set the game name
     * @param mName String of new game name
     */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * return the WinPoints which are needed to win the game
     * @return int of winpoints
     */
    public int getmWinpoints() {
        return mWinpoints;
    }

    /**
     * sets the winPoints which are needed to win the game
     * @param mWinpoints int of winPoints
     */
    public void setmWinpoints(int mWinpoints) {
        this.mWinpoints = mWinpoints;
    }

    /**
     * return the Database GameKey
     * @return String of current Gamekey in Database
     */
    public String getmGameKey() {
        return mGameKey;
    }

    /**
     * Sets the Database Gamekey
     * @param mGameKey String of the new Gamekey
     */
    public void setmGameKey(String mGameKey) {
        this.mGameKey = mGameKey;
    }

    /**
     * Return the number of Decks that are used in the game
     * @return int of number of decks uses for the game
     */
    public int getmResourcesCount() {
        return mResourcesCount;
    }

    /**
     * sets the number of decks that are used in the game
     * @param mResourcesCount int new number of decks used
     */
    public void setmResourcesCount(int mResourcesCount) {
        this.mResourcesCount = mResourcesCount;
    }

    /**
     * return list of Decks which are selected for the game
     * @return List<Deck> of decks that are used in the game
     */
    public List<Deck> getmSelectedDecks() {
        return mSelectedDecks;
    }

    /**
     * sets new list of selected Decks
     * @param mSelectedDecks List<Deck> of new selected decks
     */
    public void setmSelectedDecks(List<Deck> mSelectedDecks) {
        this.mSelectedDecks = mSelectedDecks;
    }

    /**
     * gets the amount of users in lobby
     * @return int of users that were in the lobby
     */
    public int getmUsersInLobby() {
        return mUsersInLobby;
    }

    /**
     * sets new value for users in lobby
     * @param mUsersInLobby int new value for users in lobby
     */
    public void setmUsersInLobby(int mUsersInLobby) {
        this.mUsersInLobby = mUsersInLobby;
    }

    /**
     * gets the amount of users who are in the game
     * @return int amount of users in the game
     */
    public int getmUsersInGame() {
        return mUsersInGame;
    }

    /**
     * sets new value for the amount of users in game
     * @param mUsersInGame int new value for users in game
     */
    public void setmUsersInGame(int mUsersInGame) {
        this.mUsersInGame = mUsersInGame;
    }

    /**
     * gets the ID of the Czar
     * @return int of czarID
     */
    public int getmCzarID() {
        return mCzarID;
    }

    /**
     * sets new value for CzarID
     * @param mCzarID int new value for CzarID
     */
    public void setmCzarID(int mCzarID) {
        this.mCzarID = mCzarID;
    }

    /**
     * gets CurrentRound Object
     * @return CurrentRound Object
     */
    public CurrentRound getmCurrentRound() {
        return mCurrentRound;
    }

    /**
     * sets new value for CurrentRound Object
     * @param mCurrentRound new CurrentRound Object
     */
    public void setmCurrentRound(CurrentRound mCurrentRound) {
        this.mCurrentRound = mCurrentRound;
    }

    /**
     * return a list of users who are in the game
     * @return List<UserGame> of users who are in the game
     */
    public List<UserGame> getmUserGameList() {
        return mUserGameList;
    }

    /**
     * set a new list for usersInGame
     * @param mUserGameList List<UserGame> list as new value
     */
    public void setmUserGameList(List<UserGame> mUserGameList) {
        this.mUserGameList = mUserGameList;
    }

    /**
     * sets the current game status
     * @param mGameStatus int new value for current game status
     */
    public void setmGameStatus(int mGameStatus) {
        this.mGameStatus = mGameStatus;
    }

    /**
     * gets the current game status
     * @return int of the gamestatus
     */
    public int getmGameStatus() {
        return mGameStatus;
    }

}
