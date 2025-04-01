package chris.dicom;

import org.dcm4che3.tool.storescp.StoreSCP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class DicomServerApplication {
	private static final String[] REQUEST_METHOD_SUPPORTED = { "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD" };
	public static void main(String[] args) {
		
		//String[] test= {"storescp", "-b", "IOC@localhost:3006","--directory","E:\\IOC Folder","status"} ;
		//StoreSCP.main(test);
		SpringApplication.run(DicomServerApplication.class, args);
	}
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("").allowedHeaders("*").allowedMethods(REQUEST_METHOD_SUPPORTED);
            }
        };
    }

}
