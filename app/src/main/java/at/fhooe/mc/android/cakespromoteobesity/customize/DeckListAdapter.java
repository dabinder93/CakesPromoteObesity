package at.fhooe.mc.android.cakespromoteobesity.customize;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.lobby.Lobby;
import at.fhooe.mc.android.cakespromoteobesity.lobby.LobbyOverview;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Custom Adapter for the CustomizeActivity, includes Button to upload a deck
 */
public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {
    private List<Deck> mDeckList;
    private Context mContext;
    private User mUser;
    private AlertDialog.Builder passwordBuilder;

    public DeckListAdapter(Context _context, final List<Deck> _obj) {
        mDeckList = _obj;
        mContext = _context;
    }


    /**
     * Defines the object elements to the UI Elements
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder needs to contain an variable for every View,Button,etc... of the LobbyList item
        public TextView tv_deckData;
        public Button btn_uploadDeck;

        public ViewHolder(final View _itemView) {
            super(_itemView);
            tv_deckData = (TextView)_itemView.findViewById(R.id.tv_custom_deck_data);
            btn_uploadDeck = (Button)_itemView.findViewById(R.id.btn_custom_deck_upload);

            _itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    Intent i = new Intent(mContext, CustomizeDeck.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DeckKey", getAdapterPosition());
                    i.putExtras(bundle);
                    mContext.startActivity(i);
                }
            });
            _itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View _view) {
                    CustomizeActivity.mCustomDecks.remove(getAdapterPosition());
                    //mDeckList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    return false;
                }
            });
        }
    }

    //Automatically implemented
    @Override
    public DeckListAdapter.ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
        Context context = _parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View deckListView = inflater.inflate(R.layout.activity_customize_deck_item,_parent,false);

        DeckListAdapter.ViewHolder viewHolder = new DeckListAdapter.ViewHolder(deckListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeckListAdapter.ViewHolder _holder, final int _position) {
        _holder.btn_uploadDeck.setText("^");

        _holder.tv_deckData.setText(mDeckList.get(_position).toString());
        _holder.btn_uploadDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = MainActivity.mainRef.child("Resources-custom").push().getKey();
                MainActivity.mainRef.child("Resources-custom").child(key).setValue(mDeckList.get(_position));
                Toast.makeText(mContext.getApplicationContext(),"Uploaded Deck to Database",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeckList.size();
    }

}
