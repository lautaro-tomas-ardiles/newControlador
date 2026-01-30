# Guía de Uso: Compilación con Docker/Podman

Este documento contiene los comandos necesarios para construir la APK de **newControlador** utilizando el Dockerfile configurado.

## Paso 1: Construir (o re-construir) la imagen
Cada vez que realices cambios en el código fuente, ejecuta este comando para actualizar la imagen y compilar la nueva APK. Docker usará la caché para que sea más rápido.

```bash
docker build -t app-builder .
```

## Paso 2: Extraer la APK generada
Una vez que la construcción termine, debes copiar el archivo desde el contenedor a tu carpeta local.

### Opción A: Manual (Recomendada para Codespaces/Fedora)
```bash
# 1. Crear un contenedor temporal basado en la imagen
docker create --name extract app-builder

# 2. Copiar el archivo APK a tu carpeta actual
docker cp extract:/newControlador-debug.apk ./app-debug.apk

# 3. Eliminar el contenedor temporal
docker rm extract
```

### Opción B: Comando rápido (Equivalente)
Este comando crea el contenedor, extrae el archivo y lo borra automáticamente en un solo paso:
```bash
docker run --rm -v $(pwd):/export app-builder cp /newControlador-debug.apk /export/app-debug.apk
```

---

**Nota:** el dockerfile y los comandos me los dio la AI y lo mismo con este MD
