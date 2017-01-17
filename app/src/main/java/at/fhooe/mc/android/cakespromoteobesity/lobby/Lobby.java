package at.fhooe.mc.android.cakespromoteobesity.lobby;

import java.util.List;

/**
 * Created by Bastian on 13.01.2017.
 */

public class Lobby {
    private String mName;
    private String mPassword;
    private String mMaxPlayers;
    private String mWinpoints;
    private List<String> mSelectedDecks;

    //private List<String> mSelectedDecks;
    //private int mUsersInLobby;

    public Lobby(String _name, String _password, String _maxPlayers, String _winPoints, List<String> _selectedDecks){
        mName = _name;
        mPassword = _password;
        mMaxPlayers = _maxPlayers;
        mWinpoints = _winPoints;
        mSelectedDecks = _selectedDecks;
    }

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

    public List<String> getmDecks() {
        return mSelectedDecks;
    }

    public void setmDecks(List<String> mDecks) {
        this.mSelectedDecks = mDecks;
    }

 /*   public List<String> getmSelectedDecks() {
        return mSelectedDecks;
    }

    public void setmSelectedDecks(List<String> mSelectedDecks) {
        this.mSelectedDecks = mSelectedDecks;
    } */
}


