package at.fhooe.mc.android.cakespromoteobesity.lobby;

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
import at.fhooe.mc.android.cakespromoteobesity.main.MainActivity;
import at.fhooe.mc.android.cakespromoteobesity.user.User;

/**
 * LobbyListAdapter is building up the Lobby Items for the List
 * It gets the items TextViews,Buttons,... from the activity_lobby_item.xml file
 * It also includes the OnClickListener for the Join-Buttons that will get the player into the mLobby
 */
public class LobbyListAdapter extends RecyclerView.Adapter<LobbyListAdapter.ViewHolder> {

    private List<Lobby> mLobbyList;
    private Context mContext;
    private User mUser;
    private AlertDialog.Builder passwordBuilder;

    public LobbyListAdapter(Context _context, final List<Lobby> _obj) {
        mLobbyList = _obj;
        mContext = _context;
    }


    /**
     * Defines the object elements to the UI Elements
     */
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

        _holder.tv_lobbyName.setText(lobby.getmName());
        _holder.tv_lobbyHost.setText(lobby.getmUserList().get(0));
        _holder.tv_lobbyPlayers.setText(String.valueOf(lobby.getmUsersInLobby()) + "/" + String.valueOf(lobby.getmMaxPlayers()));

        StringBuffer b = new StringBuffer(" ");
        for (int i = 0; i < lobby.getmDecks().size(); i++) {
            b.append(lobby.getmDecks().get(i).getmDeckName());
            if (i != lobby.getmDecks().size()-1) b.append(", ");
        }
        _holder.tv_lobbyDeckList.setText(b.toString());

        if (!lobby.getmPassword().equals("")) _holder.imgView_pw.setVisibility(View.VISIBLE);
        else _holder.imgView_pw.setVisibility(View.GONE);

        Button btn = _holder.btn_joinLobby;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Join the mLobby - outsourced to enterLobby()
                //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(lobby.getmLobbyKey());
                //Toast.makeText(getContext(),"Button wurde gedrückt",Toast.LENGTH_SHORT).show();

                if (mLobbyList.get(_position).getmUsersInLobby() < mLobbyList.get(_position).getmMaxPlayers()) {
                    //check if password
                    if (!mLobbyList.get(_position).getmPassword().equals("")) {
                        //go check password
                        passwordBuilder = new AlertDialog.Builder(mContext);
                        passwordBuilder.setMessage("Enter the Lobby Password");
                        passwordBuilder.setCancelable(true);
                        final EditText passwordText = new EditText(mContext);
                        passwordBuilder.setView(passwordText);

                        passwordBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Enter the lobby
                                if (passwordText.getText().toString().equals(mLobbyList.get(_position).getmPassword())) {
                                    dialogInterface.cancel();
                                    enterLobby(_position);
                                }else {
                                    passwordBuilder.setMessage("Wrong password! Try again.");
                                }
                            }
                        });
                        passwordBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog passwordDialog = passwordBuilder.create();
                        passwordDialog.show();
                    }else enterLobby(_position);
                }else Toast.makeText(mContext,"Lobby is currently full",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLobbyList.size();
    }

    private void enterLobby(int _position) {
        //Log.i("LobbyListAdapter","Entered the enterLobby() method");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Lobbies").child(mLobbyList.get(_position).getmLobbyKey());
        mUser = MainActivity.mUser;
        mLobbyList.get(_position).getmUserList().add(mUser.getmName());
        mLobbyList.get(_position).setmUsersInLobby(mLobbyList.get(_position).getmUsersInLobby()+1);
        ref.setValue(mLobbyList.get(_position));

        mUser.setmUserGameID(mLobbyList.get(_position).getmUsersInLobby()-1);

        mUser.setmIsHost(false);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getmUserKey()).setValue(mUser);
        MainActivity.mUser = mUser;

        Intent i = new Intent(mContext, LobbyOverview.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("LobbyObject", mLobbyList.get(_position));
        i.putExtras(bundle);
        mContext.startActivity(i);
    }
}
