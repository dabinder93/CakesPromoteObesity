package at.fhooe.mc.android.cakespromoteobesity.lobby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

public class LobbyOverview extends AppCompatActivity {

    private String mLobbyKey;
    private DatabaseReference ref;
    TextView hostName;
    TextView lobbyName;
    TextView players;
    TextView deckList;
    ListView listView;
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



        //Set Reference to userList in lobby
        ref = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).child("mUserList");
        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, ref) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView)v.findViewById(android.R.id.text1);
                textView.setText(model);
                players.setText(String.valueOf(lobby.getmUsersInLobby()) + "/" + String.valueOf(lobby.getmMaxPlayers()));
            }
        };
        listView.setAdapter(firebaseListAdapter);
    }

    @Override
    public void onBackPressed() {
        int i = 0;
        while (true) {
            if (lobby.getmUserList().get(i) == mUser.getmName()) {
                lobby.getmUserList().remove(i);
                break;
            }
            i++;
        }

        lobby.setmUsersInLobby(lobby.getmUsersInLobby()-1);
        FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).setValue(lobby);
    }
}
