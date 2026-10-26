# Personal app: shrink unused code but keep names so reflection-based libraries keep working.
-dontobfuscate
-keepattributes Signature,InnerClasses,EnclosingMethod,*Annotation*,RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations,AnnotationDefault,SourceFile,LineNumberTable

# Our own code (Room entities, kotlinx-serialization models, navigation routes, workers, widget).
-keep class com.kintukka.hanger.** { *; }

# Anthropic SDK (Jackson + Kotlin reflection over its model classes).
-keep class com.anthropic.** { *; }
-keep class com.fasterxml.jackson.** { *; }
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }

# Optional platform integrations referenced by OkHttp / Jackson / annotations that don't exist on Android.
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn java.beans.**
-dontwarn javax.annotation.**
-dontwarn com.google.errorprone.annotations.**
-dontwarn org.slf4j.**

# victools json-schema generator (used by the Anthropic SDK for class-based schemas we do not use) references JDK-only reflection.
-dontwarn java.lang.reflect.AnnotatedType
-dontwarn java.lang.reflect.AnnotatedParameterizedType
