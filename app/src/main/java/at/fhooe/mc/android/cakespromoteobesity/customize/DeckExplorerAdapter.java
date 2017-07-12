package at.fhooe.mc.android.cakespromoteobesity.customize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Deck;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * Custom Adapter for the deckexplorer activity, includes a button to download a deck
 */
public class DeckExplorerAdapter extends RecyclerView.Adapter<DeckExplorerAdapter.ViewHolder>{
    private List<Deck> mDeckList;
    private Context mContext;
    private User mUser;
    private AlertDialog.Builder passwordBuilder;

    public DeckExplorerAdapter(Context _context, final List<Deck> _obj) {
        mDeckList = _obj;
        mContext = _context;
    }


    /**
     * Defines the object elements to the UI Elements
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder needs to contain an variable for every View,Button,etc... of the LobbyList item
        public TextView tv_deckData;
        public Button btn_downloadDeck;

        public ViewHolder(final View _itemView) {
            super(_itemView);
            tv_deckData = (TextView) _itemView.findViewById(R.id.tv_explorer_deck_data);
            btn_downloadDeck = (Button) _itemView.findViewById(R.id.btn_explorer_deck_download);

            /*_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    Intent i = new Intent(mContext, CustomizeDeck.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DeckKey", getAdapterPosition());
                    i.putExtras(bundle);
                    mContext.startActivity(i);
                }
            });*/
        }
    }

    //Automatically implemented
    @Override
    public DeckExplorerAdapter.ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
        Context context = _parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View deckListView = inflater.inflate(R.layout.activity_deck_explorer_item,_parent,false);

        DeckExplorerAdapter.ViewHolder viewHolder = new DeckExplorerAdapter.ViewHolder(deckListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DeckExplorerAdapter.ViewHolder _holder, final int _position) {
        _holder.btn_downloadDeck.setText("+");

        _holder.tv_deckData.setText(mDeckList.get(_position).toString());
        _holder.btn_downloadDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomizeActivity.mCustomDecks.add(mDeckList.get(_position));
                Toast.makeText(mContext.getApplicationContext(),"Downloaded Deck to local storage",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeckList.size();
    }

}
