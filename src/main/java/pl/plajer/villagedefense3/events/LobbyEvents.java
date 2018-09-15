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

package pl.plajer.villagedefense3.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.arena.Arena;
import pl.plajer.villagedefense3.arena.ArenaRegistry;
import pl.plajer.villagedefense3.arena.ArenaState;
import pl.plajerlair.core.services.exception.ReportedException;

/**
 * Created by Tom on 16/06/2015.
 */
public class LobbyEvents implements Listener {

  private Main plugin;

  public LobbyEvents(Main plugin) {
    this.plugin = plugin;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFoodLose(FoodLevelChangeEvent event) {
    try {
      if (event.getEntity().getType() != EntityType.PLAYER) {
        return;
      }
      Player player = (Player) event.getEntity();
      if (ArenaRegistry.getArena(player) == null) {
        return;
      }
      Arena arena = ArenaRegistry.getArena(player);
      if (arena.getArenaState() == ArenaState.STARTING || arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS) {
        event.setCancelled(true);
      }
    } catch (Exception ex) {
      new ReportedException(plugin, ex);
    }
  }

  @EventHandler
  public void onLobbyDamage(EntityDamageEvent event) {
    try {
      if (event.getEntity().getType() != EntityType.PLAYER) {
        return;
      }
      Player player = (Player) event.getEntity();
      Arena arena = ArenaRegistry.getArena(player);
      if (arena == null || arena.getArenaState() == ArenaState.IN_GAME) {
        return;
      }
      event.setCancelled(true);
      player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    } catch (Exception ex) {
      new ReportedException(plugin, ex);
    }
  }

}
