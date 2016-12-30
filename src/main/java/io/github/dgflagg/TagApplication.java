package io.github.dgflagg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by dgflagg on 11/15/16.
 */
@Slf4j
@SpringBootApplication
public class TagApplication {

    public static void main(String[] args) {

        log.info("starting TagApplication");
        SpringApplication.run(TagApplication.class, args);

    }

}
