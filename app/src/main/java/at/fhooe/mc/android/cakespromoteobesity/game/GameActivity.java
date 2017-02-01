package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
public class GameActivity extends AppCompatActivity {

    private Game mGame;
    private User mUser;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Games");
    private volatile boolean allPlayersJoined;
    private List<DeckGame> mCardsInUse;
    private String response;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mUser = MainActivity.mUser;

        Log.i("GameActivity", Thread.currentThread().getName() + "OnCreate");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mCardsInUse = new ArrayList<DeckGame>();
        allPlayersJoined = false;
        //Get game passed from Intent
        final String mGameKey = (String) bundle.getSerializable("GameKey");

        ref.child(mGameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                mGame.setmUsersInGame(mGame.getmUsersInGame() + 1);

                //Set mCardsInUse
                if (mUser.isHost()) {
                    for (Deck deck : mGame.getmSelectedDecks()) {
                        mCardsInUse.add(new DeckGame(deck.getmDeckID()));
                    }
                }

                //Get GameStatus to 1 if all Players joined
                if (mGame.getmUsersInGame() == mGame.getmUsersInLobby()) mGame.setmGameStatus(1);

                ref.child(mGame.getmGameKey()).setValue(mGame);
                Log.i("GameActivity", "increasing usersInGame");
                Log.i("GameActivity", Thread.currentThread().getName() + "1. DataChange");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("GameActivity", "in on Cancelled");
            }
        });

        ref.child(mGameKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                Log.i("GameActivity", "mGame is overwritten");
                Log.i("GameActivity", "GameStatus has value " + String.valueOf(mGame.getmGameStatus()));
                switch (mGame.getmGameStatus()) {
                    case 0 : {
                        //nothing (new) should happen
                    }break;
                    case 1 : {
                        //Host is filling missing Cards up
                        if (mUser.isHost()) {
                            fillCardsUp();
                        }
                    }break;
                    case 2 : {
                        //Round starting, Prompt gets played
                    }break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void fillCardsUp() {
        //set the Status to 0 - no new methods get created when mGame gets updated in the DB
        if (mGame.getmGameStatus() == 1) {
            mGame.setmGameStatus(0);
            ref.child(mGame.getmGameKey()).setValue(mGame);
        }
        for (int userIndex = 0; userIndex < mGame.getmUsersInGame(); userIndex++) {
            int cardCount = mGame.getmUserGameList().get(userIndex).getmCardCount();
            for (int cardsInHand = cardCount; cardsInHand < 10; cardsInHand++) {
                boolean cardIsChecked = true;
                while (cardIsChecked) {
                    int resourceID = (int) (Math.random() * mGame.getmResourcesCount());
                    Deck resourceDeck = mGame.getmSelectedDecks().get(resourceID);
                    int cardID = (int) (Math.random() * resourceDeck.getmWhiteCardCount());

                    for (DeckGame deck : mCardsInUse) {
                        if (deck.getmDeckName().equals(mGame.getmSelectedDecks().get(resourceID).getmDeckID())) {
                            try {
                                Thread.sleep(deck.getmCardResponsesID().size()*10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();e.printStackTrace();
                            }
                            if (deck.getmCardResponsesID() == null) {
                                deck.setmCardResponsesID(new ArrayList<Integer>(cardID));
                                cardIsChecked = false;
                                fetchResponse(cardID, resourceDeck.getmDeckID(), mGame.getmUserGameList().get(userIndex), userIndex);
                            } else {
                                cardIsChecked = false;
                                for (int i : deck.getmCardResponsesID()) {
                                    if (i == cardID) {
                                        cardIsChecked = true;
                                    }
                                }
                                if (!cardIsChecked) {
                                    deck.addCardToResponses(cardID);
                                    fetchResponse(cardID, resourceDeck.getmDeckID(), mGame.getmUserGameList().get(userIndex), userIndex);
                                }
                            }
                        }
                    }
                }
            }
        }
        //Cards get pushed in the fetchedResponse() method now
        //ref.child(mGame.getmGameKey()).setValue(mGame);
    }

    private void fetchResponse(int _cardID, String _resourceName, UserGame _userGame, final int _userIndex) {
        Log.i("GameActivity","Entered fetchResponse");
        FirebaseDatabase.getInstance().getReference().child("Resources").child(_resourceName).child("Responses").child("Deck").child(String.valueOf(_cardID))
                .child("Response").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response = dataSnapshot.getValue(String.class);
                Log.i("Fetch Data", "Response = " + response);
                //Log.i("Fetch Data","Add Response to hand");
                mGame.getmUserGameList().get(_userIndex).addCardToHand(response);
                mGame.getmUserGameList().get(_userIndex).setmCardCount(mGame.getmUserGameList().get(_userIndex).getmCardCount() + 1);

                //Check if all cards are filled up, start the Round = 2
                if (_userIndex ==  mGame.getmUserGameList().size()-1 && mGame.getmUserGameList().get(_userIndex).getmCardCount() == 10) mGame.setmGameStatus(2);

                //let the listener have some time, he is so old and slow
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Push the Game to the DB
                ref.child(mGame.getmGameKey()).setValue(mGame);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("GameActivity","End of fetchResponse");
    }
}