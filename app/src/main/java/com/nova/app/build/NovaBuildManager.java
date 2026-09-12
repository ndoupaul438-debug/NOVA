package com.nova.app.build;

import android.content.Context;
import java.io.File;

public final class NovaBuildManager {

    private final File root;

    public NovaBuildManager(Context context) {
        Context appContext = context.getApplicationContext();

        File base = appContext.getExternalFilesDir(null);
        if (base == null) {
            base = appContext.getFilesDir();
        }

        root = new File(base, "NOVA/builds");
        root.mkdirs();
    }

    public File apkOutput() {
        return output("apk", "NOVA.apk");
    }

    public File aabOutput() {
        return output("aab", "NOVA.aab");
    }

    public File webOutput() {
        return output("web", "NOVA-web.zip");
    }

    public File zipOutput() {
        return output("zip", "NOVA-project.zip");
    }

    private File output(String directory, String filename) {
        File dir = new File(root, directory);
        dir.mkdirs();
        return new File(dir, filename);
    }
}
