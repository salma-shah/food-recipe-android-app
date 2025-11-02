package com.example.foodbunny.helpers;

import android.os.Handler;
import android.os.Looper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OpenAIHelper {
    private static final String API_URL = "https://api.aimlapi.com/v1/chat/completions";

    //    private static final String API_URL = "https://api.aimlapi.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final String apiKey;

    public interface Callback {
        void onResponse(String response);
        void onError(String error);
    }

    public OpenAIHelper(String apiKey) {
        this.apiKey = apiKey;
    }

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    public void ask(String prompt, Callback callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-4o-mini");
            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);
            json.put("messages", messages);
        } catch (JSONException e) {
            callback.onError("JSON error: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                new Handler(Looper.getMainLooper()).post(() ->
                        callback.onError("Network error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onError("API Error: " + response.code() + " - " + responseBody));
                    return;
                }

                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    String reply = choices.getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content")
                            .trim();

                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onResponse(reply));

                } catch (JSONException e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onError("Parse error: " + e.getMessage()));
                }
            }
        });
    }
}
