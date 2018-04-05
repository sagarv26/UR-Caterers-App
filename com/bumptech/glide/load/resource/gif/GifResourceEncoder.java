package com.bumptech.glide.load.resource.gif;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifencoder.AnimatedGifEncoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import java.io.IOException;
import java.io.OutputStream;

public class GifResourceEncoder implements ResourceEncoder<GifDrawable> {
    private static final Factory FACTORY = new Factory();
    private static final String TAG = "GifEncoder";
    private final BitmapPool bitmapPool;
    private final Factory factory;
    private final BitmapProvider provider;

    static class Factory {
        Factory() {
        }

        public GifDecoder buildDecoder(BitmapProvider bitmapProvider) {
            return new GifDecoder(bitmapProvider);
        }

        public GifHeaderParser buildParser() {
            return new GifHeaderParser();
        }

        public AnimatedGifEncoder buildEncoder() {
            return new AnimatedGifEncoder();
        }

        public Resource<Bitmap> buildFrameResource(Bitmap bitmap, BitmapPool bitmapPool) {
            return new BitmapResource(bitmap, bitmapPool);
        }
    }

    public boolean encode(com.bumptech.glide.load.engine.Resource<com.bumptech.glide.load.resource.gif.GifDrawable> r19, java.io.OutputStream r20) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.ssa.SSATransform.placePhi(SSATransform.java:82)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:50)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
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
        r18 = this;
        r10 = com.bumptech.glide.util.LogTime.getLogTime();
        r6 = r19.get();
        r6 = (com.bumptech.glide.load.resource.gif.GifDrawable) r6;
        r12 = r6.getFrameTransformation();
        r14 = r12 instanceof com.bumptech.glide.load.resource.UnitTransformation;
        if (r14 == 0) goto L_0x001f;
    L_0x0012:
        r14 = r6.getData();
        r0 = r18;
        r1 = r20;
        r9 = r0.writeDataDirect(r14, r1);
    L_0x001e:
        return r9;
    L_0x001f:
        r14 = r6.getData();
        r0 = r18;
        r4 = r0.decodeHeaders(r14);
        r0 = r18;
        r14 = r0.factory;
        r7 = r14.buildEncoder();
        r0 = r20;
        r14 = r7.start(r0);
        if (r14 != 0) goto L_0x003b;
    L_0x0039:
        r9 = 0;
        goto L_0x001e;
    L_0x003b:
        r8 = 0;
    L_0x003c:
        r14 = r4.getFrameCount();
        if (r8 >= r14) goto L_0x0076;
    L_0x0042:
        r2 = r4.getNextFrame();
        r0 = r18;
        r13 = r0.getTransformedFrame(r2, r12, r6);
        r14 = r13.get();	 Catch:{ all -> 0x0071 }
        r14 = (android.graphics.Bitmap) r14;	 Catch:{ all -> 0x0071 }
        r14 = r7.addFrame(r14);	 Catch:{ all -> 0x0071 }
        if (r14 != 0) goto L_0x005d;
    L_0x0058:
        r9 = 0;
        r13.recycle();
        goto L_0x001e;
    L_0x005d:
        r3 = r4.getCurrentFrameIndex();	 Catch:{ all -> 0x0071 }
        r5 = r4.getDelay(r3);	 Catch:{ all -> 0x0071 }
        r7.setDelay(r5);	 Catch:{ all -> 0x0071 }
        r4.advance();	 Catch:{ all -> 0x0071 }
        r13.recycle();
        r8 = r8 + 1;
        goto L_0x003c;
    L_0x0071:
        r14 = move-exception;
        r13.recycle();
        throw r14;
    L_0x0076:
        r9 = r7.finish();
        r14 = "GifEncoder";
        r15 = 2;
        r14 = android.util.Log.isLoggable(r14, r15);
        if (r14 == 0) goto L_0x001e;
    L_0x0083:
        r14 = "GifEncoder";
        r15 = new java.lang.StringBuilder;
        r15.<init>();
        r16 = "Encoded gif with ";
        r15 = r15.append(r16);
        r16 = r4.getFrameCount();
        r15 = r15.append(r16);
        r16 = " frames and ";
        r15 = r15.append(r16);
        r16 = r6.getData();
        r0 = r16;
        r0 = r0.length;
        r16 = r0;
        r15 = r15.append(r16);
        r16 = " bytes in ";
        r15 = r15.append(r16);
        r16 = com.bumptech.glide.util.LogTime.getElapsedMillis(r10);
        r15 = r15.append(r16);
        r16 = " ms";
        r15 = r15.append(r16);
        r15 = r15.toString();
        android.util.Log.v(r14, r15);
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.gif.GifResourceEncoder.encode(com.bumptech.glide.load.engine.Resource, java.io.OutputStream):boolean");
    }

    public GifResourceEncoder(BitmapPool bitmapPool) {
        this(bitmapPool, FACTORY);
    }

    GifResourceEncoder(BitmapPool bitmapPool, Factory factory) {
        this.bitmapPool = bitmapPool;
        this.provider = new GifBitmapProvider(bitmapPool);
        this.factory = factory;
    }

    private boolean writeDataDirect(byte[] data, OutputStream os) {
        try {
            os.write(data);
            return true;
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Failed to write data to output stream in GifResourceEncoder", e);
            }
            return false;
        }
    }

    private GifDecoder decodeHeaders(byte[] data) {
        GifHeaderParser parser = this.factory.buildParser();
        parser.setData(data);
        GifHeader header = parser.parseHeader();
        GifDecoder decoder = this.factory.buildDecoder(this.provider);
        decoder.setData(header, data);
        decoder.advance();
        return decoder;
    }

    private Resource<Bitmap> getTransformedFrame(Bitmap currentFrame, Transformation<Bitmap> transformation, GifDrawable drawable) {
        Resource<Bitmap> bitmapResource = this.factory.buildFrameResource(currentFrame, this.bitmapPool);
        Resource<Bitmap> transformedResource = transformation.transform(bitmapResource, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        if (!bitmapResource.equals(transformedResource)) {
            bitmapResource.recycle();
        }
        return transformedResource;
    }

    public String getId() {
        return "";
    }
}
