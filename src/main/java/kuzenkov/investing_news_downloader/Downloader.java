package kuzenkov.investing_news_downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {

    String URL = "https://www.investing.com/economic-calendar/Service/getCalendarFilteredData";
    String dateFrom = "2018-01-01";
    String dateTo = "2019-01-05";
    static HttpURLConnection connection;

    public static void main(String[] args) throws IOException {

//        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
//
////            HttpGet request = new HttpGet("https://google.com");
////            HttpGet request = new HttpGet("https://habr.com//");
//            HttpGet request = new HttpGet("https://investing.com/");
////            HttpGet request = new HttpGet("https://www.investing.com/economic-calendar/Service/getCalendarFilteredData");
//            request.setHeader("User-Agent", "Abra-Kadabra");
//
//            HttpResponse response = client.execute(request);
//            System.out.println("RESULT STATUS");
//            System.out.println(response.getStatusLine());
//
////            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
////                    response.getEntity().getContent()));
////
////            StringBuilder builder = new StringBuilder();
////
////            String line;
////
////            while ((line = bufReader.readLine()) != null) {
////
////                builder.append(line);
////                builder.append(System.lineSeparator());
////            }
////
////            System.out.println(builder);
//        }



        Downloader downloader = new Downloader();
        try{
            URL url = new URL (downloader.URL);
//            URL url = new URL ("https://investing.com/");
//            URL url = new URL ("https://google.com");
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Abra-Kadabra"); // Указываем, иначе 403
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("country", "[5]");
            connection.setRequestProperty("dateFrom", downloader.dateFrom);
            connection.setRequestProperty("dateTo", downloader.dateTo);
            connection.setRequestProperty("timeZone", "8");
            connection.setRequestProperty("timeFilter", "timeRemain");
            connection.setRequestProperty("limit_from", "0");
            connection.setRequestProperty("importance", "[1, 2, 3]");
            connection.setRequestProperty("Host", "www.investing.com");
            connection.setRequestProperty("Origin", "https://www.investing.com");
            connection.setRequestProperty("Referer", "https://www.investing.com/economic-calendar/");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            connection.connect();
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            System.out.println(connection.getHeaderFields());

//            String s = connection.getContentType();
//            Object o = connection.getContent();

            StringBuilder content;

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            System.out.println(content.toString());

        } catch (MalformedURLException mfe) {

        } catch (IOException ioe) {

        } finally{
            connection.disconnect();
        }
    }
}
