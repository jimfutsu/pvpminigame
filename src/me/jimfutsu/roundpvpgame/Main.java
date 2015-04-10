package me.jimfutsu.roundpvpgame;

import me.jimfutsu.roundpvpgame.Kits.Archer;
import me.jimfutsu.roundpvpgame.Kits.Mage;
import me.jimfutsu.roundpvpgame.Kits.Warrior;
import me.jimfutsu.roundpvpgame.gamemech.Start;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin implements Listener{

    public FileConfiguration config = getConfig();

    public boolean loaded = false;
    public boolean kitloaded = false;
    public boolean isGame = false;

    public static Inventory gameselector = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Game Selector");
    public static Inventory kitselector = Bukkit.createInventory(null, 9, ChatColor.YELLOW + "Kit Selector");

    @Override
    public void onEnable(){
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new Warrior(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Archer(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Mage(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Start(this), this);

        //Begin load host GUI
        if(!loaded){
            ItemStack diamondsword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta diamondswordmeta = diamondsword.getItemMeta();
            diamondswordmeta.setDisplayName(ChatColor.AQUA + "3 Kit PvP Mode!");
            diamondsword.setItemMeta(diamondswordmeta);

            gameselector.setItem(4, diamondsword);
            loaded = true;
        }

        //Begin load kit GUi
        if(!kitloaded){
            ItemStack ironsword = new ItemStack(Material.IRON_SWORD);
            ItemStack bow = new ItemStack(Material.BOW);
            ItemStack diamondhoe = new ItemStack(Material.DIAMOND_HOE);
            ItemMeta ironswordmeta = ironsword.getItemMeta();
            ItemMeta bowmeta = bow.getItemMeta();
            ItemMeta diamondhoemeta = diamondhoe.getItemMeta();
            ironswordmeta.setDisplayName(ChatColor.AQUA + "Warrior");
            bowmeta.setDisplayName(ChatColor.AQUA + "Archer");
            diamondhoemeta.setDisplayName(ChatColor.AQUA + "Mage");
            ironsword.setItemMeta(ironswordmeta);
            bow.setItemMeta(bowmeta);
            diamondhoe.setItemMeta(diamondhoemeta);

            kitselector.setItem(3, ironsword);
            kitselector.setItem(4, bow);
            kitselector.setItem(5, diamondhoe);
        }

        //Load Config
        config.addDefault("Game", null);
        config.options().copyDefaults(true);
        saveConfig();

    }

    @Override
    public void onDisable(){

    }

    public HashMap<String, String> kit = new HashMap<String, String>();
    public ArrayList<String> ingame = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            //Host and Start Game
            if (cmd.getName().equalsIgnoreCase("HostGame")) {
                p.openInventory(gameselector);
            }
            //Join Command
            if (cmd.getName().equalsIgnoreCase("JoinGame")) {
                if(isGame){
                    if(!ingame.contains(p.getName())){
                        ingame.add(p.getName());
                        p.getInventory().clear();
                        p.getInventory().setHelmet(null);
                        p.getInventory().setChestplate(null);
                        p.getInventory().setLeggings(null);
                        p.getInventory().setBoots(null);
                        //KitSelector
                        ItemStack blazerod = new ItemStack(Material.BLAZE_ROD);
                        ItemMeta blazerodmeta = blazerod.getItemMeta();
                        blazerodmeta.setDisplayName(ChatColor.AQUA + "Kit selector for 'game'");
                        blazerod.setItemMeta(blazerodmeta);
                        p.getInventory().addItem(blazerod);
                        teleportToLobby(p.getName());
                        if(ingame.size() >= 2){
                            Start startmech = new Start(this);
                            startmech.initiateStart();
                        }
                    }
                }
            }
            //Admin Tools Begin
            if(cmd.getName().equalsIgnoreCase("GameSetup")){
                if(p.hasPermission("Game.admin")){
                    if(args.length == 0){
                        p.sendMessage(ChatColor.RED + "This is the gamesetup command for the current 'Game' plugin, please use one or more arguments to continue");
                    }
                    if(args.length == 1){
                        if(args[0].equalsIgnoreCase("setlobbyloc")){
                            World lobbyworld = p.getWorld();
                            Integer lobbylocx = p.getLocation().getBlockX();
                            Integer lobbylocy = p.getLocation().getBlockY();
                            Integer lobbylocz = p.getLocation().getBlockZ();
                            String lobbyworldtostring = lobbyworld.getName();

                            config.set("Game.lobbyloc.world", lobbyworldtostring);
                            config.set("Game.lobbyloc.x", lobbylocx);
                            config.set("Game.lobbyloc.y", lobbylocy);
                            config.set("Game.lobbyloc.z", lobbylocz);
                            saveConfig();

                            p.sendMessage(ChatColor.AQUA + "You have set the lobby location for 'Game'");
                        }
                        if(args[0].equalsIgnoreCase("setgameloc")){
                            World gameworld = p.getWorld();
                            Integer gamelocx = p.getLocation().getBlockX();
                            Integer gamelocy = p.getLocation().getBlockY();
                            Integer gamelocz = p.getLocation().getBlockZ();
                            String gameworldtostring = gameworld.getName();

                            config.set("Game.gameloc.world", gameworldtostring);
                            config.set("Game.gameloc.x", gamelocx);
                            config.set("Game.gameloc.y", gamelocy);
                            config.set("Game.gameloc.z", gamelocz);
                            saveConfig();

                            p.sendMessage(ChatColor.AQUA + "You have set the game location for 'Game'");
                        }
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        @SuppressWarnings("deprecation")
        Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
        if(e.getInventory().getName().equalsIgnoreCase(gameselector.getName())){
            if(!e.getCurrentItem().hasItemMeta()){
                return;
            }
            if(e.getCurrentItem() == null){
                return;
            }
                    if(e.getCurrentItem().getType() == Material.DIAMOND_SWORD){
                        e.setCancelled(true);
                        //TODO Begin game, not sure if complete
                        if(!isGame) {
                            ingame.add(p.getName());
                            p.getInventory().clear();
                            p.getInventory().setHelmet(null);
                            p.getInventory().setChestplate(null);
                            p.getInventory().setLeggings(null);
                            p.getInventory().setBoots(null);
                            //KitSelector
                            ItemStack blazerod = new ItemStack(Material.BLAZE_ROD);
                            ItemMeta blazerodmeta = blazerod.getItemMeta();
                            blazerodmeta.setDisplayName(ChatColor.AQUA + "Kit selector for 'game'");
                            blazerod.setItemMeta(blazerodmeta);
                            p.getInventory().addItem(blazerod);
                            teleportToLobby(p.getName());
                            Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "A game has begun! Use the command /JoinGame to join the game!");
                            isGame = true;
                        }
                        else{
                            p.closeInventory();
                            p.sendMessage(ChatColor.RED + "A game is already in progress, please wait for it to be over!");
                        }
                    }
                    else{
                        e.setCancelled(true);
                    }
            }
            else if(e.getInventory().getName().equalsIgnoreCase(kitselector.getName())){
            if(!e.getCurrentItem().hasItemMeta()){
                return;
            }
            if(e.getCurrentItem() == null){
                return;
            }
                    if (e.getCurrentItem().getType() == Material.IRON_SWORD) {
                        e.setCancelled(true);
                        Warrior warriorclass = new Warrior(this);
                        warriorclass.selectWarrior(p.getName());
                        }
                    if(e.getCurrentItem().getType() == Material.BOW){
                        e.setCancelled(true);
                        Archer archerclass = new Archer(this);
                        archerclass.selectArcher(p.getName());
                    }
                    if(e.getCurrentItem().getType() == Material.DIAMOND_HOE){
                        e.setCancelled(true);
                        Mage mageclass = new Mage(this);
                        mageclass.selectMage(p.getName());
                    }
            }
        }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
            if(p.getItemInHand().getType() == Material.BLAZE_ROD){
                if(ingame.contains(p.getName())){
                    p.openInventory(kitselector);
                }
            }
        }
    }

    //Methods
    public void teleportToLobby(String p){
        String lobbyworld = config.getString("Game.lobbyloc.world");
        Integer lobbylocx = config.getInt("Game.lobbyloc.x");
        Integer lobbylocy = config.getInt("Game.lobbyloc.y");
        Integer lobbylocz = config.getInt("Game.lobbyloc.z");
        World lobbyworldtoworld = Bukkit.getWorld(lobbyworld);

        Location lobbyloc = new Location(lobbyworldtoworld, lobbylocx, lobbylocy, lobbylocz);

        Player player = Bukkit.getPlayer(p);
        player.teleport(lobbyloc);
        player.sendMessage(ChatColor.AQUA + "You have been sent to the lobby!");

    }

}
