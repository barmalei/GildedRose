
# Problem of statement

Original challenge is described https://github.com/emilybache/GildedRose-Refactoring-Kata/blob/main/GildedRoseRequirements.txt

First let's re-formulate the challenge following more structured way. There are different types of items whose quality are updated on daily basis depending on product type and product expiration.
  * *Common Cases*
    * Product is not expired then quality degrades by 1
    * Expired product degrades twice faster

  * *Particular Cases*
    * "Aged Brie" increases its quality the older it gets
    * "Backstage" increases its quality the older it gets as follow:
        * sellIn > 10 then +1
        * 5 < sellIn <= 10 then +2
        * 0 < sellIn <= 5 then  +3
    * "Sulfuras" never be sold, quality is constant value
    * "Conjured" (NEW case) degrades in quality twice as fast as normal

  * *Boundary conditions*
    * Item quality has to be >= 0 and <= 50
    * Sulfuras quality is 80

# Repo artifacts

The repo contains number of refactored Gilded Rose implementations listed below:
  * **ReadableGlidedRose**  The class improves readability of code to help catch the ideas of the algorithm faster than original "GlidedRose" code.
  * **BriefGlidedRose**  The purpose of the given implementation is minimalist/brief code.
  * **RedesignedfGlidedRose** This is a simple customizable implementation. The idea is making extending Glided Rose assortment with new Item types more easy and flexible.

## ReadableGlidedRose

The idea is re-organizing code to make it more clear and understandable. "Make the code clear" means the code should better expose problem of statement to help a developer faster identifying the problem the code tries to solve. Logically the code has been re-organized:
  * Remove unnecessary embedding
  * Keep all general cases on the same level
  * Split boundary condition from the logical conditions


## BriefGlidedRose

The purpose is making compact implementation of the given assessment task. Code could look a bit weird, less clear, but technically it works. The exercise can be useful to see how surplus and redundant the initial code is.


## RedesignedfGlidedRose implementation

The purpose of the code is making the final solution supportable, extendable and flexible. The key idea is introducing simple "ItemUpdateRule" interface that defines two things:
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

## Test cases

Test cases can be found in **GlidedRoseTest** class. Pay attention the following:
  * The idea is using the original "GlidedRose" output as "source of truth" to compare its output to custom Glided Rose implementations mentioned above.
  * There is number of predefined test items declared in the test case code
  * There is number of items that are generated randomly to improve test coverage and simplify writing test cases


# Simplification, improvements

 * RedesignedfGlidedRose: all designed classes for the sake of simplicity and faster reviewing are static inner classes. That should not happen in real development. In most cases classes and interfaces should be placed in individual files.
 * Test cases have required re-factoring to:
    * split single test case to multiple following the common best practice (one test case class per one class we are testing)
    * move common, re-usable part (random Item generator, Item marcher, etc) into a base test case class and inherit it whenever it is neede
 * There are number of places where we store references to objects (Item, Item[]) unsafely getting shared copies of the same instance in different parts. It is better to fix it with cloning/copying the instances.
 * The way how we match items is very weak (case sensitive, free text match). It is better to introduce sort of Item type property and description property. Mathing should be done by type (type can be Enum). One more way to improve it is using pattern matching (regexp).
 * Better and more graceful design:
    * Use standard functional interface as the key class that defines an Item update rules. Then we can pass it as lambda and we don't need to have a special abstract class or interface.
    * Matching can be done on the level of main class:
```java
    RedesignedGildedRose gr = new RedesignedGildedRose(new Item[] { ... });
    gr.addUpdateRule("<math string>" | item_type>, item -> {
        ...
        return newItem;
    }); // add new rule

```

  * Item itself should be implemented better manner (argument validation, equal, hash, etc)






