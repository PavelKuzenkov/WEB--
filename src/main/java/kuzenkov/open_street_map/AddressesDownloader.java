package kuzenkov.open_street_map;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class AddressesDownloader {

    private static String URL = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address";
    private static String token = "19a889e8335ef7df50973eb6f8d58ad96b79b34e";
    static HttpURLConnection connection;
    static List<String> stringList = new ArrayList<>();
    static List<String> reqList = new ArrayList<>();
    static List<String> resultList = new ArrayList<>();
    static List<String> nullList = new ArrayList<>();
    static Map<String, String> parameters = new HashMap<>();


    public static void main(String[] args) {
        prepare();
        for (int i = 1; i < reqList.size(); i++) {
            System.out.println("Запрос " + i + " из " + reqList.size());
            request(reqList.get(i), true);
            String responseString = getResponseString();
            System.out.println(responseString);
            if (responseString.contains("geo_lat") && responseString.contains("geo_lon")) {
                addGeo(responseString, i);
            } else {
                request(reqList.get(i), false);
                responseString = getResponseString();
                System.out.println(responseString);
                if (responseString.contains("geo_lat") && responseString.contains("geo_lon")) {
                    addGeo(responseString, i);
                    } else {
                    nullList.add(stringList.get(i) + ";null;null" + System.lineSeparator());
                }
            }
        }
        createResultFile();
    }

    private static void request(String address, boolean firsTry) {
        try {
            java.net.URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Authorization", "Token " + token);
            parameters.put("query", address);
            if (firsTry) {
                parameters.put("count", "1");
            }
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeChars(getParamsString(parameters));
            parameters.clear();
            out.flush();
            out.close();
            connection.connect();
        } catch (MalformedURLException mfe) {
            mfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    private static void addGeo(String responseString, int i) {
        String geo = responseString.replaceAll(".*(\"geo_lat\":\"(\\d*\\.\\d*)\",\"geo_lon\":\"(\\d*\\.\\d*)\"|\"geo_lat\":null,\"geo_lon\":null).*", "$1");
        String[] latLon = geo.split(",");
        latLon[0] = latLon[0].replaceAll("\"geo_lat\":\"", "");
        latLon[0] = latLon[0].replaceAll("\"", "");
        latLon[1] = latLon[1].replaceAll("\"geo_lon\":\"", "");
        latLon[1] = latLon[1].replaceAll("\"", "");
        System.out.println(latLon[0]);
        System.out.println(latLon[1]);
        resultList.add(stringList.get(i) + ";" + latLon[0] + ";" + latLon[1]);
    }

    private static String getResponseString() {
        StringBuilder content = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
            br.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return content.toString();
    }

    private static void prepare(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("addresses.csv"));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringList.add(inputLine);
                String[] lines = inputLine.split(";");
                reqList.add(lines[4].replaceAll("\"", "").replaceAll("\\\\", "/").trim());
            }
            parameters = new HashMap<>();
            System.out.println(stringList.size());
            System.out.println(reqList.size());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String getParamsString(final Map<String, String> params) {
        final StringBuilder result = new StringBuilder();
        result.append("{");
        params.forEach((name, value) -> {
                result.append(" \"");
                result.append(name);
                result.append("\": \"");
                result.append(value);
                result.append("\",");
        });
        result.deleteCharAt(result.length() - 1);
        if (params.containsKey("count")) {
            result.deleteCharAt(result.length() - 1);
            result.deleteCharAt(result.length() - 2);
        }
        result.append(" }").append(System.lineSeparator());
        final String resultString = result.toString();
        System.out.println(resultString);
        return resultString;
    }

    private static void createResultFile() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter("addresses-result.csv"))) {
            br.write(stringList.get(0) + System.lineSeparator());
            for (String s : resultList) {
                br.write(s);
            }
            for (String s : nullList) {
                br.write(s);
            }
            br.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
