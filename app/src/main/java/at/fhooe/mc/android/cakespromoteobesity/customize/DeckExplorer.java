package at.fhooe.mc.android.cakespromoteobesity.customize;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;

/**
 * shows the list of custom created decks online - user is able to download them into their locally stored list of decks
 */
public class DeckExplorer extends AppCompatActivity {

    private DatabaseReference mRef = MainActivity.mainRef;
    private List<Deck> mDecksList;
    private DeckExplorerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_explorer);
        setTitle("Deck Explorer");

        //Set the recView
        mRecyclerView = (RecyclerView)findViewById(R.id.recView_explorer_deck);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpDeckList();
    }

    private void setUpDeckList() {
        //Set up the List of Decks
        //mRecyclerView = (RecyclerView)findViewById(R.id.recView_explorer_deck);
        mRef.child("Resources-custom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(DeckExplorer.this,"There are no decks online",Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(DeckExplorer.this,"Decks found",Toast.LENGTH_SHORT).show();
                    mDecksList = new ArrayList<Deck>();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Deck deck = snap.getValue(Deck.class);
                        mDecksList.add(deck);
                    }
                }
                mAdapter = new DeckExplorerAdapter(DeckExplorer.this,mDecksList);
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
    protected void onDestroy() {
        super.onDestroy();
        mAdapter = null;
    }
}
