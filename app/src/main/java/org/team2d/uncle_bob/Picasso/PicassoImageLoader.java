package org.team2d.uncle_bob.Picasso;

/**
 * Created by nikolaev on 27.11.16.
 */

import android.content.Context;
import android.widget.ImageView;

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
            mPicasso = Picasso.with(context);
        }

        mPicasso.load(url)
                .placeholder(placeholder)
                .error(errorPlaceHolder)
                .into(target);

    }
}