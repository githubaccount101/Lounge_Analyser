
package mkw;

import java.util.*;

public enum Track {
    LC("LUIGI CIRCUIT","LUIGI"),
    MMM("MOO MOO MEADOWS", "MOO MOO", "MOO"),
    MG("MUSHROOM GORGE","GORGE"),
    TF("TOAD''S FACTORY", "TOADS FACTORY", "TOAD FACTORY", "TOAD", "FACTORY"),
    MC("MARIO CIRCUIT"),
    DKS("DK SUMMIT", "DK SNOWBOARD CROSS", "DKSC", "SUMMIT", "CROSS"),
    CM("COCONUT MALL","COCONUT","MALL"),
    WGM("WARIO'S GOLD MINE","WARIOS GOLD MINE","WARIO", "MINE", "GOLD MINE","GOLDMINE","GOLD"),
    DC("DAISY CIRCUIT","DAISY"),
    MT("MAPLE TREEWAY","MAPLE","TREEWAY"),
    KC("KOOPA CAPE","KOOPA","CAPE"),
    GV("GRUMBLE VOLCANO","GRUMBLE"),
    DDR("DRY DRY RUINS", "DRY DRY", "DRYDRY"),
    MH("MOONVIEW HIGHWAY","MVH", "MOON","MOONVIEW", "HIGHWAY"),
    BC("BOWSER''S CASTLE", "BOWSERS CASTLE", "BCWII"),
    RR("RAINBOW ROAD", "RAINBOW"),
    RPB("GCN PEACH BEACH", "PB", "GCN PB"),
    RYF("DS YOSHI FALLS","YF","DS YF"),
    RGV2("SNES GHOST VALLEY 2","SNES GHOST VALLEY TWO","GV2", "SNES GV2"),
    RMR("N64 MARIO RACEWAY", "RACEWAY"),
    RSL("N64 SHERBET LAND", "SHERBET", "SL", "N64 SL"),
    RSGB("GBA SHY GUY BEACH", "SGB"),
    RDS("DS DELFINO SQUARE","DSDS","DELFINO", "DS"),
    RWS("GCN WALUIGI STADIUM","STADIUM", "WALUIGI", "WS"),
    RDH("DS DESERT HILLS","DESERT HILLS", "DH", "DS DH"),
    RBC3("GBA BOWSER's CASTLE 3", "BC3", "GBA BC3"),
    RDKJP("N64 DK''S JUNGLE PARKWAY", "DKJP", "N64 DKJP", "DKP","N64 DKS JUNGLE PARKWAY"),
    RMC("GCN MARIO CIRCUIT","GCN MC", "GCN Mario"),
    RMC3("SNES MARIO CIRCUIT 3", "MC3", "SNES MC3"),
    RPG("DS PEACH GARDENS", "PG", "DS PG", "GARDENS"),
    RDKM("GCN DK MOUNTAIN","DKM", "GCN DKM"),
    RBC("N64 BOWSER''S CASTLE", "BC64", "64BC", "N64 BC","N64 BOWSERS CASTLE");

    private String[] aliases;
    public static final List<Track> ALL = List.copyOf(Arrays.asList(values()));
    public static final Map<String, Track> ALL_BY_ALIAS = toAliaseMap();

    Track(final String... aliases) {
        this.aliases = aliases;
    }

    public String getFullName(){
        return this.aliases[0];
    }


    private static Map<String, Track> toAliaseMap() {
        Map<String, Track> testMap = new HashMap<>();
        for (Track track : ALL) {
            testMap.put(track.name(), track);
            for (String alias : track.aliases) {
                testMap.put(alias, track);
            }
        }
        return testMap;
    }

    public static Optional<Track> fromString(final String s) {
        return Optional.ofNullable(toAliaseMap().get(s.toUpperCase()));
    }

    private static final List<Track> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Track randomTrack()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
