package philosophy.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpService {
	
	private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    /**
     * Reads from 'reader' by 'blockSize' until end-of-stream, and returns its complete contents.
     */
    private static String readAll(InputStreamReader reader, int blockSize) throws IOException {
        final char buffer[] = new char[blockSize];
        StringBuilder builder = new StringBuilder();
        while (true) {
            final int readSize = reader.read(buffer);
            if (readSize >= 0)
                builder.append(buffer, 0, readSize);
            else
                break;
        }
        return builder.toString();
    }

    /**
     * Returns from 'reader' until end-of-stream, and returns its complete contents.
     */
    private static String readAll(InputStreamReader reader) throws IOException {
        return readAll(reader, 1024 * 1024);
    }

    /**
     * Interprets a string as a URL.  If the string isn't a valid URL, prints an error message and returns null.
     */
    public static URL stringToURL(String string) throws MalformedURLException {
        try {
            return new URL(string);
        } catch (MalformedURLException exception) {
        	log.error("invalid URL: " + string + ": " + exception);
            throw new MalformedURLException("Invalid URL");
        }
    }

    /**
     * Retrieves the body of a URL.
     *
     * Opens a connection to the URL, makes a request, and retrieves the response.  Returns the body.  If the
     * URL cannot be opened or the response cannot be read, prints an error message and returns null.
     */
    public static String get(URL url) throws IOException {
        try {
            final URLConnection connection = url.openConnection();
            final InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            try {
                return readAll(reader);
            } finally {
                reader.close();
            }
        } catch (IOException e) {
        	log.error("can't open URL: " + url + ": " + e);
            throw new IOException(String.format("BAD URL: %s", e.getMessage()));
        }
    }
}