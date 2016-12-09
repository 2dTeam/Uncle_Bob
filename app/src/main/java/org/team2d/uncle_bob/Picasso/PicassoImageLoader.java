package org.team2d.uncle_bob.Picasso;

/**
 * Created by nikolaev on 27.11.16.
 */

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader {
    private final static PicassoImageLoader INSTANCE = new PicassoImageLoader();

    private Picasso mPicasso = null;


    public static PicassoImageLoader getInstance() {
        return INSTANCE;
    }

    public void load(final Context context, final String url, int placeholder,
                     final int errorPlaceHolder, final ImageView target) {

        if (mPicasso == null) {
            try {
                Picasso.setSingletonInstance(mPicasso);
            } catch (IllegalStateException ignored) {
                // Picasso instance was already set
                // cannot set it after Picasso.with(Context) was already in use
            }
            mPicasso = Picasso.with(context);
        }

        mPicasso.load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(placeholder)
                .into(target, new Callback() {
                    @Override
                    public void onSuccess() {}

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(url)
                                .error(errorPlaceHolder)
                                .into(target, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Log.d("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

    }
}