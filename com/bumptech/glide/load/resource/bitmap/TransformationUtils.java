package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

public final class TransformationUtils {
    public static final int PAINT_FLAGS = 6;
    private static final String TAG = "TransformationUtils";

    private TransformationUtils() {
    }

    public static Bitmap centerCrop(Bitmap recycled, Bitmap toCrop, int width, int height) {
        if (toCrop == null) {
            return null;
        }
        if (toCrop.getWidth() == width && toCrop.getHeight() == height) {
            return toCrop;
        }
        float scale;
        Bitmap result;
        float dx = 0.0f;
        float dy = 0.0f;
        Matrix m = new Matrix();
        if (toCrop.getWidth() * height > toCrop.getHeight() * width) {
            scale = ((float) height) / ((float) toCrop.getHeight());
            dx = (((float) width) - (((float) toCrop.getWidth()) * scale)) * 0.5f;
        } else {
            scale = ((float) width) / ((float) toCrop.getWidth());
            dy = (((float) height) - (((float) toCrop.getHeight()) * scale)) * 0.5f;
        }
        m.setScale(scale, scale);
        m.postTranslate((float) ((int) (dx + 0.5f)), (float) ((int) (dy + 0.5f)));
        if (recycled != null) {
            result = recycled;
        } else {
            result = Bitmap.createBitmap(width, height, getSafeConfig(toCrop));
        }
        setAlpha(toCrop, result);
        new Canvas(result).drawBitmap(toCrop, m, new Paint(6));
        return result;
    }

    public static Bitmap fitCenter(Bitmap toFit, BitmapPool pool, int width, int height) {
        if (toFit.getWidth() != width || toFit.getHeight() != height) {
            float minPercentage = Math.min(((float) width) / ((float) toFit.getWidth()), ((float) height) / ((float) toFit.getHeight()));
            int targetWidth = (int) (((float) toFit.getWidth()) * minPercentage);
            int targetHeight = (int) (((float) toFit.getHeight()) * minPercentage);
            if (toFit.getWidth() != targetWidth || toFit.getHeight() != targetHeight) {
                Config config = getSafeConfig(toFit);
                Bitmap toReuse = pool.get(targetWidth, targetHeight, config);
                if (toReuse == null) {
                    toReuse = Bitmap.createBitmap(targetWidth, targetHeight, config);
                }
                setAlpha(toFit, toReuse);
                if (Log.isLoggable(TAG, 2)) {
                    Log.v(TAG, "request: " + width + "x" + height);
                    Log.v(TAG, "toFit:   " + toFit.getWidth() + "x" + toFit.getHeight());
                    Log.v(TAG, "toReuse: " + toReuse.getWidth() + "x" + toReuse.getHeight());
                    Log.v(TAG, "minPct:   " + minPercentage);
                }
                Canvas canvas = new Canvas(toReuse);
                Matrix matrix = new Matrix();
                matrix.setScale(minPercentage, minPercentage);
                canvas.drawBitmap(toFit, matrix, new Paint(6));
                return toReuse;
            } else if (!Log.isLoggable(TAG, 2)) {
                return toFit;
            } else {
                Log.v(TAG, "adjusted target size matches input, returning input");
                return toFit;
            }
        } else if (!Log.isLoggable(TAG, 2)) {
            return toFit;
        } else {
            Log.v(TAG, "requested target size matches input, returning input");
            return toFit;
        }
    }

    @TargetApi(12)
    public static void setAlpha(Bitmap toTransform, Bitmap outBitmap) {
        if (VERSION.SDK_INT >= 12 && outBitmap != null) {
            outBitmap.setHasAlpha(toTransform.hasAlpha());
        }
    }

    @TargetApi(5)
    @Deprecated
    public static int getOrientation(String pathToOriginal) {
        int degreesToRotate = 0;
        try {
            degreesToRotate = getExifOrientationDegrees(new ExifInterface(pathToOriginal).getAttributeInt("Orientation", 0));
        } catch (Exception e) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "Unable to get orientation for image with path=" + pathToOriginal, e);
            }
        }
        return degreesToRotate;
    }

    @Deprecated
    public static Bitmap orientImage(String pathToOriginal, Bitmap imageToOrient) {
        return rotateImage(imageToOrient, getOrientation(pathToOriginal));
    }

    public static Bitmap rotateImage(Bitmap imageToOrient, int degreesToRotate) {
        Bitmap result = imageToOrient;
        if (degreesToRotate != 0) {
            try {
                Matrix matrix = new Matrix();
                matrix.setRotate((float) degreesToRotate);
                result = Bitmap.createBitmap(imageToOrient, 0, 0, imageToOrient.getWidth(), imageToOrient.getHeight(), matrix, true);
            } catch (Exception e) {
                if (Log.isLoggable(TAG, 6)) {
                    Log.e(TAG, "Exception when trying to orient image", e);
                }
            }
        }
        return result;
    }

    public static int getExifOrientationDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public static Bitmap rotateImageExif(Bitmap toOrient, BitmapPool pool, int exifOrientation) {
        Matrix matrix = new Matrix();
        initializeMatrixForRotation(exifOrientation, matrix);
        if (matrix.isIdentity()) {
            return toOrient;
        }
        RectF newRect = new RectF(0.0f, 0.0f, (float) toOrient.getWidth(), (float) toOrient.getHeight());
        matrix.mapRect(newRect);
        int newWidth = Math.round(newRect.width());
        int newHeight = Math.round(newRect.height());
        Config config = getSafeConfig(toOrient);
        Bitmap result = pool.get(newWidth, newHeight, config);
        if (result == null) {
            result = Bitmap.createBitmap(newWidth, newHeight, config);
        }
        matrix.postTranslate(-newRect.left, -newRect.top);
        new Canvas(result).drawBitmap(toOrient, matrix, new Paint(6));
        return result;
    }

    private static Config getSafeConfig(Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Config.ARGB_8888;
    }

    static void initializeMatrixForRotation(int exifOrientation, Matrix matrix) {
        switch (exifOrientation) {
            case 2:
                matrix.setScale(-1.0f, 1.0f);
                return;
            case 3:
                matrix.setRotate(180.0f);
                return;
            case 4:
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 5:
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 6:
                matrix.setRotate(90.0f);
                return;
            case 7:
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                return;
            case 8:
                matrix.setRotate(-90.0f);
                return;
            default:
                return;
        }
    }
}
