package at.fhooe.mc.android.cakespromoteobesity.lobby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.extra.MultiSelectionSpinner;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Allows the user to create a Game with his/her preferenced settings
 * Has a reference to Firebase to get all the available decks
 * Upon clicking the Butoon at the bottom, a Lobby Object will be uploaded to the Database and
 * will then be added to the List of all Lobbies
 */
public class CreateLobby extends AppCompatActivity implements View.OnClickListener{

    //References to Database
    final DatabaseReference ref = MainActivity.mainRef; // = FirebaseDatabase.getInstance().getReference();
    //final DatabaseReference resourcesRef = ref.child("Resources");
    //final DatabaseReference decksRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cakespromoteobesity.firebaseio.com/Decks");
    final DatabaseReference decksRef = MainActivity.mainRef.child("Decks"); // = FirebaseDatabase.getInstance().getReference().child("Decks");



    public static final String TAG = "CreateLobby Test";
    EditText lobbyName;
    EditText lobbyPassword;
    Spinner dropdown_players;
    Spinner dropdown_winpoints;
    MultiSelectionSpinner dropdown_decks;
    Button startLobby;
    Lobby newLobby;

    //List which contains all Decks with names
    private List<Deck> deckList;

    //List which contains the NAMES of the Decks (not ID)
    private List<String> deckListString;
    String mLobbyKey;
    User mUser = MainActivity.mUser;

    int maxPlayer,winPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_create);

        lobbyName = (EditText) findViewById(R.id.et_name);
        lobbyPassword = (EditText) findViewById(R.id.et_password);
        dropdown_players = (Spinner)findViewById(R.id.spinner_players);
        dropdown_winpoints = (Spinner)findViewById(R.id.spinner_winpoints);
        dropdown_decks = (MultiSelectionSpinner) findViewById(R.id.spinner_decks);
        startLobby = (Button) findViewById(R.id.btn_startLobby);
        startLobby.setOnClickListener(this);
        ArrayAdapter<String> adapter_decks;

        //Player count Spinner
        String[] items_players = new String[]{"3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter_players = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_players);
        dropdown_players.setAdapter(adapter_players);

        //Points to Win Spinner
        String[] items_winpoints = new String[]{"3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter_winpoints = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_winpoints);
        dropdown_winpoints.setAdapter(adapter_winpoints);

        //Get Deck Names for each existing Deck from Database -> stores it in decklist
        deckList = new ArrayList<>();
        deckListString = new ArrayList<>();


        decksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    Deck deck = snap.getValue(Deck.class);
                    deckList.add(deck);
                    deckListString.add(deck.getmDeckName());
                }
                dropdown_decks.setItems(deckListString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_startLobby:{
                String name = lobbyName.getText().toString();
                String password = lobbyPassword.getText().toString();

                try{
                    maxPlayer = Integer.parseInt(dropdown_players.getSelectedItem().toString());
                    winPoints = Integer.parseInt(dropdown_winpoints.getSelectedItem().toString());
                }catch(NumberFormatException _e) {
                    _e.printStackTrace();
                }
                //deckList List<decks> -> now used
                //List<String> deckIndexSelected = dropdown_decks.getSelectedStrings();

                List<Integer> deckIndexSelected = dropdown_decks.getSelectedIndicies();
                List<Deck> selectedDecks = new ArrayList<Deck>();
                for (int i = 0; i < deckIndexSelected.size(); i++) {
                    selectedDecks.add(deckList.get(deckIndexSelected.get(i)));
                }

                if (selectedDecks.size() != 0) {
                    //Lobby Objekt
                    mUser.setmIsHost(true);
                    mUser.setmUserGameID(0);
                    MainActivity.mUser = mUser;
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getmUserKey());
                    dbRef.setValue(mUser);
                    mLobbyKey = ref.child("Lobbies").push().getKey();
                    newLobby = new Lobby(name, password, maxPlayer, winPoints, mLobbyKey, selectedDecks, mUser);
                    //Push Lobby Object into Database /Testbranch
                    ref.child("Lobbies").child(mLobbyKey).setValue(newLobby);

                    Intent i = new Intent(this, LobbyOverview.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LobbyObject", newLobby);
                    i.putExtras(bundle);
                    startActivity(i);
                }else Toast.makeText(this,"No decks selected for your Game",Toast.LENGTH_SHORT).show();
            }break;
        }
    }
}


