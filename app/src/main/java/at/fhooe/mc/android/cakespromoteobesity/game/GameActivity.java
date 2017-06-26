package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import at.fhooe.mc.android.cakespromoteobesity.card.ResponseWithUser;
import at.fhooe.mc.android.cakespromoteobesity.card.ResponseWithUserList;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;
import at.fhooe.mc.android.cakespromoteobesity.user.UserGame;

/**
 * This is the class which has the logic of the game and does push- and retrieves from the Firebase DB
 * Has a ValueEventListener which will retrieve the Game-Object from the DB when someone makes changes to it
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Game mGame;
    private User mUser;
    private DatabaseReference mainRef = MainActivity.mainRef;
    private DatabaseReference ref = mainRef.child("Games");
    private List<DeckGame> mCardsInUse;
    private List<UserGame> mUserScoreList;
    private List<Response> mAnswersArray;
    private List<ResponseWithUser> mAnswersList;
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
    private final int CHECK_ANSWERS = 9;
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
                    case CHECK_ANSWERS: {
                        if (mUser.isHost()) checkAnswers();
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
                                mGame.getmUserGameList().get(userIndex).setmCardCount(mGame.getmUserGameList().get(userIndex).getmCardCount()+1);
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
     * Fetches the Prompt which should be displayed to all users, from the database
     * After the card got Fetched, the Gamestatus will change to setUpView, so that the game can continue
     */
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
        /*if (viewNotLoaded) {
            mGame.setmGameStatus(SET_UP_VIEW);
            viewNotLoaded = false;
        }
        else mGame.setmGameStatus(PLAYERS_CHOOSE_CARD);
        */
        mGame.setmGameStatus(SET_UP_VIEW);
        ref.child(mGame.getmGameKey()).setValue(mGame);
    }

    /**
     * Sets up the ContentView, gets called once at the beginning round
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

        //Set the scoreboard ListView
        mUserScoreList = mGame.getmUserGameList();
        scoreAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_list_item_1,mUserScoreList);
        mScoreView.setAdapter(scoreAdapter);

        if (mUser.isHost()) {
            mGame.setmGameStatus(PLAYERS_CHOOSE_CARD);
            ref.child(mGame.getmGameKey()).setValue(mGame);
        }
    }

    /**
     * The players select a card from their Hand, which will be locked in and then displayed
     * to the Czar later.
     * the "activity_game" will be updated.
     * Every player got a countdown of 60 seconds for picking a card.
     * If all Players picked a card , or the Countdown goes to 0, the gamestatus changes to CZAR_CHOOSES_CARD and the game
     * carries on
     */
    private void playersChooseCard() {
        setTitle("Players choosing cards");
        //Log.i("GameActivity","Player choosing Cards");

        //make new Adapter for Czar choosing?
        mResponsesView.setVisibility(View.VISIBLE);
        mAnswerIndex = 0;

        //Set the ListViews
        mAnswersArray = new ArrayList<>(mGame.getmCurrentRound().getmPromptInPlay().getPick());
        for (int i = 0; i < mGame.getmCurrentRound().getmPromptInPlay().getPick(); i++) mAnswersArray.add(mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand().get(i));
        LinearLayoutManager managerAnswers = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mAnswersView.setLayoutManager(managerAnswers);
        final ResponseAdapter adapterAnswers = new ResponseAdapter(this, mAnswersArray, true);
        mAnswersView.setAdapter(adapterAnswers);
        adapterAnswers.setOnItemClickListener(new ResponseAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View _view, int _position) {
                //Log.i("GameActivity","Answer in Game, pos: " + _position);
                mAnswerIndex = _position;
            }
        });

        LinearLayoutManager managerResponses = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mResponsesView.setLayoutManager(managerResponses);
        final ResponseAdapter adapterResponses = new ResponseAdapter(this,mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand(), false);
        mResponsesView.setAdapter(adapterResponses);
        adapterResponses.setOnItemClickListener(new ResponseAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View _view, int _position) {
                //Log.i("GameActivity","Response in Game, pos: " + _position);
                mResponseIndex = _position;
                Response newItem = mGame.getmUserGameList().get(mUser.getmUserGameID()).getmCardsInHand().get(_position);
                mAnswersArray.set(mAnswerIndex,newItem);
                adapterAnswers.notifyDataSetChanged();
            }
        });

        mGame.setmGameStatus(CHECK_ANSWERS);
        if (mUser.isHost()) ref.child(mGame.getmGameKey()).setValue(mGame);

        mPlayersAreChoosing = true;
        if(mUser.getmUserGameID() != mGame.getmCzarID()) {
            lockCard.setVisible(true);
            mAnswersView.setVisibility(View.VISIBLE);
            hasSelectedCard = false;
            mStatusView.setText("Card Czar is "+mGame.getmUserGameList().get(mGame.getmCzarID()).getmName()+", select a Card");
        }
        else {
            lockCard.setVisible(false);
            mAnswersView.setVisibility(View.INVISIBLE);
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
                    /*if (mGame.getmCurrentRound().getmPickCount() != mGame.getmUserGameList().size()-1 && mUser.getmUserGameID() == mGame.getmCzarID()) {
                        mGame.setmGameStatus(CZAR_CHOOSES_CARD);
                        ref.child(mGame.getmGameKey()).setValue(mGame);
                    }*/
                    if (mGame.getmGameStatus() != CZAR_CHOOSES_CARD && mUser.getmUserGameID() == mGame.getmCzarID()) {
                        mGame.setmGameStatus(CZAR_CHOOSES_CARD);
                        ref.child(mGame.getmGameKey()).setValue(mGame);
                    }
                }
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task,0,1000);
    }

    private void checkAnswers() {
        boolean allPeopleAnswered = true;
        for (int i = 0; i < mGame.getmUserGameList().size(); i++) {
            if (mGame.getmUserGameList().get(i).getmSelectedCards() == null && mGame.getmCzarID() != i) allPeopleAnswered = false;
            else if (mGame.getmCzarID() != i) Log.i("GameActivity","Answer: " + mGame.getmUserGameList().get(i).getmSelectedCards().get(0).getText());
        }
        if (allPeopleAnswered) {
            mGame.setmGameStatus(CZAR_CHOOSES_CARD);
            ref.child(mGame.getmGameKey()).setValue(mGame);
        }
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
        boolean nooneSelectedCard = true;
        for (int i = 0; i < mGame.getmUserGameList().size(); i++) {
            if (mGame.getmUserGameList().get(i).getmSelectedCards() != null) nooneSelectedCard = false;
        }
        if (nooneSelectedCard) {
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
                    //mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
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
                hasSelectedCard = true;
            }
            if (mGame.getmCzarID() == mUser.getmUserGameID()) mStatusView.setText("Pick the best answer");
            else mStatusView.setText(mGame.getmUserGameList().get(mGame.getmCzarID()).getmName()+ " is picking");
            mPlayersAreChoosing= false;
            mResponsesView.setVisibility(View.INVISIBLE);
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
                        //commented this out so lockcard stay visible for czar in next round
                        //setLockCard(false);
                        if (!hasSelectedCard && /*mGame.getmGameStatus() == CZAR_CHOOSES_CARD &&*/ mGame.getmCzarID() == mUser.getmUserGameID()) {
                            //Toast.makeText(getApplicationContext(),"The Czar hasn't chosen a card! He gets a point deducted.",Toast.LENGTH_SHORT).show();
                            Log.i("GameActivity","Czar didn't pick a card.");
                            mGame.getmUserGameList().get(mGame.getmCzarID()).setmPoints(mGame.getmUserGameList().get(mGame.getmCzarID()).getmPoints()-1);
                            mGame.setmGameStatus(CHECK_POINTS_FOR_ROUND);
                            mGame.getmCurrentRound().setmCzarPickID(0);
                            for (UserGame user : mGame.getmUserGameList()) {
                                if (user.getmSelectedCards() != null) user.setmSelectedCards(null);
                            }

                            //mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
                            mGame.setmCzarID((mGame.getmCzarID()+1)%mGame.getmUsersInGame());
                            ref.child(mGame.getmGameKey()).setValue(mGame);
                        }
                    }
                }
            };

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task,0,1000);

            //Get the answers
            mAnswerIndex = 0;
            mAnswersList = new ArrayList<>();

            List<ResponseWithUserList> responsesList = new ArrayList<>();
            for (int i = 0; i < mGame.getmUserGameList().size(); i++) {
                List<Response> responses = mGame.getmUserGameList().get(i).getmSelectedCards();
                if (responses != null) responsesList.add(new ResponseWithUserList(responses,i));
            }
            Collections.shuffle(responsesList);

            for (ResponseWithUserList respList : responsesList) {
                for (Response resp : respList.getResponsesList()) {
                    mAnswersList.add(new ResponseWithUser(resp,respList.getUserID()));
                }
            }
            for (ResponseWithUser resp : mAnswersList) {
                Log.i("GameActivity", "Response: " + resp.getResponse().getText() + ", userID: " + resp.getUserID());
            }

            if (mGame.getmCurrentRound().getmPromptInPlay().getPick() > 1) {
                //more then 1 answer - GridLayout needed
                GridLayoutManager manager = new GridLayoutManager(this,mGame.getmCurrentRound().getmPromptInPlay().getPick(),GridLayoutManager.VERTICAL,false);
                mAnswersView.setLayoutManager(manager);
            }else {
                //only 1 answer - normal layout
                LinearLayoutManager managerAnswers = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
                mAnswersView.setLayoutManager(managerAnswers);
            }
            ResponseListAdapter listAdapter = new ResponseListAdapter(this, mAnswersList);
            mAnswersView.setAdapter(listAdapter);
            listAdapter.setOnItemClickListener(new ResponseListAdapter.onRecyclerViewItemClickListener() {
                @Override
                public void onItemClickListener(View _view, int _position) {
                    //Log.i("GameActivity","Answer in Game, pos: " + _position);
                    mAnswerIndex = _position;
                }
            });

            mAnswersView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks if a User got all Points to Win the Game
     * StatusView gets updated, for which player got a Point this round.
     * If someone won the game, status goes to DISCONNECT else FILLHANDS.
     */
    private void checkPointsForRound() {
        /*int userID = 0;
        for (CardWithUser cwu : mGame.getmCurrentRound().getmCardWithUserList()) {
            if (cwu.getmCardText().equals(selectedCard)) {
                userID = cwu.getmUserGameID();
                break;
            }
        }*/
        //int userID = mGame.getmCurrentRound().getmCardWithUserList().get(mGame.getmCurrentRound().getmCzarPickID()).getmUserGameID();
        int userID = mGame.getmCurrentRound().getmCzarPickID();

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
                //mGame.getmCurrentRound().setmPickCount(0);
                //mGame.getmCurrentRound().setmCardWithUserList(new ArrayList<CardWithUser>());
                for (UserGame user : mGame.getmUserGameList()) {
                    if (user.getmSelectedCards() != null) user.setmSelectedCards(null);
                }

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
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mGame.getmUsersInGame()-1 == mUser.getmUserGameID()) {
            mGame.setmUsersInGame(mGame.getmUsersInGame()-1);
            if(mGame.getmUsersInGame() == 0) {
                ref.child(mGame.getmGameKey()).removeValue();
            }else ref.child(mGame.getmGameKey()).setValue(mGame);
        }

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
        lockCard.setTitle("Confirm");
        return super.onCreateOptionsMenu(menu);
    }


    //Confirm selected answer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lockCard: {
                //selected Card is not used anymore - if gone
                if (mPlayersAreChoosing) {
                    //Player chooses a card
                    boolean hasChosenDifferentCards = true;
                    for (int i = 0; i < mAnswersArray.size(); i++) {
                        String text = mAnswersArray.get(i).getText();
                        Log.i("GameActivity","AnswersArray #" + i + ", " + text);
                        for (int j = i+1; j < mAnswersArray.size(); j++) {
                            if (text.equals(mAnswersArray.get(j).getText())) hasChosenDifferentCards = false;
                        }
                    }
                    Log.i("GameActivity",String.valueOf(hasChosenDifferentCards));
                    if (hasChosenDifferentCards) {
                        lockCard.setVisible(false);
                        UserGame user = mGame.getmUserGameList().get(mUser.getmUserGameID());
                        user.setmSelectedCards(mAnswersArray);
                        for (Response resp : mAnswersArray) {
                            //Log.i("GameActivity","AnswersArray used: " + resp.getText());
                            user.removeCardFromHand(resp);
                            user.setmCardCount(user.getmCardCount()-1);
                        }
                        hasSelectedCard = true;
                        mStatusView.setText("You have chosen your card(s).");
                        ref.child(mGame.getmGameKey()).child("mUserGameList").child(String.valueOf(mUser.getmUserGameID())).setValue(user);
                    }else Toast.makeText(this,"You need to use different cards as answers!",Toast.LENGTH_SHORT).show();
                } else {
                    //Czar chooses a card
                    lockCard.setVisible(false);
                    hasSelectedCard = true;
                    //countDownValCzar = 1;
                    mGame.setmGameStatus(CHECK_POINTS_FOR_ROUND);
                    mGame.getmCurrentRound().setmCzarPickID(mAnswersList.get(mAnswerIndex).getUserID());
                    ref.child(mGame.getmGameKey()).setValue(mGame);
                }
            }break;
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
    /*@Override
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
    }*/

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