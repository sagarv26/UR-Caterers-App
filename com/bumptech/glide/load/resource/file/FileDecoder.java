package com.bumptech.glide.load.resource.file;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.File;

public class FileDecoder implements ResourceDecoder<File, File> {
    public Resource<File> decode(File source, int width, int height) {
        return new FileResource(source);
    }

    public String getId() {
        return "";
    }
}
