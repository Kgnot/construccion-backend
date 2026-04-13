#!/bin/bash

TOPIC="sensor/electrolyte"
USER_ID="doctor-juan"
DEVICE_ID="sim-electrolyte-01"
TOTAL_MENSAJES=1000

echo "🔥 Iniciando bombardeo de ELECTROLITOS ($TOTAL_MENSAJES muestras)..."

for i in $(seq 1 $TOTAL_MENSAJES); do

    # Suma 5 minutos por iteración (muy frecuente)
    TIMESTAMP=$(date -u -d "2024-10-25 10:00:00 UTC + $((i * 5)) minutes" +"%Y-%m-%dT%H:%M:%SZ")

    # Variaciones aleatorias realistas (rangos críticos estrechos)
    SOD=$(awk 'BEGIN{srand(); printf "%.1f", 136 + rand() * 8}')        # 136 - 144 mEq/L
    POT=$(awk 'BEGIN{srand(); printf "%.1f", 3.5 + rand() * 1.5}')      # 3.5 - 5.0 mEq/L

    JSON="{\"deviceId\":\"$DEVICE_ID\",\"userId\":\"$USER_ID\",\"timestamp\":\"$TIMESTAMP\",\"sodium\":$SOD,\"sodiumUnit\":\"MEQ_L\",\"potassium\":$POT,\"potassiumUnit\":\"MEQ_L\"}"

    mosquitto_pub -h localhost -p 1883 -t "$TOPIC" -q 1 -m "$JSON"
    echo "[$i/$TOTAL_MENSAJES] Electrolitos a las $TIMESTAMP -> Sodio: $SOD | Potasio: $POT"

    sleep 0.2
done

echo "✅ ¡Electrolitos terminado!"