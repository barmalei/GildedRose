package com.gildedrose;

class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            if (!items[i].name.equals("Aged Brie")
                    && !items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                if (items[i].quality > 0) {
                    if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                        // All items except "Aged brie" or  "Backstage" or "Sulfuras"
                        items[i].quality = items[i].quality - 1;

                        // modified part
                        if (items[i].name.equals("Conjured Mana Cake") && items[i].quality > 0) {
                             items[i].quality = items[i].quality - 1;
                        }
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    //
                    // Items: "Aged brie"  or "Backstage"
                    items[i].quality = items[i].quality + 1;

                    if (items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        // "Backstage"
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                // All except "Sulfuras"
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].name.equals("Aged Brie")) {
                    if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].quality > 0) {
                            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                                //  Condition: degrades twice as fast
                                //  All items except "Aged brie" or "Backstage" or "Sulfuras"
                                items[i].quality = items[i].quality - 1;

                                // modified
                                if (items[i].name.equals("Conjured Mana Cake") && items[i].quality > 0) {
                                     items[i].quality = items[i].quality - 1;
                                }
                            }
                        }
                    } else {
                        // "Sulfuras" set to zero :)
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    //  Condition: degrades twice as fast
                    //  "Aged Brie"
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
        }
    }
}