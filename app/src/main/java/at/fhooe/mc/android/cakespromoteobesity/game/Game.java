package at.fhooe.mc.android.cakespromoteobesity.game;

import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.Deck;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

/**
 * Created by Bastian on 25.01.2017.
 */

public class Game extends Lobby {
    /*private String mName;
    private String mPassword;
    private int mMaxPlayers;
    private int mWinpoints;
    private String mLobbyKey;
    private List<Deck> mSelectedDecks;
    private List<String> mUserList;
    private int mUsersInLobby;
    private boolean mGameIsStarting;*/
    private String mName;
    private int mWinpoints;
    private String mGameKey;
    private int mResourcesCount;
    private List<Deck> mSelectedDecks;
    private List<String> mUserList;
    private int mUsersInLobby;
    private int mUseresInGame;
    private boolean mRunGame;
    private int mCzarID;
    private int mTimerForCzar;
    private int mTimerForPlayer;
    private Round mCurRound;
}
