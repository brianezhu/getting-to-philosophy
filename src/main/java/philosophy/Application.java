package philosophy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import philosophy.models.Page;
import philosophy.models.PageRepository;
import philosophy.models.Run;
import philosophy.models.RunRepository;
import philosophy.services.MainService;

import java.util.HashMap;

@EnableJpaRepositories(basePackageClasses=RunRepository.class)
@SpringBootApplication
public class Application {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Bean
	public CommandLineRunner init(RunRepository runRepository, PageRepository pageRepository) {
		return (evt) -> {
		    log.info("Setting up application.");
		    
		    log.info("Testing database.");
		    Run run1 = new Run("https://en.wikipedia.org/wiki/Arithmetic", "not_started");		
		    Page page1 = new Page("https://en.wikipedia.org/wiki/Arithmetic", run1);
		    Page page2 = new Page("https://en.wikipedia.org/wiki/Mathematics", run1);
		    Page page3 = new Page("https://en.wikipedia.org/wiki/Calculus", run1);
		    
		    runRepository.save(run1);
		    pageRepository.save(page1);
		    pageRepository.save(page2);
		    pageRepository.save(page3);

		    for (Run run : runRepository.findAll()) {
				log.info(run.toString());
			}
		    
		    for (Page page : pageRepository.findAll()) {
				log.info(page.toString());
			}

		    for (Page page : pageRepository.findByRunIdOrderByCreatedDateAsc(run1.getId()) ) {
				log.info(page.toString());
			}
		};
	}
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
