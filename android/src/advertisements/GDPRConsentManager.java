package advertisements;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.androidfung.geoip.IpApiService;
import com.androidfung.geoip.ServicesManager;
import com.androidfung.geoip.model.GeoIpResponseModel;
import com.halogengames.poof.Poof;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class GDPRConsentManager {
    public enum ConsentStat{
        Unknown, Personalized, NonPersonalized
    }

    private Activity activity;

    private SharedPreferences sharedPrefs;
    private ConsentStat status;

    private String country;
    private ArrayList<String> EUCountries;

    GDPRConsentManager(Activity activity){
        this.activity = activity;
        sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
        String cons = sharedPrefs.getString("consent_status","Unknown");
        status = ConsentStat.valueOf(cons);

        getLocationInfo();

        addEUCountries();
    }

    private void addEUCountries(){
        EUCountries = new ArrayList<>();
        EUCountries.add("Austria");
        EUCountries.add("Italy");
        EUCountries.add("Belgium");
        EUCountries.add("Latvia");
        EUCountries.add("Bulgaria");
        EUCountries.add("Lithuania");
        EUCountries.add("Croatia");
        EUCountries.add("Luxembourg");
        EUCountries.add("Cyprus");
        EUCountries.add("Malta");
        EUCountries.add("Czech Republic");
        EUCountries.add("Netherlands");
        EUCountries.add("Denmark");
        EUCountries.add("Poland");
        EUCountries.add("Estonia");
        EUCountries.add("Portugal");
        EUCountries.add("Finland");
        EUCountries.add("Romania");
        EUCountries.add("France");
        EUCountries.add("Slovakia");
        EUCountries.add("Germany");
        EUCountries.add("Slovenia");
        EUCountries.add("Greece");
        EUCountries.add("Spain");
        EUCountries.add("Hungary");
        EUCountries.add("Sweden");
        EUCountries.add("Ireland");
        EUCountries.add("United Kingdom");
    }

    public boolean isEURegion(){
        return EUCountries.indexOf(country) != -1;
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

    private void getLocationInfo(){
        IpApiService ipApiService = ServicesManager.getGeoIpService();
        ipApiService.getGeoIp().enqueue(new Callback<GeoIpResponseModel>() {
            @Override
            public void onResponse(Call<GeoIpResponseModel> call, retrofit2.Response<GeoIpResponseModel> response) {
                country = response.body().getCountry();
//                String city = response.body().getCity();
//                String countryCode = response.body().getCountryCode();
//                double latitude = response.body().getLatitude();
//                double longtidue = response.body().getLongitude();
//                region = response.body().getRegion();
//                String timezone = response.body().getTimezone();
//                String isp = response.body().getIsp();
            }

            @Override
            public void onFailure(Call<GeoIpResponseModel> call, Throwable t) {
                Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
