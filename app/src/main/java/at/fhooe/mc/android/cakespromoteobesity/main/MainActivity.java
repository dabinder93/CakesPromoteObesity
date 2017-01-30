package at.fhooe.mc.android.cakespromoteobesity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobby.JoinLobby;
import at.fhooe.mc.android.cakespromoteobesity.lobby.CreateLobby;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Entrance Activity of the App which represents the Options to Join or Create a Lobby
 * and the Option to enter a User Name for the Game.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity Test";
    private Button mJoinLobby, mCreateLobby;
    private EditText et_playerName;
    public static User mUser;
    public static String mUserKey;
    private String mPlayerName;
    public static final DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference usersRef = mainRef.child("Users");
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TAG", "in OnCreate");

        mCreateLobby = (Button) findViewById(R.id.btn_createLobby);
        mJoinLobby = (Button) findViewById(R.id.btn_joinLobby);
        mCreateLobby.setOnClickListener(this);
        mJoinLobby.setOnClickListener(this);
        et_playerName = (EditText) findViewById(R.id.et_playerName);


        //Get a deck into Decks-Branch
        /*String deckName = "UK Starter Kit";
        String deckID = "UKMain";
        int whiteCard = 34;
        int blackCard = 65;
        Deck deck = new Deck(deckName,deckID,blackCard,whiteCard);
        String key = mainRef.child("Decks").push().getKey();
        mainRef.child("Decks").child(deckID).setValue(deck);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        //If you already entered a UserName and get Back to the Activity, the Option to enter a Username
        //will be disabled
        if (mUser != null) {
            et_playerName.setVisibility(View.GONE);
            findViewById(R.id.tv_playerName).setVisibility(View.GONE);
        }
        Log.i("TAG", "in onResume");
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            //Leads to CreateLobby Activity and Pushes User to Firebase, if the entered Username Letter Size is > 3
            case R.id.btn_createLobby: {
                i = new Intent(this, CreateLobby.class);
                if (mUser == null) {
                    mPlayerName = et_playerName.getText().toString();
                    if(mPlayerName.length() < 4){
                        Toast.makeText(this, "Your user name must have at least 4 letters", Toast.LENGTH_SHORT).show();
                        //enterPlayerName();

                    }else{
                        mUserKey = usersRef.push().getKey();
                        mUser = new User(mPlayerName,mUserKey);
                        usersRef.child(mUserKey).setValue(mUser);
                        startActivity(i);
                    }
                }else{
                    startActivity(i);
                }
            }
            break;
            case R.id.btn_joinLobby: {
                i = new Intent(this, JoinLobby.class);
                if (mUser == null) {
                    mPlayerName = et_playerName.getText().toString();
                    if(mPlayerName.length() < 4){
                        Toast.makeText(this, "Playername must have at least 4 Letters", Toast.LENGTH_SHORT).show();
                        //enterPlayerName();

                    }else{
                        mUserKey = usersRef.push().getKey();
                        mUser = new User(mPlayerName,mUserKey);
                        usersRef.child(mUserKey).setValue(mUser);
                        startActivity(i);
                    }
                }else{
                    startActivity(i);
                }
            }
            break;
            default: {
                Toast.makeText(this, "Error on onClick", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Removes User from Database when App is Destroyed
        usersRef.child(mUser.getmUserKey()).removeValue();
        mUser = null;
    }

    /* void enterPlayerName() {

            //User has not entered a name yet
            AlertDialog.Builder playerNameDialog = new AlertDialog.Builder(this);
            playerNameDialog.setTitle("Enter Player Name");


            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            playerNameDialog.setView(input);

            playerNameDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mPlayerName = input.getText().toString();
                            mUser = new User(mPlayerName);
                            mUserKey = usersRef.push().getKey();
                            usersRef.child(mUserKey).setValue(mUser);
                            startActivity(i);
                        }
                    });

            playerNameDialog.setNegativeButton("Dismiss",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            //AlertDialog dialog = playerNameDialog.create();
            playerNameDialog.show();
        }
*/
}

