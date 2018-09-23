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

package pl.plajer.villagedefense3.arena.initializers;

import java.util.Random;

import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.GenericAttributes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pl.plajer.villagedefense3.Main;
import pl.plajer.villagedefense3.arena.Arena;
import pl.plajer.villagedefense3.creatures.CreatureUtils;
import pl.plajer.villagedefense3.creatures.v1_13_R2.BabyZombie;
import pl.plajer.villagedefense3.creatures.v1_13_R2.FastZombie;
import pl.plajer.villagedefense3.creatures.v1_13_R2.GolemBuster;
import pl.plajer.villagedefense3.creatures.v1_13_R2.HardZombie;
import pl.plajer.villagedefense3.creatures.v1_13_R2.PlayerBuster;
import pl.plajer.villagedefense3.creatures.v1_13_R2.RidableIronGolem;
import pl.plajer.villagedefense3.creatures.v1_13_R2.RidableVillager;
import pl.plajer.villagedefense3.creatures.v1_13_R2.TankerZombie;
import pl.plajer.villagedefense3.creatures.v1_13_R2.VillagerSlayer;
import pl.plajer.villagedefense3.creatures.v1_13_R2.WorkingWolf;
import pl.plajer.villagedefense3.handlers.ChatManager;
import pl.plajer.villagedefense3.utils.XMaterial;

/**
 * @author Plajer
 * <p>
 * Created at 16.07.2018
 */
public class ArenaInitializer1_13_R2 extends Arena {

  public ArenaInitializer1_13_R2(String ID, Main plugin) {
    super(ID, plugin);
  }

  public void spawnFastZombie(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    FastZombie fastZombie = new FastZombie(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.subtractZombiesToSpawn();
  }

  @Override
  public void spawnHalfInvisibleZombie(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    FastZombie fastZombie = new FastZombie(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.setRemoveWhenFarAway(false);
    zombie.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.subtractZombiesToSpawn();
  }

  @Override
  public void spawnKnockbackResistantZombies(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    TankerZombie fastZombie = new TankerZombie(location.getWorld());
    fastZombie.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setItemInMainHand(XMaterial.GOLDEN_AXE.parseItem());
    zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.subtractZombiesToSpawn();
  }

  public void spawnBabyZombie(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    BabyZombie fastZombie = new BabyZombie(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    CreatureUtils.applyHealthBar(zombie);
    zombie.setRemoveWhenFarAway(false);
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    this.addZombie((Zombie) fastZombie.getBukkitEntity());

    super.subtractZombiesToSpawn();
  }

  public void spawnHardZombie(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    HardZombie fastZombie = new HardZombie(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie(zombie);
    super.subtractZombiesToSpawn();
  }

  @Override
  public void spawnSoftHardZombie(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    HardZombie fastZombie = new HardZombie(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie(zombie);
    super.subtractZombiesToSpawn();
  }

  public void spawnGolemBuster(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    GolemBuster fastZombie = new GolemBuster(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setHelmet(new ItemStack(Material.TNT));
    zombie.getEquipment().setHelmetDropChance(0.0F);
    zombie.getEquipment().setItemInMainHandDropChance(0F);
    zombie.setRemoveWhenFarAway(false);
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie(zombie);

    super.subtractZombiesToSpawn();
  }

  public void spawnPlayerBuster(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size()));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    PlayerBuster fastZombie = new PlayerBuster(location.getWorld());
    fastZombie.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(fastZombie, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) fastZombie.getBukkitEntity();
    zombie.getEquipment().setHelmet(new ItemStack(Material.TNT));
    zombie.getEquipment().setHelmetDropChance(0.0F);
    zombie.getEquipment().setItemInMainHandDropChance(0F);
    zombie.getEquipment().setBoots(XMaterial.GOLDEN_BOOTS.parseItem());
    zombie.getEquipment().setLeggings(XMaterial.GOLDEN_LEGGINGS.parseItem());
    zombie.getEquipment().setChestplate(XMaterial.GOLDEN_CHESTPLATE.parseItem());
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie(zombie);

    super.subtractZombiesToSpawn();
  }

  public void spawnVillagerSlayer(Random random) {
    Location location = zombieSpawns.get(random.nextInt(zombieSpawns.size() - 1));
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    VillagerSlayer villagerSlayer = new VillagerSlayer(location.getWorld());
    villagerSlayer.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(villagerSlayer, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Zombie zombie = (Zombie) villagerSlayer.getBukkitEntity();
    zombie.getEquipment().setItemInMainHand(new ItemStack(Material.EMERALD));
    zombie.getEquipment().setItemInMainHandDropChance(0F);
    zombie.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    zombie.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
    zombie.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
    zombie.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
    CreatureUtils.applyHealthBar(zombie);
    this.addZombie(zombie);

    super.subtractZombiesToSpawn();
  }

  public void spawnVillager(Location location) {
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    RidableVillager ridableVillager = new RidableVillager(location.getWorld());
    ridableVillager.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(ridableVillager, CreatureSpawnEvent.SpawnReason.CUSTOM);
    Villager villager = (Villager) ridableVillager.getBukkitEntity();
    villager.setRemoveWhenFarAway(false);
    this.addVillager((Villager) ridableVillager.getBukkitEntity());
  }

  public void spawnGolem(Location location, Player player) {
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    RidableIronGolem ironGolem = new RidableIronGolem(location.getWorld());
    ironGolem.setPosition(location.getX(), location.getY(), location.getZ());
    ironGolem.setCustomName(new ChatMessage(ChatManager.colorMessage("In-Game.Spawned-Golem-Name").replace("%player%", player.getName())));
    ironGolem.setCustomNameVisible(true);
    mcWorld.addEntity(ironGolem, CreatureSpawnEvent.SpawnReason.CUSTOM);

    this.addIronGolem((org.bukkit.entity.IronGolem) ironGolem.getBukkitEntity());
  }

  public void spawnWolf(Location location, Player player) {
    net.minecraft.server.v1_13_R2.World mcWorld = ((CraftWorld) location.getWorld()).getHandle();
    WorkingWolf wolf = new WorkingWolf(location.getWorld());
    wolf.setPosition(location.getX(), location.getY(), location.getZ());
    mcWorld.addEntity(wolf, CreatureSpawnEvent.SpawnReason.CUSTOM);
    wolf.setCustomName(new ChatMessage(ChatManager.colorMessage("In-Game.Spawned-Wolf-Name").replace("%player%", player.getName())));
    wolf.setCustomNameVisible(true);
    wolf.setInvisible(false);
    ((Wolf) wolf.getBukkitEntity()).setOwner(player);

    this.addWolf((Wolf) wolf.getBukkitEntity());
  }

}
