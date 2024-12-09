package com.example.wearef1;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView email_tv;
    private TextView username_tv;
    private Button update_info_btn;
    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        email_tv = view.findViewById(R.id.profile_page_email_tv);
        username_tv = view.findViewById(R.id.profile_page_username_tv);
        update_info_btn = view.findViewById(R.id.profile_page_update_user_info);

        String email = UserPreferenceManager.getUser(getContext()).getEmail();

        if(!email.isEmpty()) {
            email_tv.setText(email);
        }

        String username = UserPreferenceManager.getUser(getContext()).getUsername();

        if(!username.isEmpty()){
            username_tv.setText(username);
        }

        update_info_btn.setOnClickListener(v -> {
            showCustomDialog();
        });

        return view;
    }

    private void showCustomDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialogue);
        dialog.setCancelable(true);

        // Access the views from the custom layout
        TextView title = dialog.findViewById(R.id.dialog_title);
        EditText input_username = dialog.findViewById(R.id.dialog_input_username);
        EditText input_password = dialog.findViewById(R.id.dialog_input_password);
        Button positiveButton = dialog.findViewById(R.id.dialog_positive_button);
        Button negativeButton = dialog.findViewById(R.id.dialog_negative_button);

        // Set up click listeners
        positiveButton.setOnClickListener(v -> {
            String userInput_Username = input_username.getText().toString().trim();
            String userInput_Password = input_password.getText().toString().trim();
            if (!userInput_Username.isEmpty() && !userInput_Password.isEmpty()) {
                saveUserName(userInput_Username);
                savePassword(userInput_Password);
                dialog.dismiss();
            } else if (!userInput_Password.isEmpty()) {
                savePassword(userInput_Password);
                dialog.dismiss();
            } else if (!userInput_Username.isEmpty()) {
                saveUserName(userInput_Username);
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());

        // Adjust the dialog to take up the full width
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Optional: Remove background padding
        }
    }


    private void saveUserName(String newUsername) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Query the Realtime Database for the username
        databaseReference.orderByChild("username").equalTo(newUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Username already exists
                            Log.d("UsernameCheck", "Username is already taken.");
                            Toast.makeText(getContext(), "Username is already taken.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Username is available, update it
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String userId = currentUser.getUid();
                                databaseReference.child(userId).child("username").setValue(newUsername)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Log.d("UpdateUsername", "Username updated successfully.");
                                                username_tv.setText(newUsername);
                                                Toast.makeText(getContext(), "Username updated successfully.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e("UpdateUsername", "Failed to update username.", task.getException());
                                                Toast.makeText(getContext(), "Failed to update username.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("UsernameCheck", "Error querying database.", databaseError.toException());
                        Toast.makeText(getContext(), "Error querying database.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void savePassword(String userInput_Password) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updatePassword(userInput_Password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("UpdatePassword", "Password updated successfully.");
                        } else {
                            // Handle errors
                            Log.e("UpdatePassword", "Error updating password", task.getException());
                        }
                    });
        }
    }
}