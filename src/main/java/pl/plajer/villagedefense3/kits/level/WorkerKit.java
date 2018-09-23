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

package pl.plajer.villagedefense3.kits.level;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.arena.Arena;
import pl.plajer.villagedefense3.arena.ArenaRegistry;
import pl.plajer.villagedefense3.handlers.ChatManager;
import pl.plajer.villagedefense3.kits.kitapi.KitRegistry;
import pl.plajer.villagedefense3.kits.kitapi.basekits.LevelKit;
import pl.plajer.villagedefense3.user.User;
import pl.plajer.villagedefense3.user.UserManager;
import pl.plajer.villagedefense3.utils.ArmorHelper;
import pl.plajer.villagedefense3.utils.Utils;
import pl.plajer.villagedefense3.utils.WeaponHelper;
import pl.plajer.villagedefense3.utils.XMaterial;
import pl.plajer.villagedefense3.villagedefenseapi.StatsStorage;
import pl.plajerlair.core.services.exception.ReportedException;
import pl.plajerlair.core.utils.ConfigUtils;

/**
 * Created by Tom on 19/07/2015.
 */
public class WorkerKit extends LevelKit implements Listener {

  public WorkerKit(Main plugin) {
    this.setLevel(ConfigUtils.getConfig(plugin, "kits").getInt("Required-Level.Worker"));
    this.setName(ChatManager.colorMessage("Kits.Worker.Kit-Name"));
    List<String> description = Utils.splitString(ChatManager.colorMessage("Kits.Worker.Kit-Description"), 40);
    this.setDescription(description.toArray(new String[0]));
    KitRegistry.registerKit(this);
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }


  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return UserManager.getUser(player.getUniqueId()).getStat(StatsStorage.StatisticType.LEVEL) >= this.getLevel() || player.hasPermission("villagefense.kit.worker");
  }

  @Override
  public void giveKitItems(Player player) {
    ArmorHelper.setColouredArmor(Color.PURPLE, player);
    player.getInventory().addItem(WeaponHelper.getUnBreakingSword(WeaponHelper.ResourceType.WOOD, 10));
    player.getInventory().addItem(WeaponHelper.getEnchantedBow(Enchantment.DURABILITY, 10));
    player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 10));
    player.getInventory().addItem(new ItemStack(XMaterial.OAK_DOOR.parseMaterial(), 2));
  }

  @Override
  public Material getMaterial() {
    return XMaterial.OAK_DOOR.parseMaterial();
  }

  @Override
  public void reStock(Player player) {
    player.getInventory().addItem(XMaterial.OAK_DOOR.parseItem());
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onDoorPlace(BlockPlaceEvent e) {
    try {
      Arena arena = ArenaRegistry.getArena(e.getPlayer());
      if (arena == null) {
        return;
      }
      User user = UserManager.getUser(e.getPlayer().getUniqueId());
      ItemStack stack = e.getPlayer().getInventory().getItemInMainHand();
      if (user.isSpectator() || stack == null || !arena.getDoorLocations().containsKey(e.getBlock().getLocation())
          || !(stack.getType() == XMaterial.OAK_DOOR.parseMaterial())) {
        e.setCancelled(true);
        return;
      }
      e.setCancelled(false);
      e.getPlayer().sendMessage(ChatManager.colorMessage("Kits.Worker.Game-Item-Place-Message"));
    } catch (Exception ex) {
      new ReportedException(JavaPlugin.getPlugin(Main.class), ex);
    }
  }

}
