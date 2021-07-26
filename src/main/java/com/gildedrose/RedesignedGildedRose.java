package com.gildedrose;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * The class adds some elementary design to GildedRose implementation to make it more
 * customizable and flexible.
 */
public class RedesignedGildedRose extends GildedRose {
    /**
     *  Update Item rule interface that answers two question:
     *   - If the given rule applicable to be applied to the given item
     *   - How to update an item
     */
    public interface ItemUpdateRule {
        Item    update(Item item);
        boolean match(Item item);
    }

    /**
     * Default
     */
    public static class DefaultItemUpdateRule implements ItemUpdateRule {
        private String namePattern;
        private int    qualityIncreament;

        public DefaultItemUpdateRule(String newNamePattern) {
            this(newNamePattern, -1);
        }

        public DefaultItemUpdateRule(String newNamePattern, int newQualityIncreament) {
            this.namePattern       = newNamePattern;
            this.qualityIncreament = newQualityIncreament;
        }

        public String getNamePattern() {
            return this.namePattern;
        }

        public int getQualityIncreament() {
            return this.qualityIncreament;
        }

        @Override
        public Item update(Item item) {
            if (item == null) {
                throw new IllegalArgumentException("Null item is not allowed");
            }

            item.sellIn--;
            item.quality += qualityIncreament;
            if (item.sellIn < 0) {
                item.quality += qualityIncreament;
            }

            return this.fixBoundaries(item);
        }

        @Override
        public boolean match(Item item) {
            if (item == null) {
                throw new IllegalArgumentException("Null item is not allowed");
            }

            return namePattern == null || namePattern.equalsIgnoreCase(item.name);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }

            DefaultItemUpdateRule nr = (DefaultItemUpdateRule) obj;
            return  nr.getNamePattern() == namePattern || (namePattern != null && namePattern.equals(nr.getNamePattern()))
                &&  qualityIncreament == nr.getQualityIncreament();
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.namePattern, this.qualityIncreament);
        }

        protected Item fixBoundaries(Item item) {
            if (item.quality < 0)  {
                item.quality = 0;
            } else if (item.quality > 50) {
                item.quality = 50;
            }
            return item;
        }
    }

    public static class BrieUpdateRule extends DefaultItemUpdateRule {
        public BrieUpdateRule() {
            super("Aged Brie", 1);
        }
    }

    public static class BackstageUpdateRule extends DefaultItemUpdateRule {
        public BackstageUpdateRule() {
            super("Backstage passes to a TAFKAL80ETC concert");
        }

        @Override
        public Item update(Item item) {
            if (item.sellIn - 1 < 0) {
                item.quality = 0;
            } else {
                item.quality++;
                if (item.sellIn <= 10) {
                    item.quality++;
                }

                if (item.sellIn <= 5) {
                    item.quality++;
                }
            }
            item.sellIn--;
            return this.fixBoundaries(item);
        }
    }

    public static class SufulasUpdateRule extends DefaultItemUpdateRule {
        public SufulasUpdateRule() {
            super("Sulfuras, Hand of Ragnaros");
        }

        @Override
        public Item update(Item item) {
            return item;
        }
    }

    public static class ConjuredUpdateRule extends DefaultItemUpdateRule {
        public ConjuredUpdateRule() {
            super("Conjured Mana Cake", -2);
        }
    }


    private List<ItemUpdateRule> rules;
    private ItemUpdateRule       defaultRule;

    public RedesignedGildedRose(Item[] items) {
        super(Stream.of(items)
            .map(item -> new Item(item.name, item.sellIn, item.quality))
            .toArray(Item[]::new)
        );

        this.defaultRule = new DefaultItemUpdateRule(null);

        this.regUpdateRules(
            new BrieUpdateRule(),
            new BackstageUpdateRule(),
            new SufulasUpdateRule(),
            new ConjuredUpdateRule()
        );
    }

    public void regUpdateRules(ItemUpdateRule... newRules) {
        if (this.rules == null) {
            this.rules = new ArrayList();
        }
        this.rules = Arrays.asList(newRules);
    }

    public void addUpdateRule(ItemUpdateRule newRule) {
        if (this.rules == null) {
            this.rules = new ArrayList();
        }
        this.rules.add(newRule);
    }

    public void setDefaultUpdateRue(ItemUpdateRule newDefaultRule) {
        this.defaultRule = newDefaultRule;
    }

    @Override
    public void updateQuality() {
        Arrays.asList(this.items).forEach(item -> {
            ItemUpdateRule rule = this.rules.stream().filter(r -> r.match(item)).findFirst().orElse(this.defaultRule);
            if (rule != null) {
                rule.update(item);
            }
        });
    }
}