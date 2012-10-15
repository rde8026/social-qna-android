package com.social.qna.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: reldridge1
 * Date: 10/15/12
 * Time: 11:55 AM
 */
@Singleton
public class ReverseLocation {

    @Inject Context context;

    public static interface ReverseLookupCallback {
        public void onSuccess(String address);
        public void onFailure(String error);
    }

    public void reverseLocationLookup(final double latitude, final double longitude, final ReverseLookupCallback callback) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Geocoder geocoder = new Geocoder(context);
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 2);
                    if (addresses != null && !addresses.isEmpty()) {
                        callback.onSuccess(flattenAddress(addresses.get(0)));
                    }
                } catch (Exception e) {
                    callback.onFailure(e.getMessage());
                }
            }
        }.start();
    }

    private String flattenAddress(Address address) {
        StringBuilder sb = new StringBuilder();
        if (address.getLocality() != null && !"".equalsIgnoreCase(address.getLocality())) {
            sb.append(address.getLocality());
        }
        if (address.getAdminArea() != null && !"".equalsIgnoreCase(address.getAdminArea())) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(address.getAdminArea());
        }
        return sb.toString();
    }

}
