#!/bin/bash
set -e

# JavaFX-Umgebung einrichten
JAVAFX_HOME="$HOME/javafx-sdk-25.0.4"

# JavaFX-Module
JAVAFX_MODULES="--module-path $JAVAFX_HOME/lib --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web"

# Hauptklasse
MAIN_CLASS="gui.ChemierechnerApp"

# In das Projektverzeichnis wechseln (Ordner dieses Skripts)
cd "$(dirname "$0")/Chemierechner"

# Kompilieren und Ressourcen (CSS, Bilder, ...) nach bin kopieren
echo "Kompiliere Chemierechner..."
rm -rf bin
javac $JAVAFX_MODULES -d bin $(find src -name '*.java')
(cd src && find . -type f ! -name '*.java' -exec cp --parents {} ../bin \;)

# Programm starten
echo "Starte Chemierechner mit JavaFX..."
java $JAVAFX_MODULES --enable-native-access=javafx.graphics,javafx.media,javafx.web -cp bin $MAIN_CLASS
