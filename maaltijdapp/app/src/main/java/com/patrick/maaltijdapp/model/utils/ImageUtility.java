package com.patrick.maaltijdapp.model.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import com.patrick.maaltijdapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Patrick on 18/01/2018.
 */

/**
 * Provides specific operations for images.
 */
public class ImageUtility
{
    private static final String TAG = ImageUtility.class.getSimpleName();

    /**
     * Converts a Bitmap object to a byte array.
     *
     * @return Returns a byte array the Bitmap object.
     */
    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        if (bitmap != null)
        {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
        }

        return stream.toByteArray();
    }

    /**
     * Converts a byte array to a Bitmap object.
     *
     * @return Returns a Bitmap object of the byte array.
     */
    public static Bitmap getImage(byte[] image)
    {
        Bitmap bitmap = null;

        if (image != null)
        {
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        return bitmap;
    }

    /**
     * Gets the default image.
     *
     * @deprecated  As loading error or placeholder images is better with Picasso library.
     * @param context The Context object associated with the current state of the application for the current request.
     * @return Returns a Bitmap object of the default image to display.
     */
    @Deprecated
    public static Bitmap defaultImage(Context context)
    {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
    }

    /**
     * Gets the image from an URI.
     *
     * @param context The Context object associated with the current state of the application for the current request.
     * @param uri The path used to identify a resource.
     * @return Returns a Bitmap object of the image.
     */
    public static Bitmap getBitmapFromURI(Context context, Uri uri)
    {
        final int REQUIRED_SIZE = 1024;
        Bitmap bitmap = null;

        try
        {
            InputStream input = context.getContentResolver().openInputStream(uri);

            if (input != null)
            {
                BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
                onlyBoundsOptions.inJustDecodeBounds = true;
                onlyBoundsOptions.inDither = true;//optional
                onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
                BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
                input.close();

                if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
                {
                    return null;
                }

                int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

                double ratio = (originalSize > REQUIRED_SIZE) ? (originalSize / REQUIRED_SIZE) : 1.0;

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
                bitmapOptions.inDither = true;
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                input = context.getContentResolver().openInputStream(uri);

                if (input != null)
                {
                    bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
                    input.close();
                }
            }
        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG, e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio)
    {
        int result = Integer.highestOneBit((int)Math.floor(ratio));

        if(result == 0)
        {
            return 1;
        }
        else
        {
            return result;
        }
    }

    /**
     * Gets the file name from an URI.
     *
     * @param context The Context object associated with the current state of the application for the current request.
     * @param uri The path used to identify a resource.
     * @return Returns a String of the file name.
     */
    public static String getFileName(Context context, Uri uri)
    {
        String fileName = "";

        if (uri.getScheme().equals("content"))
        {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

            try
            {
                if (cursor != null && cursor.moveToFirst())
                {
                    fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally
            {
                cursor.close();
            }
        }

        if (fileName.equals(""))
        {
            fileName = uri.getPath();
            int cut = fileName.lastIndexOf('/');

            if (cut != -1)
            {
                fileName = fileName.substring(cut + 1);
            }
        }

        return fileName;
    }
}
