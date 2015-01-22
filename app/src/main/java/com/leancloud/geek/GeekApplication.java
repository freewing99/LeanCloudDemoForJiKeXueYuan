package com.leancloud.geek;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;

/**
 * Created by wujun on 1/17/15.
 */
public class GeekApplication extends Application {

    @Override
    public void onCreate() {
        AVOSCloud.initialize(this, "6ty078bfwqcbqkzevxqy9yod9sec0u50z6pmg6dqdh0q7upv", "5cloagvrox0qy53gqdwcp2g13li1szv9aczgys20i104vlw5");
        super.onCreate();
    }
}
