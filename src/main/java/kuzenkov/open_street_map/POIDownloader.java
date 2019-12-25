package kuzenkov.open_street_map;

import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.net.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POIDownloader {

    private static String urlWithParams = "http://openstreetmap.ru/api/poi?action=getpoibbox&nclass=root%2Cdemocracy" +
            "%2Cadministrative%2Ctown_hall%2Cgovernment%2Cregister_office%2Cmigration%2Ctax_inspection%2Cpolice" +
            "%2Cpension_fund%2Cembassy%2Cprosecutor%2Cbailiff%2Csocial_facility%2Ccourthouse%2Ccustoms%2Cprison" +
            "%2Cfood%2Cbar%2Ccafe%2Cbiergarten%2Cpub%2Crestaurant%2Cfood_court%2Cfast_food%2Chealth%2Cpharmacy" +
            "%2Chospital%2Cveterinary%2Cdoctors%2Cnursing_home%2Cmortuary%2Cclinic%2Cambulance_station%2Cdentist" +
            "%2Cinfrastructure%2Cwater_supply%2Cwater_works%2Cwater_tower%2Cwater_well%2Cwastewater_plant" +
            "%2Csurveillance%2Clandfill%2Ccommunications%2Ctelephone_exchange%2Ccommunication_tower%2Cconstruction" +
            "%2Ctransport_constructions%2Cbridge%2Ctunnel%2Cpower%2Cpower_generator%2Csubstation%2Ctransformer" +
            "%2Cpower_plant%2Cculture%2Clibrary%2Czoo%2Ccinema%2Cclubs%2Cclub_automobile%2Cclub_astronomy" +
            "%2Cclub_charity%2Cclub_veterans%2Cclub_game%2Cclub_history%2Cclub_cinema%2Cclub_art%2Cclub_computer" +
            "%2Cclub_motorcycle%2Cclub_music%2Cclub_board_games%2Cclub_nature%2Cclub_shooting%2Cclub_hunting" +
            "%2Cclub_linux%2Cclub_fishing%2Cclub_sport%2Cclub_theatre%2Cclub_tourism%2Cclub_fan%2Cclub_photography" +
            "%2Cclub_chess%2Cclub_ethnic%2Cmuseum%2Ccommunity_centre%2Cdance%2Ctheatre%2Ccircus%2Carts_centre%2Cshop" +
            "%2Chifi%2Ccar_parts%2Ccar%2Coutdoor%2Canime%2Cantiques%2Ccharity%2Cboutique%2Cchemist%2Cbicycle" +
            "%2Cvariety_store%2Cgas%2Cinterior_decoration%2Cbaby_goods%2Cwindow_blind%2Cnewsagent%2Cpet%2Ctoys" +
            "%2Cdoityourself%2Cstationery%2Catv%2Ckiosk%2Cbookshop%2Ccarpet%2Ccomputer%2Ccosmetics%2Cpaint%2Ccopyshop" +
            "%2Ckitchen%2Ctableware%2Cpawnbroker%2Cbed%2Cfurniture%2Cmotorcycle%2Cmusical_instrument%2Corganic%2Cshoes" +
            "%2Cclothes%2Cglaziery_shop%2Coptician_shop%2Ctrade%2Chunting%2Cgift%2Cbedding%2Cart%2Cticket%2Cvideo" +
            "%2Cshop_food%2Cbakery%2Calcohol%2Cdeli%2Cconfectionery%2Cconvenience%2Cseafood%2Cice_cream%2Cbutcher" +
            "%2Cbeverages%2Cgreengrocer%2Csupermarket%2Cmall%2Cfarm%2Cvacuum_cleaner%2Cradiotechnics%2Cframe" +
            "%2Cfishing_shop%2Cmarketplace%2Cmobile_phone%2Cbathroom_furnishing%2Csecond_hand%2Cerotic%2Cgarden_centre" +
            "%2Chardware%2Chearing_aids%2Cdive%2Csports%2Cbag%2Csupermarket%2Ctobacco%2Cfabric%2Cmall%2Cherbalist" +
            "%2Cdepartment_store%2Chouseware%2Cflorist%2Csewing%2Ccurtain%2Celectronics%2Cjewelry%2Cship_chandler" +
            "%2Ceducation%2Cdriving_school%2Clibrary%2Ckindergarten%2Cuniversity%2Ccollege%2Cschool%2Coffice" +
            "%2Cestate_agent%2Cadministrative%2Ctown_hall%2Carchitect%2Cemployment_agency%2Cbookmaker%2Caccountant" +
            "%2Cit%2Cresearch%2Ceducational_institution%2Cngo%2Cnewspaper%2Cadvertising_agency%2Cinsurance%2Cstudio" +
            "%2Ctelecommunication%2Ctravel_agent%2Ccompany%2Clawyer%2Cnotary%2Cnatural%2Cbay%2Cwaterfall%2Cvolcano" +
            "%2Ccave_entrance%2Cpeak%2Ccape%2Cisland%2Cislet%2Cbeach%2Cstrait%2Cspring%2Csaddle%2Creligion%2Ccemetery" +
            "%2Cplace_of_worship%2Cplace_of_worship_muslim%2Cplace_of_worship_jewish%2Cplace_of_worship_christian" +
            "%2Ccraft%2Ctailor%2Cboatbuilder%2Cscaffolder%2Cpottery%2Cupholsterer%2Ctinsmith%2Cstand_builder" +
            "%2Ckey_cutter%2Csun_protection%2Cwindow_construction%2Coptician_craft%2Cstonemason%2Ccaterer%2Croofer" +
            "%2Cblacksmith%2Cpainter%2Cmetal_construction%2Chvac%2Cshoemaker%2Crigger%2Cparquet_layer%2Cbeekeeper" +
            "%2Cbookbinder%2Cbrewery%2Csawmill%2Cbasket_maker%2Ctiler%2Csaddler%2Csailmaker%2Cdressmaker" +
            "%2Ccraft_computer%2Cagricultural_engines%2Chandicraft%2Cgardener%2Cplumber%2Csculptor%2Clocksmith" +
            "%2Cglaziery_craft%2Ccarpenter%2Csweep%2Ccarpet_layer%2Cinsulation%2Cphotographer%2Cphotographic_laboratory" +
            "%2Cclockmaker%2Cwatchmaker%2Cplasterer%2Celectrician%2Cjeweller%2Csport_and_entertainment%2Cwater_park" +
            "%2Csauna%2Cswimming_pool%2Crunning_track%2Cbicycle_track%2Craceway%2Cplayground%2Chorse_track%2Cice_rink" +
            "%2Cfishing%2Cminiature_golf%2Cnightclub%2Cpark%2Ctheme_park%2Cdog_park%2Cbeach_resort%2Cgolf_course" +
            "%2Cmarina%2Cbicycle_rental%2Cslipway%2Cpitch%2Cclub_sport%2Csports_centre%2Cstadium%2Cdance%2Ctransport" +
            "%2Cbicycle_transport%2Cbicycle_parking%2Cbicycle_rental%2Cwater_transport%2Cferry_terminal%2Cslipway" +
            "%2Cship_navigation%2Clateral_buoy%2Clighthouse%2Cleading_beacon%2Cair_transport%2Cairport%2Chelipad" +
            "%2Cpersonal_transport%2Ccar_wash%2Ccar_repair%2Ccar_repair_tyres%2Cparking_entrance%2Cspeed_camera" +
            "%2Cfuel%2Cev_charging%2Cparking%2Cvehicle_ramp%2Cpublic_transport%2Cbus_station%2Csubway_entrance" +
            "%2Crailway_halt%2Crailway_station%2Cbus_stop%2Ctram_stop%2Csubway_station%2Ctaxi%2Cfunicular" +
            "%2Croad_obstacles%2Cford%2Cgate%2Cspeed_camera%2Cbump%2Chump%2Ccushion%2Cmountain_pass%2Ctoll_booth" +
            "%2Clift_gate%2Ctransport_constructions%2Cbridge%2Ctunnel%2Ctourism%2Cviewpoint%2Csights%2Cartwork" +
            "%2Cviewpoint%2Ccity_gate%2Cattraction%2Ccastle%2Czoo%2Cruins%2Clighthouse%2Cboundary_stone%2Cmemorial" +
            "%2Cwreck%2Cmonument%2Cmuseum%2Ctheme_park%2Cbattlefield%2Carchaeological_site%2Crune_stone%2Cship" +
            "%2Cfountain%2Cfort%2Cinformation%2Ccamp_site%2Cpicnic_site%2Cdrinking_water%2Clodging%2Cguest_house" +
            "%2Chotel%2Cmotel%2Chostel%2Cspring%2Ctravel_agent%2Cclub_tourism%2Cshelter%2Cfunicular%2Cclock%2Cservice" +
            "%2Cdriving_school%2Ctailor%2Csauna%2Cev_charging%2Cinternet_cafe%2Cnails%2Cmassage%2Ccurrencyexchange" +
            "%2Chairdresser%2Crecycling%2Cpost_office%2Cpost_box%2Claundry%2Ccar_rental%2Cbicycle_rental" +
            "%2Ccraft_computer%2Cfuneral_directors%2Cbeauty_salon%2Ctattoo%2Ctelephone%2Ctoilets%2Cdry_cleaning" +
            "%2Cfinance%2Cbank%2Catm%2Caccountant%2Cpawnbroker%2Ccurrencyexchange%2Cmoney_lender%2Cemergency%2Cpolice" +
            "%2Cfire_station%2Cfire_hydrant%2Cambulance_station%2Cemergency_phone&";

    static HttpURLConnection connection;
    static List<String> reqList = new ArrayList<>();
    static List<String> resultList = new ArrayList<>();

    public static void main(String[] args) {
        prepare();
        for (int i = 1; i < reqList.size(); i++) {
            System.out.println("Запрос " + i + " из " + reqList.size());
            String responseString = request(reqList.get(i));
            System.out.println(responseString);
            String[] pois = responseString.split("\\{\"id\": ");
            for (int j = 1; j < pois.length; j++) {
                resultList.add(getPOIString(reqList.get(i), pois[j]));
            }
        }
        createResultFile();
    }

    private static String getPOIString(String geo, String poi) {
        String resultPoi = geo.replaceAll(";", " - ");
        Pattern name = Pattern.compile("\"class_ru\": \"([^\"]*)\"");
        Pattern lat = Pattern.compile("\"lat\": (\\d*\\.\\d*)");
        Pattern lon = Pattern.compile("\"lon\": (\\d*\\.\\d*)");
        Matcher nameMatcher = name.matcher(poi);
        Matcher latMatcher = lat.matcher(poi);
        Matcher lonMatcher = lon.matcher(poi);
        latMatcher.find();
        nameMatcher.find();
        lonMatcher.find();
            resultPoi = resultPoi + ";" + nameMatcher.group(1) + ";" + latMatcher.group(1) + " - " + lonMatcher.group(1) + System.lineSeparator();
        return resultPoi;
    }

    private static String request(String geo) {
        String responseString = "";
        try {
            String[] latLon = geo.split(";");
            String geoParams = getParamsString(latLon);
            java.net.URL url = new URL(urlWithParams + geoParams);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "text/javascript; Charset=Utf-8");
            connection.setRequestProperty("Transfer-Encoding", "chunked");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.connect();
            System.out.println(geo);
            System.out.println(geoParams);
            System.out.println(connection.getResponseCode());
            responseString = getResponseString();
        } catch (MalformedURLException mfe) {
            mfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            connection.disconnect();
            return responseString;
        }
    }

    private static String getResponseString() {
        StringBuilder content = new StringBuilder();
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
            br.close();
            result = StringEscapeUtils.unescapeJson(content.toString());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        return result;
    }

    private static void prepare(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("addresses-result.csv"));
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                String[] lines = inputLine.split(";");
                reqList.add(lines[lines.length - 2] + ";" + lines[lines.length - 1]);
            }
            System.out.println(reqList.size());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String getParamsString(String[] geo) {
        //t=54.31752454089308&r=48.38078498840332&b=54.29889636035831&l=48.347740173339844 - образец
        //Коэффициенты актуальны для Хабаровска. 610-615 метров это примерно 0.0055 градуса по широте и 0.0084 градуса по долготе. Для других мест потребуется вычислять свои
        Double dt = Double.valueOf(geo[0]) + (0.0055d / 2d);
        Double dr = Double.valueOf(geo[1]) + (0.0084d / 2d);
        Double db = Double.valueOf(geo[0]) - (0.0055d / 2d);
        Double dl = Double.valueOf(geo[1]) - (0.0084d / 2d);
        String t = dt.toString();
        String r = dr.toString();
        String b = db.toString();
        String l = dl.toString();
        String resultString = "t=" + t + "&r=" + r + "&b=" + b + "&l=" + l;
        return resultString;
    }

    private static void createResultFile() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter("POIs.csv"))) {
            br.write("Координаты адреса из старого csv;Имя POI;координаты POI" + System.lineSeparator());
            for (String s : resultList) {
                br.write(s);
            }
            br.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
