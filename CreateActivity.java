package com.example.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateActivity extends AppCompatActivity {
    static final int REQ_CODE_SPEECH_INPUT = 100;
    TextView textView;
    EditText editTexttitle, editTexttext;
    Button buttonsave, buttoncancel;
    Button button;
    ImageButton camera_open_id;
    ImageView click_image_id;
    TextView mVoiceInputTv;
    ImageButton mSpeakBtn;
    SharedPreferences sharedPreferences;
    String create = "http://13.233.64.181:4000/api/createnote";
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        editTexttext = findViewById(R.id.etText);
        editTexttitle = findViewById(R.id.extext);
        buttonsave = findViewById(R.id.btnsave);
        buttoncancel = findViewById(R.id.btncancel);
        button = findViewById(R.id.view);
        mSpeakBtn = findViewById(R.id.btnSpeak);
        mVoiceInputTv = findViewById(R.id.voiceInput);
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = (ImageView) findViewById(R.id.click_image);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CreateActivity.this);


        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTexttitle.getText().toString();
                String text = editTexttext.getText().toString();
                String userId = sharedPreferences.getString("userId", "");
                try {
                    myApi(create, title, text, userId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateActivity.this, ShowActivity.class);
                startActivity(intent);


            }
        });

        buttoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked();
            }
        });
    }


    private void myApi(String count, String title, String text, String userId) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", userId);
        jsonObject.put("title", title);
        jsonObject.put("description", text);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, count, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("@@@@@@@@@@@@", String.valueOf(response));

                try {
                    JSONObject dataobj = response.getJSONObject("data");
                    String userId = dataobj.getString("user_id");
                    String title = dataobj.getString("title");
                    String description = dataobj.getString("description");
                    String id = dataobj.getString("_id");

                    Log.e("notedatataa", userId + "\n" + title + "\n" + description + "\n" + id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Toast.makeText(CreateActivity.this, "Saved", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", String.valueOf(error));


            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void onCancelClicked() {
        this.finish();
    }
}



