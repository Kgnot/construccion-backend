#!/bin/bash

TOPIC="sensor/lipid"
USER_ID="doctor-juan"
DEVICE_ID="sim-lipid-01"
TOTAL_MENSAJES=400

echo "🔥 Iniciando bombardeo de LÍPIDOS ($TOTAL_MENSAJES muestras)..."

for i in $(seq 1 $TOTAL_MENSAJES); do

    # Suma 30 minutos por iteración
    TIMESTAMP=$(date -u -d "2024-10-25 09:00:00 UTC + $((i * 30)) minutes" +"%Y-%m-%dT%H:%M:%SZ")

    # Variaciones aleatorias realistas
    CHOL=$(awk 'BEGIN{srand(); printf "%.1f", 150 + rand() * 80}')      # 150 - 230 mg/dL
    TRI=$(awk 'BEGIN{srand(); printf "%.1f", 70 + rand() * 130}')       # 70 - 200 mg/dL

    JSON="{\"deviceId\":\"$DEVICE_ID\",\"userId\":\"$USER_ID\",\"timestamp\":\"$TIMESTAMP\",\"totalCholesterol\":$CHOL,\"totalCholesterolUnit\":\"MG_DL\",\"triglycerides\":$TRI,\"triglyceridesUnit\":\"MG_DL\"}"

    mosquitto_pub -h localhost -p 1883 -t "$TOPIC" -q 1 -m "$JSON"
    echo "[$i/$TOTAL_MENSAJES] Lípidos a las $TIMESTAMP -> Col. Total: $CHOL | Triglic: $TRI"

    sleep 0.2
done

echo "✅ ¡Lípidos terminado!"