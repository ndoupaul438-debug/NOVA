package com.nova.app.tools;

import java.util.Arrays;
import java.util.List;

public final class NovaTools {

    private NovaTools() {}

    public static List<String> all() {
        return Arrays.asList(
                "JSON",
                "CSV",
                "PDF",
                "Image Tools",
                "Data Analysis",
                "Converters",
                "Utilities",
                "File Tools"
        );
    }
}
