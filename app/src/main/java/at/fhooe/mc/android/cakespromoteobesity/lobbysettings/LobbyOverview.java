package at.fhooe.mc.android.cakespromoteobesity.lobbysettings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;

public class LobbyOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_overview);

        TextView hostName = (TextView) findViewById(R.id.tv_lobbyOv_hostName);
        TextView lobbyName = (TextView) findViewById(R.id.tv_lobbyOv_lobbyName);
        TextView players = (TextView) findViewById(R.id.tv_lobbyOv_players);
        TextView deckList = (TextView) findViewById(R.id.tv_lobbyOv_deckList);


    }
}
