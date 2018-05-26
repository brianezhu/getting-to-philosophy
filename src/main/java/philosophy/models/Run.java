package philosophy.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class Run {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    private String startingPath;
    
	@CreationTimestamp
    private Date createdDate;
	
	private String status;
	
	@OneToMany
	private Collection<Page> pages;

	protected Run() {}

    public Run(String startingPath, String status) {
        this.startingPath = startingPath;
        this.status = status;
    }

	public Long getId() {
		return id;
	}
	
    public String getStartingPath() {
		return startingPath;
	}

	public void setStartingPath(String startingPath) {
		this.startingPath = startingPath;
	}

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Collection<Page> getPages() {
		return pages;
	}
	
	public ArrayList<String> getPageLinks(){
		ArrayList<String> links = new ArrayList<String>();
		for(Page page : pages) {
			links.add(page.getUrlPath());
		}
		return links;
	}

	public void setPages(Collection<Page> pages) {
		this.pages = pages;
	}

    @Override
    public String toString() {
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = formatter.format(createdDate);

        return String.format(
                "Run[id=%d, startingPath='%s', status='%s', date='%s']",
                id, startingPath, status, date);
    }
}