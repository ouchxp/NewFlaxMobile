package flax.core;

import android.app.Application;

public class FlaxApplication extends Application{
	private static FlaxApplication instance;

    public static FlaxApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
