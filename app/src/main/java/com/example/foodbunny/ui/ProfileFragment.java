package com.example.foodbunny.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.objects.User;


public class ProfileFragment extends Fragment {
    private TextView tvProfileEmail, tvEditRecipe;
    private Button btnUpdate, btnLogOut;
    private ImageButton btnRecipeArrow;
    private EditText etProfilePassword, etProfileUsername;
    private DBHelper dbHelper;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_profile, container, false);

       etProfilePassword = view.findViewById(R.id.etProfilePassword);
       etProfileUsername = view.findViewById(R.id.etProfileUsername);
       tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
       tvEditRecipe = view.findViewById(R.id.tvEditRecipe);
       btnLogOut = view.findViewById(R.id.btnLogOut);
       btnUpdate = view.findViewById(R.id.btnUpdate);
       btnRecipeArrow = view.findViewById(R.id.btnRecipeArrow);

        dbHelper = new DBHelper(requireActivity());
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // user id through email
        int userId = dbHelper.getUserId(userEmail);

        // if user email is present, then get profile details
        if (userEmail != null)
        {
            // get the user info then put it into the respective fields
            User user = dbHelper.getUserProfile(userEmail);

            if (user != null)
            {
                etProfilePassword.setText(user.getPassword());
                etProfileUsername.setText(user.getName());
                tvProfileEmail.setText(user.getEmail());
            }
            else
            {
                etProfilePassword.setText("Password is not available");
                etProfileUsername.setText("Username is not available");
                tvProfileEmail.setText("Email is not available");
            }

        }

        btnUpdate.setOnClickListener(v-> updateUserProfile());
        btnLogOut.setOnClickListener(v-> logOutUser());

        // taking user to edit recipe page by arrow or the text itself
        btnRecipeArrow.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), EditYourRecipesActivity.class);
            // passing the user id
            intent.putExtra("user_id", userId);
            v.getContext().startActivity(intent);
        });

        tvEditRecipe.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), EditYourRecipesActivity.class);
            // passing the user id
            intent.putExtra("user_id", userId);
            v.getContext().startActivity(intent);
        });

        return view;
    }

    private void logOutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout").
                setMessage("Are you sure that you want to logout?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();  // remove all saved data
                        editor.apply();

                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void updateUserProfile() {
        String newUsername = etProfileUsername.getText().toString().trim();
        String newPassword = etProfilePassword.getText().toString().trim();

        String passwordValidation = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$";

        // email will not be updated as it is unique
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        if (!newPassword.matches(passwordValidation)) {
            Toast.makeText(requireActivity(), "Password needs to be at least 8 characters long.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!newPassword.isEmpty() && !newUsername.isEmpty())
        {
            // calling the update user info function from dbhelper
            boolean isUpdated = dbHelper.updateUserProfile(userEmail, newUsername, newPassword);

            // if updated successfully
            if (isUpdated)
            {
                Toast.makeText(requireActivity(), "The profile details were updated successfully.", Toast.LENGTH_LONG).show();
            }
            // if not updated
            else
            {
                Toast.makeText(requireActivity(), "Something went wrong with updating the profile details.", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(requireActivity(), "No fields can be left empty", Toast.LENGTH_LONG).show();
        }

    }
}