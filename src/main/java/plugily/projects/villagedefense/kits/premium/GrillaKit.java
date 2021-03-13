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

import org.apache.logging.log4j.core.util.UuidUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.util.UUID;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTList;
import de.tr7zw.nbtapi.NBTListCompound;
import io.netty.handler.codec.base64.Base64;
import pl.plajerlair.commonsbox.minecraft.helper.ArmorHelper;
import pl.plajerlair.commonsbox.minecraft.item.ItemBuilder;
import plugily.projects.villagedefense.api.StatsStorage;
import plugily.projects.villagedefense.kits.KitRegistry;
import plugily.projects.villagedefense.kits.basekits.LevelKit;
import plugily.projects.villagedefense.utils.Utils;

/**
 * @author ExistingEevee
 * <p>
 * Created at 03.12.2021
 */
public class GrillaKit extends LevelKit implements Listener {

  public GrillaKit() {
    setName("§4Grilla§r");
    List<String> description = Utils.splitString("Unlocked for those who are truly dedicated to the grill", 40);
    setDescription(description.toArray(new String[0]));
    KitRegistry.registerKit(this);
    getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    setLevel(8);

  }

  @Override
  public boolean isUnlockedByPlayer(Player player) {
      return getPlugin().getUserManager().getUser(player).getStat(StatsStorage.StatisticType.LEVEL) >= this.getLevel() || player.hasPermission("villagedefense.kit.grila") || 
    		  player.getUniqueId().toString().equals("9c479b2f-4369-4ff3-87df-081a06acd15e") || 
    		  player.getUniqueId().toString().equals("d4241c31-87b4-487b-85f0-3aaf84be4025") || 
    		  player.getUniqueId().toString().equals("d7502c55-c17f-4d0c-8144-d20ed136a729");
  }

  @Override
  public void giveKitItems(Player player) {
	ArmorHelper.setColouredArmor(Color.ORANGE, player);

    player.getInventory().addItem(new ItemBuilder(Material.FLINT_AND_STEEL).enchantment(Enchantment.FIRE_ASPECT, 10).enchantment(Enchantment.DAMAGE_ALL, 4).name("Griller's Lighter").build());
    player.getInventory().addItem(new ItemStack(Material.SADDLE));
    
    ItemStack grillHat = new ItemStack(Material.PLAYER_HEAD);

    String textureValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNjZmQ5NGU5MjVlYWI0MzMwYTc2OGFmY2FlNmMxMjhiMGEyOGUyMzE0OWVlZTQxYzljNmRmODk0YzI0ZjNkZSJ9fX0=="; 

    NBTItem nbti = new NBTItem(grillHat); // Creating the wrapper.

    NBTCompound disp = nbti.addCompound("display");
    disp.setString("Name", "Grilla's Helmet"); // Setting the name of the Item

    NBTCompound skull = nbti.addCompound("SkullOwner"); // Getting the compound, that way we can set the skin information
    skull.setString("Name", "Grilla's Helmet"); // Owner's name

    NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
    texture.setString("Value",  textureValue);

    ItemMeta grillHatMeta = nbti.getItem().getItemMeta(); // Refresh the ItemStack
    
    grillHatMeta.setDisplayName("§aGrilla's Helmet");
    
    grillHat.setItemMeta(grillHatMeta);
    
    player.getInventory().setHelmet(grillHat);
  }

  @Override
  public Material getMaterial() {
    return Material.FLINT_AND_STEEL;
  }

  @Override
  public void reStock(Player player) {
	  //none
  }
}
