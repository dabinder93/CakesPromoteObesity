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
                for (Deck deck : mGame.getmSelectedDecks()) {
                    mCardsInUse.add(new DeckGame(deck.getmDeckID()));
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

        //Host holt Karten lokal aus - Ignore
        /*if (mUser.isHost()) {
            for (Deck deck : mGame.getmSelectedDecks()) {
                FirebaseDatabase.getInstance().getReference().child("Resources").child(deck.getmDeckID()).child("Responses").child("Deck").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        responsesList = dataSnapshot.getValue(ResponseDeck.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }*/

        /*synchronized (synchro){
            while(blabla){
                try {
                    Log.i("GameActivity", "in try block");
                    synchro.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }*/

        ref.child(mGameKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                Log.i("GameActivity", "mGame is overwritten");
                /*if (mGame.getmUsersInGame() == mGame.getmUsersInLobby() && mUser.isHost() && firstRound) {
                    firstRound = false;
                    //mGame.setmRunGame(true);
                    mGame.setmGameStatus(1);
                    ref.child(mGame.getmGameKey()).setValue(mGame);
                    allPlayersJoined = true;
                    //blabla = false;
                    Log.i("GameActivity", Thread.currentThread().getName() + "2. DataChange");
                    synchronized (synchro){
                        synchro.notify();
                    }
                }*/

                Log.i("GameActivity", "GameStatus has value " + String.valueOf(mGame.getmGameStatus()));
                switch (mGame.getmGameStatus()) {
                    case 0 : {
                        //nothing (new) should happen
                    }break;
                    case 1 : {
                        //Host is filling missing Cards up
                        if (mUser.isHost()) fillCardsUp();
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


        Log.i("GameActivity", "entering while");
        /*synchronized (synchro) {
            while(!allPlayersJoined){
                try {
                    synchro.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }*/
        Log.i("GameActivity", "left while");
        /*if (mUser.isHost()) {
            for (Deck deck : mGame.getmSelectedDecks()) {
                mCardsInUse.add(new DeckGame(deck.getmDeckID()));
            }
            fillCardsUp();
        }*/
        //runGameSequence();
    }

    private void fillCardsUp() {
        //set the Status to 0 - no new methods get created when mGame gets updated in the DB
        mGame.setmGameStatus(0);
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
                    Thread.sleep(200);
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

 /*   private void runGameSequence() {
        //Karten auffüllen
        if (mUser.getmUserGameID() == mGame.getmCzarID()) {

            final AtomicInteger requestCount = new AtomicInteger();
            final AtomicInteger failedCount = new AtomicInteger();
            final Object requestMutex = new Object();

            for (int userIndex = 0; userIndex < mGame.getmUsersInGame(); userIndex++) {
                for (int cardsInHand = mGame.getmUserGameList().get(userIndex).getmCardCount(); cardsInHand < 10; cardsInHand++) {
                    //Kartenauffüllmethode
                    //fillUpHandWithCard();

                    int resourceID = (int) (Math.random() * mGame.getmResourcesCount());
                    final Deck resourceDeck = mGame.getmSelectedDecks().get(resourceID);

                    final int cardID = (int) (Math.random() * resourceDeck.getmWhiteCardCount());

                    new FetchThread(requestCount, failedCount, mGame, cardID, resourceDeck, userIndex).start();

                    for(DeckGame cards:mGame.getmCardsInUse()){
                        if (cards.getmDeckName().equals(resourceDeck.getmDeckName())) {
                            //Add card to CardsInUse and to User
                            cards.addCardToResponses(cardID);

                        }
                    }
                }

                int iteration = mGame.getmUsersInGame() * 10;
                while (requestCount.get() < iteration - failedCount.get()) {

                }
                Log.i("GameActivity", "Updating is done");
                ref.child(mGame.getmGameKey()).setValue(mGame);
            }
        }
    }
        class FetchThread extends Thread{
            AtomicInteger requestCount;
            AtomicInteger failedCount;
            Game mGame;
            int cardID;
            Deck resourceDeck;
            int userIndex;

            FetchThread(AtomicInteger _requestCount, AtomicInteger _failedCount, Game _mGame, int _cardID, Deck _resourceDeck, int _userIndex){
                requestCount = _requestCount;
                failedCount = _failedCount;
                mGame = _mGame;
                cardID = _cardID;
                resourceDeck = _resourceDeck;
                userIndex = _userIndex;
            }

            @Override
            public void run() {
                DatabaseReference responseRef = FirebaseDatabase.getInstance().getReference().child("Resources").child(resourceDeck.getmDeckID()).child("Responses").child("Deck")
                        .child(String.valueOf(cardID)).child("Response");

                final int finalUserIndex = userIndex;
                responseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String response = dataSnapshot.getValue(String.class);
                        //Toast.makeText(GameActivity.this,"Response = " + response, Toast.LENGTH_SHORT).show();
                        Log.i("Fetch Data","Response = " + response);
                        Log.i("Fetch Data","Add Response to hand");
                        //if (response == null) Log.i("GameActivity","Response = null");
                        mGame.getmUserGameList().get(finalUserIndex).addCardToHand(response);
                        mGame.getmUserGameList().get(finalUserIndex).setmCardCount(mGame.getmUserGameList().get(finalUserIndex).getmCardCount()+1);

                        requestCount.incrementAndGet();
                        Log.i("GameActivity", "notified");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("GameActivity", "Data fetching failed");
                        failedCount.incrementAndGet();
                    }
                });

            }
        }
    }



 /*   private void fillUpHandWithCard() {
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
    } */


