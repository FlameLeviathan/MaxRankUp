package me.FlameKnight15.MaxRankUp;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.Ranks.CMIRank;
import me.clip.ezprestige.EZPrestige;
import me.clip.ezprestige.PrestigeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Prestige {
    Main main;
    Prestige(Main m){
        main = m;
    }


    public void prestige(Player player) {
        CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        // Get current rank
        CMIRank currentRank = user.getRank();
        String playerRank = currentRank.getName();
        PrestigeManager manager = new PrestigeManager((EZPrestige) Bukkit.getPluginManager().getPlugin("EzPrestige"));
        double playerBalance = main.econ.getBalance(player);
        double prestigeCost = manager.getNextPrestigeCost(player);
        if (playerRank != null) {
            if ((playerRank).equalsIgnoreCase("Free")) {
                String[] playerGroups = main.perms.getPlayerGroups(player);
                ArrayList<String> listedPlayerGroups = new ArrayList<String>();

                for(int inc = 0; inc < playerGroups.length; inc++){
                    listedPlayerGroups.add(playerGroups[inc]);
                }
                if(/*listedPlayerGroups.contains("a15") && listedPlayerGroups.contains("p50") ||*/ listedPlayerGroups.contains("p800")){
                    return;
                } else
                if (playerBalance >= prestigeCost) {
                    main.prestiging = true;

                    //if (player.hasPermission("maxrankup.autoprestige")) {
                    //if (player.hasPermission("ezprestige.canprestige")) {
                    //player.sendMessage("Working");
                    if (Bukkit.isPrimaryThread()) {
                        //player.chat("/" + "ezprestige");
/*                        try {
                            Thread.sleep(main.config.getInt("prestigeDelay"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        //player.chat("/" + "ezprestige");
                        manager.prestigePlayer(player);

                        final long PERIOD = main.config.getInt("prestigeDelay"); // Adjust to suit timing
                        long lastTime = System.currentTimeMillis() - PERIOD;
                        long thisTime = System.currentTimeMillis();
                        if ((thisTime - lastTime) >= PERIOD) {
                            lastTime = thisTime;
                        }
                        main.prestiging = false;
                        return;
                    } else {
                        Bukkit.getScheduler().runTask(main, new Runnable() {
                            @Override
                            public void run() {
                                //player.chat("/" + "ezprestige");
/*                                try {
                                    Thread.sleep(main.config.getInt("prestigeDelay"));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }*/
                                manager.prestigePlayer(player);

                                //player.chat("/" + "ezprestige");
                                final long PERIOD = main.config.getInt("prestigeDelay"); // Adjust to suit timing
                                long lastTime = System.currentTimeMillis() - PERIOD;
                                long thisTime = System.currentTimeMillis();
                                if ((thisTime - lastTime) >= PERIOD) {
                                    lastTime = thisTime;
                                }
                                main.prestiging = false;
                                return;
                    /*try {
                        Thread.sleep(30);
                        return;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                            }
                        });
                        return;
                    }
                    //}
                    //}
                } else {
                    return;
                }
            } else {
                return;
            }

        }
        return;
    }
}
