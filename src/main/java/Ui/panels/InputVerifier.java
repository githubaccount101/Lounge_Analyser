package Ui.panels;

import Ui.RaceDao;
import mk8dx.TierD;
import mk8dx.TrackD;
import mkw.Tier;
import mkw.Track;
import shared.Format;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class InputVerifier {


    public static boolean VerifyTierD(String input) {
        if(TierD.fromString(input).isPresent()){
            return true;
        }
        return false;
    }

    public static TierD getTierD(String input) {
        TierD tier = TierD.fromString(input).get();
        System.out.println("you've entered "+tier.getTier());
        return tier;
    }

    public static boolean VerifyTier(String input) {
        if(Tier.fromString(input).isPresent()){
            return true;
        }
        return false;
    }

    public static Tier getTier(String input) {
        Tier tier = Tier.fromString(input).get();
        System.out.println("you've entered "+tier.getTier());
        return tier;
    }

    public static boolean verifyFormat(String input){
        if(Format.fromString(input).isPresent()){
            return true;
        }
        return false;
    }

    public static Format getFormat(String input){
        Format format = Format.fromString(input).get();
        System.out.println("you've entered "+format.getFormat());
        return format;
    }

    public static boolean verifySF(String input){
        boolean properInt = input.matches("[1-9]|10|11|12");

        return properInt;
    }

    public static boolean verifyPlayers(String input){
        boolean properInt = input.matches("1[0-2]");
        return properInt;
    }

    public static boolean verifyFinish(String input, int players){
        boolean properInt = false;
        if(players == 10){
            if(input.matches("[1-9]|10")){
                properInt = true;
            }
        }
        if(players == 11){
            if(input.matches("[1-9]|10|11")){
                properInt = true;
            }
        }
        if(players == 12){
            if(input.matches("[1-9]|10|11|12")){
                properInt = true;
            }
        }
        return properInt;
    }

    public static boolean verifyTrackD(String input){
        return TrackD.fromString(input).isPresent();
    }

    public static TrackD getTrackD(String input){
        return TrackD.fromString(input).get();
    }

    public static boolean verifyTrack(String input){
        return Track.fromString(input).isPresent();
    }

    public static Track getTrack(String input){
        return Track.fromString(input).get();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("enter players");
            String input = scanner.nextLine();
            if(verifyPlayers(input)){
                while(true){
                    System.out.println("enter sf");
                    String input2 = scanner.nextLine();
                    boolean x = verifyFinish(input2,Integer.parseInt(input));
                    System.out.println(x);
                    if(x){
                        break;
                    }
                }
            }else{
                System.out.println("invalid players");
            }
        }
    }

    public static boolean verifyLastX(String input){
        try{
            int num = Integer.parseInt(input);
            if(num>0&&num<= RaceDao.getEventsStored()){
                return true;
            }else{
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean verifyLastXD(String input){
        try{
            int num = Integer.parseInt(input);
            if(num>0&&num<= RaceDao.getEventsDStored()){
                return true;
            }else{
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean verifyRandom(String input){
        boolean properInt = input.matches("^([1-9][0-9]{0,2}|1000)$");
        return properInt;
    }

    public static void relativePopup(String content, String title, Component location){
        final JOptionPane pane = new JOptionPane(content);
        final JDialog d = pane.createDialog((JFrame)null, title);
        d.setLocationRelativeTo(location);
        d.setVisible(true);
    }

    @Deprecated
    public static void InputErrorBox(String infoMessage)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Input Error", JOptionPane.INFORMATION_MESSAGE);
    }

}
