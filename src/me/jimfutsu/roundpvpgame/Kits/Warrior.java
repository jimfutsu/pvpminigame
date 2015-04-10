package me.jimfutsu.roundpvpgame.Kits;

import me.jimfutsu.roundpvpgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Warrior implements Listener{

    private Main plugin;

    public Warrior(Main plugin)
    {
        this.plugin = plugin;
    }

    public void selectWarrior(String p){
        Bukkit.getServer().getPlayer(p).sendMessage(ChatColor.AQUA + "You have selected the " + ChatColor.YELLOW + "Warrior " + ChatColor.AQUA + "kit!");
        if(!plugin.kit.containsKey(p)){
            plugin.kit.put(p, "warrior");
        }
        else{
            plugin.kit.remove(p);
            plugin.kit.put(p, "warrior");
        }
    }

    public void giveWarriorItems(String player){

        Player p = Bukkit.getServer().getPlayer(player);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS);
        ItemStack sword = new ItemStack(Material.IRON_SWORD);

        p.getInventory().clear();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);

        p.getInventory().setHelmet(helmet);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setLeggings(leggings);
        p.getInventory().setBoots(boots);
        p.getInventory().addItem(sword);

    }

}
