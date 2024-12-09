package com.example.wearef1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<Post> postList;

    public PostAdapter(ArrayList<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvAuthor.setText(post.getAuthor());
        holder.tvContent.setText(post.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        holder.tvTimestamp.setText(sdf.format(new Date(post.getTimestamp())));

        String favoriteTeamId = UserPreferenceManager.getUser(holder.itemView.getContext()).getFavoriteTeamId();

        if (favoriteTeamId == null || post.getId() == null) {
            Log.e("PostAdapter", "favoriteTeamId or post.getId() is null");
            return;
        }

        // Handle Comments
        DatabaseReference commentsRef = FirebaseDatabase.getInstance()
                .getReference("posts")
                .child(favoriteTeamId)
                .child(post.getId())
                .child("comments");

        ArrayList<Comment> commentList = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        holder.rvComments.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvComments.setAdapter(commentAdapter);

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        commentList.add(comment);
                    }
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        // Submit a new comment
        holder.btnSubmitComment.setOnClickListener(v -> {
            Log.d("Comment: ", "Comment written");
            String commentContent = holder.etCommentContent.getText().toString().trim();
            if (!commentContent.isEmpty()) {
                String userName = UserPreferenceManager.getUser(v.getContext()).getUsername();
                String commentId = commentsRef.push().getKey();
                Comment comment = new Comment(userName, commentContent, System.currentTimeMillis());

                if (commentId != null) {
                    commentsRef.child(commentId).setValue(comment);
                    holder.etCommentContent.setText(""); // Clear input field
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor, tvContent, tvTimestamp;
        RecyclerView rvComments; // RecyclerView for comments
        EditText etCommentContent; // EditText for adding new comments
        Button btnSubmitComment; // Button for submitting comments

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor_post);
            tvContent = itemView.findViewById(R.id.tvContent_post);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp_post);

            // Comments section
            rvComments = itemView.findViewById(R.id.rvComments);
            etCommentContent = itemView.findViewById(R.id.etCommentContent);
            btnSubmitComment = itemView.findViewById(R.id.btnSubmitComment);
        }
    }

}
