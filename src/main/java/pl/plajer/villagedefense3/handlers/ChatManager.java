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

package pl.plajer.villagedefense3.handlers;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.arena.Arena;
import pl.plajer.villagedefense3.handlers.language.LanguageManager;
import pl.plajer.villagedefense3.utils.MessageUtils;
import pl.plajerlair.core.services.exception.ReportedException;
import pl.plajerlair.core.utils.MinigameUtils;

/**
 * Created by Tom on 27/07/2014.
 */
public class ChatManager {

  public static String PLUGIN_PREFIX;

  public ChatManager(String prefix) {
    PLUGIN_PREFIX = prefix;
  }

  public static String colorRawMessage(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public static void broadcast(Arena arena, String message) {
    for (Player p : arena.getPlayers()) {
      p.sendMessage(PLUGIN_PREFIX + message);
    }
  }

  public static String colorMessage(String message) {
    try {
      return ChatColor.translateAlternateColorCodes('&', LanguageManager.getLanguageMessage(message));
    } catch (NullPointerException e1) {
      new ReportedException(JavaPlugin.getPlugin(Main.class), e1);
      e1.printStackTrace();
      MessageUtils.errorOccured();
      Bukkit.getConsoleSender().sendMessage("Game message not found!");
      if (LanguageManager.isDefaultLanguageUsed()) {
        Bukkit.getConsoleSender().sendMessage("Please regenerate your language.yml file! If error still occurs report it to the developer!");
      } else {
        Bukkit.getConsoleSender().sendMessage("Locale message string not found! Please contact developer!");
      }
      Bukkit.getConsoleSender().sendMessage("Access string: " + message);
      return "ERR_MESSAGE_NOT_FOUND";
    }
  }

  public static String formatMessage(Arena arena, String message, int integer) {
    String returnString = message;
    returnString = StringUtils.replace(returnString, "%NUMBER%", Integer.toString(integer));
    returnString = colorRawMessage(formatPlaceholders(returnString, arena));
    return returnString;
  }

  public static String formatMessage(Arena arena, String message, Player player) {
    String returnString = message;
    returnString = StringUtils.replace(returnString, "%PLAYER%", player.getName());
    returnString = colorRawMessage(formatPlaceholders(returnString, arena));
    return returnString;
  }

  private static String formatPlaceholders(String message, Arena arena) {
    String returnString = message;
    returnString = StringUtils.replace(returnString, "%TIME%", Integer.toString(arena.getTimer()));
    returnString = StringUtils.replace(returnString, "%FORMATTEDTIME%", MinigameUtils.formatIntoMMSS((arena.getTimer())));
    returnString = StringUtils.replace(returnString, "%PLAYERSIZE%", Integer.toString(arena.getPlayers().size()));
    returnString = StringUtils.replace(returnString, "%MAXPLAYERS%", Integer.toString(arena.getMaximumPlayers()));
    returnString = StringUtils.replace(returnString, "%MINPLAYERS%", Integer.toString(arena.getMinimumPlayers()));
    return returnString;
  }

  public static void broadcastAction(Arena a, Player p, ActionType action) {
    switch (action) {
      case JOIN:
        broadcast(a, formatMessage(a, ChatManager.colorMessage("In-Game.Messages.Join"), p));
        break;
      case LEAVE:
        broadcast(a, formatMessage(a, ChatManager.colorMessage("In-Game.Messages.Leave"), p));
        break;
      case DEATH:
        broadcast(a, formatMessage(a, ChatManager.colorMessage("In-Game.Messages.Death"), p));
        break;
    }
  }

  public enum ActionType {
    JOIN, LEAVE, DEATH
  }

}

