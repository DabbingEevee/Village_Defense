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
import org.bukkit.inventory.ItemStack;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.handlers.ChatManager;
import pl.plajer.villagedefense3.kits.kitapi.KitRegistry;
import pl.plajer.villagedefense3.kits.kitapi.basekits.LevelKit;
import pl.plajer.villagedefense3.user.UserManager;
import pl.plajer.villagedefense3.utils.ArmorHelper;
import pl.plajer.villagedefense3.utils.Utils;
import pl.plajer.villagedefense3.utils.WeaponHelper;
import pl.plajer.villagedefense3.utils.XMaterial;
import pl.plajer.villagedefense3.villagedefenseapi.StatsStorage;
import pl.plajerlair.core.utils.ConfigUtils;

/**
 * Created by Tom on 18/08/2014.
 */
public class PuncherKit extends LevelKit {

  public PuncherKit(Main plugin) {
    setName(ChatManager.colorMessage("Kits.Puncher.Kit-Name"));
    List<String> description = Utils.splitString(ChatManager.colorMessage("Kits.Puncher.Kit-Description"), 40);
    this.setDescription(description.toArray(new String[0]));
    setLevel(ConfigUtils.getConfig(plugin, "kits").getInt("Required-Level.Puncher"));
    KitRegistry.registerKit(this);
  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
    return UserManager.getUser(player.getUniqueId()).getStat(StatsStorage.StatisticType.LEVEL) >= this.getLevel() || player.hasPermission("villagefense.kit.puncher");
  }

  @Override
  public void giveKitItems(Player player) {
    player.getInventory().addItem(WeaponHelper.getEnchanted(XMaterial.DIAMOND_SHOVEL.parseItem(), new Enchantment[] {
            Enchantment.DURABILITY, Enchantment.KNOCKBACK, Enchantment.DAMAGE_ALL}, new int[]{10, 5, 2}));
    ArmorHelper.setColouredArmor(Color.BLACK, player);
    player.getInventory().addItem(WeaponHelper.getEnchantedBow(Enchantment.DURABILITY, 5));
    player.getInventory().addItem(new ItemStack(Material.ARROW, 25));
    player.getInventory().addItem(new ItemStack(XMaterial.COOKED_PORKCHOP.parseMaterial(), 8));
  }

  @Override
  public Material getMaterial() {
    return XMaterial.DIAMOND_SHOVEL.parseMaterial();
  }

  @Override
  public void reStock(Player player) {

  }
}
