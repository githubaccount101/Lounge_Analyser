
package mk8dx;
import mkw.*;

import java.util.*;

public enum TrackD {
    MKS("MARIO KART STADIUM","MKS","MARIO KART STADIUM"),
    WP("WATER PARK","WP","WATER PARK", "WATER"),
    SSC("SWEET SWEET CANYON","SSC","SWEET SWEET CANYON","SWEET", "CANYON"),
    TR("THWOMP RUINS","TR","THWOMP RUINS", "RUINS"),
    MC("MARIO CIRCUIT","MC","MARIO CIRCUIT"),
    TH("TOAD HARBOR", "TH","TOAD HARBOR","HARBOR"),
    TM("TWISTED MANSION","TM","TWISTED MANSION","TWISTED","MANSION"),
    SGF("SHY GUY FALLS","SGF","SHY GUY FALLS","FALLS"),
    SA("SUNSHINE AIRPORT","SA","SUNSHINE AIRPORT","AIRPORT", "SUNSHINE"),
    DS("DOLPHIN SHOALS","DS","DOLPHIN SHOALS","DOLPHIN","SHOALS"),
    ED("ELECTRODROME","ED","ELECTRODROME", "E"),
    MW("MOUNT WARIO","MW","MOUNT WARIO"),
    CC("CLOUDTOP CRUISE","CC","CLOUDTOP CRUISE","CLOUD"),
    BDD("BONE-DRY DUNES","BDD","BONE-DRY DUNES","BONE", "BONE DRY","DUNES"),
    BC("BOWSER'S CASTLE","BC","BOWSER''S CASTLE", "BOWSER''S CASTLE", "BOWSERS CASTLE" ),
    RR("RAINBOW ROAD","RR","RAINBOW ROAD"),

    RMMM("Wii MOO MOO MEADOWS", "rMMM","Wii MOO MOO MEADOWS","MOO", "MMM"),
    RMC("GBA MARIO CIRCUIT","rMC","GBA MARIO CIRCUIT","GBA MC", "GBA Mario"),
    RCCB("DS CHEEP CHEEP BEACH","rCCB","DS CHEEP CHEEP BEACH", "CHEEP", "CCB"),
    RTT("N64 TOAD'S TURNPIKE","rTT","N64 TOAD''S TURNPIKE", "TURNPIKE","TT"),
    RDDD("GCN DRY DRY DESERT","rDDD","GCN DRY DRY DESERT", "DRY DRY"),
    RDP3("SNES DONUT PLAINS 3","rDP3","SNES DONUT PLAINS 3", "DP3"),
    RRRY("N64 ROYAL RACEWAY","rRRy","N64 ROYAL RACEWAY", "RACEWAY", "ROYAL", "ROYAL RACEWAY"),
    RDKJ("3DS DK JUNGLE","rDKJ","3DS DK JUNGLE", "DK JUNGLE", "DKJ", "JUNGLE"),
    RWS("DS WARIO STADIUM", "rWS","DS WARIO STADIUM","WARIO STADIUM", "WS"),
    RSL("GCN SHERBET LAND","rSL","GCN SHERBET LAND", "SHERBET", "SL", "GCN SL"),
    RMP("3DS MUSIC PARK","rMP","3DS MUSIC PARK","MUSIC PARK", "MP"),
    RYV("N64 YOSHI VALLEY","rYV","N64 YOSHI VALLEY","YOSHI VALLEY", "VALLEY", "YV"),
    RTTC("DS TICK-TOCK CLOCK","rTTC","DS TICK-TOCK CLOCK","TTC"),
    RPPS("3DS PIRANHA PLANT SLIDE","rPPS","3DS PIRANHA PLANT SLIDE","PPS"),
    RGV("WII GRUMBLE VOLCANO","rGV","WII GRUMBLE VOLCANO", "GV", "GRUMBLE"),
    RRRD("N64 RAINBOW ROAD","rRRd","N64 RAINBOW ROAD"),

    DYC("GCN YOSHI CIRCUIT","dYC","GCN YOSHI CIRCUIT","YOSHI CIRCUIT"),
    DEA("EXCITEBIKE ARENA","dEA","EXCITEBIKE ARENA","EXCITEBIKE","BIKE","EA"),
    DDD("DRAGON DRIFTWAY","dDD","DRAGON DRIFTWAY","DRIFTWAY","DRAGON"),
    DMC("MUTE CITY","dMC","MUTE CITY","MUTE"),
    DBP("GCN BABY PARK","dBP","GCN BABY PARK", "BABY","BP"),
    DCL("GBA CHEESE LAND","dCL","GBA CHEESE LAND","CHEESE LAND", "CHEESE"),
    DWW("WILD WOODS","dWW","WILD WOODS","WW"),
    DAC("ANIMAL CROSSING","dAC","ANIMAL CROSSING","AC"),
    DWGM("WII WARIO'S GOLD MINE","dWGM","WII WARIO''S GOLD MINE","WII WARIOS GOLD MINE","WARIOS GOLD MINE","WARIO",
            "WARIO'S GOLD MINE","MINE", "GOLD MINE","GOLDMINE","GOLD", "WGM"),
    DRR("SNES RAINBOW ROAD","dRR","SNES RAINBOW ROAD"),
    DIIO("ICE ICE OUTPOST","dIIO","ICE ICE OUTPOST","ICE","OUTPOST", "IIO"),
    DHC("HYRULE CIRCUIT","dHC","HYRULE CIRCUIT","HYRULE","HC"),
    DNBC("3DS NEO BOWSER CITY","dNBC","3DS NEO BOWSER CITY","NBC","NEO BOWSER CITY","NEOBOWSERCITY"),
    DRIR("GBA RIBBON ROAD","dRiR","GBA RIBBON ROAD","RIBBON ROAD", "RIBBON"),
    DSBS("SUPER BELL SUBWAY", "dSBS","SUPER BELL SUBWAY","SBS", "SUBWAY"),
    DBB("BIB BLUE","dBB","BIB BLUE","BB");

    private String[] aliases;
    public static final List<TrackD> ALL = List.copyOf(Arrays.asList(values()));
    public static final Map<String, TrackD> ALL_BY_ALIAS = toAliaseMap();

    TrackD(final String... aliases) {
        this.aliases = aliases;
    }

    public String getFullName(){
        return this.aliases[0];
    }

    public String getAbbreviation(){
        return this.aliases[1];
    }

    public String getSearchName(){
        return this.aliases[2];
    }


    private static Map<String, TrackD> toAliaseMap() {
        Map<String, TrackD> testMap = new HashMap<>();
        for (TrackD trackD : ALL) {
            testMap.put(trackD.name(), trackD);
            for (String alias : trackD.aliases) {
                testMap.put(alias, trackD);
            }
        }
        return testMap;
    }

    public static Optional<TrackD> fromString(final String s) {
        return Optional.ofNullable(toAliaseMap().get(s.toUpperCase()));
    }

    private static final List<TrackD> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static TrackD randomTrackD()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
