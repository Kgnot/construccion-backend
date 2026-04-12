package uni.csw.medibug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
public class MedibugApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedibugApplication.class, args);
    }

}
