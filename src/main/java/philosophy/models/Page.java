package philosophy.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class Page {
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @CreationTimestamp
    private Date createdDate;

    private String urlPath;
 
	@ManyToOne
	private Run run;
	
    protected Page() {}

    public Page(String urlPath, Run run) {
        this.urlPath= urlPath;
        this.run = run;
    }
	
	public Run getRun() {
		return run;
	}
	
	public void setRun(Run run) {
		this.run = run;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

    @Override
    public String toString() {
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = formatter.format(createdDate);

        return String.format(
                "Page[id=%d, urlPath='%s', date='%s']",
                id, urlPath, date);
    }

}
