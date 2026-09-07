package com.nova.app.workspace;

import android.content.Context;
import java.io.File;

public final class NovaWorkspace {

    private final File root;
    private final File projects;
    private final File builds;
    private final File backups;
    private final File cache;
    private final File temp;

    public NovaWorkspace(Context context) {
        root = new File(context.getFilesDir(), "nova-workspace");
        projects = new File(root, "projects");
        builds = new File(root, "builds");
        backups = new File(root, "backups");
        cache = new File(root, "cache");
        temp = new File(root, "temp");

        mkdir(root);
        mkdir(projects);
        mkdir(builds);
        mkdir(backups);
        mkdir(cache);
        mkdir(temp);

        for (String type : new String[]{
                "apps","games","websites","webapps",
                "ai","apis","scripts"
        }) {
            mkdir(new File(projects, type));
        }

        for (String type : new String[]{
                "apk","aab","web","zip"
        }) {
            mkdir(new File(builds, type));
        }
    }

    private void mkdir(File f) {
        if (!f.exists()) f.mkdirs();
    }

    public File getRoot() { return root; }
    public File getProjects() { return projects; }
    public File getBuilds() { return builds; }
    public File getBackups() { return backups; }
    public File getCache() { return cache; }
    public File getTemp() { return temp; }
}
