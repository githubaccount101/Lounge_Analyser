
package mk8dx;

import mkw.*;

import java.util.*;


public enum TierD {
    E("e"),
    F("f"),
    D("d"),
    C("c"),
    B("b"),
    A("a"),
    S("s"),
    X("x");

    private String tier;
    public static final List<TierD> ALL = List.copyOf(Arrays.asList(values()));
    public static Optional<TierD> fromString(final String s) {
        return Optional.ofNullable(toAliaseMap().get(s));
    }

    private TierD(String tier) { // constructor
        this.tier = tier;
    }

    public String getTier(){
        return tier;
    }

    private static Map<String, TierD> toAliaseMap() {
        Map<String, TierD> testMap = new HashMap<>();
        for (TierD tier : ALL) {
            testMap.put(tier.name(), tier);
            testMap.put(tier.getTier(), tier);
        }
        return testMap;
    }

    private static final List<TierD> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public static TierD randomTierD()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

}