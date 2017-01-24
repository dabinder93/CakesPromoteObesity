package at.fhooe.mc.android.cakespromoteobesity.lobby;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.Deck;
import at.fhooe.mc.android.cakespromoteobesity.user.User;


public class Lobby implements Serializable{
    private String mName;
    private String mPassword;
    private String mMaxPlayers;
    private String mWinpoints;
    private String mLobbyKey;
    private List<Deck> mSelectedDecks;
    private List<String> mUserList;
    private int mUsersInLobby;


    /**
     * Default Construcor needed for Firebase usage
     */
    public Lobby(){}

    public Lobby(String _name, String _password, String _maxPlayers, String _winPoints, String _lobbyID, List<Deck> _selectedDecks, User _user){
        mName = _name;
        mPassword = _password;
        mMaxPlayers = _maxPlayers;
        mWinpoints = _winPoints;
        mLobbyKey = _lobbyID;
        mSelectedDecks = _selectedDecks;
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

    public String getmMaxPlayers() {
        return mMaxPlayers;
    }

    public void setmMaxPlayers(String mMaxPlayers) {
        this.mMaxPlayers = mMaxPlayers;
    }

    public String getmWinpoints() {
        return mWinpoints;
    }

    public void setmWinpoints(String mWinpoints) {
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

 /*   public List<String> getmSelectedDecks() {
        return mSelectedDecks;
    }

    public void setmSelectedDecks(List<String> mSelectedDecks) {
        this.mSelectedDecks = mSelectedDecks;
    } */
}


