# Personal app: shrink unused code but keep names so reflection-based libraries keep working.
-dontobfuscate
-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault,SourceFile,LineNumberTable

# Our own code (Room entities, kotlinx-serialization models, navigation routes, workers, widget).
-keep class com.kintukka.hanger.** { *; }

# Optional platform integrations referenced by OkHttp (via Coil) and annotations that don't exist on Android.
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn java.beans.**
-dontwarn javax.annotation.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn org.slf4j.**
