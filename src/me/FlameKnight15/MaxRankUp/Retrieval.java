package me.FlameKnight15.MaxRankUp;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class Retrieval {
    Main main;
    Retrieval(Main m){
        main = m;
    }
    /*String getPlayerRank(Player player) {
        *//*FileConfiguration rankUps = YamlConfiguration.loadConfiguration(new File("plugins/CMI/ranks.yml"));
        Set localSet = rankUps.getKeys(false);
        ArrayList<String> ranks = new ArrayList<>();
        String playerRank = null;

        for (Object configObject = localSet.iterator(); ((Iterator) configObject).hasNext(); ) {
            String rank = (String) ((Iterator) configObject).next();
            ranks.add(rank);
        }*//*
        *//*String playerRank = perms.getPlayerGroups(player)[0];
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i).equalsIgnoreCase(perms.getPlayerGroups(player)[0])) {
                playerRank = ranks.get(i);
            } /*else if (perms.getPlayerGroups(player)[1] != null) {
                if (ranks.get(i).equalsIgnoreCase(perms.getPlayerGroups(player)[1])) {
                    playerRank = ranks.get(i);
                }
            }*//*
        CMIUser playerUser = CMI.getInstance().getPlayerManager().getUser(player);
        // Get current rank
        CMIRank currentRank = playerUser.getRank();
        String playerRank;
        playerRank = currentRank.getName();
        return playerRank;
    }*/


    /*    public double getRankupCost(Player player) {
     *//*FileConfiguration rankUps = YamlConfiguration.loadConfiguration(new File("plugins/CMI/ranks.yml"));
        Set localSet = rankUps.getKeys(false);
        ArrayList<String> ranks = new ArrayList<>();
        String playerRank = null;
        double rankupCost = 0;

        for (Object configObject = localSet.iterator(); ((Iterator) configObject).hasNext(); ) {
            String rank = (String) ((Iterator) configObject).next();
            ranks.add(rank);
        }*//*
        String playerRank = null;
        double rankupCost = 0;
        for (int i = 0; i < ranks.size(); i++) {
            if (ranks.get(i).equalsIgnoreCase(perms.getPlayerGroups(player)[0])) {
                playerRank = ranks.get(i);
                if (!playerRank.equalsIgnoreCase("Free"))
                    rankupCost = rankUps.getDouble("" + ranks.get(i + 1) + ".MoneyCost");
            } else if (!(perms.getPlayerGroups(player)[1].isEmpty() || perms.getPlayerGroups(player)[1] == null))
                if (ranks.get(i).equalsIgnoreCase(perms.getPlayerGroups(player)[1])) {
                    playerRank = ranks.get(i);
                    if (!playerRank.equalsIgnoreCase("Free"))
                        rankupCost = rankUps.getDouble("" + ranks.get(i + 1) + ".MoneyCost");
                }
        }

        return rankupCost;
    }*/


/*    String getPlayerPrestige(Player player) {
        FileConfiguration prestigeUps = YamlConfiguration.loadConfiguration(new File("plugins/EZPrestige/prestiges.yml"));
        Set localSet = prestigeUps.getConfigurationSection("prestiges").getKeys(false);
        ArrayList<String> prestiges = new ArrayList<>();
        String playerRank = null;

        for (Object configObject = localSet.iterator(); ((Iterator) configObject).hasNext(); ) {
            String rank = (String) ((Iterator) configObject).next();
            prestiges.add(rank);
        }

        for (int i = 0; i < prestiges.size(); i++) {
            if (prestiges.get(i).equalsIgnoreCase(main.perms.getPlayerGroups(player)[0])) {
                playerRank = prestiges.get(i);
                //prestigeCost = rankUps.getDouble("" + prestiges.get(i + 1) + ".cost");
            } else if (!(main.perms.getPlayerGroups(player)[1].isEmpty() || (main.perms.getPlayerGroups(player)[1] == null))) {
                if (prestiges.get(i).equalsIgnoreCase(main.perms.getPlayerGroups(player)[1])) {
                    playerRank = prestiges.get(i);
                    //prestigeCost = rankUps.getDouble("" + prestiges.get(i + 1) + ".cost");
                }
            }


        }

        return playerRank;
    }*/

    double getPlayerPrestigeCost(Player player) {
        FileConfiguration prestigeUps = YamlConfiguration.loadConfiguration(new File("plugins/EZPrestige/prestiges.yml"));
        Set localSet = prestigeUps.getConfigurationSection("prestiges").getKeys(false);
        ArrayList<String> prestiges = new ArrayList<>();
        double prestigeCost = 0;

        for (Object configObject = localSet.iterator(); ((Iterator) configObject).hasNext(); ) {
            String rank = (String) ((Iterator) configObject).next();
            prestiges.add(rank);
        }

        for (int i = 0; i < prestiges.size(); i++) {
            if (prestiges.get(i).equalsIgnoreCase(main.perms.getPlayerGroups(player)[0])) {
                prestigeCost = prestigeUps.getDouble("prestiges." + prestiges.get(i + 1) + ".cost");
            } else if (!(main.perms.getPlayerGroups(player)[1].equals(null))) {
                if (prestiges.get(i).equalsIgnoreCase(main.perms.getPlayerGroups(player)[1])) {
                    prestigeCost = prestigeUps.getDouble("prestiges." + prestiges.get(i + 1) + ".cost");
                }else{
                    prestigeCost = prestigeUps.getDouble("prestiges." + prestiges.get(0) + ".cost");
                }
            } else{
                prestigeCost = prestigeUps.getDouble("prestiges." + prestiges.get(0) + ".cost");
            }


        }

        return prestigeCost;
    }
}
