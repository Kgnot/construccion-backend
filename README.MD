# Medibug

## InfluxDB

Debemos crear al base de datos
***

```sql
CREATE
DATABASE medibug_telemetry;
```

## Mosquitto

Los topics que se van a usar son:

- sensor/blood
- sensor/electrolyte
- sensor/metabolic
- sensor/lipid

Las pruebas: 
```bash
mosquitto_pub -h localhost -p 1883 -t "sensor/metabolic" -q 1 -m '{"deviceId":"sim-001","userId":"doctor-juan","timestamp":"2024-10-25T15:31:00Z","glucose":95.5,"glucoseUnit":"MG_DL","creatinine":1.1,"bloodUreaNitrogen":14.0,"uricAcid":5.2,"ph":7.4,"calcium":9.2}'
mosquitto_pub -h localhost -p 1883 -t "sensor/lipid" -q 1 -m '{"deviceId":"sim-002","userId":"doctor-juan","timestamp":"2024-10-25T15:31:05Z","totalCholesterol":190.0,"totalCholesterolUnit":"MG_DL","triglycerides":130.5,"triglyceridesUnit":"MG_DL"}'
mosquitto_pub -h localhost -p 1883 -t "sensor/electrolyte" -q 1 -m '{"deviceId":"sim-003","userId":"doctor-juan","timestamp":"2024-10-25T15:31:10Z","sodium":140.5,"sodiumUnit":"MEQ_L","potassium":4.2,"potassiumUnit":"MEQ_L"}'
mosquitto_pub -h localhost -p 1883 -t "sensor/blood" -q 1 -m '{"deviceId":"sim-004","userId":"doctor-juan","timestamp":"2024-10-25T15:31:15Z","hemoglobin":14.1,"hemoglobinUnit":"G_DL","whiteBloodCells":7200,"whiteBloodCellsUnit":"CELLS_MCL","platelets":250000,"plateletsUnit":"CELLS_MCL","iron":105.2,"ironUnit":"MG_DL","redBloodCells":4.6}'
```

## Grafana: 

Aqui usaremos grafana, nada raro por el estilo.
Nos conectamos a influxDB 

Esto es para un plugin
```grafana
sudo grafana-cli plugins install grafana-mqtt-datasource

# Reiniciar Grafana
sudo systemctl restart grafana-server
```