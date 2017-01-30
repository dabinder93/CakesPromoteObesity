package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.card.DeckGame;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;
import at.fhooe.mc.android.cakespromoteobesity.user.UserGame;

/**
 * This is the class which has the logic of the game and does push- and retrieves from the Firebase DB
 * Has a ValueEventListener which will retrieve the Game-Object from the DB when someone makes changes to it
 */
public class GameActivity extends AppCompatActivity implements IOnGetDataListener {

    private Game mGame;
    private User mUser;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Games");
    private String response;
    private boolean firstRound;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mUser = MainActivity.mUser;
        firstRound = true;

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        //Get game passed from Intent
        final String mGameKey = (String)bundle.getSerializable("GameKey");

        ref.child(mGameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                mGame.setmUsersInGame(mGame.getmUsersInGame()+1);
                ref.child(mGameKey).setValue(mGame);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ref.child(mGameKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                if (mGame.getmUsersInGame() == mGame.getmUsersInLobby() && mUser.isHost() && firstRound) {
                    mGame.setmRunGame(true);
                    ref.child(mGameKey).setValue(mGame);
                }
                if (mGame.ismRunGame() && firstRound) runGameSequence();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void runGameSequence() {
        firstRound = false;

        //Karten auffüllen
        if (mUser.getmUserGameID() == mGame.getmCzarID()) {
            for (int userIndex = 0; userIndex < mGame.getmUsersInGame(); userIndex++) {
                for (int cardsInHand = mGame.getmUserGameList().get(userIndex).getmCardCount(); cardsInHand < 10; cardsInHand++) {
                    //Kartenauffüllmethode
                    //fillUpHandWithCard();

                    int resourceID = (int)(Math.random()*mGame.getmResourcesCount());
                    final Deck resourceDeck = mGame.getmSelectedDecks().get(resourceID);

                    final int cardID = (int) (Math.random()*resourceDeck.getmWhiteCardCount());

                    DatabaseReference responseRef = FirebaseDatabase.getInstance().getReference().child("Resources").child(resourceDeck.getmDeckID()).child("Responses").child("Deck")
                            .child(String.valueOf(cardID)).child("Response");

                    final int finalUserIndex = userIndex;
                    responseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            response = dataSnapshot.getValue(String.class);
                            //Toast.makeText(GameActivity.this,"Response = " + response, Toast.LENGTH_SHORT).show();
                            Log.i("GameActivity","Response = " + response);
                            Log.i("GameActivity","Add Response to hand");
                            //if (response == null) Log.i("GameActivity","Response = null");
                            mGame.getmUserGameList().get(finalUserIndex).addCardToHand(response);
                            mGame.getmUserGameList().get(finalUserIndex).setmCardCount(mGame.getmUserGameList().get(finalUserIndex).getmCardCount()+1);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    for (DeckGame cards : mGame.getmCardsInUse()) {
                        if (cards.getmDeckName().equals(resourceDeck.getmDeckName())) {
                            //Add card to CardsInUse and to User
                            cards.addCardToResponses(cardID);
                        }
                    }
                }
            }

            Log.i("GameActivity","Updating is done");
            ref.child(mGame.getmGameKey()).setValue(mGame);

        }


    }

    private void fillUpHandWithCard() {
        int resourceID = (int)(Math.random()*mGame.getmResourcesCount());
        final Deck resourceDeck = mGame.getmSelectedDecks().get(resourceID);

        final int cardID = (int) (Math.random()*resourceDeck.getmWhiteCardCount());

        DatabaseReference responseRef = FirebaseDatabase.getInstance().getReference().child("Resources").child(resourceDeck.getmDeckID()).child("Responses").child("Deck")
                .child(String.valueOf(cardID)).child("Response");

        responseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                response = dataSnapshot.getValue(String.class);
                //Toast.makeText(GameActivity.this,"Response = " + response, Toast.LENGTH_SHORT).show();
                Log.i("GameActivity","Response = " + response);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void mReadDataOnce(String child, IOnGetDataListener listener) {
        listener.onStartDB();
    }

    @Override
    public void onStartDB() {

    }

    @Override
    public void onSuccessDB(DataSnapshot data) {

    }

    @Override
    public void onFailedDB(DatabaseError databaseError) {

    }
}
