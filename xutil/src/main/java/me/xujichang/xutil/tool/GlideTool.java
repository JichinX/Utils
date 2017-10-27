package me.xujichang.xutil.tool;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import me.xujichang.xutil.R;

/**
 * Created by xjc on 2017/6/10.
 */

public class GlideTool {
    public static RequestOptions getDefaultRequestOperations() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image_none)
                .error(R.drawable.ic_broken_image)
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        return options;
    }
}
