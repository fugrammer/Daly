package com.example.thamt.daly.Chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thamt.daly.Chat.ChatFragment.OnListFragmentInteractionListener;
import com.example.thamt.daly.R;

import java.util.List;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

  public ChatRecyclerViewAdapter(List<?> items, OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
      return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }
}
