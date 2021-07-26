package com.gildedrose;

import java.time.temporal.ValueRange;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 *  Various test cases.
 */
public class GildedRoseTest {
    // define how many randomly generated items have to be generated
    private static final int NUMBER_OF_GENERATED_ITEMS = 100;

    //
    // test sets to cover boundary cases per every item type
    //
    private static final Item[] PREDEFINED_ITEMS = new Item[] {
        new Item("+5 Dexterity Vest", 0, 50),
        new Item("+5 Dexterity Vest", -1, 50),
        new Item("+5 Dexterity Vest", 1, 50),
        new Item("+5 Dexterity Vest", 0, 30),
        new Item("+5 Dexterity Vest", -1, 30),
        new Item("+5 Dexterity Vest", 1, 30),
        new Item("+5 Dexterity Vest", 10, 30),
        new Item("Aged Brie", 0, 50),
        new Item("Aged Brie", -1, 50),
        new Item("Aged Brie", 1, 50),
        new Item("Aged Brie", 0, 10),
        new Item("Aged Brie", -1, 0),
        new Item("Aged Brie", 1, 1),
        new Item("Aged Brie", 10, 20),
        new Item("Sulfuras, Hand of Ragnaros", 0, 80),
        new Item("Sulfuras, Hand of Ragnaros", -1, 80),
        new Item("Sulfuras, Hand of Ragnaros", 1, 80),
        new Item("Sulfuras, Hand of Ragnaros", 10, 80),
        new Item("Backstage passes to a TAFKAL80ETC concert", 0, 50),
        new Item("Backstage passes to a TAFKAL80ETC concert", -1, 50),
        new Item("Backstage passes to a TAFKAL80ETC concert", 1, 50),
        new Item("Backstage passes to a TAFKAL80ETC concert", 0, 10),
        new Item("Backstage passes to a TAFKAL80ETC concert", -1, 0),
        new Item("Backstage passes to a TAFKAL80ETC concert", 1, 12),
        new Item("Backstage passes to a TAFKAL80ETC concert", 10, 19),
        new Item("Conjured Mana Cake", 0, 50),
        new Item("Conjured Mana Cake", -1, 50),
        new Item("Conjured Mana Cake", 1, 50),
        new Item("Conjured Mana Cake", 0, 10),
        new Item("Conjured Mana Cake", -1, 0),
        new Item("Conjured Mana Cake", 1, 19),
        new Item("Conjured Mana Cake", 12, 22)
    };

    //
    // Defines random item generator per very item type and ranges sellIn
    //
    private static final ItemGenerator[] ITEMS = new ItemGenerator[] {
        new ItemGenerator("Sulfuras, Hand of Ragnaros", ValueRange.of(-1, 10), 80),
        new ItemGenerator("Aged Brie", ValueRange.of(-1, 10), ValueRange.of(0, 30)),
        new ItemGenerator("Backstage passes to a TAFKAL80ETC concert", ValueRange.of(1, 20), ValueRange.of(10, 50)),
        new ItemGenerator("Conjured Mana Cake", ValueRange.of(-1, 20), ValueRange.of(1, 20)),
        new ItemGenerator("Elixir of the Mongoose", ValueRange.of(-1, 20), ValueRange.of(1, 20)),
        new ItemGenerator("+5 Dexterity Vest", ValueRange.of(-1, 20), ValueRange.of(1, 20))
    };

    /**
     *  Utility class to generate an Item of the given type with the randomly generated quality and sellIn properties.
     */
    private static class ItemGenerator {
        private static final Random RND = new Random();

        private ValueRange qualityRange;
        private ValueRange sellIn;
        private String     name;

        private ItemGenerator(String newName, int sellIn, int quality) {
            this(newName, ValueRange.of(sellIn, sellIn), ValueRange.of(quality, quality));
        }

        private ItemGenerator(String newName, ValueRange newSellInRange, int quality) {
            this(newName, newSellInRange, ValueRange.of(quality, quality));
        }

        private ItemGenerator(String newName, ValueRange newSellInRange, ValueRange newQualityRange) {
            this.name         = newName;
            this.sellIn       = newSellInRange;
            this.qualityRange = newQualityRange;
        }

        private Item generate() {
            return new Item(
                this.name,
                rndInRange(this.sellIn),
                rndInRange(this.qualityRange)
            );
        }

        private int rndInRange(ValueRange range) {
            int min = (int) range.getMinimum();
            int max = (int) range.getMaximum();
            return min + RND.nextInt(max - min + 1);
        }
    }

    //
    //  It is expected the Item class has to have more smart implementation:
    //     -- Validates input parameters
    //     -- Implements equals/hash method
    //     -- Has getter or/and setter properties methods
    //
    //  Since it is not allowed to modify Item class by the task definition then
    //  we comment the method content.
    //
    @Test
    void testItem() {
        // Assertions.assertThrows(IllegalArgumentException.class, () -> {
        //     new Item("Test name", 0, -1);
        // });

        // Assertions.assertThrows(IllegalArgumentException.class, () -> {
        //     new Item(null, 0, 1);
        // });
    }

    @Test
    void testReadableGlidedRose() {
        Item[]             testItems = this.getItemsTestset();
        GildedRose         app1 = new GildedRose(dup(testItems));
        ReadableGildedRose app2 = new ReadableGildedRose(dup(testItems));
        this.testAppMatch(app1, app2, 81);
    }

    @Test
    void testBriefGlidedRose() {
        Item[]          testItems = this.getItemsTestset();
        GildedRose      app1 = new GildedRose(dup(testItems));
        BriefGildedRose app2 = new BriefGildedRose(dup(testItems));
        this.testAppMatch(app1, app2, 81);
    }

    @Test
    void testRedesignedGlidedRose() {
        Item[]               testItems = this.getItemsTestset();
        GildedRose           app1 = new GildedRose(dup(testItems));
        RedesignedGildedRose app2 = new RedesignedGildedRose(dup(testItems));
        this.testAppMatch(app1, app2, 81);
    }

    private Item[] getItemsTestset() {
        return Stream.concat(
            Stream.of(generateItems(NUMBER_OF_GENERATED_ITEMS)),
            Stream.of(this.dup(PREDEFINED_ITEMS))
        ).toArray(Item[]::new);
    }

    private Item[] generateItems(int size) {
        return new Random().ints(size, 0, ITEMS.length)
            .mapToObj(index -> ITEMS[index].generate())
            .toArray(Item[]::new);
    }

    private Item[] dup(Item[] items) {
        return Stream.of(items)
            .map(item -> new Item(item.name, item.sellIn, item.quality))
            .toArray(Item[]::new);
    }

    protected void testAppMatch(GildedRose app1, GildedRose app2, int days) {
        assertNotNull(app1);
        assertNotNull(app2);
        assertTrue(days > 0, "Test if number of days to count is greater than zero");
        assertEquals(app1.items.length, app2.items.length, "Test if two items arrays have identical length");
        for (int i = 0; i < days; i++) {
            Item[] appItems1 = app1.items;
            Item[] appItems2 = app2.items;

            for (int j = 0; j <  appItems1.length; j++) {
                Item expectedItem = appItems1[j];
                Item testItem     = appItems2[j];
                assertEquals(expectedItem.name, testItem.name, String.format("Match { %s } and { %s } items name", expectedItem, testItem));
                assertEquals(expectedItem.sellIn, testItem.sellIn, String.format("Match { %s } and { %s } items sellIn", expectedItem, testItem));
                assertEquals(expectedItem.quality, testItem.quality, String.format("Match { %s } and { %s } items quality", expectedItem, testItem));
            }

            app1.updateQuality();
            app2.updateQuality();
        }
    }
}
