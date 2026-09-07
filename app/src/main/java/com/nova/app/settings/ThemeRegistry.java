package com.nova.app.settings;

import java.util.Arrays;
import java.util.List;

public final class ThemeRegistry {

    private ThemeRegistry() {}

    public static List<String> all() {
        return Arrays.asList(
                NovaTheme.NOVA_LIGHT,
                NovaTheme.NOVA_DARK,
                NovaTheme.MIDNIGHT,
                NovaTheme.OCEAN,
                NovaTheme.AURORA,
                NovaTheme.EMERALD,
                NovaTheme.CRIMSON,
                NovaTheme.AMOLED
        );
    }
}
