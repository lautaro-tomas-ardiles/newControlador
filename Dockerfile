# =========================================================================
# Etapa 1: Imagen base con las dependencias necesarias (JDK y Android SDK)
# =========================================================================
# Usamos una imagen que ya tiene JDK. La imagen de eclipse-temurin es una buena opción con OpenJDK.
FROM eclipse-temurin:21-jdk-jammy AS base

# Variables de entorno para la configuración de Android
ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV PATH $PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

# Instalar dependencias necesarias del sistema operativo y aceptar licencias de Android SDK
# La variable DEBIAN_FRONTEND evita que se pidan configuraciones interactivas durante la instalación.
RUN apt-get update && \
    export DEBIAN_FRONTEND=noninteractive && \
    apt-get install -y --no-install-recommends wget unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Descargar e instalar las herramientas de línea de comandos de Android SDK
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget --quiet --output-document=${ANDROID_SDK_ROOT}/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip -q ${ANDROID_SDK_ROOT}/cmdline-tools.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    rm ${ANDROID_SDK_ROOT}/cmdline-tools.zip

# Aceptar las licencias del SDK para evitar problemas durante la compilación
RUN yes | sdkmanager --licenses >/dev/null

# Instalar los paquetes del SDK necesarios.
# ¡IMPORTANTE! Ajusta 'android-34' a la versión de compileSdk de tu proyecto (la encuentras en build.gradle).
# Puedes añadir más paquetes si los necesitas (ej: 'build-tools;34.0.0').
RUN sdkmanager "platforms;android-34" "platform-tools"

# =========================================================================
# Etapa 2: Construcción de la aplicación usando Gradle
# =========================================================================
FROM base AS builder

# Etiqueta de autor
LABEL authors="lardiles"

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar solo los archivos de Gradle primero para aprovechar la caché de Docker.
# Si estos archivos no cambian, Docker no volverá a descargar las dependencias.
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle/ gradle/

# Otorgar permisos de ejecución al Gradle Wrapper
RUN chmod +x ./gradlew

# Descargar las dependencias de Gradle (modo offline para la siguiente etapa)
RUN ./gradlew --no-daemon dependencies || true

# Copiar el resto del código fuente de la aplicación
COPY app/ app/

# Construir la aplicación y generar el APK de release.
# --no-daemon se recomienda en entornos de CI/Docker.
RUN ./gradlew --no-daemon :app:assembleRelease

# =========================================================================
# Etapa 3: Imagen final (Opcional, para extraer el APK)
# =========================================================================
# Esta etapa crea una imagen mínima que solo contiene el APK generado.
FROM scratch AS exporter

# Copiar el APK desde la etapa de construcción a la imagen final.
COPY --from=builder /app/app/build/outputs/apk/release/app-release.apk /newControlador.apk

# ENTRYPOINT para que el contenedor no haga nada más que existir para copiar el archivo.
ENTRYPOINT ["echo", "APK listo en /newControlador.apk. Usa 'docker cp' para extraerlo."]
