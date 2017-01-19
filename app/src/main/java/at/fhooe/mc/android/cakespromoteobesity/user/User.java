package at.fhooe.mc.android.cakespromoteobesity.user;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
public class User extends AppCompatActivity{

    private String mName;
    private int mUserGameID;
    private boolean mIsHost;

    public void addToLobby(final String _key){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference(); //"https://cakespromoteobesity.firebaseio.com/Lobbies");
        final DatabaseReference lobbyRef = db.child("Lobbies").child(_key);

        //final Firebase usersInLobbiesRef = new Firebase("https://cakespromoteobesity.firebaseio.com/Lobbies");

        lobbyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    Toast.makeText(User.this, "Snapshot existiert nicht", Toast.LENGTH_SHORT).show();
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
    public User(String _name, boolean _isHost){
        mName = _name;
        mIsHost = _isHost;
    }

    public User(String _name){
        mName = _name;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmUserGameID() {
        return mUserGameID;
    }

    public void setmUserGameID(int mUserGameID) {
        this.mUserGameID = mUserGameID;
    }

    public boolean ismIsHost() {
        return mIsHost;
    }

    public void setmIsHost(boolean mIsHost) {
        this.mIsHost = mIsHost;
    }
}