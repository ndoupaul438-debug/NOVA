package com.nova.app.projects;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public File createProject(String name, String type) throws IOException {

        String safeName = name.trim()
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        if (safeName.length() == 0) {
            throw new IOException("Project name cannot be empty");
        }

        File typeFolder = new File(root, type.toLowerCase());
        if (!typeFolder.exists()) {
            typeFolder.mkdirs();
        }

        File project = new File(typeFolder, safeName);

        if (!project.exists()) {
            project.mkdirs();
        }

        new File(project, "src").mkdirs();
        new File(project, "assets").mkdirs();
        new File(project, "tests").mkdirs();
        new File(project, "builds").mkdirs();
        new File(project, ".nova/history").mkdirs();

        File json = new File(project, "project.json");

        FileWriter writer = new FileWriter(json);
        writer.write(
                "{\n" +
                "  \"name\": \"" + escape(safeName) + "\",\n" +
                "  \"type\": \"" + escape(type) + "\",\n" +
                "  \"version\": \"0.1.0\",\n" +
                "  \"created\": \"" + System.currentTimeMillis() + "\",\n" +
                "  \"updated\": \"" + System.currentTimeMillis() + "\",\n" +
                "  \"nova\": {\n" +
                "    \"projectBrain\": true,\n" +
                "    \"versionHistory\": true\n" +
                "  }\n" +
                "}\n"
        );
        writer.close();

        File readme = new File(project, "README.md");
        FileWriter readmeWriter = new FileWriter(readme);
        readmeWriter.write("# " + safeName + "\n\nCreated with NOVA.\n");
        readmeWriter.close();

        return project;
    }

    public List<File> getProjects() {

        List<File> result = new ArrayList<>();

        collect(root, result);

        return result;
    }

    private void collect(File directory, List<File> result) {

        File[] children = directory.listFiles();

        if (children == null) {
            return;
        }

        for (File file : children) {

            if (file.isDirectory()) {

                if (new File(file, "project.json").exists()) {
                    result.add(file);
                } else {
                    collect(file, result);
                }
            }
        }
    }

    public boolean deleteProject(File project) {

        if (project == null || !project.exists()) {
            return false;
        }

        return deleteRecursive(project);
    }

    private boolean deleteRecursive(File file) {

        if (file.isDirectory()) {

            File[] children = file.listFiles();

            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }

        return file.delete();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
