package at.fhooe.mc.android.cakespromoteobesity.lobbysettings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import at.fhooe.mc.android.cakespromoteobesity.R;

public class CreateLobby extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);

        Spinner dropdown_players = (Spinner)findViewById(R.id.spinner_players);
        String[] items_players = new String[]{"3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter_players = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_players);
        dropdown_players.setAdapter(adapter_players);

        Spinner dropdown_winpoints = (Spinner)findViewById(R.id.spinner_winpoints);
        String[] items_winpoints = new String[]{"3", "4", "5", "6", "7", "8"};
        ArrayAdapter<String> adapter_winpoints = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items_winpoints);
        dropdown_winpoints.setAdapter(adapter_winpoints);
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case (R.id.btn_startLobby) : {
                //Lobby Objeckt

                //Intent starten
            }break;
            default: {
                Toast.makeText(this,"Error on onClick",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
