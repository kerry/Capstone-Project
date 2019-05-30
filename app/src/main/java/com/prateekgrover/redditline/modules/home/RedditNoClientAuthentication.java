package com.prateekgrover.redditline.modules.home;

import android.util.Base64;

import androidx.annotation.NonNull;

import net.openid.appauth.NoClientAuthentication;
import net.openid.appauth.TokenRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RedditNoClientAuthentication implements net.openid.appauth.ClientAuthentication {

    /**
     * Name of this authentication method.
     */
    public static final String NAME = "none";

    /**
     * The default (singleton) instance of {@link NoClientAuthentication}.
     */
    public static final RedditNoClientAuthentication INSTANCE = new RedditNoClientAuthentication();

    private RedditNoClientAuthentication() {
        // no need to instantiate separate instances from INSTANCE
    }

    /**
     * {@inheritDoc}
     *
     * @return always `null`.
     */
    @Override
    public Map<String, String> getRequestHeaders(@NonNull String clientId) {
        Map<String, String> additionalParameters = new HashMap<>();
        String basicAuth = "Basic " + new String(Base64.encode((clientId + ":").getBytes(),Base64.NO_WRAP));
        additionalParameters.put("Authorization", basicAuth);
        return additionalParameters;
    }

    /**
     * {@inheritDoc}
     *
     * Where no alternative form of client authentication is used, the client_id is simply
     * sent as a client identity assertion.
     */
    @Override
    public Map<String, String> getRequestParameters(@NonNull String clientId) {
        return Collections.singletonMap(TokenRequest.PARAM_CLIENT_ID, clientId);
    }
}
