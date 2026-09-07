package com.nova.app.projects;

import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjectManager {

    private final File root;

    public ProjectManager(Context context) {
        root = new File(context.getFilesDir(), "nova/projects");
        if (!root.exists()) {
            root.mkdirs();
        }
    }

    public File getRoot() {
        return root;
    }

    public File createProject(String name, String type) {
        String safeName = sanitize(name);
        String safeType = sanitize(type);

        File typeDir = new File(root, safeType);
        if (!typeDir.exists()) {
            typeDir.mkdirs();
        }

        File project = new File(typeDir, safeName);

        if (!project.exists()) {
            project.mkdirs();
        }

        new File(project, "src").mkdirs();
        new File(project, "assets").mkdirs();
        new File(project, "tests").mkdirs();
        new File(project, "builds").mkdirs();
        new File(project, ".nova").mkdirs();
        new File(project, ".nova/history").mkdirs();
        new File(project, ".nova/backups").mkdirs();

        try {
            JSONObject info = new JSONObject();
            info.put("name", name);
            info.put("type", type);
            info.put("version", "1.0.0");
            info.put("created", now());
            info.put("updated", now());
            info.put("projectBrain", true);
            info.put("versionHistory", true);

            write(new File(project, "project.json"), info.toString(2));

            File readme = new File(project, "README.md");
            if (!readme.exists()) {
                write(readme,
                        "# " + name + "\n\n" +
                        "NOVA project\n\n" +
                        "Type: " + type + "\n\n" +
                        "Created with NOVA.\n");
            }

            File brain = new File(project, ".nova/project-brain.json");
            JSONObject projectBrain = new JSONObject();
            projectBrain.put("project", name);
            projectBrain.put("type", type);
            projectBrain.put("goal", "");
            projectBrain.put("architecture", "");
            projectBrain.put("dependencies", "");
            projectBrain.put("todo", "");
            projectBrain.put("notes", "");
            projectBrain.put("lastError", "");
            projectBrain.put("updated", now());

            write(brain, projectBrain.toString(2));

        } catch (Exception ignored) {
        }

        return project;
    }

    public List<File> getProjects() {
        List<File> result = new ArrayList<>();

        if (!root.exists()) {
            return result;
        }

        File[] types = root.listFiles();
        if (types == null) {
            return result;
        }

        for (File type : types) {
            if (!type.isDirectory()) continue;

            File[] projects = type.listFiles();
            if (projects == null) continue;

            for (File project : projects) {
                if (project.isDirectory()) {
                    result.add(project);
                }
            }
        }

        Collections.sort(result, new Comparator<File>() {
            @Override
            public int compare(File a, File b) {
                return Long.compare(
                        b.lastModified(),
                        a.lastModified()
                );
            }
        });

        return result;
    }

    public JSONObject readInfo(File project) {
        try {
            File file = new File(project, "project.json");

            if (!file.exists()) {
                return new JSONObject();
            }

            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader(file));

            StringBuilder data = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            reader.close();

            return new JSONObject(data.toString());

        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public boolean deleteProject(File project) {
        return deleteRecursive(project);
    }

    public boolean renameProject(File project, String newName) {
        if (project == null || !project.exists()) {
            return false;
        }

        File parent = project.getParentFile();
        File destination =
                new File(parent, sanitize(newName));

        if (destination.exists()) {
            return false;
        }

        boolean renamed = project.renameTo(destination);

        if (renamed) {
            try {
                JSONObject info = readInfo(destination);
                info.put("name", newName);
                info.put("updated", now());
                write(new File(destination, "project.json"),
                        info.toString(2));
            } catch (Exception ignored) {
            }
        }

        return renamed;
    }

    private boolean deleteRecursive(File file) {
        if (file == null || !file.exists()) {
            return true;
        }

        File[] children = file.listFiles();

        if (children != null) {
            for (File child : children) {
                deleteRecursive(child);
            }
        }

        return file.delete();
    }

    private String sanitize(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "Untitled";
        }

        return value.trim()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private String now() {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.US
        ).format(new Date());
    }

    private void write(File file, String data) throws Exception {
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.close();
    }
}
