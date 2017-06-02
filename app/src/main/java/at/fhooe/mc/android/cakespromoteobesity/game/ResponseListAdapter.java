package at.fhooe.mc.android.cakespromoteobesity.game;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import at.fhooe.mc.android.cakespromoteobesity.R;
import at.fhooe.mc.android.cakespromoteobesity.card.Response;

/**
 * Created by Bastian on 29.05.2017.
 */

public class ResponseListAdapter extends RecyclerView.Adapter<ResponseListAdapter.ViewHolder> {

    private List<Response> mItemList;
    private Context mContext;
    private onRecyclerViewItemClickListener mItemClickListener;
    private int selectedItem = 0;

    public void setOnItemClickListener(onRecyclerViewItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface onRecyclerViewItemClickListener {
        void onItemClickListener(View view, int position);
    }


    public ResponseListAdapter(Context _context, final List<Response> _obj) {
        mItemList = _obj;
        mContext = _context;
    }


    /*@Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        Log.i("GameActivity","OnBindViewHolder from Adapter, pos =  " + position);
        super.onBindViewHolder(holder, position, payloads);
        mPosition = position;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        //ViewHolder needs to contain an variable for every View,Button,etc... of the LobbyList item
        //public RecyclerView recV_responses;
        public TextView tv_response;
        public TextView tv_id;
        public ImageView imgV_icon;

        public ViewHolder(final View _itemView) {
            super(_itemView);
            //recV_responses = (RecyclerView)_itemView.findViewById(R.id.recView_game_responses);
            tv_response = (TextView)_itemView.findViewById(R.id.tv_game_prompt_text);
            tv_id = (TextView)_itemView.findViewById(R.id.tv_game_prompt_id);
            _itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View _view) {
                    //Log.i("GameActivity","OnClick, Pos: " + getAdapterPosition());
                    if (mItemClickListener != null) {
                        selectedItem = getAdapterPosition();
                        mItemClickListener.onItemClickListener(_view, getAdapterPosition());
                        notifyDataSetChanged();
                        //Somehow not working
                        /*for (int i = 0; i < mItemList.size(); i++) {
                            if (i == selectedItem) {
                                _itemView.setBackgroundColor(Color.RED);
                                Log.i("GameActivity","Red on: " + selectedItem);
                            }
                            else _itemView.setBackgroundColor(Color.TRANSPARENT);
                        }*/
                        //_itemView.setBackgroundColor(Color.RED);
                        _itemView.setSelected(true);
                    }
                }
            });

        }
    }

    @Override
    public ResponseListAdapter.ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType) {
        LayoutInflater inflater = LayoutInflater.from(_parent.getContext());

        View lobbyListView = inflater.inflate(R.layout.activity_game_response_item,_parent,false);

        ViewHolder viewHolder = new ViewHolder(lobbyListView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResponseListAdapter.ViewHolder _holder, int _position) {
        //_holder.recV_responses.setText(mItemList.get(_position));
        _holder.tv_response.setText(mItemList.get(_position).getText());
        _holder.tv_id.setText(mItemList.get(_position).getId());

        for (int i = 0; i < mItemList.size(); i++) {
            if (i == selectedItem) {
                //_holder.itemView.setBackgroundColor(Color.RED);
                _holder.itemView.setSelected(true);
                //Log.i("GameActivity","Red on: " + selectedItem);
            }
            else {
                //_holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                _holder.itemView.setSelected(false);
            }
        }

        //Following is working ->notify
        if (selectedItem == _position) _holder.itemView.setBackgroundColor(Color.RED);
        else _holder.itemView.setBackgroundColor(Color.TRANSPARENT);



        //_holder.itemView.setSelected(selectedItem == _position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}