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

package pl.plajer.villagedefense3.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.arena.Arena;
import pl.plajer.villagedefense3.arena.ArenaRegistry;
import pl.plajer.villagedefense3.arena.ArenaUtils;
import pl.plajer.villagedefense3.handlers.ChatManager;
import pl.plajer.villagedefense3.handlers.setup.SetupInventory;
import pl.plajer.villagedefense3.utils.StringMatcher;
import pl.plajer.villagedefense3.utils.Utils;
import pl.plajer.villagedefense3.utils.XMaterial;
import pl.plajerlair.core.services.exception.ReportedException;
import pl.plajerlair.core.utils.ConfigUtils;
import pl.plajerlair.core.utils.LocationUtils;

/**
 * Created by Tom on 7/08/2014.
 */
public class MainCommand implements CommandExecutor {

  private Main plugin;
  private AdminCommands adminCommands;
  private GameCommands gameCommands;

  public MainCommand(Main plugin, boolean register) {
    this.plugin = plugin;
    if (register) {
      adminCommands = new AdminCommands(plugin);
      gameCommands = new GameCommands(plugin);
      TabCompletion completion = new TabCompletion(plugin);
      plugin.getCommand("villagedefense").setExecutor(this);
      plugin.getCommand("villagedefense").setTabCompleter(completion);
      plugin.getCommand("villagedefenseadmin").setExecutor(this);
      plugin.getCommand("villagedefenseadmin").setTabCompleter(completion);
    }
  }

  public AdminCommands getAdminCommands() {
    return adminCommands;
  }

  boolean checkSenderPlayer(CommandSender sender) {
    if (sender instanceof Player) {
      return true;
    }
    sender.sendMessage(ChatManager.colorMessage("Commands.Only-By-Player"));
    return false;
  }

  boolean checkIsInGameInstance(Player player) {
    if (ArenaRegistry.getArena(player) == null) {
      player.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.Not-Playing"));
      return false;
    }
    return true;
  }

  boolean hasPermission(CommandSender sender, String perm) {
    if (sender.hasPermission(perm)) {
      return true;
    }
    sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.No-Permission"));
    return false;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    try {
      if (cmd.getName().equalsIgnoreCase("villagedefenseadmin")) {
        if (args.length == 0) {
          adminCommands.sendHelp(sender);
          return true;
        }
        if (args[0].equalsIgnoreCase("stop")) {
          adminCommands.stopGame(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("list")) {
          adminCommands.printList(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("forcestart")) {
          adminCommands.forceStartGame(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("respawn")) {
          if (args.length == 1) {
            adminCommands.respawn(sender);
          } else {
            adminCommands.respawnOther(sender, args[1]);
          }
          return true;
        } else if (args[0].equalsIgnoreCase("spychat")) {
          adminCommands.toggleSpyChat(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
          adminCommands.reloadInstances(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("delete")) {
          if (args.length != 1) {
            adminCommands.deleteArena(sender, args[1]);
          } else {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.Type-Arena-Name"));
          }
          return true;
        } else if (args[0].equalsIgnoreCase("setprice")) {
          if (args.length != 1) {
            adminCommands.setItemPrice(sender, args[1]);
          } else {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type price of item!");
          }
          return true;
        } else if (args[0].equalsIgnoreCase("tp")) {
          if (args.length == 1) {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.Type-Arena-Name"));
            return true;
          }
          if (args.length == 2) {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type location type: END, START, LOBBY");
            return true;
          }
          adminCommands.teleportToInstance(sender, args[1], args[2]);
          return true;
        } else if (args[0].equalsIgnoreCase("clear")) {
          if (args.length == 1) {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type valid mob type to clear: VILLAGER, ZOMBIE, GOLEM");
            return true;
          }
          if (args[1].equalsIgnoreCase("villager")) {
            adminCommands.clearVillagers(sender);
          } else if (args[1].equalsIgnoreCase("zombie")) {
            adminCommands.clearZombies(sender);
          } else if (args[1].equalsIgnoreCase("golem")) {
            adminCommands.clearGolems(sender);
          } else {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type valid mob type to clear: VILLAGER, ZOMBIE, GOLEM");
            return true;
          }
          return true;
        } else if (args[0].equalsIgnoreCase("addorbs")) {
          if (args.length == 1) {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type number of orbs to give!");
            return true;
          }
          if (args.length == 2) {
            adminCommands.addOrbs(sender, args[1]);
          } else {
            adminCommands.addOrbsOther(sender, args[2], args[1]);
          }
          return true;
        } else if (args[0].equalsIgnoreCase("setwave")) {
          if (args.length == 1) {
            sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type number of wave to set!");
            return true;
          }
          adminCommands.setWave(sender, args[1]);
          return true;
        }
        adminCommands.sendHelp(sender);
        List<StringMatcher.Match> matches = StringMatcher.match(args[0], Arrays.asList("stop", "list", "forcestart", "respawn", "spychat",
            "reload", "setshopchest", "delete", "setprice", "tp", "clear", "addorbs", "setwave"));
        if (!matches.isEmpty()) {
          sender.sendMessage(ChatManager.colorMessage("Commands.Did-You-Mean").replace("%command%", "vda " + matches.get(0).getMatch()));
        }
        return true;
      }
      if (cmd.getName().equalsIgnoreCase("villagedefense")) {
        if (args.length == 0) {
          sender.sendMessage(ChatManager.colorMessage("Commands.Main-Command.Header"));
          sender.sendMessage(ChatManager.colorMessage("Commands.Main-Command.Description"));
          if (sender.hasPermission("villagedefense.admin")) {
            sender.sendMessage(ChatManager.colorMessage("Commands.Main-Command.Admin-Bonus-Description"));
          }
          sender.sendMessage(ChatManager.colorMessage("Commands.Main-Command.Footer"));
          return true;
        }
        if (args.length > 1) {
          if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("addspawn") || args[1].equalsIgnoreCase("edit")) {
            if (!checkSenderPlayer(sender) || !hasPermission(sender, "villagedefense.admin.create")) {
              return true;
            }
            adminCommands.performSetup(sender, args);
            return true;
          }
        }
        if (args[0].equalsIgnoreCase("join")) {
          if (args.length == 2) {
            gameCommands.joinGame(sender, args[1]);
            return true;
          }
          sender.sendMessage(ChatManager.colorMessage("Commands.Type-Arena-Name"));
          return true;
        } else if (args[0].equalsIgnoreCase("randomjoin")) {
          gameCommands.joinRandomGame(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("stats")) {
          if (args.length == 2) {
            gameCommands.sendStatsOther(sender, args[1]);
          }
          gameCommands.sendStats(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("top")) {
          if (args.length == 2) {
            gameCommands.sendTopStatistics(sender, args[1]);
          } else {
            sender.sendMessage(ChatManager.colorMessage("Commands.Statistics.Type-Name"));
          }
          return true;
        } else if (args[0].equalsIgnoreCase("leave")) {
          gameCommands.leaveGame(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("selectkit")) {
          gameCommands.openKitMenu(sender);
          return true;
        } else if (args[0].equalsIgnoreCase("create")) {
          if (args.length == 2) {
            adminCommands.createArena(sender, args);
            return true;
          }
          sender.sendMessage(ChatManager.colorMessage("Commands.Type-Arena-Name"));
          return true;
        } else if (args[0].equalsIgnoreCase("admin")) {
          if (args.length == 1) {
            adminCommands.sendHelp(sender);
            return true;
          }
          if (args[1].equalsIgnoreCase("stop")) {
            adminCommands.stopGame(sender);
            return true;
          } else if (args[1].equalsIgnoreCase("list")) {
            adminCommands.printList(sender);
            return true;
          } else if (args[1].equalsIgnoreCase("forcestart")) {
            adminCommands.forceStartGame(sender);
            return true;
          } else if (args[1].equalsIgnoreCase("respawn")) {
            if (args.length == 2) {
              adminCommands.respawn(sender);
            } else {
              adminCommands.respawnOther(sender, args[2]);
            }
            return true;
          } else if (args[1].equalsIgnoreCase("spychat")) {
            adminCommands.toggleSpyChat(sender);
            return true;
          } else if (args[1].equalsIgnoreCase("reload")) {
            adminCommands.reloadInstances(sender);
            return true;
          } else if (args[1].equalsIgnoreCase("delete")) {
            if (args.length != 2) {
              adminCommands.deleteArena(sender, args[2]);
            } else {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.Type-Arena-Name"));
            }
            return true;
          } else if (args[1].equalsIgnoreCase("setprice")) {
            if (args.length != 2) {
              adminCommands.setItemPrice(sender, args[2]);
            } else {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type price of item!");
            }
            return true;
          } else if (args[1].equalsIgnoreCase("tp")) {
            if (args.length == 2) {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.Type-Arena-Name"));
              return true;
            }
            if (args.length == 3) {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type location type: END, START, LOBBY");
              return true;
            }
            adminCommands.teleportToInstance(sender, args[2], args[3]);
            return true;
          } else if (args[1].equalsIgnoreCase("clear")) {
            if (args.length == 2) {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type valid mob type to clear: VILLAGER, ZOMBIE, GOLEM");
              return true;
            }
            if (args[2].equalsIgnoreCase("villager")) {
              adminCommands.clearVillagers(sender);
            } else if (args[2].equalsIgnoreCase("zombie")) {
              adminCommands.clearZombies(sender);
            } else if (args[2].equalsIgnoreCase("golem")) {
              adminCommands.clearGolems(sender);
            } else {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type valid mob type to clear: VILLAGER, ZOMBIE, GOLEM");
              return true;
            }
            return true;
          } else if (args[1].equalsIgnoreCase("addorbs")) {
            if (args.length == 2) {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type number of orbs to give!");
              return true;
            }
            if (args.length == 3) {
              adminCommands.addOrbs(sender, args[2]);
            } else {
              adminCommands.addOrbsOther(sender, args[3], args[2]);
            }
            return true;
          } else if (args[1].equalsIgnoreCase("setwave")) {
            if (args.length == 2) {
              sender.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "Please type number of wave to set!");
              return true;
            }
            adminCommands.setWave(sender, args[2]);
            return true;
          }
          adminCommands.sendHelp(sender);
          List<StringMatcher.Match> matches = StringMatcher.match(args[1], Arrays.asList("stop", "list", "forcestart", "respawn", "spychat",
              "reload", "setshopchest", "delete", "setprice", "tp", "clear", "addorbs", "setwave"));
          if (!matches.isEmpty()) {
            sender.sendMessage(ChatManager.colorMessage("Commands.Did-You-Mean").replace("%command%", "vd admin " + matches.get(0).getMatch()));
          }
          return true;
        } else {
          List<StringMatcher.Match> matches = StringMatcher.match(args[0], Arrays.asList("join", "leave", "stats", "top", "admin", "create", "selectkit"));
          if (!matches.isEmpty()) {
            sender.sendMessage(ChatManager.colorMessage("Commands.Did-You-Mean").replace("%command%", "vd " + matches.get(0).getMatch()));
          }
          return true;
        }
      }
      return false;
    } catch (Exception e) {
      new ReportedException(plugin, e);
      return false;
    }
  }

  void onTpCommand(Player player, String ID, LocationType type) {
    if (!ConfigUtils.getConfig(plugin, "arenas").contains("instances." + ID)) {
      player.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.No-Arena-Like-That"));
      return;
    }
    Arena arena = ArenaRegistry.getArena(ID);
    switch (type) {
      case LOBBY:
        if (arena.getLobbyLocation() == null) {
          player.sendMessage(ChatColor.RED + "Lobby location isn't set for this arena!");
          return;
        }
        arena.teleportToLobby(player);
        player.sendMessage(ChatColor.GRAY + "Teleported to LOBBY location from arena" + ID);
        break;
      case START:
        if (arena.getLobbyLocation() == null) {
          player.sendMessage(ChatColor.RED + "Start location isn't set for this arena!");
          return;
        }
        arena.teleportToStartLocation(player);
        player.sendMessage(ChatColor.GRAY + "Teleported to START location from arena" + ID);
        break;
      case END:
        if (arena.getLobbyLocation() == null) {
          player.sendMessage(ChatColor.RED + "End location isn't set for this arena!");
          return;
        }
        arena.teleportToEndLocation(player);
        player.sendMessage(ChatColor.GRAY + "Teleported to END location from arena" + ID);
        break;
      default:
        break; //o.o
    }
  }

  void performSetup(Player player, String[] args) {
    if (args[1].equalsIgnoreCase("setup") || args[1].equals("edit")) {
      if (ArenaRegistry.getArena(args[0]) == null) {
        player.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.No-Arena-Like-That"));
        return;
      }
      new SetupInventory(ArenaRegistry.getArena(args[0])).openInventory(player);
      return;
    }
    if (!(args.length > 2)) {
      return;
    }
    FileConfiguration config = ConfigUtils.getConfig(plugin, "arenas");
    if (!config.contains("instances." + args[0])) {
      player.sendMessage(ChatManager.PLUGIN_PREFIX + ChatManager.colorMessage("Commands.No-Arena-Like-That"));
      return;
    }

    if (args[1].equalsIgnoreCase("addspawn")) {
      if (args[2].equalsIgnoreCase("zombie")) {
        int i;
        if (!config.contains("instances." + args[0] + ".zombiespawns")) {
          i = 0;
        } else {
          i = config.getConfigurationSection("instances." + args[0] + ".zombiespawns").getKeys(false).size();
        }
        i++;
        LocationUtils.saveLoc(plugin, config, "arenas", "instances." + args[0] + ".zombiespawns." + i, player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Zombie spawn added!");
        return;
      }
      if (args[2].equalsIgnoreCase("villager")) {
        int i;
        if (!config.contains("instances." + args[0] + ".villagerspawns")) {
          i = 0;
        } else {
          i = config.getConfigurationSection("instances." + args[0] + ".villagerspawns").getKeys(false).size();
        }

        i++;
        LocationUtils.saveLoc(plugin, config, "arenas", "instances." + args[0] + ".villagerspawns." + i, player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Villager spawn added!");
        return;
      }
      if (args[2].equalsIgnoreCase("doors")) {
        Block block = player.getTargetBlock(null, 10);
        if (plugin.is1_11_R1() || plugin.is1_12_R1()) {
          if (block.getType() != Material.WOODEN_DOOR) {
            player.sendMessage(ChatColor.RED + "Target block is not oak door!");
            return;
          }
        } else {
          if (block.getType() != XMaterial.OAK_DOOR.parseMaterial()) {
            player.sendMessage(ChatColor.RED + "Target block is not oak door!");
            return;
          }
        }
        String ID = args[0];
        int i;
        if (!config.contains("instances." + ID + ".doors")) {
          i = 0;
        } else {
          i = config.getConfigurationSection("instances." + ID + ".doors").getKeys(false).size();
        }
        i++;

        Block relativeBlock = null;
        if (plugin.is1_11_R1() || plugin.is1_12_R1()) {
          if (block.getRelative(BlockFace.DOWN).getType() == Material.WOODEN_DOOR) {
            relativeBlock = block;
            block = block.getRelative(BlockFace.DOWN);
          } else if (block.getRelative(BlockFace.UP).getType() == Material.WOODEN_DOOR) {
            relativeBlock = block.getRelative(BlockFace.UP);
          }
        } else {
          if (block.getRelative(BlockFace.DOWN).getType() == XMaterial.OAK_DOOR.parseMaterial()) {
            relativeBlock = block;
            block = block.getRelative(BlockFace.DOWN);
          } else if (block.getRelative(BlockFace.UP).getType() == XMaterial.OAK_DOOR.parseMaterial()) {
            relativeBlock = block.getRelative(BlockFace.UP);
          }
        }
        if (relativeBlock == null) {
          player.sendMessage("This door doesn't have 2 blocks? Maybe it's bugged? Try placing it again.");
          return;
        }
        String location = block.getWorld().getName() + "," + block.getX() + "," + block.getY() + "," + block.getZ() + ",0.0" + ",0.0";
        String relativeLocation = relativeBlock.getWorld().getName() + "," + relativeBlock.getX() + "," + relativeBlock.getY() + "," + relativeBlock.getZ() + ",0.0" + ",0.0";
        config.set("instances." + ID + ".doors." + i + ".location", relativeLocation);
        config.set("instances." + ID + ".doors." + i + ".byte", 8);
        i++;
        config.set("instances." + ID + ".doors." + i + ".location", location);
        if (plugin.is1_13_R1() || plugin.is1_13_R2()) {
          config.set("instances." + ID + ".doors." + i + ".byte", Utils.getDoorByte(((Door) block.getState().getData()).getFacing()));
        } else {
          config.set("instances." + ID + ".doors." + i + ".byte", block.getData());
        }
        player.sendMessage(ChatColor.GREEN + "Door successfully added!");
        ConfigUtils.saveConfig(plugin, config, "arenas");
        return;
      }
    }
    if (!(args[1].equalsIgnoreCase("set"))) {
      return;
    }
    if (args.length == 3) {
      if (args[2].equalsIgnoreCase("lobbylocation") || args[2].equalsIgnoreCase("lobbyloc")) {
        String location = player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ()
            + "," + player.getLocation().getYaw() + ",0.0";
        config.set("instances." + args[0] + ".lobbylocation", location);
        player.sendMessage("VillageDefense: Lobby location for arena/instance " + args[0] + " set to " + LocationUtils.locationToString(player.getLocation()));
      } else if (args[2].equalsIgnoreCase("Startlocation") || args[2].equalsIgnoreCase("Startloc")) {
        String location = player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ()
            + "," + player.getLocation().getYaw() + ",0.0";
        config.set("instances." + args[0] + ".Startlocation", location);
        player.sendMessage("VillageDefense: Start location for arena/instance " + args[0] + " set to " + LocationUtils.locationToString(player.getLocation()));
      } else if (args[2].equalsIgnoreCase("Endlocation") || args[2].equalsIgnoreCase("Endloc")) {
        String location = player.getLocation().getWorld().getName() + "," + player.getLocation().getX() + "," + player.getLocation().getY() + "," + player.getLocation().getZ()
            + "," + player.getLocation().getYaw() + ",0.0";
        config.set("instances." + args[0] + ".Endlocation", location);
        player.sendMessage("VillageDefense: End location for arena/instance " + args[0] + " set to " + LocationUtils.locationToString(player.getLocation()));
      } else {
        player.sendMessage(ChatColor.RED + "Invalid Command!");
        player.sendMessage(ChatColor.RED + "Usage: /vd <ARENA > set <StartLOCTION | LOBBYLOCATION | EndLOCATION>");
      }
    } else if (args.length == 4) {
      if (args[2].equalsIgnoreCase("MAXPLAYERS") || args[2].equalsIgnoreCase("maximumplayers")) {
        config.set("instances." + args[0] + ".maximumplayers", Integer.parseInt(args[3]));
        player.sendMessage("VillageDefense: Maximum players for arena/instance " + args[0] + " set to " + Integer.parseInt(args[3]));

      } else if (args[2].equalsIgnoreCase("MINPLAYERS") || args[2].equalsIgnoreCase("minimumplayers")) {
        config.set("instances." + args[0] + ".minimumplayers", Integer.parseInt(args[3]));
        player.sendMessage("VillageDefense: Minimum players for arena/instance " + args[0] + " set to " + Integer.parseInt(args[3]));
      } else if (args[2].equalsIgnoreCase("MAPNAME") || args[2].equalsIgnoreCase("NAME")) {
        config.set("instances." + args[0] + ".mapname", args[3]);
        player.sendMessage("VillageDefense: Map name for arena/instance " + args[0] + " set to " + args[3]);
      } else if (args[2].equalsIgnoreCase("WORLD") || args[2].equalsIgnoreCase("MAP")) {
        boolean exists = false;
        for (World world : Bukkit.getWorlds()) {
          if (world.getName().equalsIgnoreCase(args[3])) {
            exists = true;
          }
        }
        if (!exists) {
          player.sendMessage(ChatManager.PLUGIN_PREFIX + ChatColor.RED + "That world doesn't exists!");
          return;
        }
        config.set("instances." + args[0] + ".world", args[3]);
        player.sendMessage("VillageDefense: World for arena/instance " + args[0] + " set to " + args[3]);
      } else {
        player.sendMessage(ChatColor.RED + "Invalid Command!");
        player.sendMessage(ChatColor.RED + "Usage: /vd set <MINPLAYERS | MAXPLAYERS> <value>");
      }
    }
    ConfigUtils.saveConfig(plugin, config, "arenas");
  }

  void createArenaCommand(Player player, String[] args) {
    for (Arena arena : ArenaRegistry.getArenas()) {
      if (arena.getID().equalsIgnoreCase(args[1])) {
        player.sendMessage(ChatColor.DARK_RED + "Arena with that ID already exists!");
        player.sendMessage(ChatColor.DARK_RED + "Usage: /vd create <ID>");
        return;
      }
    }
    if (ConfigUtils.getConfig(plugin, "arenas").contains("instances." + args[1])) {
      player.sendMessage(ChatColor.DARK_RED + "Instance/Arena already exists! Use another ID or delete it first!");
    } else {
      createInstanceInConfig(args[1], player.getWorld().getName());
      player.sendMessage(ChatColor.BOLD + "------------------------------------------");
      player.sendMessage(ChatColor.YELLOW + "      Instance " + args[1] + " created!");
      player.sendMessage("");
      player.sendMessage(ChatColor.GREEN + "Edit this arena via " + ChatColor.GOLD + "/vd " + args[1] + " edit" + ChatColor.GREEN + "!");
      player.sendMessage(ChatColor.GOLD + "Don't know where to start? Check out tutorial video:");
      player.sendMessage(ChatColor.GOLD + "https://bit.ly/2xwRU8S");
      player.sendMessage(ChatColor.BOLD + "------------------------------------------- ");
    }
  }

  private void createInstanceInConfig(String ID, String worldName) {
    String path = "instances." + ID + ".";
    FileConfiguration config = ConfigUtils.getConfig(plugin, "arenas");
    LocationUtils.saveLoc(plugin, config, "arenas", path + "lobbylocation", Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
    LocationUtils.saveLoc(plugin, config, "arenas", path + "Startlocation", Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
    LocationUtils.saveLoc(plugin, config, "arenas", path + "Endlocation", Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
    config.set(path + "minimumplayers", 1);
    config.set(path + "maximumplayers", 10);
    config.set(path + "mapname", ID);
    config.set(path + "signs", new ArrayList<>());
    config.set(path + "isdone", false);
    config.set(path + "world", worldName);
    ConfigUtils.saveConfig(plugin, config, "arenas");

    Arena arena = ArenaUtils.initializeArena(ID);

    arena.setMinimumPlayers(ConfigUtils.getConfig(plugin, "arenas").getInt(path + "minimumplayers"));
    arena.setMaximumPlayers(ConfigUtils.getConfig(plugin, "arenas").getInt(path + "maximumplayers"));
    arena.setMapName(ConfigUtils.getConfig(plugin, "arenas").getString(path + "mapname"));
    arena.setLobbyLocation(LocationUtils.getLocation(ConfigUtils.getConfig(plugin, "arenas").getString(path + "lobbylocation")));
    arena.setStartLocation(LocationUtils.getLocation(ConfigUtils.getConfig(plugin, "arenas").getString(path + "Startlocation")));
    arena.setEndLocation(LocationUtils.getLocation(ConfigUtils.getConfig(plugin, "arenas").getString(path + "Endlocation")));
    arena.setReady(false);

    ArenaRegistry.registerArena(arena);
  }

  enum LocationType {
    LOBBY, END, START
  }

}
