package me.jimfutsu.roundpvpgame.gamemech;

import me.jimfutsu.roundpvpgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathManagment implements Listener{

    private Main plugin;

    public DeathManagment(Main plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = (Player) e.getEntity();
        if(plugin.ingame.contains(p.getName())){
            plugin.ingame.remove(p.getName());
            if(plugin.kit.containsKey(p.getName())){
                plugin.kit.remove(p.getName());
            }
            p.getInventory().clear();
            p.getInventory().setHelmet(null);
            p.getInventory().setChestplate(null);
            p.getInventory().setLeggings(null);
            p.getInventory().setBoots(null);
            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "Player " + ChatColor.YELLOW + p.getName() + ChatColor. AQUA + " has been ELIMINATED from the current 'game'!");
            if(plugin.ingame.size() <= 1){
                for(String player : plugin.ingame){
                    Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The games have ended, and " + ChatColor.YELLOW + player + ChatColor.AQUA + " has been declared the victor!");
                    plugin.isGame = false;
                    Bukkit.getServer().getPlayer(player).teleport(Bukkit.getServer().getPlayer(player).getWorld().getSpawnLocation());
                }
            }
        }
    }
}
