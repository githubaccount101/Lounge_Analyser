package Ui;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

@Deprecated
public class Main {

    static String rId = "";
    static final String roomUrl = "https://wiimmfi.de/stats/mkwx/list/";


    public static void main(String[] args) {

    }

    public static void testRoom(String friendCode){
        Document doc = null;
        try {
            doc = Jsoup.connect("https://wiimmfi.de/stats/mkwx/list/r3113399").get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Elements tables = doc.getElementsByClass("table11");
        for(Element table:tables){
            Elements tableElements = table.children();
            for(Element tableElement:tableElements){
                Elements subElements = tableElement.children();

                int i = 0;
                int finish = 0;
                int raceCounter = 0;
                for(Element subElement: subElements){
                    if(subElement.hasAttr("id")){
                        Elements tableHeader = subElement.getAllElements();
                        for(Element e:tableHeader){
                            if(e.hasClass("tc")){
                                if(i!=0||finish!=0){
                                    System.out.println(friendCode +" placed "+finish+" out of "+i);
                                }
                                i =0;
                                finish = 0;
                                String longText = e.text();
                                String raceNumber = longText.substring(longText.lastIndexOf("#") + 1);
                                String race = raceNumber.substring( 0, raceNumber.indexOf(")"));

                                String preTrack = longText.substring(longText.lastIndexOf("Track: "));
                                String track = preTrack.substring(0,preTrack.indexOf(" ("));
                                String track2 = track.substring(track.indexOf(" ")+1);

                                System.out.println("race: "+race+"      ");
                                System.out.print(track2+"\n");
                            }
                        }
                    }


                    if(subElement.className().matches("tr0")||subElement.className().matches("tr1")) {
                        String fc = subElement.text();
                        fc = fc.replaceAll("\\s.*", "");

                        i++;
                        if(fc.matches(friendCode)){
                            finish = i;
                            System.out.println("fc found for this race");
                        }
                    }
                }
                System.out.println("for the first race");
                if(i!=0||finish!=0){
                    System.out.println(friendCode +" placed "+finish+" out of "+i);
                }
                i =0;
                finish = 0;
            }
        }
    }


    public static String getRoomId(String friendCode){
        Document doc = null;
        try {
            doc = Jsoup.connect("https://wiimmfi.de/stats/mkwx").get();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Elements tables = doc.getElementsByClass("table11");
        for(Element table:tables){
            Elements tableElements = table.children();
            for(Element tableElement:tableElements){
                Elements subElements = tableElement.children();
                for(Element subElement: subElements){

                    if(subElement.hasAttr("id")){
                        String id = subElement.id();

                        rId = id;
                        System.out.println("rId is now: "+id);
                    }

                    if(subElement.className().matches("tr0")||subElement.className().matches("tr1")) {
                        String fc = subElement.text();
                        fc = fc.replaceAll("\\s.*", "");
                        System.out.println(fc);

                        if(fc.matches(friendCode)){
                            System.out.println(fc+ "is in room: "+rId);
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
