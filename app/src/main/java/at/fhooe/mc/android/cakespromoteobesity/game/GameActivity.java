package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.CardWithUser;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.card.DeckInfo;
import at.fhooe.mc.android.cakespromoteobesity.card.DeckGame;
import at.fhooe.mc.android.cakespromoteobesity.card.Prompt;
import at.fhooe.mc.android.cakespromoteobesity.card.Response;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;
import at.fhooe.mc.android.cakespromoteobesity.user.UserGame;

/**
 * This is the class which has the logic of the game and does push- and retrieves from the Firebase DB
 * Has a ValueEventListener which will retrieve the Game-Object from the DB when someone makes changes to it
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Game mGame;
    private User mUser;
    private DatabaseReference mainRef = MainActivity.mainRef;
    private DatabaseReference ref = mainRef.child("Games");
    private List<DeckGame> mCardsInUse;
    private List<UserGame> mUserScoreList;
    private List<Response> mAnswersArray;
    private String response, prompt, selectedCard;
    private TextView mPromptText, mStatusView, mCountDownView, mPromptId, mPromptPick;
    private ListView mResponseView, mScoreView;
    private RecyclerView mResponsesView, mAnswersView;

    private ArrayAdapter<String> responseAdapter;
    private ArrayAdapter<CardWithUser> promptAdapter;
    private ArrayAdapter<UserGame> scoreAdapter;
    private Integer countdownValPlayer, countDownValCzar;
    private MenuItem lockCard;
    private boolean hasSelectedCard, mPlayersAreChoosing, hasNotSetUpGame, viewNotLoaded = true;
    private ProgressBar loadingBar;
    private List<Deck> mDeckList;
    private int mPromptsSum, mResponsesSum, mResponseIndex, mAnswerIndex;

    private final int DO_NOTHING = 0;
    private final int FETCH_CARDS = 1;
    private final int FILL_HANDS_WITH_CARDS = 2;
    private final int GET_PROMPT = 3;
    private final int SET_UP_VIEW = 8;
    private final int PLAYERS_CHOOSE_CARD = 4;
    private final int CZAR_CHOOSES_CARD = 5;
    private final int CHECK_POINTS_FOR_ROUND = 6;
    private final int DISCONNECT_FROM_GAME = 7;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_game);
        //setContentView(R.layout.activity_game_setup); //-> done in the LobbyActivity

        //loadingBar = (ProgressBar)findViewById(R.id.progressBar_game_setup);
        //loadingBar.setVisibility(View.VISIBLE);

        mUser = MainActivity.mUser;

        Log.i("GameActivity", Thread.currentThread().getName() + " OnCreate");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mCardsInUse = new ArrayList<DeckGame>();
        //Get game passed from Intent
        final String mGameKey = (String) bundle.getSerializable("GameKey");

        ref.child(mGameKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mGame = dataSnapshot.getValue(Game.class);
                mGame.setmUsersInGame(mGame.getmUsersInGame() + 1);
                //Log.i("GameActivity", "increasing usersInGame to " + mGame.getmUsersInGame());
                setTitle(mGame.getmName() + " Game");

                //Get GameStatus to next status (fetch cards) if all Players joined
                if (mGame.getmUsersInGame() == mGame.getmUsersInLobby()) mGame.setmGameStatus(FETCH_CARDS);

                //Set mCardsInUse
                if (mUser.isHost()) {
                    mPromptsSum = mResponsesSum = 0;
                    for (DeckInfo deckInfo : mGame.getmSelectedDecks()) {
                        mCardsInUse.add(new DeckGame(deckInfo.getmDeckID()));
                        mPromptsSum += deckInfo.getmBlackCardCount();
                        mResponsesSum += deckInfo.getmWhiteCardCount();
                    }
                    Log.i("GameActivity","Prompt Sum: " + mPromptsSum + ", Resp Sum: " + mResponsesSum);
                }

                //hasNotSetUpGame = false;
                //Push new value
                ref.child(mGame.getmGameKey()).setValue(mGame);
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
                    case DO_NOTHING: {
                    }break;
                    case FETCH_CARDS: {
                        if (mUser.isHost()) fetchCards();
                    }break;
                    case FILL_HANDS_WITH_CARDS: {
                        if (mUser.isHost()) fillHands();
                    }break;
                    case GET_PROMPT: {
                        if (mUser.isHost()) getPrompt();
                    }break;
                    case SET_UP_VIEW: {
                        setUpView();
                    }break;
                    case PLAYERS_CHOOSE_CARD: {
                        playersChooseCard();
                    }break;
                    case CZAR_CHOOSES_CARD: {
                        countdownValPlayer = 1;
                        czarChoosesCard();
                    }break;
                    case CHECK_POINTS_FOR_ROUND: {
                        countDownValCzar = 1;
                        //could it be enough if only host?
                        checkPointsForRound();
                    }break;
                    case DISCONNECT_FROM_GAME: {
                        disconnectFromGame();
                    }break;
                    default : {
                        Log.i("GameActivity","Default statement reached in ValueEventListener switch-case");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * fetches all the Cards from online and offline and saves it in the DeckList
     * status will then change to start the Game (with fillHands)
     */
    private void fetchCards() {
        //get all the Cards from all the Decks at once
        Log.i("GameActivity","Entered fetchCards");
        mDeckList = new ArrayList<>();

        //get the cards online
        DatabaseReference cardRef = FirebaseDatabase.getInstance().getReference();//.child("Resources-official");
        cardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("GameActivity","cardRef");
                //mDecksTest = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.child("Resources-official").getChildren()) {
                    Deck deck = snap.getValue(Deck.class);
                    //check if deck is checked to be used for this game
                    for (DeckInfo info : mGame.getmSelectedDecks()) {
                        if (info.getmDeckName().equals(deck.getName())) {
                            mDeckList.add(deck);
                            break;
                        }
                    }
                }

                for (DataSnapshot snap : dataSnapshot.child("Resources-unofficial").getChildren()) {
                    Deck deck = snap.getValue(Deck.class);
                    for (DeckInfo info : mGame.getmSelectedDecks()) {
                        if (info.getmDeckName().equals(deck.getName())) {
                            mDeckList.add(deck);
                            break;
                        }
                    }
                }
                Collections.sort(mDeckList);

                //get the cards offline from the user



                Log.i("Main","Count of chosen Decks: " + mDeckList.size());
                mGame.setmGameStatus(FILL_HANDS_WITH_CARDS);
                ref.child(mGame.getmGameKey()).setValue(mGame);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    /**
     * fills the Hands of all users with cards, until all users have 10 cards
     * then changes the status to get the prompt
     */
    private void fillHands() {
        Log.i("GameActivity","Entered fillHands");
        //mGame.setmGameStatus(DO_NOTHING);
        //ref.child(mGame.getmGameKey()).setValue(mGame); //no need anymore - value only gets pushed at the end of this method

        for (Deck deck : mDeckList) {
            Log.i("GameActivity" ,"Deck Name = " + deck.getName());
        }

        int testLoop = 1;
        for (int userIndex = 0; userIndex < mGame.getmUsersInGame(); userIndex++) {
            //for every User
            int cardsHand;
            if (mGame.getmUserGameList().get(userIndex).getmCardsInHand() == null) cardsHand = 0;
            else cardsHand = mGame.getmUserGameList().get(userIndex).getmCardsInHand().size();
            for (int cardsInHand = cardsHand; cardsInHand < 10; cardsInHand++) {
                //for every missing card in hand
                boolean newCardNotFound = true;
                while (newCardNotFound) {
                    //while new card not found
                    //get a random Number and check in which Deck it belongs to
                    int randomID = (int)(Math.random()*mResponsesSum);
                    for (int deckIndex = 0; deckIndex < mGame.getmSelectedDecks().size(); deckIndex++) {
                        //for every deck in selected decks
                        DeckInfo info = mGame.getmSelectedDecks().get(deckIndex);
                        if (randomID < info.getmWhiteCardCount()) {
                            //if random id is lower than deck size, the id belongs in this deck
                            boolean isNotUsed = true;
                            if (mCardsInUse.get(deckIndex).getmCardResponsesID().size() == mGame.getmSelectedDecks().get(deckIndex).getmWhiteCardCount()) {
                                mCardsInUse.get(deckIndex).setmCardResponsesID(new ArrayList<Integer>());
                                Log.i("GameActivity","Used all cards from " + mGame.getmSelectedDecks().get(deckIndex).getmDeckName() + ", resetting those cards");
                            }else {
                                for (int num : mCardsInUse.get(deckIndex).getmCardResponsesID()) {
                                    if (num == randomID) {
                                        isNotUsed = false;
                                        break;
                                    }
                                }
                            }
                            if (isNotUsed) {
                                //Card not used, can be added to the users hand
                                Log.i("GameActivity","Random ID is not used yet, #" + testLoop + " - random = " + randomID + ", responseCount = " + info.getmWhiteCardCount());
                                testLoop++;

                                if (mCardsInUse.get(deckIndex).getmCardResponsesID() == null) mCardsInUse.get(deckIndex).setmCardResponsesID(new ArrayList<Integer>());
                                mCardsInUse.get(deckIndex).addCardToResponses(randomID);
                                newCardNotFound = false;

                                mGame.getmUserGameList().get(userIndex).addCardToHand(new Response(mDeckList.get(deckIndex).getResponses().get(randomID), mDeckList.get(deckIndex).getId()));
                                break;
                            }
                        }else randomID -= info.getmWhiteCardCount(); //random id gets lowered by the size, now goes in loop to next deck to check
                    }//for every deck in selected decks
                }//while new card not found
            }// for every missing card in hand
        }//for every User

        //set status to next, push new value with new cards to db
        mGame.setmGameStatus(GET_PROMPT);
        ref.child(mGame.getmGameKey()).setValue(mGame);
    }

    /**
     * Fills the Cards of all Users up to 10 -> obsolete
     */
    private void fillCardsUp() {
        //set the Status to 0 - no new methods get created when mGame gets updated in the DB
        if (mGame.getmGameStatus() == 1) {
            mGame.setmGameStatus(DO_NOTHING);
            ref.child(mGame.getmGameKey()).setValue(mGame);
        }
        for (int userIndex = 0; userIndex < mGame.getmUsersInGame(); userIndex++) {
            //for every user
            //sleep should be obsolete
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int cardCount = mGame.getmUserGameList().get(userIndex).getmCardCount();
            for (int cardsInHand = cardCount; cardsInHand < 10; cardsInHand++) {
                //for every missing card
                boolean cardIsChecked = true;
                while (cardIsChecked) {
                    //while the chosen card is already being used
                    //get a better randomizer - should include total number of cards in the deck so every card has the same chance
                    int resourceID = (int) (Math.random() * mGame.getmResourcesCount());
                    DeckInfo resourceDeckInfo = mGame.getmSelectedDecks().get(resourceID);
                    int cardID = (int) (Math.random() * resourceDeckInfo.getmWhiteCardCount());

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
                                fetchResponse(cardID, resourceDeckInfo.getmDeckID(), userIndex);
                            } else {
                                cardIsChecked = false;
                                for (int i : deck.getmCardResponsesID()) {
                                    if (i == cardID) {
                                        cardIsChecked = true;
                                    }
                                }
                                if (!cardIsChecked) {
                                    deck.addCardToResponses(cardID);
                                    fetchResponse(cardID, resourceDeckInfo.getmDeckID(), userIndex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Fetches a Response from Database and stores it in the response String
     * After all card got Fetched, the Gamestatus will change to 2, so that the game can continue
     * @param _cardID int of the cardID in the database
     * @param _resourceName String of the DeckInfo Resource ID from the database
     * @param _userIndex int of the user in the UserGameList, who gets the fetched Card
     */
    private void fetchResponse(int _cardID, String _resourceName, final int _userIndex) {
        Log.i("GameActivity","Entered fetchResponse");
        FirebaseDatabase.getInstance().getReference().child("Resources").child(_resourceName).child("Responses").child("DeckInfo").child(String.valueOf(_cardID))
                .child("Response").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                response = dataSnapshot.getValue(String.class);
                Log.i("GameActivity", "Response = " + response);
                //Log.i("Fetch Data","Add Response to hand");
                //mGame.getmUserGameList().get(_userIndex).addCardToHand(response); //commment out cause exception
                mGame.getmUserGameList().get(_userIndex).setmCardCount(mGame.getmUserGameList().get(_userIndex).getmCardCount() + 1);

                //Check if all cards are filled up, start the Round = 2
                //if (_userIndex ==  mGame.getmUserGameList().size()-1 && mGame.getmUserGameList().get(_userIndex).getmCardCount() == 10) mGame.setmGameStatus(GET_PROMPT);
                boolean filledUp = true;
                for (UserGame user : mGame.getmUserGameList()) {
                    if (user.getmCardCount() != 10) {
                        filledUp = false;
                        break;
                    }
                }
                if (filledUp) mGame.setmGameStatus(GET_PROMPT);

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

    /**
     * Fetches the Prompt which should be displayed to all users, from the database
     * After the card got Fetched, the Gamestatus will change to 3, so that the game can continue
     */
    /*private void getPrompt() {
        boolean cardIsChecked = true;
        while (cardIsChecked) {
            int resourceID = (int) (Math.random() * mGame.getmResourcesCount());
            DeckInfo resourceDeckInfo = mGame.getmSelectedDecks().get(resourceID);
            int cardID = (int) (Math.random() * resourceDeckInfo.getmBlackCardCount());

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
                        //fetchResponse(cardID, resourceDeckInfo.getmDeckID(), mGame.getmUserGameList().get(userIndex), userIndex);
                        Log.i("GameActivity","Entered fetchPrompt");
                        FirebaseDatabase.getInstance().getReference().child("Resources").child(resourceDeckInfo.getmDeckID()).child("Prompts").child("DeckInfo").child(String.valueOf(cardID))
                                .child("Prompt").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                prompt = dataSnapshot.getValue(String.class);
                                Log.i("GameActivity","Prompt = " + prompt);
                                mGame.getmCurrentRound().setmPromptInPlay(prompt);
                                mGame.setmGameStatus(PLAYERS_CHOOSE_CARD);
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
    }*/

    private void getPrompt() {
        boolean newCardNotFound = true;
        while (newCardNotFound) {
            //while new card not found
            //get a random Number and check in which Deck it belongs to
            int randomID = (int)(Math.random()*mPromptsSum);
            for (int deckIndex = 0; deckIndex < mGame.getmSelectedDecks().size(); deckIndex++) {
                //for every deck in selected decks
                DeckInfo info = mGame.getmSelectedDecks().get(deckIndex);
                if (randomID < info.getmBlackCardCount()) {
                    //if random id is lower than deck size, the id belongs in this deck
                    boolean isNotUsed = true;
                    for (int num : mCardsInUse.get(deckIndex).getmCardPromptsID()) {
                        if (num == randomID) {
                            isNotUsed = false;
                            break;
                        }
                    }
                    if (isNotUsed) {
                        //Card not used, can be added to the users hand
                        Log.i("GameActivity","Prompt: " + mDeckList.get(deckIndex).getPrompts().get(randomID).getText());

                        if (mCardsInUse.get(deckIndex).getmCardResponsesID() == null) mCardsInUse.get(deckIndex).setmCardPromptsID(new ArrayList<Integer>());
                        mCardsInUse.get(deckIndex).addCardToPrompts(randomID);
                        newCardNotFound = false;
                        Prompt p = mDeckList.get(deckIndex).getPrompts().get(randomID);
                        p.setId(mDeckList.get(deckIndex).getId());
                        mGame.getmCurrentRound().setmPromptInPlay(p);
                        break;
                    }
                }else randomID -= info.getmBlackCardCount(); //random id gets lowered by the size, now goes in loop to next deck to check
            }//for every deck in selected decks
        }//while new card not found

        //set status to next, push new value with new cards to db
        if (viewNotLoaded) {
            mGame.setmGameStatus(SET_UP_VIEW);
            viewNotLoaded = false;
        }
        else mGame.setmGameStatus(PLAYERS_CHOOSE_CARD);
        ref.child(mGame.getmGameKey()).setValue(mGame);
    }

    /**
     * Sets up the ContentView, gets called once at the beginning
     */
    private void setUpView() {
        setContentView(R.layout.activity_game);
        //mGame.setmGameStatus(DO_NOTHING);
        selectedCard = null;

        //Find the views
        mPromptText = (TextView)findViewById(R.id.tv_game_prompt);
        mPromptId = (TextView)findViewById(R.id.tv_game_prompt_id);
        mPromptPick = (TextView)findViewById(R.id.tv_game_prompt_pick);
        mStatusView = (TextView)findViewById(R.id.tv_game_status);
        mCountDownView = (TextView)findViewById(R.id.tv_game_countDown);
        //mResponseView = (ListView)findViewById(R.id.listView_game_responses);
        mScoreView = (ListView)findViewById(R.id.listView_game_score);
        mResponsesView = (RecyclerView)findViewById(R.id.recView_game_responses);
        mAnswersView = (RecyclerView)findViewById(R.id.recView_game_answers);

        //Set the views
        mPromptText.setText(mGame.getmCurrentRound().getmPromptInPlay().getText());
        mPromptId.setText(mGame.getmCurrentRound().getmPromptInPlay().getId());
        mPromptPick.setText("Select: " + mGame.getmCurrentRound().getmPromptInPlay().getPick());


        //responseAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_list_item_1,mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand());
        //mResponseView.setAdapter(responseAdapter);
        //mResponseView.setSpinnerSelectedItemsListner(this);

        //Set the ListViews
        mAnswersArray = new ArrayList<>(mGame.getmCurrentRound().getmPromptInPlay().getPick());
        for (int i = 0; i < mGame.getmCurrentRound().getmPromptInPlay().getPick(); i++) mAnswersArray.add(mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand().get(i));
        LinearLayoutManager manager2 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        //manager2.setSmoothScrollbarEnabled(false);
        mAnswersView.setLayoutManager(manager2);
        final ResponseListAdapter adapter2 = new ResponseListAdapter(this, mAnswersArray);
        mAnswersView.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new ResponseListAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View _view, int _position) {
                Log.i("GameActivity","Answer in Game, pos: " + _position);
                mAnswerIndex = _position;
            }
        });

        LinearLayoutManager manager1 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mResponsesView.setLayoutManager(manager1);
        final ResponseListAdapter adapter1 = new ResponseListAdapter(this,mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand());
        mResponsesView.setAdapter(adapter1);
        adapter1.setOnItemClickListener(new ResponseListAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View _view, int _position) {
                Log.i("GameActivity","Response in Game, pos: " + _position);
                mResponseIndex = _position;

                /*boolean notInUse = true;
                for (Response resp : mAnswersArray) {
                    if (newItem.getText().equals(resp.getText())) {
                        notInUse = false;
                    }
                }
                if (notInUse) {
                    mAnswersArray.set(mAnswerIndex,newItem);
                    adapter2.notifyDataSetChanged();
                }else Log.i("GameActivity","Response already in use");*/

                Response newItem = mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand().get(_position);
                mAnswersArray.set(mAnswerIndex,newItem);
                adapter2.notifyDataSetChanged();
            }
        });


        mUserScoreList = mGame.getmUserGameList();
        scoreAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_list_item_1,mUserScoreList);
        mScoreView.setAdapter(scoreAdapter);

        mGame.setmGameStatus(PLAYERS_CHOOSE_CARD);
        ref.child(mGame.getmGameKey()).setValue(mGame);
    }

    /**
     * The players select a card from their Hand, which will be locked in and then displayed
     * to the Czar later.
     * the "activity_game" will be updated.
     * Every player got a countdown of 60 seconds for picking a card.
     * If all Players picked a card , or the Countdown goes to 0, the gamestatus changes to 4 and the game
     * carries on
     */
    private void playersChooseCard() {
        setTitle("Players choosing cards");
        //Log.i("GameActivity","Player choosing Cards");

        //make new Adapter for Czar choosing?
        //mResponsesView.setVisibility(View.VISIBLE);

        mPlayersAreChoosing = true;
        if(mUser.getmUserGameID() != mGame.getmCzarID()) {
            lockCard.setVisible(true);
            hasSelectedCard = false;
            mStatusView.setText("Card Czar is "+mGame.getmUserGameList().get(mGame.getmCzarID()).getmName()+", select a Card");
        }
        else {
            lockCard.setVisible(false);
            hasSelectedCard = true;
            mStatusView.setText("You are the Card Czar");
        }

        countdownValPlayer = 61;
        mCountDownView.setText(String.valueOf(countdownValPlayer));

        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                countdownValPlayer--;
                //mCountDownView.setText(String.valueOf(countdownValPlayer));
                displayTimerText(String.valueOf(countdownValPlayer));
                if (countdownValPlayer > 0) displayTimerText(String.valueOf(countdownValPlayer));
                else {
                    this.cancel();
                    if (mGame.getmCzarID() != mUser.getmUserGameID()) setLockCard(false);
                    if (mGame.getmCurrentRound().getmPickCount() != mGame.getmUserGameList().size()-1 && mUser.getmUserGameID() == mGame.getmCzarID()) {
                        mGame.setmGameStatus(CZAR_CHOOSES_CARD);
                        ref.child(mGame.getmGameKey()).setValue(mGame);
                    }
                }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task,0,1000);
    }

    /**
     * The Czar selects a card from the available responses.
     * The Owner of the selected Card gets a Point. If no Cards were selected by the Players, the Card
     * Czar get a point. If the Countdown runs to 0 , the Czar loses a point.
     * If the Czar picked a card , or the Countdown goes to 0, or no cards were chosen by the players.
     * If the Czar picked a card, the gamestatus goes to 5.
     * If the Countdown runs to 0, the gamestatus goes to 1.
     * If no player picked a card, the Point calculation will be done here, and if the Czar hasn't got all
     * Points, the status changes to 1
     * If the Czar got all Points the status goes to 6.
     */
    private void czarChoosesCard() {
        //check if noone chose a card
        if (mGame.getmCurrentRound().getmPickCount() == 0) {
            Toast.makeText(GameActivity.this,"No one picked a card! The czar gets the point.",Toast.LENGTH_SHORT).show();

            int czarpoint = mGame.getmUserGameList().get(mGame.getmCzarID()).getmPoints()+1;
            //mGame.getmUserGameList().get(mGame.getmCzarID()).setmPoints(mGame.getmUserGameList().get(mGame.getmCzarID()).getmPoints()+1);
            if (czarpoint == mGame.getmWinpoints()) {
                mStatusView.setText("The Winner is " + mGame.getmUserGameList().get(mGame.getmCzarID()).getmName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mUser.getmUserGameID() == mGame.getmCzarID()) {
                    mGame.getmUserGameList().get(mGame.getmCzarID()).setmPoints(czarpoint);
                    mGame.setmGameStatus(DISCONNECT_FROM_GAME);
                    ref.child(mGame.getmGameKey()).setValue(mGame);
                }
            }else {
                if (mUser.getmUserGameID() == mGame.getmCzarID()) {
                    mGame.getmUserGameList().get(mGame.getmCzarID()).setmPoints(czarpoint);
                    mGame.getmCurrentRound().setmPickCount(0);
                    mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
                    mGame.setmCzarID((mGame.getmCzarID()+1)%mGame.getmUsersInGame());
                    mGame.setmGameStatus(GET_PROMPT);
                    ref.child(mGame.getmGameKey()).setValue(mGame);
                }
            }
        }else {
            //Normal action
            selectedCard = null;
            if(mUser.getmUserGameID() == mGame.getmCzarID()){
                lockCard.setVisible(true);
                hasSelectedCard = false;
            }else{
                lockCard.setVisible(false);
            }

            if (mGame.getmCzarID() == mUser.getmUserGameID()) mStatusView.setText("Pick the best answer");
            else mStatusView.setText(mGame.getmUserGameList().get(mGame.getmCzarID()).getmName()+ " is picking");

            mPlayersAreChoosing= false;
            //mGame.setmGameStatus(DO_NOTHING);

            countDownValCzar = 61;
            mCountDownView.setText(String.valueOf(countDownValCzar));

            TimerTask task = new TimerTask(){
                @Override
                public void run() {
                    countDownValCzar--;
                    //mCountDownView.setText(String.valueOf(countDownValCzar));
                    if (countDownValCzar > 0) displayTimerText(String.valueOf(countDownValCzar));
                    else {
                        this.cancel();
                        setLockCard(false);
                        if (!hasSelectedCard && mGame.getmGameStatus() == 4) {
                            //Toast.makeText(getApplicationContext(),"The Czar hasn't chosen a card! He gets a point deducted.",Toast.LENGTH_SHORT).show();
                            mGame.getmUserGameList().get(mGame.getmCzarID()).setmPoints(mGame.getmUserGameList().get(mGame.getmCzarID()).getmPoints()-1);
                            mGame.setmGameStatus(FILL_HANDS_WITH_CARDS);
                            mGame.getmCurrentRound().setmPickCount(0);
                            mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
                            if (mGame.getmCzarID() == mUser.getmUserGameID()) {
                                mGame.setmCzarID((mGame.getmCzarID()+1)%mGame.getmUsersInGame());
                                ref.child(mGame.getmGameKey()).setValue(mGame);
                            }
                        }
                    }
                }
            };

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task,0,1000);

            if (mGame.getmCurrentRound().getmPickCount() != 0) {
                promptAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_list_item_1,mGame.getmCurrentRound().getmCardWithUserList());
                //mResponseView.setAdapter(promptAdapter);
                //mResponseView.setSpinnerSelectedItemsListner(this);
            }
        }
    }

    /**
     * Checks if a User got all Points to Win the Game
     * StatusView gets updated, for which player got a Point this round.
     * If someone won the game, status goes to 6 else 1.
     */
    private void checkPointsForRound() {
        /*int userID = 0;
        for (CardWithUser cwu : mGame.getmCurrentRound().getmCardWithUserList()) {
            if (cwu.getmCardText().equals(selectedCard)) {
                userID = cwu.getmUserGameID();
                break;
            }
        }*/
        int userID = mGame.getmCurrentRound().getmCardWithUserList().get(mGame.getmCurrentRound().getmCzarPickID()).getmUserGameID();

        mStatusView.setText("The point goes to " + mGame.getmUserGameList().get(userID).getmName());
        mGame.getmUserGameList().get(userID).setmPoints(mGame.getmUserGameList().get(userID).getmPoints() + 1);

        //Check if someone got all Points
        if(mGame.getmUserGameList().get(userID).getmPoints() == mGame.getmWinpoints()){
            mStatusView.setText("The Winner is " + mGame.getmUserGameList().get(userID).getmName());
            if (mUser.getmUserGameID() == mGame.getmCzarID()) {
                mGame.setmGameStatus(DISCONNECT_FROM_GAME);
                ref.child(mGame.getmGameKey()).setValue(mGame);
            }
        }else{
            if (mUser.getmUserGameID() == mGame.getmCzarID()) {
                mGame.getmCurrentRound().setmPickCount(0);
                mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
                mGame.setmCzarID((mGame.getmCzarID()+1)%mGame.getmUsersInGame());
                mGame.setmGameStatus(FILL_HANDS_WITH_CARDS);
                ref.child(mGame.getmGameKey()).setValue(mGame);
            }
        }
    }

    /**
     * All Players disconnect from the game and get to the Scoreboard View
     */
    private void disconnectFromGame() {
        mGame.setmGameStatus(DO_NOTHING);
        try {
            Thread.sleep((int)(Math.random()*200));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mGame.setmUsersInGame(mGame.getmUsersInGame()-1);
        if(mGame.getmUsersInGame() == 0) {
            ref.child(mGame.getmGameKey()).removeValue();
        }else ref.child(mGame.getmGameKey()).setValue(mGame);

        Intent i = new Intent(this, ScoreboardActvity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Scoreboard", (Serializable) mGame.getmUserGameList());
        i.putExtras(bundle);
        startActivity(i);
    }


    /**
     * create item in taskbar
     * @param menu
     * @return
     */
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
        switch (item.getItemId()) {
            case R.id.lockCard:
                if (selectedCard != null) {
                    //Toast.makeText(GameActivity.this, "clicked "+item, Toast.LENGTH_SHORT).show();
                    if (mPlayersAreChoosing) {
                        //Player chooses a card
                        lockCard.setVisible(false);
                        if (mGame.getmCurrentRound().getmCardWithUserList() == null) {
                            List<CardWithUser> cardWithUsers = new ArrayList<>();
                            cardWithUsers.add(new CardWithUser(mUser.getmUserGameID(), selectedCard));
                            mGame.getmCurrentRound().setmCardWithUserList(cardWithUsers);
                        } else {
                            mGame.getmCurrentRound().addCardToCardWithUserList(new CardWithUser(mUser.getmUserGameID(), selectedCard));
                        }
                        mGame.getmCurrentRound().setmPickCount(mGame.getmCurrentRound().getmPickCount() + 1);
                        if (mGame.getmCurrentRound().getmPickCount() == mGame.getmUsersInGame() - 1) {
                            mGame.setmGameStatus(CZAR_CHOOSES_CARD);
                            //countdownValPlayer = 1;
                        }

                        //Remove selected Card from Hand
                        mGame.getmUserGameList().get(mUser.getmUserGameID()).removeCardFromHand(selectedCard);
                        mGame.getmUserGameList().get(mUser.getmUserGameID()).setmCardCount(mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardCount() - 1);

                        hasSelectedCard = true;
                        mStatusView.setText("You picked: " + selectedCard);
                        ref.child(mGame.getmGameKey()).setValue(mGame);
                    } else {
                        //Czar chooses a card
                        lockCard.setVisible(false);
                        hasSelectedCard = true;
                        //countDownValCzar = 1;
                        mGame.setmGameStatus(CHECK_POINTS_FOR_ROUND);
                        ref.child(mGame.getmGameKey()).setValue(mGame);
                    }
                }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * onClick Listener for recyclerViews - obsolete
     * @param _view
     */
    @Override
    public void onClick(View _view) {
        Log.i("GameActivity", "OnClick");
        switch (_view.getId()) {
            case (R.id.recView_game_responses) : {
                mResponseIndex = mResponsesView.getChildAdapterPosition(_view);
                Log.i("GameActivity", "Adapterpos: " + mResponseIndex);
            }break;
            case (R.id.recView_game_answers) : {
                mAnswerIndex = mAnswersView.getChildAdapterPosition(_view);
                Log.i("GameActivity", "Adapterpos: " + mAnswerIndex);
            }break;
        }
    }

    /**
     * on listview item click
     * @param adapterView
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mPlayersAreChoosing) {
            //Players select a card
            if(!hasSelectedCard){
                selectedCard = mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand().get(position).getText();
            }
            Log.i("GameActivity", "selected Card  = " + selectedCard);
        }else {
            //Czar selects a card
            if(!hasSelectedCard){
                selectedCard = mGame.getmCurrentRound().getmCardWithUserList().get(position).getmCardText();
                mGame.getmCurrentRound().setmCzarPickID(position);
            }
            Log.i("GameActivity", "selected Card  = " + selectedCard);
        }
    }

    /**
     * Displayes the Countdown in the CountdownView
     * @param _text new time which should be displayed
     */
    private void displayTimerText(final String _text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCountDownView.setText(_text);
            }
        });
    }

    /**
     * Sets the "LockCard" button visibility
     * @param _b boolean visible or invisible
     */
    private void setLockCard(final boolean _b) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lockCard.setVisible(_b);
            }
        });
    }
}