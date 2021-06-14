
package shared;

import mkw.Track;

import java.util.*;

import static shared.Format.values;

public enum Format {
    FFA("FFA", "1", "ffa"), TWOVTWO("2v2","2"), THREEVTHREE("3v3","3"), FOURVFOUR("4v4","4"), SIXVSIX("6v6", "6");

    private String[] aliases;
    public static final List<Format> ALL = List.copyOf(Arrays.asList(values()));
    public static final Map<String, Format> ALL_BY_ALIAS = toAliaseMap();

    Format(final String... aliases) {
        this.aliases = aliases;
    }

    public String getFormat() {
        return this.aliases[0];
    }

    private static Map<String, Format> toAliaseMap() {
        Map<String, Format> testMap = new HashMap<>();
        for (Format format : ALL) {
            testMap.put(format.name(), format);
            for (String alias : format.aliases) {
                testMap.put(alias, format);
            }
        }
        return testMap;
    }

    public static Optional<Format> fromString(final String s) {
        return Optional.ofNullable(toAliaseMap().get(s));
    }

    private static final List<Format> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Format randomFormat()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}
