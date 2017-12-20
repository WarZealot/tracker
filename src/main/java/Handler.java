import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Handler implements RequestStreamHandler {

    private static final String RDS_HOSTNAME = "trackerdb.coudjpfzgtpn.eu-central-1.rds.amazonaws.com";
    private static final String RDS_PORT = "5432";
    private static final String RDS_DB_NAME = "trackerDB";
    private static final String RDS_USERNAME = "ktkachuk";
    private static final String RDS_PASSWORD = "monster2";

    public void handleRequest(final InputStream inputStream, final OutputStream outputStream, final Context context) {
        handleRequest("", context);
    }

    public String handleRequest(String input, Context context) {
        LambdaLogger logger = context.getLogger();
        try {
            logger.log("\nCalling Class.forName");
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://" + RDS_HOSTNAME + ":" + RDS_PORT + "/" + RDS_DB_NAME;
            logger.log("\nEstablishing connection: " + url);
            DriverManager.setLoginTimeout(5);
            Connection conn = DriverManager.getConnection(url, RDS_USERNAME, RDS_PASSWORD);

            logger.log("\nTest Started");
            Statement stmt = conn.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT NOW()");

            String currentTime = "Could not get from DB.";
            if (resultSet.next()) {
                currentTime = resultSet.getObject(1).toString();
            }

            logger.log("\nSuccessfully executed query.  Result: " + currentTime);
        } catch (Exception e) {
            logger.log("\nReceived exception: " + e.getMessage());
            e.printStackTrace();
        }

        printGolemHeadlines();

        return null;
    }

    public String printGolemHeadlines() {
        try {
            System.out.println("Attempting to get golem.de");
            final Document document = Jsoup.connect("http://www.golem.de").get();
            System.out.println("Successfully acquired golem.de");
            final Elements headlines = document.select(".head2");

            System.out.println("Acquired data: " + toString(headlines));

        } catch (IOException e) {
            System.out.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
            return "FAIL";
        }
        return "OK";
    }

    private String toString(final Elements headlines) {
        StringBuffer result = new StringBuffer();
        for (Element headline : headlines) {
            result.append("\n" + headline.text());
        }
        return result.toString();
    }
}
