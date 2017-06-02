package at.fhooe.mc.android.cakespromoteobesity.lobby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.card.DeckInfo;
import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.extra.MultiSelectionSpinner;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Allows the user to create a Game with his/her preferenced settings
 * Has a reference to Firebase to get all the available decks
 * Upon clicking the Button at the bottom, a Lobby Object will be uploaded to the Database and
 * will then be added to the List of all Lobbies
 */
public class CreateLobby extends AppCompatActivity implements View.OnClickListener{

    final DatabaseReference ref = MainActivity.mainRef;
    final DatabaseReference decksRef = ref.child("DeckInfo-official");
    final DatabaseReference decksRefUnofficial = ref.child("DeckInfo-unofficial");


    EditText lobbyName;
    EditText lobbyPassword;
    Spinner dropdown_players;
    Spinner dropdown_winpoints;
    MultiSelectionSpinner dropdown_decksOfficial, dropdown_decksUnofficial;
    Button startLobby;
    Lobby newLobby;
    ProgressBar loadingBar1, loadingBar2;
    TextView cardSumText;
    int promptSumOff, promptSumUnoff, promptSumCustom;
    int responseSumOff, responseSumUnoff, responseSumCustom;
    int potentialRoundsPlayed;

    //List which contains all Decks with names
    private List<DeckInfo> deckInfoListOff, deckInfoListUnoff;

    //List which contains the NAMES of the Decks (not ID)
    private List<String> deckListStringOff, deckListStringUnoff;
    String mLobbyKey;
    User mUser = MainActivity.mUser;
    int maxPlayer,winPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_create);
        setTitle("Create a Lobby");

        lobbyName = (EditText) findViewById(R.id.et_name);
        lobbyPassword = (EditText) findViewById(R.id.et_password);
        dropdown_players = (Spinner)findViewById(R.id.spinner_players);
        dropdown_winpoints = (Spinner)findViewById(R.id.spinner_winpoints);

        cardSumText = (TextView)findViewById(R.id.tv_cardSum);
        promptSumOff = promptSumUnoff = promptSumCustom = responseSumOff = responseSumUnoff = responseSumCustom = 0;

        dropdown_decksOfficial = (MultiSelectionSpinner) findViewById(R.id.spinner_decks_off);
        dropdown_decksUnofficial = (MultiSelectionSpinner) findViewById(R.id.spinner_decks_unoff);
        dropdown_decksOfficial.setVisibility(View.INVISIBLE);
        dropdown_decksUnofficial.setVisibility(View.INVISIBLE);

        loadingBar1 = (ProgressBar)findViewById(R.id.progressBar_lobbyCreate_off);
        loadingBar1.setVisibility(View.VISIBLE);
        loadingBar2 = (ProgressBar)findViewById(R.id.progressBar_lobbyCreate_unoff);
        loadingBar2.setVisibility(View.VISIBLE);

        startLobby = (Button) findViewById(R.id.btn_startLobby);
        startLobby.setOnClickListener(this);
        startLobby.setVisibility(View.GONE);
        ArrayAdapter<String> adapter_decks;

        //Player count Spinner
        String[] items_players = new String[]{"3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter_players = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, items_players);
        dropdown_players.setAdapter(adapter_players);
        dropdown_players.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSelectedCards();
                maxPlayer = Integer.parseInt((String)dropdown_players.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Points to Win Spinner
        String[] items_winpoints = new String[]{"3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter_winpoints = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, items_winpoints);
        dropdown_winpoints.setAdapter(adapter_winpoints);
        dropdown_winpoints.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSelectedCards();
                winPoints = Integer.parseInt((String)dropdown_winpoints.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Get DeckInfo Names for each existing DeckInfo from Database -> stores it in deckInfoList
        deckInfoListOff = new ArrayList<>();
        deckListStringOff = new ArrayList<>();
        deckInfoListUnoff = new ArrayList<>();
        deckListStringUnoff = new ArrayList<>();
        updateSelectedCards();

        //deckListStringOff.add("Select some ...");
        //deckListStringUnoff.add("Select some ...");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.child("DeckInfo-official").getChildren()){
                    DeckInfo deckInfo = snap.getValue(DeckInfo.class);
                    deckInfoListOff.add(deckInfo);
                    deckListStringOff.add(deckInfo.getmDeckName());
                }
                dropdown_decksOfficial.setItems(deckListStringOff);
                dropdown_decksOfficial.setSpinnerSelectedItemsListner(new MultiSelectionSpinner.onSpinnerSelectedItemsListener() {
                    @Override
                    public void updateSelectedIndices(List<Integer> _indices) {
                        promptSumOff = responseSumOff = 0;
                        for (int i : _indices) {
                            promptSumOff += deckInfoListOff.get(i).getmBlackCardCount();
                            responseSumOff += deckInfoListOff.get(i).getmWhiteCardCount();
                            updateSelectedCards();
                        }
                    }
                });

                for (DataSnapshot snap : dataSnapshot.child("DeckInfo-unofficial").getChildren()){
                    DeckInfo deckInfo = snap.getValue(DeckInfo.class);
                    deckInfoListUnoff.add(deckInfo);
                    deckListStringUnoff.add(deckInfo.getmDeckName());
                }
                dropdown_decksUnofficial.setItems(deckListStringUnoff);
                dropdown_decksUnofficial.setSpinnerSelectedItemsListner(new MultiSelectionSpinner.onSpinnerSelectedItemsListener() {
                    @Override
                    public void updateSelectedIndices(List<Integer> _indices) {
                        promptSumUnoff = responseSumUnoff = 0;
                        for (int i : _indices) {
                            promptSumUnoff += deckInfoListUnoff.get(i).getmBlackCardCount();
                            responseSumUnoff += deckInfoListUnoff.get(i).getmWhiteCardCount();
                            updateSelectedCards();
                        }
                    }
                });

                loadingBar1.setVisibility(View.GONE);
                loadingBar2.setVisibility(View.GONE);
                dropdown_decksOfficial.setVisibility(View.VISIBLE);
                dropdown_decksUnofficial.setVisibility(View.VISIBLE);
                startLobby.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateSelectedCards() {
        potentialRoundsPlayed = Integer.parseInt((String)dropdown_players.getSelectedItem()) * (Integer.parseInt((String)dropdown_winpoints.getSelectedItem())-1) + 1;
        cardSumText.setText("Selected Prompts: " + (promptSumOff+promptSumUnoff+promptSumCustom) + ", Responses: " + (responseSumOff+responseSumUnoff+responseSumCustom) +
                "\nRecommended: " + potentialRoundsPlayed + "/" +
                (((potentialRoundsPlayed-1) * Integer.parseInt((String)dropdown_players.getSelectedItem())) + (10*Integer.parseInt((String)dropdown_players.getSelectedItem()))));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_startLobby:{
                String name = lobbyName.getText().toString();
                String password = lobbyPassword.getText().toString();

                //Decks
                boolean hasDecksSelected = false;
                List<Integer> deckIndexSelected = dropdown_decksOfficial.getSelectedIndicies();
                List<DeckInfo> selectedDeckInfos = new ArrayList<DeckInfo>();

                for (int i = 0; i < deckIndexSelected.size(); i++) {
                    selectedDeckInfos.add(deckInfoListOff.get(deckIndexSelected.get(i)));
                }
                if (selectedDeckInfos.size() != 0) hasDecksSelected = true;
                deckIndexSelected = dropdown_decksUnofficial.getSelectedIndicies();
                for (int i = 0; i < deckIndexSelected.size(); i++) {
                    selectedDeckInfos.add(deckInfoListUnoff.get(deckIndexSelected.get(i)));
                }
                if (selectedDeckInfos.size() != 0) hasDecksSelected = true;

                if (hasDecksSelected) {
                    for (DeckInfo info : selectedDeckInfos) {
                        Log.i("Lobby",info.getmDeckName() + "\n");
                    }

                    //Lobby Objekt
                    mUser.setmIsHost(true);
                    mUser.setmUserGameID(0);
                    MainActivity.mUser = mUser;

                    ref.child("Users").child(mUser.getmUserKey()).setValue(mUser);

                    mLobbyKey = ref.child("Lobbies").push().getKey();
                    newLobby = new Lobby(name, password, maxPlayer, winPoints, mLobbyKey, selectedDeckInfos, mUser);
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


