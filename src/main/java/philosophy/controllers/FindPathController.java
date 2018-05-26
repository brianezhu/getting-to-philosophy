package philosophy.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import philosophy.Application;
import philosophy.models.Run;
import philosophy.services.MainService;
import philosophy.views.RunOutput;

@RestController
public class FindPathController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private MainService mainService;
	
	@Autowired
	public FindPathController(MainService mainService) {
		this.mainService = mainService;
	}
    
    @RequestMapping(value = "/philosophy", method = RequestMethod.POST)
    public RunOutput test(@RequestBody Run run) {
    	log.info("Handling /test request.");
    	
    	log.info(run.getStartingPath());

    	String errorState = "NO_ERROR";
    	String errorMessage = "";
    	Run finishedRun;
		try {
			if(run.getStartingPath() == null) {
				throw new IOException("You must provide a starting path");
			}
			finishedRun = mainService.processFindRequest(run);
		} catch (IOException e) {
			log.debug(e.getMessage());
			finishedRun = null;
			errorState = "ERROR";
			errorMessage = e.getMessage();
		}
		
		if(errorState == "NO_ERROR") {
			log.info(String.format(
					"Error state: %s, Starting path: %s, Run id: %d",
					errorState, finishedRun.getStartingPath(), finishedRun.getId()
					));
	        return new RunOutput("completed", finishedRun.getStatus(), finishedRun.getPageLinks());
		} else {
			log.info(String.format("Error state: %s, Starting path: %s", errorState, run.getStartingPath()));
			return new RunOutput("did_not_complete", errorMessage);
		}

    }

}
