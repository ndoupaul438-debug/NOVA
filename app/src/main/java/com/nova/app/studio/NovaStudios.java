package com.nova.app.studio;

import java.util.Arrays;
import java.util.List;

public final class NovaStudios {

    private NovaStudios() {}

    public static List<String> coding() {
        return Arrays.asList(
                "Create Anything",
                "App Studio",
                "Game Studio",
                "Website Studio",
                "Web App Studio",
                "AI Studio",
                "API Studio",
                "Python Studio",
                "Code Editor",
                "File Explorer",
                "Terminal",
                "Preview",
                "Test",
                "Debug",
                "Security Scanner",
                "Build & Export"
        );
    }

    public static List<String> game() {
        return Arrays.asList(
                "2D","3D","Characters","Maps","Physics",
                "Animation","Audio","NPCs","Dialogue",
                "Save System","Multiplayer","Game Testing"
        );
    }

    public static List<String> website() {
        return Arrays.asList(
                "Visual Builder","HTML","CSS","JavaScript",
                "React","Components","Forms","Authentication",
                "Database","PWA","SEO","Export"
        );
    }

    public static List<String> create() {
        return Arrays.asList(
                "Design","Image Studio","Logo Studio",
                "Icon Studio","UI/UX","Documents","Presentations"
        );
    }
}
