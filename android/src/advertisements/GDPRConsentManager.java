package advertisements;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.halogengames.poof.Poof;

public class GDPRConsentManager {
    public enum ConsentStat{
        Unknown, Personalized, NonPersonalized
    }

    private SharedPreferences sharedPrefs;
    private ConsentStat status;


    GDPRConsentManager(Activity activity){
        sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
        String cons = sharedPrefs.getString("consent_status","Unknown");
        status = ConsentStat.valueOf(cons);
    }

    public boolean isEURegion(){

        //todo:use location here to determine below rv
        return true;
    }

    public boolean isConsentFormNeeded(){
        if(isEURegion() && status == ConsentStat.Unknown){
            return true;
        }else{
            return false;
        }
    }

    public void setConsentStatus(ConsentStat status){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString( "consent_status", status.toString());
        editor.apply();
    }

    public ConsentStat getConsentStatus(){
        return status;
    }
}
