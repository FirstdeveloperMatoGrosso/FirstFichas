# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\fsilva\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Regras de ofuscação e proteção do código

# Manter classes principais do app
-keep class br.com.firstingressos.dashboard.** { *; }

# Ofuscar todo o resto
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Remover logs em produção
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Proteção contra engenharia reversa
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
-repackageclasses 'br.com.firstingressos.obf'

# Proteção contra debugger
-dontskipnonpubliclibraryclassmembers
-allowaccessmodification
-overloadaggressively

# Manter anotações importantes
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# Manter classes do SDK Stone
-keep class br.com.stone.** { *; }
-dontwarn br.com.stone.**

# Manter classes do Supabase
-keep class io.supabase.** { *; }
-dontwarn io.supabase.**

# Proteção de recursos
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Proteção de APIs nativas
-keepclasseswithmembernames class * {
    native <methods>;
}

# Proteção de serializáveis
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Proteção de Parcelables
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Proteção de enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Proteção de classes do Android
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class * extends android.app.Fragment

# Proteção de classes do AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class com.google.android.material.** { *; }

# Proteção de classes do Kotlin
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Proteção contra injeção de código
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Proteção de bibliotecas de criptografia
-keep class javax.crypto.** { *; }
-keep class javax.security.** { *; }
-keep class java.security.** { *; }

# Proteção de classes de rede
-keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
    private final javax.net.ssl.SSLSocketFactory delegate;
}

# Proteção contra análise de stack trace
-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

# Proteção de configurações
-keepclassmembers class **.BuildConfig {
    public static <fields>;
}
