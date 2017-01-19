package at.fhooe.mc.android.cakespromoteobesity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.Deck;
import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobbysettings.CreateLobby;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity Test";
    private Button mJoinLobby, mCreateLobby;
    private EditText et_playerName;
    public static User mUser;
    public String mUserKey;
    private String mPlayerName;
    public static final DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference usersRef = mainRef.child("Users");
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TAG", "in OnCreate");
        i = new Intent(this, CreateLobby.class);
        mCreateLobby = (Button) findViewById(R.id.btn_createLobby);
        mJoinLobby = (Button) findViewById(R.id.btn_joinLobby);
        mCreateLobby.setOnClickListener(this);
        et_playerName = (EditText) findViewById(R.id.et_playerName);


        //Testreihe
        Deck deck = new Deck("UK Starter Kit","UKMain",34,65);
        String key = mainRef.child("Decks").push().getKey();
        mainRef.child("Decks").child("UKMain").setValue(deck);

        deck = new Deck("US Starter Kit","USMain",76,460);
        key = mainRef.child("Decks").push().getKey();
        mainRef.child("Decks").child("USMain").setValue(deck);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUser != null) {
            et_playerName.setVisibility(View.GONE);
            findViewById(R.id.tv_playerName).setVisibility(View.GONE);
        }
        Log.i("TAG", "in onResume");
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.btn_createLobby: {
                if (mUser == null) {
                    mPlayerName = et_playerName.getText().toString();
                    if(mPlayerName.length() < 4){
                        Toast.makeText(this, "Playername must have at least 4 Letters", Toast.LENGTH_SHORT).show();
                        //enterPlayerName();

                    }else{
                        mUser = new User(mPlayerName);
                        mUserKey = usersRef.push().getKey();
                        usersRef.child(mUserKey).setValue(mUser);
                        startActivity(i);
                    }
                }else{
                    startActivity(i);
                }
            }
            break;
            case R.id.btn_joinLobby: {
                if (mUser == null) {
                    mUser = new User(mPlayerName);
                    mUserKey = usersRef.push().getKey();
                    usersRef.child(mUserKey).setValue(mUser);
                }
            }
            break;
            default: {
                Toast.makeText(this, "Error on onClick", Toast.LENGTH_SHORT).show();
            }
        }
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

