package philosophy.services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import philosophy.models.Page;
import philosophy.models.PageRepository;
import philosophy.models.Run;
import philosophy.models.RunRepository;

@Service
public class MainService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final RunRepository runRepository;
	private final PageRepository pageRepository;
	
	@Autowired
	public MainService(RunRepository runRepository, PageRepository pageRepository){
		this.runRepository = runRepository;
		this.pageRepository = pageRepository;
	}
	
	public Run processFindRequest(Run run) throws IOException {
	
		run.setStatus("started");
	    runRepository.save(run);
	    log.info(run.toString());
	
	    findPhilosophy(run);
	    
	    log.info(String.format("Number of pages: %d", pageRepository.findByRunIdOrderByCreatedDateAsc(run.getId()).size()));
	    for (Page page : pageRepository.findByRunIdOrderByCreatedDateAsc(run.getId()) ) {
			log.info(page.getUrlPath());
		}
	    log.info(String.format("Outcome: %s", run.getStatus()));

	    run.setPages(pageRepository.findByRunIdOrderByCreatedDateAsc(run.getId()));

	    return run;
	}
	
    public Run findPhilosophy(Run run) throws IOException {

	    String article = run.getStartingPath();
	    boolean searching = true;
	    ArrayList<String> linksList = new ArrayList<String>();
	    
	    while(searching) {
	    	
		    pageRepository.save(new Page(article, run));
		    
		    if (article.equalsIgnoreCase("https://en.wikipedia.org/wiki/philosophy")) {
	            int total = linksList.size();
	            run.setStatus("success");
	    	    runRepository.save(run);
	            searching = false;
	        } else if (linksList.size() > 100) {
	        	run.setStatus("failed_too_far");
	    	    runRepository.save(run);
	    	    searching = false;
	        } else {
	            String nextLink = grabNextLink(article);
	            if(!linksList.contains(nextLink)) {
	                linksList.add(nextLink);
	                article = nextLink;
	                // continues searching
	            } else {
	            	run.setStatus("failed_loop_detected");
	        	    pageRepository.save(new Page(nextLink, run));
	        	    runRepository.save(run);
	        	    searching = false;
	            }
	        }	    	
	    }
	    
	    return run;
    }

    public String grabNextLink(String article) throws IOException {

    	Document doc;
        URL url = HttpService.stringToURL(article);   // builds URL from string

        try {
            doc = Jsoup.parse(url, 10000);                // parses wiki page, times out after 10 seconds        	
        } catch(IOException e) {
        	log.error("JSoup exception");
        	throw new IOException(String.format("Parsing URL error: %s", e.getMessage()));
        }
        
        // remove HTML tags which we don't want to consider
        doc.select("table").remove();
        doc.select("small").remove();
        doc.select("sup").remove();
        doc.select("span").remove();
        doc.select("i").remove();
    	
        Elements paragraphs = doc.select("p,ul");       // select <p> or <ul> tags (the 'main text' of the wikipedia article)

        String match = "";

        for (Element p : paragraphs) {
           	String rawHtml = p.html();

           	// remove text in parenthesis
        	rawHtml = rawHtml.replaceAll("[^_]\\(.*?\\)","");
        	
            Pattern pattern = Pattern.compile("href=\"([^#].*?)\"");   // the html link we search for
            Matcher matcher = pattern.matcher(rawHtml);

            // if we find a match, get the group 
            if (matcher.find()){
              // first link
              match = matcher.group(1);
              
              // print the group out for verification
              log.info(String.format("MATCH: '%s'\n", match));
              break;
            }
        }
        
        return "https://en.wikipedia.org" + match;
    }

    public boolean isValidUrl(String input) {
        // filters out improperly formatted URLs
        return input.startsWith("http://en.wikipedia.org/wiki/");
    }

    public boolean isFirstRealLink(String link) {
        // filters out non-wiki links
        return (link.contains("wiki") && !link.contains("wiktionary"));
    }

}
