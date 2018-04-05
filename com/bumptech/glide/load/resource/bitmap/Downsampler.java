package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.ImageHeaderParser.ImageType;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Queue;
import java.util.Set;

public abstract class Downsampler implements BitmapDecoder<InputStream> {
    public static final Downsampler AT_LEAST = new C04691();
    public static final Downsampler AT_MOST = new C04702();
    private static final int MARK_POSITION = 5242880;
    public static final Downsampler NONE = new C04713();
    private static final Queue<Options> OPTIONS_QUEUE = Util.createQueue(0);
    private static final String TAG = "Downsampler";
    private static final Set<ImageType> TYPES_THAT_USE_POOL = EnumSet.of(ImageType.JPEG, ImageType.PNG_A, ImageType.PNG);

    static class C04691 extends Downsampler {
        C04691() {
        }

        public /* bridge */ /* synthetic */ Bitmap decode(Object x0, BitmapPool x1, int x2, int x3, DecodeFormat x4) throws Exception {
            return super.decode((InputStream) x0, x1, x2, x3, x4);
        }

        protected int getSampleSize(int inWidth, int inHeight, int outWidth, int outHeight) {
            return Math.min(inHeight / outHeight, inWidth / outWidth);
        }

        public String getId() {
            return "AT_LEAST.com.bumptech.glide.load.data.bitmap";
        }
    }

    static class C04702 extends Downsampler {
        C04702() {
        }

        public /* bridge */ /* synthetic */ Bitmap decode(Object x0, BitmapPool x1, int x2, int x3, DecodeFormat x4) throws Exception {
            return super.decode((InputStream) x0, x1, x2, x3, x4);
        }

        protected int getSampleSize(int inWidth, int inHeight, int outWidth, int outHeight) {
            int i = 1;
            int maxIntegerFactor = (int) Math.ceil((double) Math.max(((float) inHeight) / ((float) outHeight), ((float) inWidth) / ((float) outWidth)));
            int lesserOrEqualSampleSize = Math.max(1, Integer.highestOneBit(maxIntegerFactor));
            if (lesserOrEqualSampleSize >= maxIntegerFactor) {
                i = 0;
            }
            return lesserOrEqualSampleSize << i;
        }

        public String getId() {
            return "AT_MOST.com.bumptech.glide.load.data.bitmap";
        }
    }

    static class C04713 extends Downsampler {
        C04713() {
        }

        public /* bridge */ /* synthetic */ Bitmap decode(Object x0, BitmapPool x1, int x2, int x3, DecodeFormat x4) throws Exception {
            return super.decode((InputStream) x0, x1, x2, x3, x4);
        }

        protected int getSampleSize(int inWidth, int inHeight, int outWidth, int outHeight) {
            return 0;
        }

        public String getId() {
            return "NONE.com.bumptech.glide.load.data.bitmap";
        }
    }

    private static android.graphics.Bitmap.Config getConfig(java.io.InputStream r6, com.bumptech.glide.load.DecodeFormat r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0024 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r5 = 5;
        r2 = com.bumptech.glide.load.DecodeFormat.ALWAYS_ARGB_8888;
        if (r7 == r2) goto L_0x000f;
    L_0x0005:
        r2 = com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
        if (r7 == r2) goto L_0x000f;
    L_0x0009:
        r2 = android.os.Build.VERSION.SDK_INT;
        r3 = 16;
        if (r2 != r3) goto L_0x0012;
    L_0x000f:
        r2 = android.graphics.Bitmap.Config.ARGB_8888;
    L_0x0011:
        return r2;
    L_0x0012:
        r1 = 0;
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r6.mark(r2);
        r2 = new com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r2.<init>(r6);	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r1 = r2.hasAlpha();	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r6.reset();	 Catch:{ IOException -> 0x0029 }
    L_0x0024:
        if (r1 == 0) goto L_0x0087;
    L_0x0026:
        r2 = android.graphics.Bitmap.Config.ARGB_8888;
        goto L_0x0011;
    L_0x0029:
        r0 = move-exception;
        r2 = "Downsampler";
        r2 = android.util.Log.isLoggable(r2, r5);
        if (r2 == 0) goto L_0x0024;
    L_0x0032:
        r2 = "Downsampler";
        r3 = "Cannot reset the input stream";
        android.util.Log.w(r2, r3, r0);
        goto L_0x0024;
    L_0x003a:
        r0 = move-exception;
        r2 = "Downsampler";	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r3 = 5;	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r2 = android.util.Log.isLoggable(r2, r3);	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        if (r2 == 0) goto L_0x005c;	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
    L_0x0044:
        r2 = "Downsampler";	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r3.<init>();	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r4 = "Cannot determine whether the image has alpha or not from header for format ";	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r3 = r3.append(r4);	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r3 = r3.append(r7);	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        r3 = r3.toString();	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
        android.util.Log.w(r2, r3, r0);	 Catch:{ IOException -> 0x003a, all -> 0x0071 }
    L_0x005c:
        r6.reset();
        goto L_0x0024;
    L_0x0060:
        r0 = move-exception;
        r2 = "Downsampler";
        r2 = android.util.Log.isLoggable(r2, r5);
        if (r2 == 0) goto L_0x0024;
    L_0x0069:
        r2 = "Downsampler";
        r3 = "Cannot reset the input stream";
        android.util.Log.w(r2, r3, r0);
        goto L_0x0024;
    L_0x0071:
        r2 = move-exception;
        r6.reset();	 Catch:{ IOException -> 0x0076 }
    L_0x0075:
        throw r2;
    L_0x0076:
        r0 = move-exception;
        r3 = "Downsampler";
        r3 = android.util.Log.isLoggable(r3, r5);
        if (r3 == 0) goto L_0x0075;
    L_0x007f:
        r3 = "Downsampler";
        r4 = "Cannot reset the input stream";
        android.util.Log.w(r3, r4, r0);
        goto L_0x0075;
    L_0x0087:
        r2 = android.graphics.Bitmap.Config.RGB_565;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.Downsampler.getConfig(java.io.InputStream, com.bumptech.glide.load.DecodeFormat):android.graphics.Bitmap$Config");
    }

    private static boolean shouldUsePool(java.io.InputStream r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0046 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r4 = 5;
        r2 = 19;
        r3 = android.os.Build.VERSION.SDK_INT;
        if (r2 > r3) goto L_0x0009;
    L_0x0007:
        r2 = 1;
    L_0x0008:
        return r2;
    L_0x0009:
        r2 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r5.mark(r2);
        r2 = new com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r2.<init>(r5);	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r1 = r2.getType();	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r2 = TYPES_THAT_USE_POOL;	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r2 = r2.contains(r1);	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r5.reset();	 Catch:{ IOException -> 0x0021 }
        goto L_0x0008;
    L_0x0021:
        r0 = move-exception;
        r3 = "Downsampler";
        r3 = android.util.Log.isLoggable(r3, r4);
        if (r3 == 0) goto L_0x0008;
    L_0x002a:
        r3 = "Downsampler";
        r4 = "Cannot reset the input stream";
        android.util.Log.w(r3, r4, r0);
        goto L_0x0008;
    L_0x0032:
        r0 = move-exception;
        r2 = "Downsampler";	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r3 = 5;	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r2 = android.util.Log.isLoggable(r2, r3);	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        if (r2 == 0) goto L_0x0043;	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
    L_0x003c:
        r2 = "Downsampler";	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        r3 = "Cannot determine the image type from header";	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
        android.util.Log.w(r2, r3, r0);	 Catch:{ IOException -> 0x0032, all -> 0x0059 }
    L_0x0043:
        r5.reset();
    L_0x0046:
        r2 = 0;
        goto L_0x0008;
    L_0x0048:
        r0 = move-exception;
        r2 = "Downsampler";
        r2 = android.util.Log.isLoggable(r2, r4);
        if (r2 == 0) goto L_0x0046;
    L_0x0051:
        r2 = "Downsampler";
        r3 = "Cannot reset the input stream";
        android.util.Log.w(r2, r3, r0);
        goto L_0x0046;
    L_0x0059:
        r2 = move-exception;
        r5.reset();	 Catch:{ IOException -> 0x005e }
    L_0x005d:
        throw r2;
    L_0x005e:
        r0 = move-exception;
        r3 = "Downsampler";
        r3 = android.util.Log.isLoggable(r3, r4);
        if (r3 == 0) goto L_0x005d;
    L_0x0067:
        r3 = "Downsampler";
        r4 = "Cannot reset the input stream";
        android.util.Log.w(r3, r4, r0);
        goto L_0x005d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.Downsampler.shouldUsePool(java.io.InputStream):boolean");
    }

    public android.graphics.Bitmap decode(java.io.InputStream r28, com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool r29, int r30, int r31, com.bumptech.glide.load.DecodeFormat r32) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x003d in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r27 = this;
        r16 = com.bumptech.glide.util.ByteArrayPool.get();
        r17 = r16.getBytes();
        r18 = r16.getBytes();
        r10 = getDefaultOptions();
        r9 = new com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
        r0 = r28;
        r1 = r18;
        r9.<init>(r0, r1);
        r21 = com.bumptech.glide.util.ExceptionCatchingInputStream.obtain(r9);
        r23 = new com.bumptech.glide.util.MarkEnforcingInputStream;
        r0 = r23;
        r1 = r21;
        r0.<init>(r1);
        r3 = 5242880; // 0x500000 float:7.34684E-39 double:2.590327E-317;
        r0 = r21;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r0.mark(r3);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r24 = 0;
        r3 = new com.bumptech.glide.load.resource.bitmap.ImageHeaderParser;	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r0 = r21;	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r3.<init>(r0);	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r24 = r3.getOrientation();	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r21.reset();	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x003d:
        r0 = r17;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r10.inTempStorage = r0;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r0 = r27;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r1 = r23;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r22 = r0.getDimensions(r1, r9, r10);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3 = 0;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r5 = r22[r3];	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3 = 1;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r6 = r22[r3];	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r4 = com.bumptech.glide.load.resource.bitmap.TransformationUtils.getExifOrientationDegrees(r24);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3 = r27;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r7 = r30;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r8 = r31;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r14 = r3.getRoundedSampleSize(r4, r5, r6, r7, r8);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r7 = r27;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r8 = r23;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r11 = r29;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r12 = r5;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r13 = r6;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r15 = r32;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r19 = r7.downsampleWithSize(r8, r9, r10, r11, r12, r13, r14, r15);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r26 = r21.getException();	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        if (r26 == 0) goto L_0x00e4;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x0071:
        r3 = new java.lang.RuntimeException;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r0 = r26;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3.<init>(r0);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        throw r3;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x0079:
        r3 = move-exception;
        r16.releaseBytes(r17);
        r0 = r16;
        r1 = r18;
        r0.releaseBytes(r1);
        r21.release();
        releaseOptions(r10);
        throw r3;
    L_0x008b:
        r20 = move-exception;
        r3 = "Downsampler";	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r7 = 5;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3 = android.util.Log.isLoggable(r3, r7);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        if (r3 == 0) goto L_0x003d;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x0095:
        r3 = "Downsampler";	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r7 = "Cannot reset the input stream";	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r0 = r20;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        android.util.Log.w(r3, r7, r0);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        goto L_0x003d;
    L_0x009f:
        r20 = move-exception;
        r3 = "Downsampler";	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r7 = 5;	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r3 = android.util.Log.isLoggable(r3, r7);	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        if (r3 == 0) goto L_0x00b2;	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
    L_0x00a9:
        r3 = "Downsampler";	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r7 = "Cannot determine the image orientation from header";	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        r0 = r20;	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
        android.util.Log.w(r3, r7, r0);	 Catch:{ IOException -> 0x009f, all -> 0x00cb }
    L_0x00b2:
        r21.reset();
        goto L_0x003d;
    L_0x00b6:
        r20 = move-exception;
        r3 = "Downsampler";
        r7 = 5;
        r3 = android.util.Log.isLoggable(r3, r7);
        if (r3 == 0) goto L_0x003d;
    L_0x00c0:
        r3 = "Downsampler";
        r7 = "Cannot reset the input stream";
        r0 = r20;
        android.util.Log.w(r3, r7, r0);
        goto L_0x003d;
    L_0x00cb:
        r3 = move-exception;
        r21.reset();	 Catch:{ IOException -> 0x00d0 }
    L_0x00cf:
        throw r3;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x00d0:
        r20 = move-exception;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r7 = "Downsampler";	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r8 = 5;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r7 = android.util.Log.isLoggable(r7, r8);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        if (r7 == 0) goto L_0x00cf;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x00da:
        r7 = "Downsampler";	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r8 = "Cannot reset the input stream";	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r0 = r20;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        android.util.Log.w(r7, r8, r0);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        goto L_0x00cf;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x00e4:
        r25 = 0;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        if (r19 == 0) goto L_0x0109;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x00e8:
        r0 = r19;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r1 = r29;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r2 = r24;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r25 = com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImageExif(r0, r1, r2);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r0 = r19;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r1 = r25;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3 = r0.equals(r1);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        if (r3 != 0) goto L_0x0109;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x00fc:
        r0 = r29;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r1 = r19;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        r3 = r0.put(r1);	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
        if (r3 != 0) goto L_0x0109;	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x0106:
        r19.recycle();	 Catch:{ IOException -> 0x008b, all -> 0x0079 }
    L_0x0109:
        r16.releaseBytes(r17);
        r0 = r16;
        r1 = r18;
        r0.releaseBytes(r1);
        r21.release();
        releaseOptions(r10);
        return r25;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.Downsampler.decode(java.io.InputStream, com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool, int, int, com.bumptech.glide.load.DecodeFormat):android.graphics.Bitmap");
    }

    protected abstract int getSampleSize(int i, int i2, int i3, int i4);

    private int getRoundedSampleSize(int degreesToRotate, int inWidth, int inHeight, int outWidth, int outHeight) {
        int targetHeight;
        int targetWidth;
        int exactSampleSize;
        if (outHeight == Integer.MIN_VALUE) {
            targetHeight = inHeight;
        } else {
            targetHeight = outHeight;
        }
        if (outWidth == Integer.MIN_VALUE) {
            targetWidth = inWidth;
        } else {
            targetWidth = outWidth;
        }
        if (degreesToRotate == 90 || degreesToRotate == 270) {
            exactSampleSize = getSampleSize(inHeight, inWidth, targetWidth, targetHeight);
        } else {
            exactSampleSize = getSampleSize(inWidth, inHeight, targetWidth, targetHeight);
        }
        return Math.max(1, exactSampleSize == 0 ? 0 : Integer.highestOneBit(exactSampleSize));
    }

    private Bitmap downsampleWithSize(MarkEnforcingInputStream is, RecyclableBufferedInputStream bufferedStream, Options options, BitmapPool pool, int inWidth, int inHeight, int sampleSize, DecodeFormat decodeFormat) {
        Config config = getConfig(is, decodeFormat);
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = config;
        if ((options.inSampleSize == 1 || 19 <= VERSION.SDK_INT) && shouldUsePool(is)) {
            setInBitmap(options, pool.getDirty((int) Math.ceil(((double) inWidth) / ((double) sampleSize)), (int) Math.ceil(((double) inHeight) / ((double) sampleSize)), config));
        }
        return decodeStream(is, bufferedStream, options);
    }

    public int[] getDimensions(MarkEnforcingInputStream is, RecyclableBufferedInputStream bufferedStream, Options options) {
        options.inJustDecodeBounds = true;
        decodeStream(is, bufferedStream, options);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static Bitmap decodeStream(MarkEnforcingInputStream is, RecyclableBufferedInputStream bufferedStream, Options options) {
        if (options.inJustDecodeBounds) {
            is.mark(MARK_POSITION);
        } else {
            bufferedStream.fixMarkLimit();
        }
        Bitmap result = BitmapFactory.decodeStream(is, null, options);
        try {
            if (options.inJustDecodeBounds) {
                is.reset();
            }
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "Exception loading inDecodeBounds=" + options.inJustDecodeBounds + " sample=" + options.inSampleSize, e);
            }
        }
        return result;
    }

    @TargetApi(11)
    private static void setInBitmap(Options options, Bitmap recycled) {
        if (11 <= VERSION.SDK_INT) {
            options.inBitmap = recycled;
        }
    }

    @TargetApi(11)
    private static synchronized Options getDefaultOptions() {
        Options decodeBitmapOptions;
        synchronized (Downsampler.class) {
            synchronized (OPTIONS_QUEUE) {
                decodeBitmapOptions = (Options) OPTIONS_QUEUE.poll();
            }
            if (decodeBitmapOptions == null) {
                decodeBitmapOptions = new Options();
                resetOptions(decodeBitmapOptions);
            }
        }
        return decodeBitmapOptions;
    }

    private static void releaseOptions(Options decodeBitmapOptions) {
        resetOptions(decodeBitmapOptions);
        synchronized (OPTIONS_QUEUE) {
            OPTIONS_QUEUE.offer(decodeBitmapOptions);
        }
    }

    @TargetApi(11)
    private static void resetOptions(Options decodeBitmapOptions) {
        decodeBitmapOptions.inTempStorage = null;
        decodeBitmapOptions.inDither = false;
        decodeBitmapOptions.inScaled = false;
        decodeBitmapOptions.inSampleSize = 1;
        decodeBitmapOptions.inPreferredConfig = null;
        decodeBitmapOptions.inJustDecodeBounds = false;
        decodeBitmapOptions.outWidth = 0;
        decodeBitmapOptions.outHeight = 0;
        decodeBitmapOptions.outMimeType = null;
        if (11 <= VERSION.SDK_INT) {
            decodeBitmapOptions.inBitmap = null;
            decodeBitmapOptions.inMutable = true;
        }
    }
}
