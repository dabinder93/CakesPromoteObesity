package at.fhooe.mc.android.cakespromoteobesity.customize;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.lobby.CreateLobby;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * shows the list of locally stored decks, saves the current list when leaving the activity
 */
public class CustomizeActivity extends AppCompatActivity
{
    private User mUser;
    static ArrayList<Deck> mCustomDecks;
    //private ListView mDeckListView;
    private RecyclerView mDeckListView;
    private DeckListAdapter mAdapter;
    private int mIndex = -1;
    private ArrayAdapter<Deck> deckAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
        setTitle("Custom Decks");

        mUser = MainActivity.mUser;
        Button btnExp = (Button)findViewById(R.id.btn_custom_explorer);
        btnExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Deck Explorer",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), DeckExplorer.class);
                startActivity(i);
            }
        });
        Button btnNew = (Button)findViewById(R.id.btn_custom_new);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CustomizeDeck.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DeckKey", -1);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        //load the saved list
        mCustomDecks = new ArrayList<Deck>();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = mPrefs.getString("Decks", "");
        if (json.isEmpty()) {
            mCustomDecks = new ArrayList<Deck>();
            Toast.makeText(this,"No decks saved yet ...",Toast.LENGTH_SHORT).show();
        } else {
            Type type = new TypeToken<List<Deck>>() {}.getType();
            mCustomDecks = gson.fromJson(json, type);
            if (mCustomDecks.size() == 0) {
                Toast.makeText(this,"No decks saved yet ...",Toast.LENGTH_SHORT).show();
            }
        }

        //Set the ListView
        /*mDeckListView = (ListView)findViewById(R.id.listView_custom_deckList);
        deckAdapter = new ArrayAdapter<Deck>(this,android.R.layout.simple_list_item_1, mCustomDecks);
        mDeckListView.setAdapter(deckAdapter);
        mDeckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _adapterView, View _view, int _position, long _id) {
                mIndex = _position;
                customizeDeck();
            }
        });
        mDeckListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> _adapterView, View _view, int _position, long _id) {
                //Toast.makeText(getApplicationContext(), "Clicked on " + _position, Toast.LENGTH_SHORT).show();
                mCustomDecks.remove(_position);
                deckAdapter.notifyDataSetChanged();
                return false;
            }
        });*/

        //Set the recView
        mDeckListView = (RecyclerView)findViewById(R.id.recView_custom_deckList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mDeckListView.setLayoutManager(layoutManager);

        mAdapter = new DeckListAdapter(CustomizeActivity.this,mCustomDecks);
        mDeckListView.setAdapter(mAdapter);

        ListView listView = (ListView)findViewById(R.id.listView_custom_deckList);
        listView.setVisibility(View.GONE);

    }

    /**
     * creates the CustomizeDeck Activity with either a deck from the list of a new deck
     */
    private void customizeDeck() {
        Intent i = new Intent(this, CustomizeDeck.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DeckKey", -1);
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        //deckAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (mCustomDecks != null) {
            Log.i("TAG","Saving ..., mCustomDeck size = " + mCustomDecks.size());
            SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mCustomDecks);
            prefsEditor.putString("Decks", json);
            prefsEditor.commit();
        }else {
            Toast.makeText(this,"No decks to save ...", Toast.LENGTH_SHORT).show();
            Log.i("TAG","No decks to save, mCustomDecks = null");
        }

        super.onBackPressed();
    }
}
