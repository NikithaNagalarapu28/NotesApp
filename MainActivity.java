package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    SignInButton signInButton;
    //Button button;
    int RC_SIGN_IN=0;
    GoogleSignInClient googleSignInClient;
    RequestQueue requestQueue;
    String EmailApi="http://13.233.64.181:4000/api/login";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton=findViewById(R.id.sign_in_button);
      //  button=findViewById(R.id.signout);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor =sharedPreferences.edit();



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }

            }
        });

    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.e("wwwwwwwwwwwwwww.", "-------------------");
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            try {
                logId(EmailApi,account.getEmail());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("name", "-------------------");

                   }
        catch (ApiException e) {
            Log.e("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    public void logId (String emailApi,String email) throws JSONException {

        final JSONObject jsonObject=new JSONObject();
        jsonObject.put("email",email);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, emailApi, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("created", String.valueOf(response));

                try {
                    JSONObject dataObj=response.getJSONObject("data");
                    String userId=dataObj.getString("_id");
                    Log.e("success",userId);
                    editor.putString("userId",userId);
                    editor.apply();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }



                Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                startActivity(intent);
                MainActivity.this.finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", String.valueOf(error));

            }
        });

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);



    }


    }






