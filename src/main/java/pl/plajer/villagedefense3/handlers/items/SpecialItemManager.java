/*
 * Village Defense 3 - Protect villagers from hordes of zombies
 * Copyright (C) 2018  Plajer's Lair - maintained by Plajer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.plajer.villagedefense3.handlers.items;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Tom on 5/02/2016.
 */
public class SpecialItemManager {

  private static HashMap<String, SpecialItem> specialItems = new HashMap<>();

  public static void addItem(String name, SpecialItem entityItem) {
    specialItems.put(name, entityItem);
  }

  public static SpecialItem getSpecialItem(String name) {
    if (specialItems.containsKey(name)) {
      return specialItems.get(name);
    }
    return null;
  }

  public static String getRelatedSpecialItem(ItemStack itemStack) {
    for (String key : specialItems.keySet()) {
      SpecialItem entityItem = specialItems.get(key);
      if (entityItem.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())) {
        return key;
      }
    }
    return null;
  }
}
