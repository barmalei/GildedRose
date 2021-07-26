# Description

https://github.com/emilybache/GildedRose-Refactoring-Kata/blob/main/GildedRoseRequirements.txt

The repo contains number of refactored Gilded Rose implementations listed below:
  * **ReadableGlidedRose**  The class improves readability of code to help catch the ideas of the algorithm faster than original "GlidedRose" code.
  * **BriefGlidedRose**  The purpose of the given implementation is minimalist/brief code.
  * **RedesignedfGlidedRose** This is a simple customizable implementation. The idea is making extending Glided Rose assortment with new Item types more easy and flexible.

Test cases can be found in **GlidedRoseTest** class. Pay attention the following:
  * The idea is using the original "GlidedRose" output as "source of truth" to compare its output to custom Glided Rose implementations mentioned above.
  * There is number of predefined test items declared in the test case code
  * There is number of items that are generated randomly to improve test coverage and simplify writing test cases


# RedesignedfGlidedRose implementation

The idea is introducing simple "ItemUpdateRule" interface that defines two things:
  * Matching items that can be updated with the given rule
  * Updating the matched item the way the rule implements

```java
    public interface ItemUpdateRule {
        Item    update(Item item);
        boolean match(Item item);
    }
```

The interface has number of implementations related to each type of Glade Rose item we have to handle:
  * **DefaultItemUpdateRule**
  * **BrieUpdateRule**
  * **BackstageUpdateRule**
  * **SufulasUpdateRule**
  * **ConjuredUpdateRule**


To illustrate the idea imagine we need to support new "Conjured" item type that degrades in quality twice as fast as normal items. First of all, let's implement the new item update rule:

```java
    public class ConjuredItemUpdateRule implements ItemUpdateRule {
        @Override
        public boolean match(Item item) {
            return "Conjured Mana Cake".equals(item.name);
        }

        @Override
        public Item update(Item item) {
            item.quality -= 2;
            item.sellIn--;
            if (item.sellIn < 0) {
                item.quality -= 2;
            }

            if (item.quality < 0) {
                item.quality = 0
            }
        }
    }
```

Then let's use the rule in Glided Rose implementation as follow:

```java
 RedesignedGildedRose gr = new RedesignedGildedRose(new Item[] { ... });
 gr.addUpdateRule(new ConjuredItemUpdateRule()); // add new rule
 gr.updateQuality();
```

Pay attention the example implementation is supposed to be used for demo purposes. The real "Conjured" items rule code is even more simple (it is based on default item update rule implementation):
```java
    public class ConjuredUpdateRule extends DefaultItemUpdateRule {
        public ConjuredUpdateRule() {
            super("Conjured Mana Cake", -2); // -2 is degradation increment supported with DefaultItemUpdateRule base class
        }
    }

```


