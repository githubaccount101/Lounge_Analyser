
package mkw;

import java.util.*;


public enum Tier {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8");

    private String tier;
    public static final List<Tier> ALL = List.copyOf(Arrays.asList(values()));
    public static Optional<Tier> fromString(final String s) {
        return Optional.ofNullable(toAliaseMap().get(s));
    }

    private Tier(String tier) { // constructor
        this.tier = tier;
    }

    public String getTier(){
        return tier;
    }

    private static Map<String, Tier> toAliaseMap() {
        Map<String, Tier> testMap = new HashMap<>();
        for (Tier tier : ALL) {
            testMap.put(tier.name(), tier);
            testMap.put(tier.getTier(), tier);
        }
        return testMap;
    }

    private static final List<Tier> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static Tier randomTier()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}