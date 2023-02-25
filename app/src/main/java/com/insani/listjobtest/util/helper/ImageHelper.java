package com.insani.listjobtest.util.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class ImageHelper {
    private ImageHelper() throws IllegalAccessException {
        throw new IllegalAccessException("This is an Utility class!");
    }

    public static void loadImageViaGlide(Context mContext, @Nullable String uri, ImageView target, int placeHolderID) {
        Glide.with(mContext)
                .asBitmap()
                .load(uri)
                .centerCrop()
                .placeholder(placeHolderID)
                .apply(RequestOptions.circleCropTransform()
                        .error(placeHolderID))
                .into(new BitmapImageViewTarget(target) {
                    @Override
                    public void setDrawable(Drawable drawable) {
                        super.setDrawable(drawable);
                    }

                    @Override
                    protected void setResource(Bitmap resource) {
                        target.setImageBitmap(resource);
                    }
                });
    }
}
