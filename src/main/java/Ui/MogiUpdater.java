package Ui;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;


public class MogiUpdater {

    static String rId = "";
    static final String roomUrl = "https://wiimmfi.de/stats/mkwx/list/";
    public String finalUrl = "";
    public HashMap<Integer, HtmlRace> races = new HashMap<>();
    public int initialStart = 0;
    public boolean roomFound = false;

    public void setUp(){
        races.clear();
    }

    public HashMap<Integer, HtmlRace> getRaces() {
        return races;
    }

    public void initializeRaces(String friendCode){
        Document doc = null;

        if(finalUrl.isBlank()){
            try {
                String url = roomUrl+getRoomId(friendCode);
                if(url.equals(roomUrl)){
                    System.out.println("not found in room");
                    return;
                }else{
                    finalUrl=url;
                }
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                System.out.println("connecting...");
                doc = Jsoup.connect(finalUrl).get();
                System.out.println("connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Elements tables = doc.getElementsByClass("table11");
        for(Element table:tables){
            Elements tableElements = table.children();
            for(Element tableElement:tableElements){
                Elements subElements = tableElement.children();

                int i = 0;
                int finish = 0;
                boolean satOut = false;
                int raceid = 0;

                for(Element subElement: subElements){
                    if(subElement.hasAttr("id")){
                        Elements tableHeader = subElement.getAllElements();
                        for(Element e:tableHeader){
                            if(e.hasClass("tc")){

                                if(i!=0||finish!=0){
                                    System.out.println(friendCode +" placed "+finish+" out of "+i);
                                }

                                if(races.containsKey(raceid)){
                                    HtmlRace htmlRace = races.get(raceid);
                                    htmlRace.setSatOut(satOut);
                                    htmlRace.setPlayers(i);
                                    htmlRace.setFinish(finish);
                                }


                                String longText = e.text();
                                String raceNumber = longText.substring(longText.lastIndexOf("#") + 1);
                                String race = raceNumber.substring( 0, raceNumber.indexOf(")"));

                                String preTrack = longText.substring(longText.lastIndexOf("Track: "));
                                String track = preTrack.substring(0,preTrack.indexOf(" ("));
                                String track2 = track.substring(track.indexOf(" ")+1);



                                System.out.println("race: "+race+"      ");
                                System.out.print(track2+"\n");

                                int raceInt = Integer.parseInt(race);
                                raceid=raceInt;

                                if(races.containsKey(raceInt)==false){
                                    races.put(raceInt, new HtmlRace(raceInt, track2));
                                }

                                i =0;
                                finish = 0;
                            }
                        }
                    }


                    if(subElement.className().matches("tr0")||subElement.className().matches("tr1")) {
                        String fc = subElement.text();
                        fc = fc.replaceAll("\\s.*", "");

                        i++;
                        if(fc.matches(friendCode)){
                            finish = i;
                            satOut = false;
                        }
                    }
                }
                System.out.println("for the first race");
                if(i!=0||finish!=0){
                    System.out.println(friendCode +" placed "+finish+" out of "+i);
                }
                if(races.containsKey(raceid)){
                    HtmlRace htmlRace = races.get(raceid);
                    htmlRace.setSatOut(satOut);
                    htmlRace.setPlayers(i);
                    htmlRace.setFinish(finish);
                }
                i =0;
                finish = 0;
                System.out.println("=======================================");
            }
        }
    }

    public String getRoomId(String friendCode){
        Document doc = null;
        try {
            System.out.println("connecting...");
            doc = Jsoup.connect("https://wiimmfi.de/stats/mkwx").get();
            System.out.println("connected");
        } catch (IOException e) {
            e.printStackTrace();
        }

        roomFound = false;
        Elements tables = doc.getElementsByClass("table11");
        for(Element table:tables){
            Elements tableElements = table.children();
            for(Element tableElement:tableElements){
                int i = 0;
                Elements subElements = tableElement.children();
                for(Element subElement: subElements){

                    if(subElement.hasAttr("id")){
                        String id = subElement.id();
                        i=0;
                        rId = id;
                        System.out.println("Searching room id: "+id);
                    }

                    if(subElement.className().matches("tr0")||subElement.className().matches("tr1")) {
                        String fc = subElement.text();
                        fc = fc.replaceAll("\\s.*", "");
                        i++;


                        if(fc.matches(friendCode)){
                            System.out.println(fc+ " found in room: "+rId);
                            System.out.println("");
                            System.out.println("---------------------");
                            System.out.println("");
                            initialStart=i;
                            roomFound = true;
                            return rId;
                        }
                    }
                }
            }
        }
        rId = "";
        return "";
    }


}
