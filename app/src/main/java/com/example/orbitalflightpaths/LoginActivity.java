package com.example.orbitalflightpaths;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public class LoginActivity extends AppCompatActivity{

    private boolean form_state = true;
    private EditText mEmail, mPasswordView, mConfirmPassword;
    private TextView switchForm;
    private Button mEmailSignInButton;
    private String query, results;
    HttpURLConnection con;
    JSONObject json_data;
    LogIn logIn;
    Register register;
    SharedPreferences myDB;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDB = getSharedPreferences("ORBITAL_DATA", MODE_PRIVATE);

        mEmail = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (form_state){
                    if (mEmail.getText().toString().trim().equals("") || mPasswordView.getText().toString().trim().equals("")){
                        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle("Error!");
                        alert.setMessage("You need to enter both the email and password.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    } else {
                        logIn = new LogIn();
                        logIn.execute();
                        Log.d("Login: ", "Calling LogIn class");
                    }
                } else {
                    if ((mEmail.getText().toString().trim().equals("") || mPasswordView.getText().toString().trim().equals("")) || mConfirmPassword.getText().toString().trim().equals("")){
                        final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle("Error!");
                        alert.setMessage("You need to enter the email and both passwords.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    } else {
                        if (mPasswordView.getText().toString().trim().equals(mConfirmPassword.getText().toString().trim())){
                            register = new Register();
                            register.execute();
                            Log.d("Register: ", "Calling Register class");
                        } else {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                            alert.setTitle("Error!");
                            alert.setMessage("Passwords do not match!");
                            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    }
                }
            }
        });

        mConfirmPassword = findViewById(R.id.confirm_password_id);
        switchForm = findViewById(R.id.new_user_text);
        switchForm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                formSwitcher(form_state);
            }
        });
    }


    private void formSwitcher(boolean state){
        if (state){
            switchForm.setText(getResources().getText(R.string.existing_user_text_string));
            mEmailSignInButton.setText(getResources().getString(R.string.action_register_button));
            mConfirmPassword.setVisibility(View.VISIBLE);


            form_state = false;
        }
        else {
            switchForm.setText(getResources().getText(R.string.new_user_text_string));
            mEmailSignInButton.setText(getResources().getString(R.string.action_sign_in));
            mConfirmPassword.setVisibility(View.GONE);


            form_state = true;
        }
    }

    final class Register extends AsyncTask<String, Void, Void> {
        ProgressDialog mProgressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("Attempting to register..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("username", mEmail.getText().toString().trim()).appendQueryParameter("password", mPasswordView.getText().toString().trim());
                query = builder.build().getEncodedQuery();
                String url = "https://orbitalflightpaths.000webhostapp.com/users/regi ster.php";
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(query);
                outputStreamWriter.flush();
            } catch (Exception e) {
                android.util.Log.e("doInBackground --> ", "Sending log in parameters: " + e.toString());
            }

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                results = sb.toString();
            } catch (Exception e) {
                android.util.Log.e("doInBackground --> ", "Reading server response: " + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                json_data = new JSONObject(results);
                int code = (json_data.getInt("code"));
                if (code == 1) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Success");
                    alert.setMessage("You have been registered! You can now log in");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                    mConfirmPassword.getText().clear();
                    formSwitcher(false);
                } else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Failed");
                    alert.setMessage("Could not register or user already exists!");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            } catch (Exception e) {
                Log.e("PostExecute --> ", "JSON Data, AlertDialog: " + e.toString());
            }

            mProgressDialog.dismiss();
        }
    }
    final class LogIn extends AsyncTask<String, Void, Void> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("Attempting to log in..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false); mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("username", mEmail.getText().toString().trim()).appendQueryParameter("password", mPasswordView.getText().toString().trim());
                query = builder.build().getEncodedQuery();
                String url = "https://orbitalflightpaths.000webhostapp.com/users/login.php";
                URL obj = new URL(url);
                con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
                outputStreamWriter.write(query);
                outputStreamWriter.flush();
            }
            catch (Exception e) {
                android.util.Log.e("Read:doInBackground--> ", "Sending log in parameters: " + e.toString());
                Toast.makeText(LoginActivity.this, "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuffer sb = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    sb.append(line + "\n");
                }
                results = sb.toString();
                Log.d("Read:doInBackground-->", "Results value: " + results);
            }
            catch (Exception e) {
                android.util.Log.e("Read:doInBackground--> ", "Reading server response: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
//                json_data = new JSONObject(results);
//                int data = (json_data.getInt("total"));
//                Log.d("LoginServerResp: ", "Return code is" + data);
                Log.d("Read:postExecute-->", "Results value: " + results);
                if (results.toLowerCase().contains("Data Matched".toLowerCase())) {
                    Log.d("LoginServerResp: ", "Evaluation");
                    logUser(mEmail.getText().toString().trim());
                    Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Failed to log in");
                    alert.setMessage("Incorrect username/password!");
                    alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(@NonNull DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            }
            catch (Exception e) {
                Log.e("Read:PostExecute --> ", "Server log in response (JSON) " + e.toString());
            }
            mProgressDialog.dismiss();
        }
        private void logUser(String username){
            editor = myDB.edit();
            editor.putString("username", username);
            editor.apply();
        }
    }


}

