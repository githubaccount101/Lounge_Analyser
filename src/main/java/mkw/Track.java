
package mkw;

import java.util.*;

public enum Track {
    LC("LUIGI CIRCUIT","LC","LUIGI CIRCUIT","LUIGI"),
    MMM("MOO MOO MEADOWS", "MMM","MOO MOO MEADOWS","MOO MOO", "MOO"),
    MG("MUSHROOM GORGE","MG","MUSHROOM GORGE","GORGE"),
    TF("TOAD'S FACTORY", "TF", "TOAD''S FACTORY","TOAD'S FACTORY", "TOADS FACTORY", "TOAD FACTORY", "TOAD", "FACTORY"),
    MC("MARIO CIRCUIT","MC","MARIO CIRCUIT"),
    DKS("DK SUMMIT", "DKS","DK SUMMIT","DK SNOWBOARD CROSS", "DKSC", "SUMMIT", "CROSS"),
    CM("COCONUT MALL","CM","COCONUT MALL","COCONUT","MALL"),
    WGM("WARIO'S GOLD MINE","WGM","WARIO''S GOLD MINE","WARIO'S GOLD MINE","WARIOS GOLD MINE","WARIO", "MINE", "GOLD MINE","GOLDMINE","GOLD"),
    DC("DAISY CIRCUIT", "DC","DAISY CIRCUIT","DAISY"),
    MT("MAPLE TREEWAY","MT","MAPLE TREEWAY","MAPLE","TREEWAY"),
    KC("KOOPA CAPE","KC","KOOPA CAPE","KOOPA","CAPE"),
    GV("GRUMBLE VOLCANO","GV","GRUMBLE VOLCANO","GRUMBLE"),
    DDR("DRY DRY RUINS", "DDR","DRY DRY RUINS","DRY DRY", "DRYDRY"),
    MH("MOONVIEW HIGHWAY","MH","MOONVIEW HIGHWAY","MVH", "MOON","MOONVIEW", "HIGHWAY"),
    BC("BOWSER'S CASTLE","BC", "BOWSER''S CASTLE","BOWSERS CASTLE", "BCWII"),
    RR("RAINBOW ROAD","RR","RAINBOW ROAD", "RAINBOW"),
    RPB("GCN PEACH BEACH","rPB","GCN PEACH BEACH", "PB", "GCN PB"),
    RYF("DS YOSHI FALLS","rYF","DS YOSHI FALLS","YF","DS YF"),
    RGV2("SNES GHOST VALLEY 2","rGV2","SNES GHOST VALLEY 2","SNES GHOST VALLEY TWO","GV2", "SNES GV2"),
    RMR("N64 MARIO RACEWAY","rMR","N64 MARIO RACEWAY", "RACEWAY"),
    RSL("N64 SHERBET LAND","rSl","N64 SHERBET LAND", "SHERBET", "SL", "N64 SL"),
    RSGB("GBA SHY GUY BEACH","rSGB","GBA SHY GUY BEACH", "SGB"),
    RDS("DS DELFINO SQUARE","rDS","DS DELFINO SQUARE","DSDS","DELFINO", "DS"),
    RWS("GCN WALUIGI STADIUM","rWS","GCN WALUIGI STADIUM","STADIUM", "WALUIGI", "WS"),
    RDH("DS DESERT HILLS","rDH","DS DESERT HILLS", "DH", "DS DH","DESERT HILLS"),
    RBC3("GBA BOWSER's CASTLE 3","rBC3","GBA BOWSER''s CASTLE 3", "BC3", "GBA BC3"),
    RDKJP("N64 DK'S JUNGLE PARKWAY","rDKJP", "N64 DK''S JUNGLE PARKWAY","DKJP", "N64 DKJP", "DKP","N64 DKS JUNGLE PARKWAY","PARKWAY", "JP","N64 DK'S JUNGLE PARKWAY","JUNGLE"),
    RMC("GCN MARIO CIRCUIT","rMC","GCN MARIO CIRCUIT","GCN MC", "GCN Mario"),
    RMC3("SNES MARIO CIRCUIT 3","rMC3","SNES MARIO CIRCUIT 3", "MC3", "SNES MC3"),
    RPG("DS PEACH GARDENS","rPG", "DS PEACH GARDENS","PG", "DS PG", "GARDENS"),
    RDKM("GCN DK MOUNTAIN","rDKM","GCN DK MOUNTAIN","DKM", "GCN DKM"),
    RBC("N64 BOWSER'S CASTLE", "rBC","N64 BOWSER''S CASTLE","BC64", "64BC", "N64 BC","N64 BOWSERS CASTLE");

    private String[] aliases;
    public static final List<Track> ALL = List.copyOf(Arrays.asList(values()));
    public static final Map<String, Track> ALL_BY_ALIAS = toAliaseMap();

    Track(final String... aliases) {
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
