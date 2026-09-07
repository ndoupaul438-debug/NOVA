package com.nova.app.ai;

import android.content.Context;

import com.nova.app.workspace.NovaWorkspace;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public final class MeganProjectGenerator {

    private final Context context;

    public MeganProjectGenerator(Context context) {
        this.context = context.getApplicationContext();
    }

    public JSONObject generate(String request) {
        JSONObject result = new JSONObject();

        try {
            String clean = request == null ? "" : request.trim();

            String type = detectType(clean);
            String name = detectName(clean);

            File workspace = new NovaWorkspace(context).getRoot();

            File projectsRoot = new File(
                    workspace,
                    "projects"
            );

            File typeRoot = new File(
                    projectsRoot,
                    folderFor(type)
            );

            File project = new File(
                    typeRoot,
                    safeName(name)
            );

            if (!project.exists() && !project.mkdirs()) {
                throw new Exception("Could not create project directory");
            }

            int files = createFiles(project, type, name);

            result.put("success", true);
            result.put("assistant", "M3GAN");
            result.put("action", "project_created");
            result.put("type", type);
            result.put("name", name);
            result.put("files_created", files);
            result.put("path", project.getAbsolutePath());
            result.put(
                    "message",
                    "M3GAN created " + name +
                    " as a " + type + " project."
            );

        } catch (Exception e) {
            try {
                result.put("success", false);
                result.put(
                        "error",
                        e.getMessage() == null
                                ? "Project generation failed"
                                : e.getMessage()
                );
            } catch (Exception ignored) {
            }
        }

        return result;
    }

    private String detectType(String request) {
        String r = request.toLowerCase(Locale.US);

        if (contains(r, "android", "apk", "mobile app", "android app")) {
            return "Android App";
        }

        if (contains(r, "website", "web site")) {
            return "Website";
        }

        if (contains(r, "web app", "webapp")) {
            return "Web App";
        }

        if (contains(r, "python")) {
            return "Python Project";
        }

        if (contains(r, "java")) {
            return "Java Project";
        }

        if (contains(r, "game", "gaming")) {
            return "Game Project";
        }

        if (contains(r, "ai project", "artificial intelligence")) {
            return "AI Project";
        }

        if (contains(r, "api", "backend")) {
            return "API Project";
        }

        if (contains(r, "script", "automation")) {
            return "Script Project";
        }

        return "Website";
    }

    private String detectName(String request) {
        String lower = request.toLowerCase(Locale.US);

        String[] markers = {
                "called ",
                "named ",
                "name it ",
                "name ",
                "project "
        };

        for (String marker : markers) {
            int index = lower.indexOf(marker);

            if (index >= 0) {
                String value =
                        request.substring(
                                index + marker.length()
                        ).trim();

                value = value
                        .replace("\"", "")
                        .replace("'", "")
                        .replace(".", "")
                        .trim();

                if (!value.isEmpty()) {
                    return cleanName(value);
                }
            }
        }

        return "M3GAN Project";
    }

    private String cleanName(String value) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);

            if (Character.isLetterOrDigit(c) ||
                    c == ' ' ||
                    c == '_' ||
                    c == '-') {
                out.append(c);
            }
        }

        String result = out.toString()
                .trim()
                .replace(" ", "_");

        return result.isEmpty()
                ? "M3GAN_Project"
                : result;
    }

    private String safeName(String name) {
        return cleanName(name)
                .replace("/", "_")
                .replace("\\", "_")
                .replace("..", "_");
    }

    private String folderFor(String type) {
        if ("Android App".equals(type)) return "apps";
        if ("Game Project".equals(type)) return "games";
        if ("Website".equals(type)) return "websites";
        if ("Web App".equals(type)) return "webapps";
        if ("AI Project".equals(type)) return "ai";
        if ("API Project".equals(type)) return "apis";
        if ("Script Project".equals(type)) return "scripts";
        return "projects";
    }

    private boolean contains(
            String value,
            String... terms
    ) {
        for (String term : terms) {
            if (value.contains(term)) {
                return true;
            }
        }

        return false;
    }

    private int createFiles(
            File project,
            String type,
            String name
    ) throws Exception {

        int count = 0;

        if ("Website".equals(type)) {

            write(
                    project,
                    "index.html",
                    websiteHtml(name)
            );

            write(
                    project,
                    "styles.css",
                    websiteCss()
            );

            write(
                    project,
                    "script.js",
                    websiteJs()
            );

            count = 3;

        } else if ("Web App".equals(type)) {

            write(
                    project,
                    "index.html",
                    webAppHtml(name)
            );

            write(
                    project,
                    "app.js",
                    webAppJs()
            );

            write(
                    project,
                    "styles.css",
                    websiteCss()
            );

            count = 3;

        } else if ("Python Project".equals(type)) {

            write(
                    project,
                    "main.py",
                    pythonMain(name)
            );

            write(
                    project,
                    "README.md",
                    readme(name, type)
            );

            count = 2;

        } else if ("Java Project".equals(type)) {

            write(
                    project,
                    "Main.java",
                    javaMain(name)
            );

            write(
                    project,
                    "README.md",
                    readme(name, type)
            );

            count = 2;

        } else if ("Game Project".equals(type)) {

            write(
                    project,
                    "index.html",
                    gameHtml(name)
            );

            write(
                    project,
                    "game.js",
                    gameJs()
            );

            write(
                    project,
                    "styles.css",
                    websiteCss()
            );

            count = 3;

        } else if ("AI Project".equals(type)) {

            write(
                    project,
                    "main.py",
                    aiMain(name)
            );

            write(
                    project,
                    "README.md",
                    readme(name, type)
            );

            count = 2;

        } else if ("API Project".equals(type)) {

            write(
                    project,
                    "server.py",
                    apiMain(name)
            );

            write(
                    project,
                    "README.md",
                    readme(name, type)
            );

            count = 2;

        } else if ("Script Project".equals(type)) {

            write(
                    project,
                    "main.py",
                    scriptMain(name)
            );

            write(
                    project,
                    "README.md",
                    readme(name, type)
            );

            count = 2;

        } else {

            File src = new File(project, "app/src/main/java");
            src.mkdirs();

            write(
                    project,
                    "README.md",
                    readme(name, type)
            );

            write(
                    project,
                    "settings.gradle",
                    "rootProject.name = '" +
                            safeName(name) +
                            "'\n"
            );

            write(
                    project,
                    "build.gradle",
                    androidBuild()
            );

            write(
                    project,
                    "app/src/main/AndroidManifest.xml",
                    androidManifest()
            );

            write(
                    project,
                    "app/src/main/java/MainActivity.java",
                    androidMain(name)
            );

            count = 5;
        }

        return count;
    }

    private void write(
            File project,
            String path,
            String content
    ) throws Exception {

        File file = new File(project, path);

        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        FileOutputStream output =
                new FileOutputStream(file);

        output.write(
                content.getBytes(
                        StandardCharsets.UTF_8
                )
        );

        output.close();
    }

    private String websiteHtml(String name) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" " +
                "content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>" + name + "</title>\n" +
                "  <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "  <main class=\"hero\">\n" +
                "    <h1>" + name + "</h1>\n" +
                "    <p>Created by NOVA and M3GAN.</p>\n" +
                "    <button onclick=\"hello()\">Get Started</button>\n" +
                "  </main>\n" +
                "  <script src=\"script.js\"></script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    private String webAppHtml(String name) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>" + name + "</title>\n" +
                "  <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div id=\"app\">\n" +
                "    <h1>" + name + "</h1>\n" +
                "    <p>Web application generated by NOVA.</p>\n" +
                "  </div>\n" +
                "  <script src=\"app.js\"></script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    private String websiteCss() {
        return "body {\n" +
                "  margin: 0;\n" +
                "  min-height: 100vh;\n" +
                "  font-family: Arial, sans-serif;\n" +
                "  background: #f5f7fb;\n" +
                "  color: #0f172a;\n" +
                "}\n" +
                ".hero {\n" +
                "  min-height: 100vh;\n" +
                "  display: grid;\n" +
                "  place-items: center;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "button {\n" +
                "  padding: 12px 20px;\n" +
                "  border: 0;\n" +
                "  border-radius: 10px;\n" +
                "  cursor: pointer;\n" +
                "}\n";
    }

    private String websiteJs() {
        return "function hello() {\n" +
                "  alert('Created with NOVA + M3GAN');\n" +
                "}\n";
    }

    private String webAppJs() {
        return "const app = document.getElementById('app');\n" +
                "console.log('NOVA web app ready', app);\n";
    }

    private String pythonMain(String name) {
        return "# " + name + "\n\n" +
                "def main():\n" +
                "    print(\"Hello from " + name + "\")\n\n" +
                "if __name__ == \"__main__\":\n" +
                "    main()\n";
    }

    private String javaMain(String name) {
        return "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"Hello from " +
                name + "\");\n" +
                "    }\n" +
                "}\n";
    }

    private String gameHtml(String name) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <title>" + name + "</title>\n" +
                "  <link rel=\"stylesheet\" href=\"styles.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "  <canvas id=\"game\" width=\"800\" height=\"500\"></canvas>\n" +
                "  <script src=\"game.js\"></script>\n" +
                "</body>\n" +
                "</html>\n";
    }

    private String gameJs() {
        return "const canvas = document.getElementById('game');\n" +
                "const ctx = canvas.getContext('2d');\n\n" +
                "function loop() {\n" +
                "  ctx.clearRect(0, 0, canvas.width, canvas.height);\n" +
                "  ctx.fillText('NOVA GAME PROJECT', 40, 60);\n" +
                "  requestAnimationFrame(loop);\n" +
                "}\n\n" +
                "loop();\n";
    }

    private String aiMain(String name) {
        return "# " + name + "\n" +
                "# AI project generated by NOVA/M3GAN\n\n" +
                "def respond(prompt):\n" +
                "    return f\"AI received: {prompt}\"\n\n" +
                "if __name__ == \"__main__\":\n" +
                "    print(respond(\"Hello M3GAN\"))\n";
    }

    private String apiMain(String name) {
        return "# " + name + " API\n" +
                "# Starter API service\n\n" +
                "from http.server import BaseHTTPRequestHandler, HTTPServer\n\n" +
                "class Handler(BaseHTTPRequestHandler):\n" +
                "    def do_GET(self):\n" +
                "        self.send_response(200)\n" +
                "        self.send_header(\"Content-Type\", \"application/json\")\n" +
                "        self.end_headers()\n" +
                "        self.wfile.write(b'{\"status\":\"ok\"}')\n\n" +
                "HTTPServer((\"0.0.0.0\", 8080), Handler).serve_forever()\n";
    }

    private String scriptMain(String name) {
        return "#!/usr/bin/env python3\n" +
                "\"\"\"Automation script generated by NOVA.\"\"\"\n\n" +
                "print(\"Running " + name + "\")\n";
    }

    private String readme(String name, String type) {
        return "# " + name + "\n\n" +
                "Generated by NOVA M3GAN.\n\n" +
                "Project type: " + type + "\n\n" +
                "This project is stored inside the NOVA workspace.\n";
    }

    private String androidBuild() {
        return "plugins {\n" +
                "    id 'com.android.application' version '8.7.3' apply false\n" +
                "}\n";
    }

    private String androidManifest() {
        return "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <application android:theme=\"@android:style/Theme.Material.Light.NoActionBar\">\n" +
                "        <activity android:name=\".MainActivity\" android:exported=\"true\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"android.intent.action.MAIN\" />\n" +
                "                <category android:name=\"android.intent.category.LAUNCHER\" />\n" +
                "            </intent-filter>\n" +
                "        </activity>\n" +
                "    </application>\n" +
                "</manifest>\n";
    }

    private String androidMain(String name) {
        return "package generated.nova;\n\n" +
                "import android.app.Activity;\n" +
                "import android.os.Bundle;\n" +
                "import android.widget.TextView;\n\n" +
                "public class MainActivity extends Activity {\n" +
                "    @Override\n" +
                "    protected void onCreate(Bundle state) {\n" +
                "        super.onCreate(state);\n" +
                "        TextView view = new TextView(this);\n" +
                "        view.setText(\"" + name + " — generated by NOVA\");\n" +
                "        setContentView(view);\n" +
                "    }\n" +
                "}\n";
    }
}
