package me.FlameKnight15.MaxRankUp;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.Ranks.CMIRank;
import com.Zrips.CMI.events.CMIUserBalanceChangeEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class Main extends JavaPlugin{
    File cFile = new File(getDataFolder(), "config.yml");
    public FileConfiguration config = YamlConfiguration.loadConfiguration(cFile);
    //String ignoreVar;
    //String prefix;
    Economy econ = null;
    Permission perms = null;
    Rankup rankup = new Rankup(this);
    Prestige prest = new Prestige(this);
    Retrieval ret = new Retrieval(this);
    boolean prestiging = false;
    //FileConfiguration rankUps = YamlConfiguration.loadConfiguration(new File("plugins/CMI/ranks.yml"));
    //Set localSet = rankUps.getKeys(false);
    //ArrayList<String> ranks = new ArrayList<>();


    @Override
    public void onEnable() {
        //Check to see if a config is loaded, if not, load the default one
        //if the config is loaded save it
        this.getLogger().info("Generating Config...");
        saveDefaultConfig();
        config.options().copyDefaults(true);
        config = YamlConfiguration.loadConfiguration(cFile);
        // auth();
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupPermissions()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
/*        rankup = new Rankup(this);
        prest = new Prestige(this);
        ret = new Retrieval(this);*/
        //String playerRank = null;
        //double rankupCost = 0;

/*        for (Object configObject = localSet.iterator(); ((Iterator) configObject).hasNext(); ) {
            String rank = (String) ((Iterator) configObject).next();
            ranks.add(rank);
        }*/

        //registerEvents(this);
        registerEvents(this, rankup);

        //prefix = ChatColor.translateAlternateColorCodes('&', config.getString("chat-Prefix"));
        //ignoreVar = ChatColor.translateAlternateColorCodes('&', config.getString("ignore-Var"));
    }

    @Override
    public void onDisable() {
        //mute.protocolManager.removePacketListeners(this);
        getLogger().info("onDisable has been invoked!");
        config = YamlConfiguration.loadConfiguration(cFile);
        config.options().copyDefaults(true);

    }

    public void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    /**
     * Handles the use of commands
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mru") || cmd.getName().equalsIgnoreCase("maxrankup")) {
            if(args.length == 0){
                if (sender instanceof Player)
                    if (sender.hasPermission("maxrankup.rankup")) {
                        rankup.maxrankup((Player) sender);
                        return true;
                    }
                } else
            if (args[0].equalsIgnoreCase("reload")) {
                if(sender.hasPermission("messagemute.reload")){
                    if(YamlConfiguration.loadConfiguration(cFile) != null){
                        reloadConfig();
                        config = YamlConfiguration.loadConfiguration(cFile);
                        sender.sendMessage( ChatColor.GREEN + "MaxRankUp has been reloaded!");
                    }else{
                        saveDefaultConfig();
                        config.options().copyDefaults(true);
                        sender.sendMessage( ChatColor.GREEN + "MaxRankUp has been reloaded!");
                    }



                    return true;
                }
                else{
                    sender.sendMessage(ChatColor.RED + "You may not perform this command!");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "You may not perform this command!");
            }
        }
        return true;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }


    /*@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        balance.put(event.getPlayer().getName(), econ.getBalance(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (balance.containsKey(event.getPlayer().getName()))
            balance.remove(event.getPlayer().getName());
    }*/

/*    private void rankUpPlayer(Player sender) {
        double senderBalance = econ.getBalance(sender);

        //Iterate through available rank ups
        FileConfiguration rankUps = YamlConfiguration.loadConfiguration(new File("plugins/CMI/ranks.yml"));
        Set localSet = rankUps.getKeys(false);
        ArrayList<String> ranks = new ArrayList<>();
        String playerRank = null;
        for (Object configObject = localSet.iterator(); ((Iterator) configObject).hasNext(); ) {
            String rank = (String) ((Iterator) configObject).next();
            ranks.add(rank);
        }

        for (int i = 0; i < ranks.size(); i++) {
            playerRank = getPlayerRank(sender);
            if (playerRank != null) {
                if (!(playerRank).equalsIgnoreCase("Free")) {
                    //if so get next rank and how much it costs
                    //See if the player has enough to rank up
                    if (senderBalance >= rankupCost) {
                        //If so rank up
                        CMI.getInstance().performCommand(sender, "cmi rankup");
                        //Pause for a tick to allow other plugin to update
                        try {
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        CMI.getInstance().performCommand(sender, "cmi rankup");
                    }
                } else {
                    Bukkit.dispatchCommand(sender, "ezprestige");
                }

            }
        }

    }*/



 /*   private void rankUpPlayer(Player sender) {
        double senderBalance = econ.getBalance(sender);

        //Iterate through available rank ups
        FileConfiguration rankUps = YamlConfiguration.loadConfiguration(new File("plugins/EZRanksPro/rankups.yml"));
        Set localSet = rankUps.getConfigurationSection("rankups").getKeys(false);
        ArrayList<String> ranks = new ArrayList<String>();
        String playerRank;
        for (Object configObject = localSet.iterator(); ((Iterator)configObject).hasNext(); ) { String rank = (String)((Iterator)configObject).next();
            ranks.add(rank);
        }

*//*            int j = rankUps.getInt("rankups." + str1 + ".order");
            String str2 = rankUps.getString("rankups." + str1 + ".prefix");
            String str3 = rankUps.getString("rankups." + str1 + ".rankup_to");
            String str4 = rankUps.getString("rankups." + str1 + ".cost");
            List localList = rankUps.getStringList("rankups." + str1 + ".rankup_actions");*//*
        //see if any match current rank

        for(int i =0; i < ranks.size(); i++) {
            Rankup localRankup = Rankup.getRankup(sender.getPlayer());
            sender.sendMessage(ChatColor.BLUE + "1st: " + localRankup + " " + perms.getPlayerGroups(sender)[0] + " ");
            //sender.sendMessage(ChatColor.RED + "2nd: " + ranks.get(i).equals(perms.getPlayerGroups(sender)[0]));
            if (ranks.get(i).equals(perms.getPlayerGroups(sender)[0])) {
                sender.sendMessage(localRankup.getRank() + " " + ranks.get(i));
                if (localRankup.getRank().equals(ranks.get(i)))*//*ranks.get(i).equals(perms.getPlayerGroups(sender)[0]))*//* {
                    sender.sendMessage("Getting There!");
                    playerRank = ranks.get(i);
                    //sender.sendMessage(ChatColor.RED + "playerRank1: " + playerRank);
                    if (!(perms.getPlayerGroups(sender)[0]).equals("Free")) {
                        //if so get next rank and how much it costs
                        //sender.sendMessage(ChatColor.RED + "playerRank: " + playerRank);
                        double rankupCost = Double.parseDouble(rankUps.getString("rankups." + ranks.get(i + 1) + ".cost"));
                        //See if the player has enough to rank up
                        if (senderBalance >= rankupCost) {
                            //If so rank up
                            //sender.performCommand("rankup");
                            sender.sendMessage("RankedUp!");
                            Bukkit.dispatchCommand(sender, "rankup");
                            //EZRanksPro.getInstance().getActionHandler().executeRankupActions(sender, localRankup);
                            //RankupEvent localRankupEvent = new RankupEvent(sender, localRankup.getRank(), localRankup.getRankup(),
                            //        localRankup.getCost(), RankupType.RANKUP);
                            continue;
                            //Rankup localRankup = Rankup.getRankup(sender);

                        } else {
                            Bukkit.dispatchCommand(sender, "rankup");
                        }
                    } else {
                        Bukkit.dispatchCommand(sender, "ezprestige");
                    }

                }
            }
        }

    }*/

}
