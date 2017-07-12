package at.fhooe.mc.android.cakespromoteobesity.customize;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.card.Prompt;
import at.fhooe.mc.android.cakespromoteobesity.card.Response;

/**
 * allows the user to customize a deck - or make a new one from scratch
 * can change everything about the deck and save it to add it to the list of locally stored decks
 */
public class CustomizeDeck extends AppCompatActivity implements View.OnClickListener {

    private int index;
    private Deck newDeck;
    private ListView promptView, responseView;
    private EditText etName, etId, etPrompt, etResponse;
    private Button addPrompt, addResponse, addDeck;
    private ArrayAdapter<Prompt> promptAdapter;
    private ArrayAdapter<String> responseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_deck);
        setTitle("Customize Deck");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get game passed from Intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        index = (int) bundle.getSerializable("DeckKey");

        newDeck = new Deck();
        if (index != -1) {
            Deck oldDeck = CustomizeActivity.mCustomDecks.get(index);
            newDeck.setName(oldDeck.getName());
            newDeck.setId(oldDeck.getId());
            newDeck.setIcon(oldDeck.getIcon());
            newDeck.setSort(oldDeck.getSort());
            newDeck.setPrompts(new ArrayList<Prompt>());
            for (Prompt p : oldDeck.getPrompts()) {
                newDeck.getPrompts().add(p);
            }
            newDeck.setResponses(new ArrayList<String>());
            for (String s : oldDeck.getResponses()) {
                newDeck.getResponses().add(s);
            }

        }else {
            newDeck.setName("Best Deck Ever");
            newDeck.setId("BDE");
            newDeck.setIcon("custom");
            newDeck.setSort(10000);
            newDeck.setPrompts(new ArrayList<Prompt>());
            newDeck.setResponses(new ArrayList<String>());
        }


        //Set up EditTexts
        etName = (EditText)findViewById(R.id.et_custom_name);
        etId = (EditText)findViewById(R.id.et_custom_id);
        etPrompt = (EditText)findViewById(R.id.et_custom_prompt);
        etResponse = (EditText)findViewById(R.id.et_custom_response);

        etName.setText(newDeck.getName());
        etId.setText(newDeck.getId());
        etPrompt.setText("I like _____ .");
        etResponse.setText("ice cream");

        //Set up ListView and Buttons
        promptView = (ListView)findViewById(R.id.listView_custom_prompts);
        promptAdapter = new ArrayAdapter<Prompt>(this,android.R.layout.simple_list_item_1, newDeck.getPrompts());
        promptView.setAdapter(promptAdapter);
        promptView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int _pos, long _id) {
                newDeck.getPrompts().remove(_pos);
                promptAdapter.notifyDataSetChanged();
                return false;
            }
        });

        responseView = (ListView)findViewById(R.id.listView_custom_responses);
        responseAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,newDeck.getResponses());
        responseView.setAdapter(responseAdapter);
        responseView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int _pos, long _id) {
                newDeck.getResponses().remove(_pos);
                responseAdapter.notifyDataSetChanged();
                return false;
            }
        });

        addPrompt = (Button)findViewById(R.id.btn_custom_savePrompt);
        addPrompt.setOnClickListener(this);
        addResponse = (Button)findViewById(R.id.btn_custom_saveResponse);
        addResponse.setOnClickListener(this);
        addDeck = (Button)findViewById(R.id.btn_custom_saveDeck);
        addDeck.setOnClickListener(this);




    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()) {
            case R.id.btn_custom_savePrompt : {
                String s = etPrompt.getText().toString();
                if (s.length() > 2) {
                    Prompt p = new Prompt();
                    p.setText(s);
                    p.setId(newDeck.getId());
                    p.setPick(1);
                    newDeck.getPrompts().add(0,p);
                    promptAdapter.notifyDataSetChanged();
                    etPrompt.setText("");
                    //InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
            }break;
            case R.id.btn_custom_saveResponse : {
                String s = etResponse.getText().toString();
                if (s.length() > 2) {
                    newDeck.getResponses().add(0,s);
                    responseAdapter.notifyDataSetChanged();
                    etResponse.setText("");
                    //InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
            }break;
            case R.id.btn_custom_saveDeck : {

                //set ids to all prompts in deck - if user changed it after creating cards
                for (Prompt p : newDeck.getPrompts()) {
                    p.setId(newDeck.getId());
                }
                newDeck.setName(etName.getText().toString());
                newDeck.setId(etId.getText().toString());

                if (index == -1) CustomizeActivity.mCustomDecks.add(newDeck);
                else CustomizeActivity.mCustomDecks.set(index,newDeck);
                finish();

            }break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Custom Deck not saved",Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
