package com.example.chat.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Activities.PrivateChatActivity;
import com.example.chat.Activities.UserProfileActivity;
import com.example.chat.R;
import com.example.chat.Utils.RecentChats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecentChatsAdapter extends RecyclerView.Adapter<RecentChatsAdapter.ViewHolder> implements Filterable {

    List<RecentChats> recentChatsInfoList;
    List<RecentChats> recentChatsInfoListFull;
    Activity activity;
    Context context;

    FirebaseUser user;
    DatabaseReference reference;

    public RecentChatsAdapter(List<RecentChats> recentChatsInfoList, Activity activity, Context context) {
        this.recentChatsInfoList = recentChatsInfoList;
        this.activity = activity;
        this.context = context;

        recentChatsInfoListFull = new ArrayList<>(recentChatsInfoList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_user_layout,
                parent,false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {

        reference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        try{
            holder.recentnameUser.setText(recentChatsInfoList.get(position).getName());
        }catch (Exception e){
            holder.recentnameUser.setText("");
        }

        Picasso.get().load(recentChatsInfoList.get(position).getPhotoPath())
                .into(holder.recentImage);

        holder.recentImage.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
            intent.putExtra("name",recentChatsInfoList.get(position).getName());
            intent.putExtra("photo",recentChatsInfoList.get(position).getPhotoPath());
            intent.putExtra("bio",recentChatsInfoList.get(position).getBio());
            intent.putExtra("UserKey",recentChatsInfoList.get(position).getUserKey());
            activity.startActivity(intent);
        });


        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), PrivateChatActivity.class);
            intent.putExtra("UserKey",recentChatsInfoList.get(position).getUserKey());
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recentChatsInfoList.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView recentnameUser;
        CircleImageView recentImage;
        CardView cardView;

        ViewHolder(View itemView){
            super(itemView);

            recentnameUser = itemView.findViewById(R.id.recentnameUser);
            recentImage = itemView.findViewById(R.id.recentChatImage);
            cardView = itemView.findViewById(R.id.cardview_recent);

        }
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<RecentChats> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(recentChatsInfoListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(RecentChats item : recentChatsInfoListFull){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recentChatsInfoList.clear();
            recentChatsInfoList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
