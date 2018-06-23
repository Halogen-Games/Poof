package GDPR;

import android.content.Context;
import android.widget.Toast;


import com.androidfung.geoip.IpApiService;
import com.androidfung.geoip.ServicesManager;
import com.androidfung.geoip.model.GeoIpResponseModel;

import retrofit2.Call;
import retrofit2.Callback;

public class GDPRLocationManager {

    private String country;

    public GDPRLocationManager(final Context context) {

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
                Toast.makeText(context, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getRegion(){
        return country;
    }
}
