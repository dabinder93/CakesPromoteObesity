package at.fhooe.mc.android.cakespromoteobesity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import at.fhooe.mc.android.cakespromoteobesity.Deck;
import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobbysettings.CreateLobby;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity Test";
    private Button mJoinLobby, mCreateLobby;
    private List<Deck> decklist;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        i = new Intent(this, CreateLobby.class);
        mCreateLobby = (Button) findViewById(R.id.btn_createLobby);
        mJoinLobby = (Button) findViewById(R.id.btn_joinLobby);


        mCreateLobby.setOnClickListener(this);
    }


    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.btn_createLobby : {

                startActivity(i);
            }break;
            case R.id.btn_joinLobby : {

            }break;
            default: {
                Toast.makeText(this,"Error on onClick",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
