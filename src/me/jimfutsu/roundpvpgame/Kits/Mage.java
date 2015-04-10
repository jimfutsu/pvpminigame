package me.jimfutsu.roundpvpgame.Kits;

import me.jimfutsu.roundpvpgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class Mage implements Listener{

    private Main plugin;

    public Mage(Main plugin)
    {
        this.plugin = plugin;
    }

    HashMap<Integer, String> entity = new HashMap<Integer, String>();
    ArrayList<String> cooldown = new ArrayList<String>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            final Player p = e.getPlayer();
            if(p.getItemInHand().getType() == Material.DIAMOND_HOE){
                if(plugin.kit.containsKey(p.getName())){
                    if(plugin.kit.get(p.getName()) == "mage"){
                        if(!cooldown.contains(p.getName())){
                            Snowball sball = p.launchProjectile(Snowball.class);
                            Vector vector = p.getEyeLocation().getDirection();
                            vector.multiply(3.5D);
                            sball.setVelocity(vector);
                            entity.put(sball.getEntityId(), p.getName());
                            cooldown.add(p.getName());
                            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                                    new Runnable() {
                                        public void run() {
                                            if (cooldown.contains(p.getName())) {
                                                cooldown.remove(p.getName());
                                                p.sendMessage(ChatColor.GREEN + "You can now launch another chicken!");
                                            }
                                        }
                                    }, 400L);
                        }
                        else{
                            p.sendMessage(ChatColor.RED + "You are currently on cooldown! You must wait 15 seconds between uses of your wand!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e){
        if(e.getEntity() instanceof Snowball){
            Integer eid = e.getEntity().getEntityId();
            if(entity.containsKey(eid)){
                Location location = e.getEntity().getLocation();
                e.getEntity().getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 4.0F, false, false);
            }
        }
    }

    public void selectMage(String p){
        Bukkit.getServer().getPlayer(p).sendMessage(ChatColor.AQUA + "You have selected the " + ChatColor.YELLOW + "Mage " + ChatColor.AQUA + "kit!");
        if(!plugin.kit.containsKey(p)){
            plugin.kit.put(p, "mage");
        }
        else{
            plugin.kit.remove(p);
            plugin.kit.put(p, "mage");
        }
    }

    public void giveMageItems(String player){

        Player p = Bukkit.getServer().getPlayer(player);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        ItemStack sword = new ItemStack(Material.GOLD_SWORD);
        ItemStack diamondhoe = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta diamondhoemeta = diamondhoe.getItemMeta();
        diamondhoemeta.setDisplayName(ChatColor.AQUA + "Mage explosive ball shooter");
        diamondhoe.setItemMeta(diamondhoemeta);

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
        p.getInventory().addItem(diamondhoe);

    }

}
