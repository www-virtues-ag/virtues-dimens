# ProGuard rules for VirtuesDimens Games library
# These rules are applied when building the library

# Keep all public classes and methods
-keep public class ag.virtues.dimens.games.** {
    public *;
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep JNI methods
-keepclasseswithmembernames class * {
    @ag.virtues.dimens.games.Keep <methods>;
}

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

# Keep companion objects
-keepclassmembers class ag.virtues.dimens.games.VirtuesDimensGames$Companion {
    public static ** getInstance();
}

# Keep singleton pattern
-keep class ag.virtues.dimens.games.VirtuesDimensGames {
    private static volatile ag.virtues.dimens.games.VirtuesDimensGames INSTANCE;
}

# Keep reflection-accessed members
-keepclassmembers class ag.virtues.dimens.games.** {
    @androidx.annotation.Keep *;
}

# Keep serialization
-keepclassmembers class ag.virtues.dimens.games.** implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable

# Keep generic signatures
-keepattributes Signature

# Keep exceptions
-keepattributes Exceptions
