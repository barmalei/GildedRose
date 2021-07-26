package com.gildedrose;

/**
 *  The class is a re-factored version of "GildedRose" class that was implemented to provide
 *  more clear version of Gilded Rose inventory updating algorithm. The idea was re-writing
 *  code to be able recognize algorithmic specific case faster.
 */
public class BriefGildedRose extends GildedRose {
    /**
     * Constructor.
     * @param  items list of items.
     */
    public BriefGildedRose(Item[] items) {
        super(items);
    }

    @Override
    public void updateQuality() {
        for (Item item : items) {
            if (item.name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                if (--item.sellIn < 0) {
                    item.quality = 0;
                } else {
                    item.quality++;
                    if (item.sellIn < 5) {
                        item.quality += 2;
                    } else if (item.sellIn < 10) {
                        item.quality++;
                    }

                    // normalize quality  to its max possible value
                    if (item.quality > 50) {
                        item.quality = 50;
                    }
                }
            } else if (!item.name.equals("Sulfuras, Hand of Ragnaros")) {
                int dq = item.name.equals("Conjured Mana Cake") ? 2
                                                                : item.name.equals("Aged Brie") ? -1 : 1;
                item.quality -= dq;
                if (--item.sellIn < 0) {
                    item.quality -= dq;
                }

                if (item.quality < 0) {
                    item.quality = 0;
                } else if (item.quality > 50) {
                    item.quality = 50;
                }
            }
        }
    }
}
