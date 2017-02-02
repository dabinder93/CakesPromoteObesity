package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.CardWithUser;
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
    private String response, prompt, selectedCard;
    private TextView mPromptView, mStatusView, mCountDownView;
    private ListView mResponseView;
    private ArrayAdapter<String> responseAdapter;
    private ArrayAdapter<CardWithUser> promptAdapter;
    private Integer countdownVal;
    private MenuItem lockCard;
    private boolean hasSelectedCard;
    private boolean mPlayersAreChoosing;
    private Thread playersChooseCardThread;
    private Thread czarChoosesCardThread;
    private ExecutorService executor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);
        mUser = MainActivity.mUser;

        Log.i("GameActivity", Thread.currentThread().getName() + " OnCreate");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mCardsInUse = new ArrayList<DeckGame>();
        allPlayersJoined = false;
        //Get game passed from Intent
        final String mGameKey = (String) bundle.getSerializable("GameKey");

        //wait for random time up to a second -> so 2 users cant join at the same time
        try {
            Thread.sleep(mUser.getmUserGameID()*200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ref.child(mGameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                mGame.setmUsersInGame(mGame.getmUsersInGame() + 1);

                //Get GameStatus to 1 if all Players joined
                if (mGame.getmUsersInGame() == mGame.getmUsersInLobby()) mGame.setmGameStatus(1);

                ref.child(mGame.getmGameKey()).setValue(mGame);
                Toast.makeText(GameActivity.this,"usersInGame is " + mGame.getmUsersInGame(), Toast.LENGTH_SHORT).show();
                Log.i("GameActivity", "increasing usersInGame to " + mGame.getmUsersInGame());

                //Set mCardsInUse
                if (mUser.isHost()) {
                    for (Deck deck : mGame.getmSelectedDecks()) {
                        mCardsInUse.add(new DeckGame(deck.getmDeckID()));
                    }
                }
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
                        if (mUser.isHost()) fillCardsUp();
                    }break;
                    case 2 : {
                        //Round starting, Prompt gets played
                        if (mUser.isHost()) getPrompt();
                    }break;
                    case 3 : {
                        if(mUser.getmUserGameID() != mGame.getmCzarID()){
                            lockCard.setVisible(true);
                        }else{
                            lockCard.setVisible(false);
                        }
                        setContentView(R.layout.activity_game);
                        mPromptView = (TextView)findViewById(R.id.tv_game_prompt);
                        mStatusView = (TextView)findViewById(R.id.tv_game_status);
                        mCountDownView = (TextView)findViewById(R.id.tv_game_countDown);
                        mResponseView = (ListView)findViewById(R.id.listView_game_responses);

                        mPromptView.setText(mGame.getmCurrentRound().getmPromptInPlay());
                        if (mGame.getmCzarID() == mUser.getmUserGameID()) mStatusView.setText("You are the Card Czar");
                        else mStatusView.setText("Card Czar is "+mGame.getmUserGameList().get(mGame.getmCzarID()).getmName()+", select a Card");

                        //Round starting, Prompt gets played
                        //playersChooseCard();
                        playersChooseCardThread = new PlayersChooseCardThread();
                        playersChooseCardThread.start();
                        countdownVal = 60;
                        localCountdown();

                    }break;
                    case 4 : {
                        if(mUser.getmUserGameID() == mGame.getmCzarID()){
                            lockCard.setVisible(true);
                            hasSelectedCard = false;
                        }else{
                            lockCard.setVisible(false);
                        }

                        if (mGame.getmCzarID() == mUser.getmUserGameID()) mStatusView.setText("Pick the best answer");
                        else mStatusView.setText(mGame.getmUserGameList().get(mGame.getmCzarID()).getmName()+ " is picking");

                        //Czar chooses a Card
                        //czarChoosesCard();
                        playersChooseCardThread.interrupt();
                        czarChoosesCardThread = new CzarChoosesCardThread();
                        czarChoosesCardThread.start();
                        countdownVal = 60;
                        localCountdown();
                    }break;
                    case 5 : {
                        czarChoosesCardThread.interrupt();
                        //Disconnect all Players from the Game
                        disconnectFromGame();

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
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                                //Thread.sleep(10+(deck.getmCardResponsesID().size()*10));
                                Thread.sleep(100);
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
                Log.i("GameActivity", "Response = " + response);
                //Log.i("Fetch Data","Add Response to hand");
                mGame.getmUserGameList().get(_userIndex).addCardToHand(response);
                mGame.getmUserGameList().get(_userIndex).setmCardCount(mGame.getmUserGameList().get(_userIndex).getmCardCount() + 1);

                //Check if all cards are filled up, start the Round = 2
                if (_userIndex ==  mGame.getmUserGameList().size()-1 && mGame.getmUserGameList().get(_userIndex).getmCardCount() == 10) mGame.setmGameStatus(2);

                //let the listener have some time, he is so old and slow
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Push the Game to the DB
                ref.child(mGame.getmGameKey()).setValue(mGame);
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("GameActivity","End of fetchResponse");
    }

    private void getPrompt() {

        boolean cardIsChecked = true;
        while (cardIsChecked) {
            int resourceID = (int) (Math.random() * mGame.getmResourcesCount());
            Deck resourceDeck = mGame.getmSelectedDecks().get(resourceID);
            int cardID = (int) (Math.random() * resourceDeck.getmBlackCardCount());

            for (DeckGame deck : mCardsInUse) {
                if (deck.getmDeckName().equals(mGame.getmSelectedDecks().get(resourceID).getmDeckID())) {
                    cardIsChecked = false;
                    for (int i : deck.getmCardPromptsID()) {
                        if (i == cardID) {
                            cardIsChecked = true;
                        }
                    }
                    if (!cardIsChecked) {
                        deck.addCardToPrompts(cardID);
                        //fetchResponse(cardID, resourceDeck.getmDeckID(), mGame.getmUserGameList().get(userIndex), userIndex);
                        Log.i("GameActivity","Entered fetchPrompt");
                        FirebaseDatabase.getInstance().getReference().child("Resources").child(resourceDeck.getmDeckID()).child("Prompts").child("Deck").child(String.valueOf(cardID))
                                .child("Prompt").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                prompt = dataSnapshot.getValue(String.class);
                                Log.i("GameActivity","Prompt = " + prompt);
                                mGame.getmCurrentRound().setmPromptInPlay(prompt);
                                mGame.setmGameStatus(3);
                                ref.child(mGame.getmGameKey()).setValue(mGame);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.i("GameActivity","error on fetchPrompt occured");
                            }
                        });
                    }
                }
            }
        }
    }

//  private void playersChooseCard() {}


    //private void czarChoosesCard() {}

    private void localCountdown(){
        new CountDownTimer(61000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                countdownVal--;
                mCountDownView.setText(String.valueOf(countdownVal));
            }
            @Override
            public void onFinish() {
                if(!hasSelectedCard){
                    lockCard.setVisible(false);
                    Toast.makeText(GameActivity.this, "You are a strong independent person who don't need no card to choose!",Toast.LENGTH_LONG).show();
                }
                if(mGame.getmCurrentRound().getmPickCount() != mGame.getmUsersInGame()-1 && (mUser.getmUserGameID() == mGame.getmCzarID())){
                    mGame.setmGameStatus(4);
                    ref.child(mGame.getmGameKey()).setValue(mGame);
                }
                return;
            }
        }.start();
    }

    /*private void startCountdown() {
        mGame.setmGameStatus(0);
        mGame.getmCurrentRound().setmCountdown(60);
        new CountDownTimer(61000,1000) {
            @Override
            public void onTick(long l) {
                mGame.getmCurrentRound().setmCountdown(mGame.getmCurrentRound().getmCountdown()-1);
                ref.child(mGame.getmGameKey()).setValue(mGame);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }*/


    private void disconnectFromGame() {
        try {
            Thread.sleep((int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        mGame.setmUsersInGame(mGame.getmUsersInGame()-1);
        if(mGame.getmUsersInGame() == 0){
            ref.child(mGame.getmGameKey()).removeValue();
        }
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        lockCard = menu.findItem(R.id.lockCard);
        lockCard.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    //Confirm selected answer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.lockCard:
                //Toast.makeText(GameActivity.this, "clicked "+item, Toast.LENGTH_SHORT).show();
                if(mPlayersAreChoosing){

                    if(mGame.getmCurrentRound().getmCardWithUserList()==null){
                        List<CardWithUser> cardWithUsers = new ArrayList<>();
                        cardWithUsers.add(new CardWithUser(mUser.getmUserGameID(), selectedCard));
                        mGame.getmCurrentRound().setmCardWithUserList(cardWithUsers);
                    }else{
                        mGame.getmCurrentRound().addCardToCardWithUserList(new CardWithUser(mUser.getmUserGameID(), selectedCard));
                    }
                    mGame.getmCurrentRound().setmPickCount(mGame.getmCurrentRound().getmPickCount()+1);
                    if(mGame.getmCurrentRound().getmPickCount() == mGame.getmUsersInGame()-1){
                        mGame.setmGameStatus(4);
                    }

                    //Remove selected Card from Hand
                    mGame.getmUserGameList().get(mUser.getmUserGameID()).removeCardFromHand(selectedCard);
                    mGame.getmUserGameList().get(mUser.getmUserGameID()).setmCardCount(mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardCount()-1);

                    lockCard.setVisible(false);
                    hasSelectedCard = true;
                    mStatusView.setText("You picked: "+selectedCard);
                    ref.child(mGame.getmGameKey()).setValue(mGame);
                }else{
                    lockCard.setVisible(false);
                    int userID = 0;
                    for(CardWithUser cwu : mGame.getmCurrentRound().getmCardWithUserList()){
                        if(cwu.getmCardText().equals(selectedCard)){
                            userID = cwu.getmUserGameID();
                            break;
                        }
                    }
                    mStatusView.setText("The point goes to "+mGame.getmUserGameList().get(userID).getmName());
                    mGame.getmUserGameList().get(userID).setmPoints(mGame.getmUserGameList().get(userID).getmPoints()+1);

                    //Check if someone got all Points
                    if(mGame.getmUserGameList().get(userID).getmPoints() == mGame.getmWinpoints()){
                        mGame.setmGameStatus(5);
                        mStatusView.setText("The Winner is " + mGame.getmUserGameList().get(userID).getmName());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {

                        }
                        ref.child(mGame.getmGameKey()).setValue(mGame);

                    }else{
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {

                        }
                        mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
                        mGame.setmCzarID((mGame.getmCzarID()+1)%mGame.getmUsersInGame());
                        mGame.setmGameStatus(1);
                        ref.child(mGame.getmGameKey()).setValue(mGame);
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private class PlayersChooseCardThread extends Thread {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            mPlayersAreChoosing= true;
            mGame.setmGameStatus(0);
            /*if(mUser.getmUserGameID() != mGame.getmCzarID()){
                lockCard.setVisible(true);
            }else{
                lockCard.setVisible(false);
            }
*/
//            setTitle("Game");
            Log.i("GameActivity","Player choosing Cards");


            if(mUser.getmUserGameID() == mGame.getmCzarID()){
                hasSelectedCard = true;
            }else{
                hasSelectedCard = false;
            }


            //mCountDownView.setText(String.valueOf(mGame.getmCurrentRound().getmCountdown()));

            //countdownVal = 60;
            mCountDownView.setText(String.valueOf(countdownVal));
            //localCountdown();

            responseAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_list_item_1,mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand());
            mResponseView.setAdapter(responseAdapter);
            mResponseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!hasSelectedCard){
                        selectedCard = mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand().get(position);
                    }
                    Log.i("GameActivity", "selected Card  = " +selectedCard);
                }
            });
        }
    }

    class CzarChoosesCardThread extends Thread{
        @Override
        public void run() {
            Looper.prepare();
            super.run();
            mPlayersAreChoosing= false;
            mGame.setmGameStatus(0);
            /*if(mUser.getmUserGameID() == mGame.getmCzarID()){
                lockCard.setVisible(true);
                hasSelectedCard = false;
            }else{
                lockCard.setVisible(false);
            }*/



            //countdownVal = 60;
            //mCountDownView.setText(String.valueOf(countdownVal));
            //localCountdown();

            //check if
            promptAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_list_item_1,mGame.getmCurrentRound().getmCardWithUserList());
            mResponseView.setAdapter(promptAdapter);
            mResponseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(!hasSelectedCard){
                        selectedCard = mGame.getmCurrentRound().getmCardWithUserList().get(position).getmCardText();
                    }
                    Log.i("GameActivity", "selected Card  = " + selectedCard);
                }
            });
        }
    }
}