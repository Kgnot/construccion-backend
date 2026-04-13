#!/bin/bash

# Configuración
TOPIC="sensor/blood"
USER_ID="doctor-juan"
DEVICE_ID="paciente-simulado-01"
TOTAL_MENSAJES=80 # Cambia este número si quieres más o menos

echo "🔥 Iniciando bombardeo de $TOTAL_MENSAJES muestras de sangre para $USER_ID..."

# Bucle principal
for i in $(seq 1 $TOTAL_MENSAJES); do

    # 1. Calcular la hora (Empieza a las 08:00:00 y suma 10 minutos por cada iteración)
    # (Requiere comando 'date' de GNU, estándar en Linux/WSL)
    TIMESTAMP=$(date -u -d "2024-10-25 08:00:00 UTC + $((i * 10)) minutes" +"%Y-%m-%dT%H:%M:%SZ")

    # 2. Generar datos médicos con ligeras variaciones aleatorias usando 'awk'
    # Rangos realistas aproximados
    HGB=$(awk 'BEGIN{srand(); printf "%.1f", 13.5 + rand() * 2.0}')      # Entre 13.5 y 15.5
    WBC=$(awk 'BEGIN{srand(); printf "%d", 6000 + rand() * 3000}')       # Entre 6000 y 9000
    PLT=$(awk 'BEGIN{srand(); printf "%d", 220000 + rand() * 60000}')    # Entre 220k y 280k
    IRON=$(awk 'BEGIN{srand(); printf "%.1f", 80.0 + rand() * 50.0}')    # Entre 80 y 130
    RBC=$(awk 'BEGIN{srand(); printf "%.1f", 4.0 + rand() * 1.5}')      # Entre 4.0 y 5.5

    # 3. Construir el JSON exacto (¡Ojo con las comillas dobles escapadas!)
    JSON="{\"deviceId\":\"$DEVICE_ID\",\"userId\":\"$USER_ID\",\"timestamp\":\"$TIMESTAMP\",\"hemoglobin\":$HGB,\"hemoglobinUnit\":\"G_DL\",\"whiteBloodCells\":$WBC,\"whiteBloodCellsUnit\":\"CELLS_MCL\",\"platelets\":$PLT,\"plateletsUnit\":\"CELLS_MCL\",\"iron\":$IRON,\"ironUnit\":\"MG_DL\",\"redBloodCells\":$RBC}"

    # 4. Enviar por Mosquitto
    mosquitto_pub -h localhost -p 1883 -t "$TOPIC" -q 1 -m "$JSON"

    # 5. Feedback en consola para que veas que está trabajando
    echo "[$i/$TOTAL_MENSAJES] Enviado a las $TIMESTAMP -> HGB: $HGB | WBC: $WBC | PLT: $PLT"

    # 6. Pausa de 200 milisegundos (0.2 segundos) para no saturar
    sleep 0.2
done

echo "✅ ¡Bombardeo completado! Revisa tu InfluxDB y tu consola de Spring Boot."