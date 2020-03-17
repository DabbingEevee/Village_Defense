package pl.plajer.villagedefense.handlers.hologram;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import pl.plajer.villagedefense.Main;
import pl.plajer.villagedefense.api.StatsStorage;
import pl.plajer.villagedefense.handlers.hologram.messages.LanguageMessage;

/**
 * @author Plajer
 * <p>
 * Created at 17.06.2019
 */
public class LeaderboardHologram {

  private final int id;
  private StatsStorage.StatisticType statistic;
  private int topAmount;
  private Hologram hologram;
  private Location location;
  private BukkitTask task;

  public LeaderboardHologram(int id, StatsStorage.StatisticType statistic, int amount, Location location) {
    this.id = id;
    this.statistic = statistic;
    this.topAmount = amount;
    this.location = location;
  }

  public void initHologram(Main plugin) {
    hologram = HologramsAPI.createHologram(plugin, location);
    hologramUpdateTask(plugin);
  }

  private void hologramUpdateTask(Main plugin) {
    task = new BukkitRunnable() {
      @Override
      public void run() {
        hologram.clearLines();
        String header = color(plugin.getLanguageConfig().getString(LanguageMessage.HOLOGRAMS_HEADER.getAccessor()));
        header = StringUtils.replace(header, "%amount%", String.valueOf(topAmount));
        header = StringUtils.replace(header, "%statistic%", color(plugin.getLanguageConfig().getString(statisticToMessage().getAccessor())));
        hologram.appendTextLine(header);
        int limit = topAmount;
        LinkedHashMap<UUID, Integer> values = (LinkedHashMap<UUID, Integer>) StatsStorage.getStats(statistic);
        List<UUID> reverseKeys = new ArrayList<>(values.keySet());
        Collections.reverse(reverseKeys);
        for (UUID key : reverseKeys) {
          if (limit == 0) {
            break;
          }
          String format = color(plugin.getLanguageConfig().getString(LanguageMessage.HOLOGRAMS_FORMAT.getAccessor()));
          format = StringUtils.replace(format, "%place%", String.valueOf((topAmount - limit) + 1));
          format = StringUtils.replace(format, "%nickname%", getPlayerNameSafely(key, plugin));
          format = StringUtils.replace(format, "%value%", String.valueOf(values.get(key)));
          hologram.appendTextLine(format);
          limit--;
        }
        if (limit > 0) {
          for (int i = 0; i < limit; limit--) {
            String format = color(plugin.getLanguageConfig().getString(LanguageMessage.HOLOGRAMS_FORMAT_EMPTY.getAccessor()));
            format = StringUtils.replace(format, "%place%", String.valueOf((topAmount - limit) + 1));
            hologram.appendTextLine(format);
          }
        }
      }
    }.runTaskTimer(plugin, 0, 100);
  }

  private String getPlayerNameSafely(UUID uuid, Main plugin) {
    try {
      return Bukkit.getOfflinePlayer(uuid).getName();
    } catch (NullPointerException ex) {
      return color(plugin.getLanguageConfig().getString(LanguageMessage.HOLOGRAMS_UNKNOWN_PLAYER.getAccessor()));
    }
  }

  private LanguageMessage statisticToMessage() {
    switch (statistic) {
      case KILLS:
        return LanguageMessage.STATISTIC_KILLS;
      case DEATHS:
        return LanguageMessage.STATISTIC_DEATHS;
      case GAMES_PLAYED:
        return LanguageMessage.STATISTIC_GAMES_PLAYED;
      case HIGHEST_WAVE:
        return LanguageMessage.STATISTIC_HIGHEST_WAVE;
      case LEVEL:
        return LanguageMessage.STATISTIC_LEVEL;
      case XP:
        return LanguageMessage.STATISTIC_EXP;
      case ORBS:
      default:
        return null;
    }
  }

  public int getId() {
    return id;
  }

  public StatsStorage.StatisticType getStatistic() {
    return statistic;
  }

  public int getTopAmount() {
    return topAmount;
  }

  public Hologram getHologram() {
    return hologram;
  }

  public Location getLocation() {
    return location;
  }

  public void stopLeaderboardUpdateTask() {
    hologram.delete();
    if(task != null && !task.isCancelled()) {
      task.cancel();
    }
  }

  private String color(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

}
