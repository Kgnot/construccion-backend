#!/bin/bash

TOPIC="sensor/metabolic"
USER_ID="doctor-juan"
DEVICE_ID="sim-metabolic-01"
TOTAL_MENSAJES=600

echo "🔥 Iniciando bombardeo METABÓLICO ($TOTAL_MENSAJES muestras)..."

for i in $(seq 1 $TOTAL_MENSAJES); do

    # Suma 15 minutos por iteración
    TIMESTAMP=$(date -u -d "2024-10-25 08:00:00 UTC + $((i * 15)) minutes" +"%Y-%m-%dT%H:%M:%SZ")

    # Variaciones aleatorias realistas
    GLUC=$(awk 'BEGIN{srand(); printf "%.1f", 80 + rand() * 40}')       # 80 - 120 mg/dL
    CREA=$(awk 'BEGIN{srand(); printf "%.2f", 0.7 + rand() * 0.8}')     # 0.7 - 1.5 mg/dL
    BUN=$(awk 'BEGIN{srand(); printf "%.1f", 8 + rand() * 16}')         # 8 - 24 mg/dL
    UA=$(awk 'BEGIN{srand(); printf "%.1f", 3.0 + rand() * 4.5}')       # 3.0 - 7.5 mg/dL
    PH=$(awk 'BEGIN{srand(); printf "%.3f", 7.35 + rand() * 0.1}')      # 7.35 - 7.45
    CALC=$(awk 'BEGIN{srand(); printf "%.1f", 8.5 + rand() * 1.7}')     # 8.5 - 10.2 mg/dL

    JSON="{\"deviceId\":\"$DEVICE_ID\",\"userId\":\"$USER_ID\",\"timestamp\":\"$TIMESTAMP\",\"glucose\":$GLUC,\"glucoseUnit\":\"MG_DL\",\"creatinine\":$CREA,\"bloodUreaNitrogen\":$BUN,\"uricAcid\":$UA,\"ph\":$PH,\"calcium\":$CALC}"

    mosquitto_pub -h localhost -p 1883 -t "$TOPIC" -q 1 -m "$JSON"
    echo "[$i/$TOTAL_MENSAJES] Metabólico a las $TIMESTAMP -> Glucosa: $GLUC | pH: $PH"

    sleep 0.2
done

echo "✅ ¡Metabólico terminado!"