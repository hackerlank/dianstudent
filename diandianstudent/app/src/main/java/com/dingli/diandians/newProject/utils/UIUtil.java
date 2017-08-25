package com.dingli.diandians.newProject.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.jakewharton.rxbinding.view.RxView;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;


public class UIUtil {

    public static final float DARK_ALPHA = .4F;
    public static final float BRIGHT_ALPHA = 1.0F;
    private static String TAG = "UIUtil";

    public static void moveCursor2End(Spannable text) {
        try {
            Selection.setSelection(text, text.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void darken(Activity activity) {
        darken(activity, false);
    }

    public static void darken(Activity activity, boolean anim) {
        changeAlpha(activity, DARK_ALPHA, anim);
    }

    public static void brighten(Activity activity) {
        brighten(activity, false);
    }

    public static void brighten(Activity activity, boolean anim) {
        changeAlpha(activity, BRIGHT_ALPHA, anim);
    }

    private static void changeAlpha(Activity activity, float alpha, boolean anim) {
        if (activity == null) {
            return;
        }
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        if (anim) {
            float startAlpha = layoutParams.alpha;
            final ValueAnimator animation = ValueAnimator.ofFloat(startAlpha, alpha);
            animation.setDuration(300);
            animation.start();
            animation.addUpdateListener(valueAnimator -> {
                layoutParams.alpha = (Float) valueAnimator.getAnimatedValue();
                activity.getWindow().setAttributes(layoutParams);
            });
            return;
        }
        layoutParams.alpha = alpha;
        activity.getWindow().setAttributes(layoutParams);
    }




    public static String getText(Editable editable) {
        return getText(editable, "");
    }

    public static String getText(Editable editable, String defaultValue) {
        if (editable == null) {
            return defaultValue;
        }
        return editable.toString();
    }

    public static String getText(TextView textView) {
        return getText(textView, "");
    }

    public static String getText(TextView textView, String defaultValue) {
        if (textView == null) {
            return defaultValue;
        }
        CharSequence charSequence = textView.getText();
        if (charSequence == null) {
            return defaultValue;
        }
        return charSequence.toString().trim();
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static void switchVisibleOrGone(View view) {
        if (view == null) {
            return;
        }
        view.setVisibility(isVisible(view) ? View.GONE : View.VISIBLE);
    }

    public static void setVisibleOrGone(View view, boolean condition) {
        if (view != null) {
            view.setVisibility(condition ? View.VISIBLE : View.GONE);
        }
    }

    public static void setVisibleOrInvisible(View view, boolean condition) {
        if (view != null) {
            view.setVisibility(condition ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public static void setInvisible(View view) {
        setVisibleOrInvisible(view, true);
    }

    public static void setGone(View view) {
        setVisibleOrGone(view, false);
    }

    public static void setEllipsis(final TextView textView, final int line) {
        ViewTreeObserver observer = textView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = textView.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (textView.getLineCount() > line) {
                    int lineEndIndex = textView.getLayout().getLineEnd(line - 1);
                    String text = textView.getText().subSequence(0, lineEndIndex - 3) + "...";
                    textView.setText(text);
                }
            }
        });

    }

    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setOnclickInterval(View view, Action1 action1) {
        RxView.clicks(view)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(action1);
    }


    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains(
                        "TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method[] methods = Activity.class.getDeclaredMethods();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                Method method = Activity.class.getDeclaredMethod(
                        "convertToTranslucent",
                        translucentConversionListenerClazz);
                method.setAccessible(true);
                method.invoke(activity, new Object[]{null});
            } else {
                Method method = Activity.class.getDeclaredMethod(
                        "convertToTranslucent",
                        translucentConversionListenerClazz,
                        ActivityOptions.class);
                method.setAccessible(true);
                method.invoke(activity, new Object[]{null, null});
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * TexTView 拼接图片
     * @param context
     * @return
     */
    public static Html.ImageGetter spliceTextView(Context context){

        Html.ImageGetter imageGetter =new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int resId = Integer.parseInt(source);
                Drawable drawable = context.getResources()
                        .getDrawable(resId);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight());

                return drawable;
            }
        };

        return imageGetter;
    }
    public static Bitmap createImageThumbnail(String filePath){
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 128*128);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        }catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}