package at.fhooe.mc.android.cakespromoteobesity.lobby;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Lobby Object, with all needed Variables
 * Constructor only gets called when a user creates a lobby
 * Default constructor needed for Firebase
 */
public class Lobby implements Serializable{
    private String mName;
    private String mPassword;
    private int mMaxPlayers;
    private int mWinpoints;
    private String mLobbyKey;
    private List<Deck> mSelectedDecks;
    private List<String> mUserList;
    private int mUsersInLobby;
    private boolean mGameIsStarting;


    //Firebase
    public Lobby(){}

    public Lobby(String _name, String _password, int _maxPlayers, int _winPoints, String _lobbyID, List<Deck> _selectedDecks, User _user){
        mName = _name;
        mPassword = _password;
        mMaxPlayers = _maxPlayers;
        mWinpoints = _winPoints;
        mLobbyKey = _lobbyID;
        mSelectedDecks = _selectedDecks;
        mGameIsStarting = false;
        mUsersInLobby = 1;
        mUserList = new ArrayList<String>();
        mUserList.add(_user.getmName());
    }

    /**
     * return the amount of the users in lobby
     * @return int of users in lobby
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
     * Gets the User Names in a list
     * @return List<String> which contains users in lobby names
     */
    public List<String> getmUserList() {
        return mUserList;
    }

    /**
     * set new userlist
     * @param mUserList List<String> new list of usernames
     */
    public void setmUserList(List<String> mUserList) {
        this.mUserList = mUserList;
    }

    /**
     * get lobby name
     * @return String of lobby name
     */
    public String getmName() {
        return mName;
    }

    /**
     * set new Lobby Name
     * @param mName String new Lobby Name
     */
    public void setmName(String mName) {
        this.mName = mName;
    }

    /**
     * Gets the Password which is selected for the lobby
     * @return String of password
     */
    public String getmPassword() {
        return mPassword;
    }

    /**
     * set Password for the lobby
     * @param mPassword String of the password
     */
    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    /**
     * gets the value of the maxPlayers who can join the lobby
     * @return int of the maxPlayers amount
     */
    public int getmMaxPlayers() {
        return mMaxPlayers;
    }

    /**
     * set the MaxPlayers amount
     * @param mMaxPlayers int for maxPlayers
     */
    public void setmMaxPlayers(int mMaxPlayers) {
        this.mMaxPlayers = mMaxPlayers;
    }

    /**
     * gets the winpoints which are needed to win the game
     * @return int of the winpoints
     */
    public int getmWinpoints() {
        return mWinpoints;
    }

    /**
     * sets the winpoints which are needed to win the game
     * @param mWinpoints int of winpoints
     */
    public void setmWinpoints(int mWinpoints) {
        this.mWinpoints = mWinpoints;
    }

    /**
     * Gets a list of decks which are selected for the game
     * @return List<Deck> of selected decks
     */
    public List<Deck> getmDecks() {
        return mSelectedDecks;
    }

    /**
     * set a new list of selected decks
     * @param mDecks List<Deck> new deck list
     */
    public void setmDecks(List<Deck> mDecks) {
        this.mSelectedDecks = mDecks;
    }

    /**
     * get the Database LobbyKey
     * @return String of the LobbyKey
     */
    public String getmLobbyKey() {
        return mLobbyKey;
    }

    /**
     * sets new database LobbyKey
     * @param mLobbyKey String new LobbyKey
     */
    public void setmLobbyKey(String mLobbyKey) {
        this.mLobbyKey = mLobbyKey;
    }

    /**
     * return GameIsStarting status
     * @return boolean game status
     */
    public boolean ismGameIsStarting() {
        return mGameIsStarting;
    }

    /**
     * set the gameIsStarting status
     * @param mGameIsStarting boolean new status
     */
    public void setmGameIsStarting(boolean mGameIsStarting) {
        this.mGameIsStarting = mGameIsStarting;
    }
}


