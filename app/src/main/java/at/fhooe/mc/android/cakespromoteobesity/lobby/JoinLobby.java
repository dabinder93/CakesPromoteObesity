package at.fhooe.mc.android.cakespromoteobesity.lobby;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;

/**
 * Displays all available open lobbies where a player can connect to
 * The list is implemented via a RecyclerView and the Lobby-Items get built up
 * with the LobbyListAdapter class
 */
public class JoinLobby extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LobbyListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Lobby> mLobbyList;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_join);
        setTitle("Join a Lobby");

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_lobby_list);
        //mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ref = FirebaseDatabase.getInstance().getReference().child("Lobbies");

    }

    /**
     * Listener which fetches the mLobby object from database and updates the LobbyListView
     */
    private void setUpLobbyList() {
        //Set up the List of Lobbies
        mLobbyList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(JoinLobby.this,"There are no open lobbies",Toast.LENGTH_SHORT).show();
                }else {
                    mLobbyList = new ArrayList<Lobby>();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Lobby lobby = snap.getValue(Lobby.class);
                        mLobbyList.add(lobby);
                    }
                }
                mAdapter = new LobbyListAdapter(JoinLobby.this,mLobbyList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpLobbyList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter = null;
    }
}
