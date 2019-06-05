package com.prateekgrover.redditline.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.prateekgrover.redditline.repository.database.SharedPreferenceManager;
import com.prateekgrover.redditline.repository.network.NetworkManager;

import net.openid.appauth.AuthState;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageHelper {

    private static ImageHelper instance;
    private Context mContext;

    public static synchronized ImageHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ImageHelper(context);
        }

        return instance;
    }

    private ImageHelper(Context context) {
        mContext = context;
    }

    public void setImage(ImageView iv, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + AppState.getInstance().getAccessToken())
                .build());

        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(mContext).load(glideUrl)
                .transition(withCrossFade())
                .apply(options)
                .into(iv);
    }
}
