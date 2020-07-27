import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-03-17 13:18
 */
//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.changing.redis")
public class DemoRedisApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoRedisApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DemoRedisApplication.class);
    }

}