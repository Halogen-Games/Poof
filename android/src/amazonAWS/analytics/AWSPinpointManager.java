package amazonAWS.analytics;

import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileUser;
import com.halogengames.poof.AWS.analytics.AWSPinpointInterface;
import com.halogengames.poof.AndroidLauncher;

public class AWSPinpointManager implements AWSPinpointInterface {

    @Override
    public void addUser(String userID) {
        final PinpointManager pinpoint = AndroidLauncher.pinpointManager;
        EndpointProfile endpointProfile = pinpoint.getTargetingClient().currentEndpoint();
        EndpointProfileUser user = new EndpointProfileUser();
        user.setUserId(userID);
        endpointProfile.setUser(user);
        pinpoint.getTargetingClient().updateEndpointProfile(endpointProfile);
    }
}
