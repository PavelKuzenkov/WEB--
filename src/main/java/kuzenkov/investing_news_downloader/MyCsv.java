package kuzenkov.investing_news_downloader;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyCsv {

    //Названия нужных новостей/индексов
    private static final String GDP = "GDP \\(QoQ\\)";
    private static final String CURRENT_ACCOUNT = "Current Account.*";
    private static final String TRADE_BALANCE = "Trade Balance.*";
    private static final String FEDERAL_BUDGET = "Federal Budget Balance.*";
    private static final String UNEMPLOYTMENT_RATE = "Unemployment Rate.*";
    private static final String NONFARM_PAYROLLS = "Nonfarm Payrolls.*";
    private static final String PERSONAL_INCOME = "Personal Income.*";
    private static final String PERSONAL_SPENDING = "Personal Spending.*";
    private static final String PRODUCTIVITY = "Productivity.*";
    private static final String JOBLESS_CLAIMS = "Initial Jobless Claims.*";
    private static final String CPI = ".*CPI.*";
    private static final String PPI = ".*PPI.*";
    private static final String BUILDING_PERMITS = "Building Permits.*";
    private static final String HOUSING_STARTS = "Housing Starts.*";
    private static final String NEW_HOME_SALES = "New Home Sales.*";
    private static final String CONSTRUCTION_SPENDING = "Construction Spending.*";
    private static final String EXISTING_HOME_SALES = "Existing Home Sales.*";
    private static final String CORE_RETAIL_SALES = "Core Retail Sales.*";
    private static final String RETAIL_SALES = "Retail Sales \\(.*";
    private static final String DURABLE_GOODS_ORDERS = ".*Durable Goods Orders.*";
    private static final String INDUSTRIAl_PRODUCTION = "Industrial Production.*";
    private static final String CAPACITY_UTILIZATION = "Capacity Utilization.*";
    private static final String FACTORY_ORDERS = "Factory Orders \\(.*";
    private static final String PMI = ".*PMI.*";
    private static final String CONSUMER_CONFIDENCE = ".*Consumer Confidence.*";
    private static final String MICHIGAN_CONSUMER_SENTIMENT = "Michigan Consumer Sentiment";

    private static final String NEWS_REGEXP =
            "(^"
                    + Arrays.stream(
                    new String[] {
                            GDP,
                            CURRENT_ACCOUNT,
                            TRADE_BALANCE,
                            FEDERAL_BUDGET,
                            UNEMPLOYTMENT_RATE,
                            NONFARM_PAYROLLS,
                            PERSONAL_INCOME,
                            PERSONAL_SPENDING,
                            PRODUCTIVITY,
                            JOBLESS_CLAIMS,
                            CPI,
                            PPI,
                            BUILDING_PERMITS,
                            HOUSING_STARTS,
                            NEW_HOME_SALES,
                            CONSTRUCTION_SPENDING,
                            EXISTING_HOME_SALES,
                            CORE_RETAIL_SALES,
                            RETAIL_SALES,
                            DURABLE_GOODS_ORDERS,
                            INDUSTRIAl_PRODUCTION,
                            CAPACITY_UTILIZATION,
                            FACTORY_ORDERS,
                            PMI,
                            CONSUMER_CONFIDENCE,
                            MICHIGAN_CONSUMER_SENTIMENT
                    })
                    .collect(Collectors.joining("$|^"))
                    + "$)";

    private static final Pattern NEWS_PATTERN = Pattern.compile(NEWS_REGEXP);

      public static void main(String[] args) {
          // first create file object for file placed at location
          // specified by filepath
          File file = new File("usa-2010-01-01-2019-01-07.csv");
          File pfile = new File("usa-2010-01-01-2019-01-07-result.csv");
          List<String[]> result = new ArrayList<>();
          try (FileReader input = new FileReader(file);
              CSVReader reader = new CSVReader(input, ';')) {
              result = reader.readAll();
          } catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }

          try (FileWriter outputfile = new FileWriter(pfile);
               CSVWriter writer = new CSVWriter(outputfile, ';')) {

              // adding header to csv
              String[] header = result.get(0);
              writer.writeNext(header);

              for (String[] string : result) {
                  if (isNeededNews(string[3])) {
                      writer.writeNext(string);
                  }
              }
          }
          catch (IOException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
      }

    private static boolean isNeededNews(String number) {
        return NEWS_PATTERN.matcher(number).find();
    }
}
