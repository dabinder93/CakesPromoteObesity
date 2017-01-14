package at.fhooe.mc.android.cakespromoteobesity.lobbysettings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

public class CreateLobby extends AppCompatActivity{
    EditText lobbyName;
    EditText lobbyPassword;
    Spinner dropdown_players;
    Spinner dropdown_winpoints;
    Button startLobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        lobbyName = (EditText) findViewById(R.id.et_name);
        lobbyPassword = (EditText) findViewById(R.id.et_password);
        dropdown_players = (Spinner)findViewById(R.id.spinner_players);
        dropdown_winpoints = (Spinner)findViewById(R.id.spinner_winpoints);
        startLobby = (Button) findViewById(R.id.btn_startLobby);


        String[] items_players = new String[]{"3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter_players = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_players);
        dropdown_players.setAdapter(adapter_players);

        String[] items_winpoints = new String[]{"3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter_winpoints = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_winpoints);
        dropdown_winpoints.setAdapter(adapter_winpoints);

        //Reference to Testbranch in Database
        final Firebase ref = new Firebase("https://cakespromoteobesity.firebaseio.com/Testbranch");

        startLobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                String name = lobbyName.getText().toString();
                String password = lobbyPassword.getText().toString();
                String maxPlayer = dropdown_players.getSelectedItem().toString();
                String winPoints = dropdown_winpoints.getSelectedItem().toString();

                //Lobby Objeckt
                Lobby newLobby = new Lobby(name, password, maxPlayer, winPoints);
                Toast toast = Toast.makeText(getApplicationContext(), newLobby.getmName()+", "+newLobby.getmPassword()+", "
                        +newLobby.getmMaxPlayers()+", "+newLobby.getmWinpoints(), Toast.LENGTH_LONG);
                toast.show();

                //Push Lobby Object into Database /Testbranch
                ref.push().setValue(newLobby);
                //Intent starten
            }
        });
    }
}
