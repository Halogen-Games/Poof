package googlePlayServices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.halogengames.poof.AndroidLauncher;
import com.halogengames.poof.R;

import java.util.prefs.Preferences;

public class PlayGamesHelper {

    private AndroidLauncher activity;

    private GoogleSignInAccount signedInAccount;
    private GoogleSignInClient signInClient;

    private View popupView;

    private GamesClient gamesClient;
    public static final int RC_SIGN_IN = 9001;

    public PlayGamesHelper(AndroidLauncher activity){
        this.activity = activity;
        signInClient = GoogleSignIn.getClient(activity, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(activity) != null;
    }

    public void signInSilently() {
        signInClient.silentSignIn().addOnCompleteListener(activity,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            signedInAccount = task.getResult();
                            if(signedInAccount!=null) {
                                gamesClient = Games.getGamesClient(activity, signedInAccount);
                                setPopUpView(popupView);
                            }
                            Log.d("status","Signed In Silently");
                        } else {
                            // Player will need to sign-in explicitly using via UI
                            startSignInIntent();
                        }
                    }
                });
    }

    public void signOut() {
        signInClient.signOut().addOnCompleteListener(activity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // at this point, the user is signed out.
                    }
                });
    }

    private void startSignInIntent() {
        Intent intent = signInClient.getSignInIntent();
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void handleSignInIntentResult(Intent data){
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            // The signed in account is stored in the result.
            signedInAccount = result.getSignInAccount();
            if(signedInAccount!=null) {
                gamesClient = Games.getGamesClient(activity, signedInAccount);
                setPopUpView(popupView);
            }
            Log.d("status","Signed In");
        } else {
            Log.d("status","couldn't sign in");
            String message = result.getStatus().getStatusMessage();
            if (message != null && !message.isEmpty()) {
                new AlertDialog.Builder(activity).setMessage(message).setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }

    public void setPopUpView(View view){
        popupView = view;
        if(gamesClient!=null) {
            gamesClient.setViewForPopups(view);
        }
    }
}
