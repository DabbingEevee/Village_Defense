/*
 * Village Defense - Protect villagers from hordes of zombies
 * Copyright (C) 2021  Plugily Projects - maintained by 2Wild4You, Tigerpanzer_02 and contributors
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

package plugily.projects.villagedefense.kits.premium;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.plajerlair.commonsbox.minecraft.helper.WeaponHelper;
import plugily.projects.villagedefense.api.StatsStorage;
import plugily.projects.villagedefense.handlers.PermissionsManager;
import plugily.projects.villagedefense.handlers.language.Messages;
import plugily.projects.villagedefense.kits.KitRegistry;
import plugily.projects.villagedefense.kits.basekits.LevelKit;
import plugily.projects.villagedefense.utils.Utils;

/**
 * Created by Tom on 28/07/2015.
 */
public class PremiumHardcoreKit extends LevelKit {

    public PremiumHardcoreKit() {
        setName(getPlugin().getChatManager().colorMessage(Messages.KITS_PREMIUM_HARDCORE_NAME));
        List<String> description = Utils.splitString(getPlugin().getChatManager().colorMessage(Messages.KITS_PREMIUM_HARDCORE_DESCRIPTION), 40);
        setDescription(description.toArray(new String[0]));
        KitRegistry.registerKit(this);
        setLevel(10);

    }

    @Override
    public boolean isUnlockedByPlayer(Player player) {
        return getPlugin().getUserManager().getUser(player).getStat(StatsStorage.StatisticType.LEVEL) >= this.getLevel() || player.hasPermission("villagedefense.kit.terminator");
    }

    @Override
    public void giveKitItems(Player player) {
    player.getInventory().addItem(WeaponHelper.getEnchanted(new ItemStack(getMaterial()),
        new Enchantment[] {Enchantment.DAMAGE_ALL}, new int[] {15}));
    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(6);
    player.getInventory().addItem(new ItemStack(Material.SADDLE));
  }

  @Override
  public Material getMaterial() {
    return Material.DIAMOND_SWORD;
  }

  @Override
  public void reStock(Player player) {
    //no restock items for this kit
  }


}
