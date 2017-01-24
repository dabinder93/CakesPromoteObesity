package at.fhooe.mc.android.cakespromoteobesity.lobbysettings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

public class LobbyOverview extends AppCompatActivity {

    private String mLobbyKey;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_overview);

        //Set Action Bar Title
        setTitle("Lobby");

        TextView hostName = (TextView) findViewById(R.id.tv_lobbyOv_hostName);
        TextView lobbyName = (TextView) findViewById(R.id.tv_lobbyOv_lobbyName);
        TextView players = (TextView) findViewById(R.id.tv_lobbyOv_players);
        TextView deckList = (TextView) findViewById(R.id.tv_lobbyOv_deckList);
        ListView listView = (ListView) findViewById(R.id.lv_playerInLobby);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        //Get lobby passed from Intent
        Lobby lobby = (Lobby)bundle.getSerializable("LobbyObject");
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

        players.setText(String.valueOf(lobby.getmUsersInLobby()) + "/" + String.valueOf(lobby.getmMaxPlayers()));

        //Set Reference to userList in lobby
        ref = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyKey).child("mUserList");
        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(this, String.class, android.R.layout.simple_list_item_1, ref) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView)v.findViewById(android.R.id.text1);
                textView.setText(model);
            }
        };
        listView.setAdapter(firebaseListAdapter);

    }
}
