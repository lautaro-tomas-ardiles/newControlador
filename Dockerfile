# =========================================================================
# Etapa 1: Imagen base con JDK y Android SDK
# =========================================================================
FROM eclipse-temurin:21-jdk-jammy AS base

ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV PATH $PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

RUN apt-get update && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt-get  install -y --no-install-recommends wget unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Instalar Command Line Tools
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget --quiet --output-document=${ANDROID_SDK_ROOT}/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip -q ${ANDROID_SDK_ROOT}/cmdline-tools.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    rm ${ANDROID_SDK_ROOT}/cmdline-tools.zip

# Aceptar licencias
RUN yes | sdkmanager --licenses >/dev/null

# Instalar SDK 35 (según tu build.gradle.kts)
RUN sdkmanager "platforms;android-35" "platform-tools" "build-tools;35.0.0"

# =========================================================================
# Etapa 2: Construcción (Builder)
# =========================================================================
FROM base AS builder
WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY build.gradle.kts settings.gradle.kts gradlew gradle.properties ./
COPY gradle/ gradle/

RUN chmod +x ./gradlew

# Descargar dependencias para aprovechar la caché de capas
RUN ./gradlew --no-daemon dependencies --stacktrace || true

# Copiar el código fuente
COPY app/ app/

# Compilar la APK (Debug es más rápido y no requiere firmas)
# Si necesitas Release, cambia a :app:assembleRelease
RUN ./gradlew --no-daemon :app:assembleDebug

# =========================================================================
# Etapa 3: Exportador
# =========================================================================
FROM alpine:latest AS exporter

# Copiamos la APK generada a la raíz de esta imagen limpia
COPY --from=builder /app/app/build/outputs/apk/debug/app-debug.apk /newControlador-debug.apk

CMD ["echo", "APK generada exitosamente. Puedes extraerla usando: docker cp <container_id>:/newControlador-debug.apk ./"]