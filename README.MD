# Description

The repo contains number of refactored Gilded Rose implementations shown below:
  * **ReadableGlidedRose**  The class improves readability of code to help catch the ideas of the task faster
  * **BriefGlidedRose**  The purpose of the given implementation is minimalist/brief code.
  * **RedesignedfGlidedRose** This is a simple customizable implementation. The idea is making extending Glided Rose assortment with new Item types more easy and flexible.

Test cases can be found in **GlidedRoseTest** class. Pay attention the following:
  * The idea is using the original "GlidedRose" output as "source of truth" to compare it to output custom Glided Rose implementations mentioned above.
  * The is number of predefined test items that are use to test code
  * There is number of items that are generated randomly to improve test coverage and simplify testing


# RedesignedfGlidedRose implementation

The idea is designing simple "ItemUpdateRule" interface that defines two things:
  * Matching items that can be updated with the given rule
  * Updating the matched item

```java
    public interface ItemUpdateRule {
        Item    update(Item item);
        boolean match(Item item);
    }
```

THe interface has got number of implementations related to each type of item we have to handle. To illustrate the idea of the design imagine we need to support new "Conjured" items that degrade in Quality twice as fast as normal items. First of all, let's implement the new item update rule:

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

Then use the rule in Glided Rose implementation as follow:

```java
 RedesignedGildedRose gr = new RedesignedGildedRose(new Item[] { ... });
 gr.addUpdateRule(new ConjuredItemUpdateRule());
 gr.updateQuality();
```


