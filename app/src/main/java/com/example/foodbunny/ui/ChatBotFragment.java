package com.example.foodbunny.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.adapters.ChatAdapter;
import com.example.foodbunny.helpers.OpenAIHelper;
import com.example.foodbunny.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatBotFragment extends Fragment {
    private DBHelper dbHelper;
    private ProgressBar pbLoading;
    private ChatAdapter chatAdapter;
    private List<Message> messageList;
    private RecyclerView rvChat;
    private EditText etMsg;
    private ImageButton btnSend;

    public ChatBotFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_chat_bot, container, false);

       // initializing
        dbHelper = new DBHelper(getContext());
        rvChat = view.findViewById(R.id.rvChat);
        btnSend = view.findViewById(R.id.btnSend);
        etMsg = view.findViewById(R.id.etMsg);
        pbLoading = view.findViewById(R.id.pbLoading);
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        // setting recycler view
        rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChat.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v->
        {
            String userMsg = etMsg.getText().toString().trim();

            // as long as user has sent a message
            if (!userMsg.isEmpty())
            {
                // add user message
                messageList.add(new Message(userMsg, true));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                rvChat.scrollToPosition(messageList.size() - 1);
                etMsg.setText("");

                // simulate bot reply
                getChatBotReply(userMsg);
            }
        });

       return view;
    }

    private void getChatBotReply(String userMsg) {
        userMsg = userMsg.toLowerCase();
        String chatBotReply = null;

        // simple basic logic
        if (userMsg.contains("hi") || userMsg.contains("hello") || userMsg.contains("hey")) {
            chatBotReply = "Hello! I'm FoodBunny! \uD83D\uDC30 How can I help you with cooking today?";
        }

        // first, we check the local database
//        else if (userMsg.contains("recipe") || userMsg.contains("recipes")) {
//            String recipeName = extractRecipeName(userMsg);
//            Recipe recipe = dbHelper.getRecipeByName(recipeName);
//
//            if (recipe != null)
//            {
//                chatBotReply = "Sure! Here are the basic details for " + recipeName + ": \n" + "Duration: " +
//                        recipe.getDuration() + "\nServings: " + recipe.getServingSize() + "\n You can find more details from the main recipe page. Just search it up on the home page!";
//            }
//            else
//            {

                // if local database doesn't have any info, then open ai will check
                // loading animation
                pbLoading.setVisibility(View.VISIBLE);

                OpenAIHelper openAIHelper = new OpenAIHelper("f39a0d4ee4a64f5ca1135329528aa0f2");
                openAIHelper.ask(userMsg, new OpenAIHelper.Callback() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ChatBotFragment", "The ChatBot's response is: " + response);
                        pbLoading.setVisibility(View.GONE);
                        messageList.add(new Message(response, false));
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        rvChat.scrollToPosition(messageList.size() - 1);
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("ChatBotFragment", "The ChatBot's error is: " + error);
                        pbLoading.setVisibility(View.GONE);
                        messageList.add(new Message("I'm not sure about that! I apologize. I can help you with finding recipes or ingredients, though!", false));
                        chatAdapter.notifyItemInserted(messageList.size() - 1);
                        rvChat.scrollToPosition(messageList.size() - 1);
                    }
                });
               // return;  // ending the function

            if (userMsg.contains("ingredient") || userMsg.contains("ingredients")) {
               chatBotReply = "You can find the ingredients of the recipe in each recipe page. Just search up the recipe name on the home page!";
          }

           // if chatbot has no response, then default will be:
//        if (chatBotReply == null) {
//         chatBotReply = "I'm not sure if I understood that! I can help with recipe related stuff, if you would like!";
//       }

        messageList.add(new Message(chatBotReply, false));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        rvChat.scrollToPosition(messageList.size() - 1);
        }


//   private String extractRecipeName(String text) {
//        return text.replace("how to make", "")
//                .replace("how to prepare", "")
//               .replace("recipe for", "")
//               .trim();
//    }
 }