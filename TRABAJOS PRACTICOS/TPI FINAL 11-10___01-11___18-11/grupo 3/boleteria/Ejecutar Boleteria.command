#!/bin/bash
# Abre la app Swing de la Boletería (TPO POO - Grupo 3). Doble click para ejecutar.
cd "$(dirname "$0")"
if ! command -v java >/dev/null 2>&1; then
  echo "No se encontró Java. Instalá Java 17 o superior (por ejemplo, Temurin 17 de adoptium.net) y volvé a abrir este archivo."
  read -p "Enter para cerrar..."
  exit 1
fi
java -jar Boleteria-TPO.jar
