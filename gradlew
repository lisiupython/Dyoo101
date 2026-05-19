#!/bin/sh
# Gradle wrapper script for CI

GRADLE_VERSION=8.5
GRADLE_HOME="$HOME/.gradle/wrapper/dists/gradle-${GRADLE_VERSION}-bin"
GRADLE_BIN="$GRADLE_HOME/gradle-${GRADLE_VERSION}/bin/gradle"

if [ ! -f "$GRADLE_BIN" ]; then
    mkdir -p "$GRADLE_HOME"
    cd "$GRADLE_HOME"
    echo "Downloading Gradle $GRADLE_VERSION..."
    curl -sL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o gradle.zip
    unzip -q gradle.zip
    rm gradle.zip
    cd - > /dev/null
fi

exec "$GRADLE_BIN" "$@"
