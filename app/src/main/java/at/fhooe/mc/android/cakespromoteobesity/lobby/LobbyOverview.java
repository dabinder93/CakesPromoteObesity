package at.fhooe.mc.android.cakespromoteobesity.lobby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.game.Game;
import at.fhooe.mc.android.cakespromoteobesity.game.GameActivity;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Displays the Name and Settings of the Lobby
 * and a List of all Players which are currently in the Lobby.
 */
public class LobbyOverview extends AppCompatActivity implements View.OnClickListener {

    private String mLobbyKey;
    private DatabaseReference ref, lobbyRef;
    TextView hostName;
    TextView lobbyName;
    TextView players;
    TextView deckList;
    ListView listView;
    Button startGame;
    User mUser;
    Lobby lobby;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_overview);

        mUser = MainActivity.mUser;

        //Set Action Bar Title
        setTitle("Lobby");
        hostName = (TextView) findViewById(R.id.tv_lobbyOv_hostName);
        lobbyName = (TextView) findViewById(R.id.tv_lobbyOv_lobbyName);
        players = (TextView) findViewById(R.id.tv_lobbyOv_players);
        deckList = (TextView) findViewById(R.id.tv_lobbyOv_deckList);
        listView = (ListView) findViewById(R.id.lv_playerInLobby);
        startGame = (Button) findViewById(R.id.btn_lobbyOV_startGame);

        //Sets visibility of the "Start Game" Button for the Host
        //Disabled for Players who joined the Lobby
        if (mUser.isHost()) startGame.setVisibility(View.VISIBLE);
        else startGame.setVisibility(View.GONE);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        //Get lobby passed from Intent
        lobby = (Lobby)bundle.getSerializable("LobbyObject");
        Log.i("LobbyOverview", lobby.getmName());

        //Extract data and show them in TextViews
        mLobbyKey = lobby.getmLobbyKey();

        lobbyName.setText(lobby.getmName());
        hostName.setText(lobby.getmUserList().get(0));

        StringBuffer b = new StringBuffer(" ");
        for (int i = 0; i < lobby.getmDecks().size(); i++) {
            b.append(lobby.getmDecks().get(i).getmDeckName());
            if (i != lobby.getmDecks().size()-1) b.append(", ");
        }
        deckList.setText(b.toString());



        //Set Reference to userList in lobby and display the players, which are currently in Lobby in the Listview
        ref = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).child("mUserList");
        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, ref) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView)v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };
        listView.setAdapter(firebaseListAdapter);

        lobbyRef = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey);
        lobbyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lobby = dataSnapshot.getValue(Lobby.class);
                    //Refreshes the Current Players in Lobby Count whenever someone joins or leaves the Lobby
                    players.setText(String.valueOf(lobby.getmUsersInLobby()) + "/" + String.valueOf(lobby.getmMaxPlayers()));
                    //When Host is Starting the Game
                    if(lobby.ismGameIsStarting()){
                        //Reduce UsersInLobby by 1 and push
                        lobby.setmUsersInLobby(lobby.getmUsersInLobby()-1);
                        //if there are still players in Lobby, refresh Lobby, else Delete the Lobby
                        if(lobby.getmUsersInLobby()!=0){
                            lobbyRef.setValue(lobby);
                        }else{
                            lobbyRef.removeValue();
                        }
                        Intent i = new Intent(LobbyOverview.this, GameActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("GameKey", lobby.getmLobbyKey());
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        startGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View _view) {
        //Start the Game if there are more then 3 Users in Lobby
        if (_view.getId() == R.id.btn_lobbyOV_startGame && mUser.isHost() && lobby.getmUsersInLobby() > 1) {
            //Creates new Game Object and pushes it into Database with same GameID as LobbyID + setGameIsStarting
            //so that other Player get notification and get into new Activity
            Game game = new Game(lobby);
            FirebaseDatabase.getInstance().getReference().child("Games").child(lobby.getmLobbyKey()).setValue(game);
            lobby.setmGameIsStarting(true);
            lobby.setmUsersInLobby(lobby.getmUsersInLobby()-1);
            FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).setValue(lobby);

            Intent i = new Intent(this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("GameKey", lobby.getmLobbyKey());
            i.putExtras(bundle);
            startActivity(i);

        }else Toast.makeText(this,"There are not enough players to start the Game",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        //When leaving the Lobby by BackPress, remove yourself(User) from the UserList
        for (int i = 0; i < lobby.getmUsersInLobby(); i++) {
            if (lobby.getmUserList().get(i).equals(mUser.getmName())) {
                lobby.getmUserList().remove(i);
                break;
            }
        }

        //reduce UserInLobby by 1 and if there are still users in the Lobby, Just update LobbyRef
        //else remove the Lobby
        lobby.setmUsersInLobby(lobby.getmUsersInLobby()-1);
        if (lobby.getmUsersInLobby() > 0) {
            FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).setValue(lobby);
        }else {
            FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).removeValue();
        }
        super.onBackPressed();
    }
}
