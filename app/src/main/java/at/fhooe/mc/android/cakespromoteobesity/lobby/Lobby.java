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


    /**
     * Default Construcor needed for Firebase usage
     */
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

    public int getmUsersInLobby() {
        return mUsersInLobby;
    }

    public void setmUsersInLobby(int mUsersInLobby) {
        this.mUsersInLobby = mUsersInLobby;
    }

    public List<String> getmUserList() {
        return mUserList;
    }

    public void setmUserList(List<String> mUserList) {
        this.mUserList = mUserList;
    }

    //private List<String> mSelectedDecks;
    //private int mUsersInLobby;

    /*public int getmUsersInLobby() {
        return mUsersInLobby;
    }

    public void setmUsersInLobby(int mUsersInLobby) {
        this.mUsersInLobby = mUsersInLobby;
    }*/
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public int getmMaxPlayers() {
        return mMaxPlayers;
    }

    public void setmMaxPlayers(int mMaxPlayers) {
        this.mMaxPlayers = mMaxPlayers;
    }

    public int getmWinpoints() {
        return mWinpoints;
    }

    public void setmWinpoints(int mWinpoints) {
        this.mWinpoints = mWinpoints;
    }

    public List<Deck> getmDecks() {
        return mSelectedDecks;
    }

    public void setmDecks(List<Deck> mDecks) {
        this.mSelectedDecks = mDecks;
    }

    public String getmLobbyKey() {
        return mLobbyKey;
    }

    public void setmLobbyKey(String mLobbyKey) {
        this.mLobbyKey = mLobbyKey;
    }

    public boolean ismGameIsStarting() {
        return mGameIsStarting;
    }

    public void setmGameIsStarting(boolean mGameIsStarting) {
        this.mGameIsStarting = mGameIsStarting;
    }

 /*   public List<String> getmSelectedDecks() {
        return mSelectedDecks;
    }

    public void setmSelectedDecks(List<String> mSelectedDecks) {
        this.mSelectedDecks = mSelectedDecks;
    } */
}


