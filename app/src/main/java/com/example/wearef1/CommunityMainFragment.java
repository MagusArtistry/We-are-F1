package com.example.wearef1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommunityMainFragment extends Fragment {

    private EditText etPostContent;
    private Button btnSubmitPost;
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private ArrayList<Post> postList;

    private DatabaseReference postsRef;

    public CommunityMainFragment() {
        // Required empty public constructor
    }
    public static CommunityMainFragment newInstance() {
        CommunityMainFragment fragment = new CommunityMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community_main, container, false);

        etPostContent = view.findViewById(R.id.etPostContent);
        btnSubmitPost = view.findViewById(R.id.btnSubmitPost);
        rvPosts = view.findViewById(R.id.rvPosts);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        String favoriteTeamId = UserPreferenceManager.getUser(getContext()).getFavoriteTeamId();
        String userName = UserPreferenceManager.getUser(getContext()).getUsername();

        // Firebase reference
        postsRef = FirebaseDatabase.getInstance().getReference("posts").child(favoriteTeamId);

        // Submit a new post
        btnSubmitPost.setOnClickListener(v -> {
            String content = etPostContent.getText().toString().trim();
            if (!content.isEmpty()) {
                String postId = postsRef.push().getKey();
                Post post = new Post(postId, userName, content, System.currentTimeMillis());

                if (postId != null) {
                    postsRef.child(postId).setValue(post);
                    etPostContent.setText(""); // Clear input field
                }
            }
        });

        // Listen for changes in posts
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        post.setId(postSnapshot.getKey());
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        return view;
    }
}