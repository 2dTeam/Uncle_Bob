package org.team2d.uncle_bob.Picasso;

/**
 * Created by nikolaev on 27.11.16.
 */

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoImageLoader {
    private final static PicassoImageLoader INSTANCE = new PicassoImageLoader();

    private PicassoImageLoader() {}

    public static PicassoImageLoader getInstance() {
        return INSTANCE;
    }

    public void load(Context context, String url, int placeholder,
                     int errorPlaceHolder, ImageView target) {
        Picasso.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(errorPlaceHolder)
                .into(target);
    }
}