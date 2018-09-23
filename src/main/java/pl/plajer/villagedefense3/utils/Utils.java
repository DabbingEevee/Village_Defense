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

package pl.plajer.villagedefense3.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.handlers.ChatManager;

/**
 * Created by Tom on 29/07/2014.
 */
public class Utils {

  public static Queue<Block> getNearbyDoors(LivingEntity entity, HashSet<Byte> transparent, int maxDistance, int maxLength) {
    if (maxDistance > 120) {
      maxDistance = 120;
    }

    Queue<Block> blocks = new LinkedList<>();
    Iterator<Block> itr = new BlockIterator(entity, maxDistance);
    while (itr.hasNext()) {
      Block block = itr.next();
      blocks.add(block);

      if (maxLength != 0 && blocks.size() > maxLength) {
        blocks.remove(0);
      }
      if (block.getType().isTransparent()) {
        break;
      }
    }
    return blocks;
  }

  public static Entity[] getNearbyEntities(Location l, int radius) {
    int chunkRadius = radius < 16 ? 1 : radius / 16;
    HashSet<Entity> radiusEntities = new HashSet<>();
    for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
      for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
        int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
        for (Entity e : new Location(l.getWorld(), x + chX * 16, y, z + chZ * 16).getChunk().getEntities()) {
          if (!(l.getWorld().getName().equalsIgnoreCase(e.getWorld().getName()))) {
            continue;
          }
          if (e.getLocation().distanceSquared(l) <= radius * radius && e.getLocation().getBlock() != l
              .getBlock()) {
            radiusEntities.add(e);
          }
        }
      }
    }
    return radiusEntities.toArray(new Entity[0]);
  }

  public static List<String> splitString(String string, int max) {
    List<String> matchList = new ArrayList<>();
    Pattern regex = Pattern.compile(".{1," + max + "}(?:\\s|$)", Pattern.DOTALL);
    Matcher regexMatcher = regex.matcher(string);
    while (regexMatcher.find()) {
      matchList.add(ChatManager.colorRawMessage("&7") + regexMatcher.group());
    }
    return matchList;
  }

  public static ItemStack getPotion(PotionType type, int tier, boolean splash) {
    ItemStack potion;
    if (!splash) {
      potion = new ItemStack(Material.POTION, 1);
    } else {
      potion = new ItemStack(Material.SPLASH_POTION, 1);
    }

    PotionMeta meta = (PotionMeta) potion.getItemMeta();
    if (tier >= 2 && !splash) {
      meta.setBasePotionData(new PotionData(type, false, true));
    } else {
      meta.setBasePotionData(new PotionData(type, false, false));
    }
    potion.setItemMeta(meta);
    return potion;
  }

  public static byte getDoorByte(BlockFace face) {
    switch (face) {
      case NORTH:
        return 3;
      case EAST:
        return 0;
      case SOUTH:
        return 1;
      case WEST:
        return 2;
      default:
        return 0;
    }
  }

  public static BlockFace getFacingByByte(byte bt) {
    switch (bt) {
      case 1:
        return BlockFace.SOUTH;
      case 2:
        return BlockFace.WEST;
      case 3:
        return BlockFace.EAST;
      case 4:
        return BlockFace.NORTH;
      default:
        return BlockFace.SOUTH;
    }
  }

  public static void playSound(Location loc, String before1_13, String after1_13) {
      if (JavaPlugin.getPlugin(Main.class).is1_13_R1() || JavaPlugin.getPlugin(Main.class).is1_13_R2()) {
        loc.getWorld().playSound(loc, Sound.valueOf(after1_13), 1, 1);
      } else {
        loc.getWorld().playSound(loc, before1_13, 1, 1);
      }

  }

}
