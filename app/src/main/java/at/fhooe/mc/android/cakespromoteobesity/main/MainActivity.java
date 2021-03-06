package at.fhooe.mc.android.cakespromoteobesity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.card.DeckInfo;
import at.fhooe.mc.android.cakespromoteobesity.customize.CustomizeActivity;
import at.fhooe.mc.android.cakespromoteobesity.extra.RulesActivity;
import at.fhooe.mc.android.cakespromoteobesity.lobby.JoinLobby;
import at.fhooe.mc.android.cakespromoteobesity.lobby.CreateLobby;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Entrance Activity of the App which represents the Options to Join or Create a Lobby
 * and the Option to enter a User Name for the Game.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mJoinLobby, mCreateLobby, mCustomDecks;
    private EditText et_playerName;
    public static User mUser;
    public static String mUserKey;
    private String mPlayerName;
    public static final DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference usersRef = mainRef.child("Users");
    Intent i;
    private MenuItem rules;
    ArrayList<Deck> mDecksTest;
    Deck mTestDeck;
    ArrayList<DeckInfo> mDeckInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.i("TAG", "in OnCreate");

        mCreateLobby = (Button) findViewById(R.id.btn_createLobby);
        mJoinLobby = (Button) findViewById(R.id.btn_joinLobby);
        mCustomDecks = (Button) findViewById(R.id.btn_customize);
        mCreateLobby.setOnClickListener(this);
        mJoinLobby.setOnClickListener(this);
        mCustomDecks.setOnClickListener(this);
        et_playerName = (EditText) findViewById(R.id.et_playerName);

        //Get a deck into Decks-Branch
        /*String deckName = "UK Starter Kit";
        String deckID = "UKMain";
        int whiteCard = 460;
        int blackCard = 90;
        DeckInfo deck = new DeckInfo(deckName,deckID,blackCard,whiteCard);
        mainRef.child("Decks").child(deckID).setValue(deck);*/

        //Test out new Deck -> this is working and getting used now
        /*DatabaseReference testRef = mainRef.child("Resources-unofficial");
        testRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Main","TestRef");
                mDecksTest = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Deck deck = snap.getValue(Deck.class);
                    mDecksTest.add(deck);
                }
                Log.i("Main","DecksCount: " + mDecksTest.size());
                Collections.sort(mDecksTest);

                //Uploads the DeckInfos into the chosen path
                mDeckInfoList = new ArrayList<DeckInfo>();
                DatabaseReference infoRef = mainRef.child("DeckInfo-unofficial");
                for (int i = 0; i < mDecksTest.size(); i++) {
                    Deck deck = mDecksTest.get(i);
                    mDeckInfoList.add(new DeckInfo(deck));
                    Log.i("Main","Name = " + deck.getName());
                }
                //Collections.sort(mDeckInfoList);
                infoRef.setValue(mDeckInfoList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        //If you already entered a UserName and get Back to the Activity, the Option to enter a Username
        //will be disabled
        if (mUser != null) {
            et_playerName.setVisibility(View.GONE);
            //findViewById(R.id.tv_playerName).setVisibility(View.GONE);
        }
        Log.i("TAG", "in onResume");
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            //Leads to CreateLobby Activity and Pushes User to Firebase, if the entered Username Letter Size is > 3
            case R.id.btn_createLobby: {
                i = new Intent(this, CreateLobby.class);
                //If the user hasn't made an User-Object yet, it gets created and pushed to Firebase
                if (mUser == null) {
                    mPlayerName = et_playerName.getText().toString();
                    if(mPlayerName.length() < 4){
                        Toast.makeText(this, "Your user name must have at least 4 letters", Toast.LENGTH_SHORT).show();
                    }else{
                        mUserKey = usersRef.push().getKey();
                        mUser = new User(mPlayerName,mUserKey);
                        usersRef.child(mUserKey).setValue(mUser);
                        startActivity(i);
                    }
                }else{
                    startActivity(i);
                }
            }break;
            case R.id.btn_joinLobby: {
                i = new Intent(this, JoinLobby.class);
                //If the user hasn't made an User-Object yet, it gets created and pushed to Firebase
                if (mUser == null) {
                    mPlayerName = et_playerName.getText().toString();
                    if(mPlayerName.length() < 4){
                        Toast.makeText(this, "Playername must have at least 4 Letters", Toast.LENGTH_SHORT).show();
                    }else{
                        mUserKey = usersRef.push().getKey();
                        mUser = new User(mPlayerName,mUserKey);
                        usersRef.child(mUserKey).setValue(mUser);
                        startActivity(i);
                    }
                }else{
                    startActivity(i);
                }
            }break;
            case R.id.btn_customize: {
                i = new Intent(this, CustomizeActivity.class);
                //If the user hasn't made an User-Object yet, it gets created and pushed to Firebase
                if (mUser == null) {
                    mPlayerName = et_playerName.getText().toString();
                    if(mPlayerName.length() < 4){
                        Toast.makeText(this, "Your user name must have at least 4 letters", Toast.LENGTH_SHORT).show();
                    }else{
                        mUserKey = usersRef.push().getKey();
                        mUser = new User(mPlayerName,mUserKey);
                        usersRef.child(mUserKey).setValue(mUser);
                        startActivity(i);
                    }
                }else{
                    startActivity(i);
                }
            }break;
            default: {
                Toast.makeText(this, "Error on onClick", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Removes User from Database when App is Destroyed
        if (mUser != null) {
            usersRef.child(mUser.getmUserKey()).removeValue();
            mUser = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rules, menu);
        rules = menu.findItem(R.id.rules);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rules: {
                Intent i = new Intent(this, RulesActivity.class);
                startActivity(i);
                break;
            }
            case R.id.exit: {
                if (mUser != null) {
                    usersRef.child(mUser.getmUserKey()).removeValue();
                    mUser = null;
                }
                System.exit(0);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

