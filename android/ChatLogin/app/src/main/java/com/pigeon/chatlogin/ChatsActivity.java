package com.pigeon.chatlogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.pigeon.chatlogin.ChatUser;

import java.io.IOException;
import java.net.URL;

public class ChatsActivity
        extends FragmentActivity
        implements GoogleApiClient.OnConnectionFailedListener
    {
        private static final String TAG = "ChatsActivity";

    private ChatUser chatUser;
    private GoogleApiClient mGoogleApiClient;

    private TextView nameView;
    private ImageView imageView;

    public static Bitmap getFacebookProfilePicture(String userId){
        Bitmap bitmap = null;

        try {
            URL imageUrl = new URL("https://graph.facebook.com/" + userId + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        }
        catch (Exception ex) {
            Log.d(TAG, ex.toString());
        }

        return bitmap;
    }

    public static Bitmap getGoogleProfilePicture(String photoUrl){
        Bitmap bitmap = null;

        try {
            URL imageUrl = new URL(photoUrl);
            bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        }
        catch (Exception ex) {
            Log.d(TAG, ex.toString());
        }

        return bitmap;
    }

        private class DownloadProfilePhoto extends AsyncTask<String, Void, Bitmap> {

            protected Bitmap doInBackground(String... params) {
                if (chatUser.getType().equals("fb")) {
                    return getFacebookProfilePicture(chatUser.getId());
                }
                else {
                    return getGoogleProfilePicture(chatUser.getProfilePictureUrl());
                }
            }

            protected void onProgressUpdate(Integer... progress) {

            }

            protected void onPostExecute(Bitmap result) {
                imageView.setImageBitmap(result);
                imageView.getLayoutParams().height = 100;
            }
        }

    private void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        gotoLoginActivity();
                    }
                });
    }

        private void signOutFacebook() {
            LoginManager.getInstance().logOut();
            gotoLoginActivity();
        }

        private void gotoLoginActivity() {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        private void gotoChatActivity() {
            Intent intent = new Intent(this, ChatActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(getString(R.string.user_key_name), chatUser);
            intent.putExtras(b);
            startActivity(intent);
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            // An unresolvable error has occurred and Google APIs (including Sign-In) will not
            // be available.
            Log.d(TAG, "onConnectionFailed:" + connectionResult);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        final Activity activity = this;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ImageButton logOutButton = (ImageButton)findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chatUser.getType().equals("fb"))
                    signOutFacebook();
                else
                    signOutGoogle();
            }
        });

        imageView = (ImageView)findViewById(R.id.imageView);

        Bundle b = this.getIntent().getExtras();
        if (b != null)
            chatUser = (ChatUser) b.getSerializable(getString(R.string.user_key_name));

        new DownloadProfilePhoto().execute();

        ImageButton newChatButton = (ImageButton)findViewById(R.id.newChatButton);
        newChatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoChatActivity();
            }
        });

    }
}
