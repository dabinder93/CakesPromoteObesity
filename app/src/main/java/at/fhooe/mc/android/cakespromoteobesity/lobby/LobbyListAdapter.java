package at.fhooe.mc.android.cakespromoteobesity.lobby;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;

/**
 * Created by Bastian on 21.01.2017.
 */

public class LobbyListAdapter extends RecyclerView.Adapter<LobbyListAdapter.ViewHolder> {

    private List<Lobby> mLobbyList;
    private Context mContext;

    public LobbyListAdapter(Context _context, final List<Lobby> _obj) {
        mLobbyList = _obj;
        mContext = _context;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder needs to contain an variable for every View,Button,etc... of the LobbyList item
        public TextView tv_lobbyName, tv_lobbyHost, tv_lobbyPlayers, tv_lobbyDeckList;
        public Button btn_joinLobby;
        public ImageView imgView_pw;

        public ViewHolder(final View _itemView) {
            super(_itemView);
            tv_lobbyName = (TextView)_itemView.findViewById(R.id.tv_lobby_item_name);
            tv_lobbyHost = (TextView)_itemView.findViewById(R.id.tv_lobby_item_host);
            tv_lobbyPlayers = (TextView)_itemView.findViewById(R.id.tv_lobby_item_players);
            tv_lobbyDeckList = (TextView)_itemView.findViewById(R.id.tv_lobby_item_decks_list);
            imgView_pw = (ImageView)_itemView.findViewById(R.id.imgView_lobby_item_pw);
            btn_joinLobby = (Button)_itemView.findViewById(R.id.btn_lobby_item_join);

        }
    }

    private Context getContext() {
        return mContext;
    }


    //Automatically implemented
    @Override
    public LobbyListAdapter.ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
        Context context = _parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View lobbyListView = inflater.inflate(R.layout.activity_lobby_item,_parent,false);

        ViewHolder viewHolder = new ViewHolder(lobbyListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LobbyListAdapter.ViewHolder _holder, final int _position) {
        final Lobby lobby = mLobbyList.get(_position);

        TextView textView = _holder.tv_lobbyName;
        textView.setText(lobby.getmName());

        textView = _holder.tv_lobbyHost;
        textView.setText(lobby.getmUserList().get(0));

        textView = _holder.tv_lobbyPlayers;
        textView.setText(String.valueOf(lobby.getmUsersInLobby()) + "/" + String.valueOf(lobby.getmMaxPlayers()));

        textView = _holder.tv_lobbyDeckList;
        StringBuffer b = new StringBuffer(" ");
        for (int i = 0; i < lobby.getmDecks().size(); i++) {
            b.append(lobby.getmDecks().get(i).getmDeckName());
            if (i != lobby.getmDecks().size()-1) b.append(", ");
        }
        textView.setText(b.toString());

        if (lobby.getmPassword() != "") _holder.imgView_pw.setVisibility(View.VISIBLE);
        else _holder.imgView_pw.setVisibility(View.GONE);

        Button btn = _holder.btn_joinLobby;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Join the lobby
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby.getmLobbyKey());
                //Toast.makeText(getContext(),"Button wurde gedrückt",Toast.LENGTH_SHORT).show();

                if (mLobbyList.get(_position).getmUsersInLobby() < mLobbyList.get(_position).getmMaxPlayers()) {
                    mLobbyList.get(_position).getmUserList().add(MainActivity.mUser.getmName());
                    ref.setValue(mLobbyList.get(_position));
                    mLobbyList.get(_position).setmUsersInLobby(mLobbyList.get(_position).getmUsersInLobby()+1);

                    Intent i = new Intent(mContext, LobbyOverview.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LobbyObject", mLobbyList.get(_position));
                    i.putExtras(bundle);
                    mContext.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLobbyList.size();
    }
}
