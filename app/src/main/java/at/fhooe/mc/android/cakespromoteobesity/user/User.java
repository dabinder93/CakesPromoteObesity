package at.fhooe.mc.android.cakespromoteobesity.user;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

/**
 * Created by David on 18.01.2017.
 */
public class User {

    private String mName;
    private int mUserGameID;
    private boolean mIsHost;
    private String mUserKey;

    public User(String _name, String _userKey, boolean _isHost){
        mName = _name;
        mIsHost = _isHost;
        mUserKey = _userKey;
    }

    public User(String _name, String _userKey){
        mName = _name;
        mIsHost = false;
        mUserKey = _userKey;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String _mName) {
        mName = _mName;
    }

    public int getmUserGameID() {
        return mUserGameID;
    }

    public void setmUserGameID(int mUserGameID) {
        this.mUserGameID = mUserGameID;
    }

    public boolean isHost() {
        return mIsHost;
    }

    public void setmIsHost(boolean _mIsHost) {
        mIsHost = _mIsHost;
    }

    public String getmUserKey() {
        return mUserKey;
    }

    public void addToLobby(final String _key){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(); //"https://cakespromoteobesity.firebaseio.com/Lobbies");
        final DatabaseReference lobbyRef = ref.child("Lobbies").child(_key);

        //final Firebase usersInLobbiesRef = new Firebase("https://cakespromoteobesity.firebaseio.com/Lobbies");

        lobbyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //Toast.makeText(User.this, "Snapshot existiert nicht", Toast.LENGTH_SHORT).show();
                }else{
                    Lobby lobby = dataSnapshot.getValue(Lobby.class);
                    //int usersInLobby = lobby.getmUsersInLobby();
                    List<String> userList = lobby.getmUserList();
                    userList.add(mName);
                    //usersInLobbiesRef.child(_key).setValue(userList);


                    Map<String, Object> mapUserList = new HashMap<String, Object>();
                    mapUserList.put("mUserList", userList);

                    //UserInLobby++
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("mUsersInLobby", lobby.getmUsersInLobby()+1);
                    lobbyRef.updateChildren(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*usersInLobbiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Lobby lobby = dataSnapshot.getValue(Lobby.class);
                //int usersInLobby = lobby.getmUsersInLobby();
                List<String> userList = lobby.getmUserList();
                userList.add(mName);
                //usersInLobbiesRef.child(_key).setValue(userList);


                Map<String, Object> mapUserList = new HashMap<String, Object>();
                mapUserList.put("mUserList", userList);

                //UserInLobby++
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("mUsersInLobby", lobby.getmUsersInLobby()+1);
                usersInLobbiesRef.updateChildren(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        }); */
    }
}
