package googlePlayServices;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;
import com.halogengames.poof.R;
import com.halogengames.poof.leaderboard.LeaderboardManager;

public class AndroidLeaderboardManager implements LeaderboardManager {
    private PlayGamesHelper playGamesHelper;
    private Activity activity;

    private static final int RC_LEADERBOARD_UI = 9004;

    public AndroidLeaderboardManager(Activity activity, PlayGamesHelper playGamesHelper){
        this.activity = activity;
        this.playGamesHelper = playGamesHelper;
    }

    @Override
    public void setTimedScore(int score) {
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(activity);
        if(acc != null) {
            Games.getLeaderboardsClient(activity, acc)
                    .submitScore(activity.getString(R.string.timed_leaderboard_id), score);
        }
    }

    @Override
    public void setRelaxedScore(int score){
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(activity);
        if(acc != null) {
            Games.getLeaderboardsClient(activity, acc).submitScore(activity.getString(R.string.relaxed_leaderboard_id), score);
        }
    }

    @Override
    public void showLeaderBoard(){
        if (playGamesHelper.isSignedIn()) {
            GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(activity);
            if (acc != null) {
                Games.getLeaderboardsClient(activity, acc)
                        .getAllLeaderboardsIntent()
                        .addOnSuccessListener(new OnSuccessListener<Intent>() {
                            @Override
                            public void onSuccess(Intent intent) {
                                activity.startActivityForResult(intent, RC_LEADERBOARD_UI);
                            }
                        });
            }
        }else {
            playGamesHelper.signInSilently();
        }
    }

}
