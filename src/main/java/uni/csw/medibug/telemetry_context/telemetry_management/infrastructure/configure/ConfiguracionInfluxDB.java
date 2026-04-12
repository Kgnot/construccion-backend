package uni.csw.medibug.telemetry_context.telemetry_management.infrastructure.configure;

import com.influxdb.v3.client.InfluxDBClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionInfluxDB {

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.token}")
    private String token;

    @Value("${influxdb.database}")
    private String database;

    @Bean
    public InfluxDBClient clienteInfluxDB() {
        return InfluxDBClient.getInstance(
                url,
                token.toCharArray(),
                database
        );
    }

}