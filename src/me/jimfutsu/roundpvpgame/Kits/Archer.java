package me.jimfutsu.roundpvpgame.Kits;

import me.jimfutsu.roundpvpgame.Main;
        import org.bukkit.Bukkit;
        import org.bukkit.ChatColor;
        import org.bukkit.Material;
        import org.bukkit.enchantments.Enchantment;
        import org.bukkit.entity.Player;
        import org.bukkit.event.Listener;
        import org.bukkit.inventory.ItemStack;
        import org.bukkit.inventory.meta.ItemMeta;


        public class Archer implements Listener {

            private Main plugin;

            public Archer(Main plugin)
            {
                this.plugin = plugin;
            }

            public void selectArcher(String p){
                Bukkit.getServer().getPlayer(p).sendMessage(ChatColor.AQUA + "You have selected the " + ChatColor.YELLOW + "Archer " + ChatColor.AQUA + "kit!");
                if(!plugin.kit.containsKey(p)){
                    plugin.kit.put(p, "archer");
                }
                else{
                    plugin.kit.remove(p);
                    plugin.kit.put(p, "archer");
                }
            }

            public void giveArcherItems(String player){

                Player p = Bukkit.getServer().getPlayer(player);
                ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                ItemStack sword = new ItemStack(Material.STONE_SWORD);
                ItemStack bow = new ItemStack(Material.BOW);
                ItemMeta bowmeta = bow.getItemMeta();
                bowmeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2,true);
                bowmeta.addEnchant(Enchantment.ARROW_FIRE, 1,true);
                bow.setItemMeta(bowmeta);

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
                p.getInventory().addItem(bow);

    }

}
