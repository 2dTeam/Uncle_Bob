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


    public static PicassoImageLoader getInstance() {
        return INSTANCE;
    }

    public void load(final Context context, final String url, int placeholder,
                     final int errorPlaceHolder, final ImageView target) {
        Picasso.with(context)
                .load(url)
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