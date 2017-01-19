package at.fhooe.mc.android.cakespromoteobesity.lobbysettings;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.fhooe.mc.android.cakespromoteobesity.Deck;
import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.extra.MultiSelectionSpinner;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;

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

    //List which contains all Decks with names
    private List<Deck> deckList;

    //List which contains the NAMES of the Decks (not ID)
    private List<String> deckListString;
    String mLobbyKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

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


                    /*Deck deck = new Deck();
                    deck.setmDeckID(snap.getKey().toString());*/

                    //get all Data in Decks
                    /*Map<String, String> map = snap.getValue(Map.class);
                    deck.setmDeckName(map.get("DeckName"));
                    deckList.add(deck);
                    Log.i(TAG, "Deck List Object Name: "+ deck.getmDeckName()+ " , ID: "+deck.getmDeckID());

                    deckListString.add(deck.getmDeckName());*/



                    /*
                    //Convert from ArrayList<Decks> into String[] for Adapter
                    String[] deckListString = new String[deckList.size()];
                    for(int i = 0; i < deckList.size(); i++) {
                        deckListString[i] = deckList.get(i).getmDeckName();
                    }
                    Log.i(TAG, "Deck List Spinner Array.list :"+ Arrays.toString(deckListString));
                    ArrayAdapter<String> adapter_decks = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, deckListString);
                    dropdown_decks.setAdapter(adapter_decks); */
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
                String maxPlayer = dropdown_players.getSelectedItem().toString();
                String winPoints = dropdown_winpoints.getSelectedItem().toString();
                List<String> deckIndexSelected = dropdown_decks.getSelectedStrings();

                //Lobby Objeckt
                Lobby newLobby = new Lobby(name, password, maxPlayer, winPoints, deckIndexSelected);
                mLobbyKey = ref.push().getKey();
                //Push Lobby Object into Database /Testbranch
                ref.child(mLobbyKey).setValue(newLobby);
                MainActivity.mUser.addToLobby(mLobbyKey);
            }break;
        }
    }
}


