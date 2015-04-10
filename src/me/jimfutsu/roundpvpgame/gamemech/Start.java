package me.jimfutsu.roundpvpgame.gamemech;

import me.jimfutsu.roundpvpgame.Kits.Archer;
import me.jimfutsu.roundpvpgame.Kits.Mage;
import me.jimfutsu.roundpvpgame.Kits.Warrior;
import me.jimfutsu.roundpvpgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class Start implements Listener{

    ArrayList<String> invincible = new ArrayList<String>();

    private Main plugin;

    public Start(Main plugin)
    {
        this.plugin = plugin;
    }

    public void initiateStart(){
        Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "60" + ChatColor.AQUA + " seconds!");
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "30" + ChatColor.AQUA + " seconds!");
            }
        }, 20 * 30);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "10" + ChatColor.AQUA + " seconds!");
            }
        }, 20*50);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "5" + ChatColor.AQUA + " seconds!");
            }
        }, 20*55);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "4" + ChatColor.AQUA + " seconds!");
            }
        }, 20*56);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "3" + ChatColor.AQUA + " seconds!");
            }
        }, 20*57);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "2" + ChatColor.AQUA + " seconds!");
            }
        }, 20*58);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "The game is starting in " + ChatColor.YELLOW + "1" + ChatColor.AQUA + " seconds!");
            }
        }, 20*59);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "Let the games begin!");
                startGame();
            }
        }, 20*60);
    }

    public void startGame(){
        String gameworld = plugin.config.getString("Game.gameloc.world");
        Integer gamelocx = plugin.config.getInt("Game.gameloc.x");
        Integer gamelocy = plugin.config.getInt("Game.gameloc.y");
        Integer gamelocz = plugin.config.getInt("Game.gameloc.z");
        World lobbyworldtoworld = Bukkit.getWorld(gameworld);

        Location lobbyloc = new Location(lobbyworldtoworld, gamelocx, gamelocy, gamelocz);

        for(String player : plugin.ingame){
            final Player p = Bukkit.getServer().getPlayer(player);
            p.teleport(lobbyloc);
            invincible.add(player);
            //TODO Give player kit items
            if(plugin.kit.containsKey(player)){
                if(plugin.kit.get(player) == "warrior"){
                    Warrior warriorclass = new Warrior(plugin);
                    warriorclass.giveWarriorItems(player);
                }
                if(plugin.kit.get(player) == "archer"){
                    Archer archercclass = new Archer(plugin);
                    archercclass.giveArcherItems(player);
                }
                if(plugin.kit.get(player) == "mage"){
                    Mage mageclass = new Mage(plugin);
                    mageclass.giveMageItems(player);
                }
            }
            else{
                plugin.kit.put(player, "warrior");
                //TODO Give player warrior items
                Warrior warriorclass = new Warrior(plugin);
                warriorclass.giveWarriorItems(player);
            }
            p.sendMessage(ChatColor.AQUA + "You are now invincible for " + ChatColor.YELLOW + "15 " + ChatColor.AQUA + "seconds");
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    invincible.remove(p.getName());
                    p.sendMessage(ChatColor.AQUA + "You are no longer invincible!");
                }
            }, 20 * 15);
        }
    }

    @EventHandler
    public void onHit(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(invincible.contains(p.getName())){
                e.setCancelled(true);
            }
        }
    }

}
