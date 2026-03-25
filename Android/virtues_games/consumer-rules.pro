# Consumer ProGuard rules for VirtuesDimens Games
# These rules are applied to consumers of the library

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep JNI methods
-keepclasseswithmembernames class * {
    @ag.virtues.dimens.games.Keep <methods>;
}

# Keep VirtuesDimens Games classes
-keep class ag.virtues.dimens.games.** { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep data classes
-keep class ag.virtues.dimens.games.GameScreenConfig { *; }
-keep class ag.virtues.dimens.games.GameVector2D { *; }
-keep class ag.virtues.dimens.games.GameRectangle { *; }

# Keep native library references
-keep class ag.virtues.dimens.games.VirtuesDimensGames {
    static {
        System.loadLibrary("appdimens_games");
    }
}
