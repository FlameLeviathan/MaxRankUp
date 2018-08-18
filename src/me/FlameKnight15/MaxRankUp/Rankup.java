package me.FlameKnight15.MaxRankUp;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.Ranks.CMIRank;
import com.Zrips.CMI.events.CMIUserBalanceChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class Rankup implements Listener {
    Main main;
    Rankup(Main m){
        main = m;
    }

    @EventHandler
    public void playerRankupOnMoney(CMIUserBalanceChangeEvent event) {
        if (event.getUser().isOnline()) {
            Player player = Bukkit.getServer().getPlayer(event.getUser().getPlayer().getName());
            if (player instanceof Player && player != null) {

/*                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (rank.equalsIgnoreCase("free")) {
                    player.sendMessage(rank);
                    if (player.hasPermission("maxrankup.autoprestige")) {
                        player.sendMessage("prestige");
                        prestige(player);
                    } else {
                        return;
                    }
                } else*/ if (player.hasPermission("maxrankup.autorank")) {
                    //if (!rank.equalsIgnoreCase("Free")) {
                    if (rankup(player))
                        return;
                    //}
                    //    return;
                    //}
                } else {
                    return;
                }
            }
        }

    }


    public boolean rankup(Player player) {
        boolean status = false;

        /*//for (int i = 0; i < ranks.size(); i++) {
        double playerBalance = econ.getBalance(player);
        String playerRank = getPlayerRank(player);
        double rankupCost = getRankupCost(player);
        if (playerRank != null) {
            if (!(playerRank.equalsIgnoreCase("Free"))) {
                //if so get next rank and how much it costs
                //See if the player has enough to rank up
                if (playerBalance >= rankupCost) {
                    //If so rank up
                    //player.sendMessage("THIS ONE: " +playerBalance + " | " + rankupCost);
                    if (player.hasPermission("cmi.command.rankup")) {
                        //runCommand(player, "cmi rankup");
                        if (Bukkit.isPrimaryThread()) {
                            player.chat("/" + "cmi rankup");
                        } else {
                            Bukkit.getScheduler().runTask(this, new Runnable() {
                                @Override
                                public void run() {
                                    player.chat("/" + "cmi rankup");
                                    try {
                                        Thread.sleep(30);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        status = true;
                    }
                    //Pause for a tick to allow other plugin to update
                        /*try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        //}

                } else {
                    return false;
                }
            } else {
                //if (player.hasPermission("maxrankup.autoprestige")) {
                //prestige(player);
                //}
                return false;
            }
        }
        //}

        return status;*/

        /*String[] playerGroups = main.perms.getPlayerGroups(player);
        ArrayList<String> listedPlayerGroups = new ArrayList<String>();
        for(int inc = 0; inc < playerGroups.length; inc++){
            listedPlayerGroups.add(playerGroups[inc]);
        }
        if(listedPlayerGroups.contains("A15") && listedPlayerGroups.contains("P50")){
            return status;
        } else*/
        if(!main.prestiging) {
            // Get user
            CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
            // Get current rank
            CMIRank currentRank = user.getRank();
            // Gets rank list player can rank to and meets requirements
            List<CMIRank> next = currentRank.getNextValidRankUps(user);
            // Looping while we can

            while (!next.isEmpty()) {
                // Taking first valid rankup
                CMIRank firstValid = next.get(0);
                // Simply changing current rank for user
                if (user.getBalance() >= firstValid.getMoneyCost()) {
                    user.setRank(firstValid);
                    // This performs regular actions, like money deduction and performing commands
                    boolean performCommnds = true;
                    boolean deductThings = true;
                    if (Bukkit.isPrimaryThread()) {
                        firstValid.finalizeRankup(user, performCommnds, deductThings);
                        try {
                            Thread.sleep(main.config.getInt("commandDelay"));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Bukkit.getScheduler().runTask(main, new Runnable() {
                            @Override
                            public void run() {
                                firstValid.finalizeRankup(user, performCommnds, deductThings);
                                final long PERIOD = main.config.getInt("commandDelay"); // Adjust to suit timing
                                long lastTime = System.currentTimeMillis() - PERIOD;
                                long thisTime = System.currentTimeMillis();
                                if ((thisTime - lastTime) >= PERIOD) {
                                    lastTime = thisTime;
                                }
                            }
                        });
                    }


                    // Clearing list
                    next.clear();
                    // Adding new entries if there is any
                    next.addAll(firstValid.getNextValidRankUps(user));
                    // this will break at moment you dont have any valid rankups
                    status = true;

                }

            }
            if (user.getRank().getName().equalsIgnoreCase("free")) {
                if (player.hasPermission("maxrankup.autoprestige")) {
                    main.prest.prestige(player);

                }
            }
        }
/*        if (user.getRank().getName().equalsIgnoreCase("free")) {
            if (player.hasPermission("maxrankup.autoprestige")) {
                main.prest.prestige(player);
                return true;
            }
        }*/

        return status;
    }

    public boolean maxrankup(Player player) {
        boolean status = false;
        boolean autoranking = false;
        // Get user
        CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        // Get current rank
        CMIRank currentRank = user.getRank();
        // Gets rank list player can rank to and meets requirements
        List<CMIRank> next = currentRank.getNextValidRankUps(user);
        // Looping while we can
        if(player.hasPermission("maxrankup.autorank") || player.hasPermission("maxrankup.autoprestige")){
            autoranking = true;
        }
        if (!autoranking) {
            while (!next.isEmpty()) {
                // Taking first valid rankup
                CMIRank firstValid = next.get(0);
                // Simply changing current rank for user
                if (user.getBalance() >= firstValid.getMoneyCost()) {

                    user.setRank(firstValid);
                    // This performs regular actions, like money deduction and performing commands
                    boolean performCommnds = true;
                    boolean deductThings = true;
                    if (Bukkit.isPrimaryThread()) {
                        firstValid.finalizeRankup(user, performCommnds, deductThings);
                        final long PERIOD = main.config.getInt("commandDelay"); // Adjust to suit timing
                        long lastTime = System.currentTimeMillis() - PERIOD;
                        long thisTime = System.currentTimeMillis();
                        if ((thisTime - lastTime) >= PERIOD) {
                            lastTime = thisTime;
                        }
                    } else {
                        Bukkit.getScheduler().runTask(main, new Runnable() {
                            @Override
                            public void run() {
                                firstValid.finalizeRankup(user, performCommnds, deductThings);
                                final long PERIOD = main.config.getInt("commandDelay");; // Adjust to suit timing
                                long lastTime = System.currentTimeMillis() - PERIOD;
                                long thisTime = System.currentTimeMillis();
                                if ((thisTime - lastTime) >= PERIOD) {
                                    lastTime = thisTime;
                                }
                            }
                        });
                    }


                    // Clearing list
                    next.clear();
                    // Adding new entries if there is any
                    next.addAll(firstValid.getNextValidRankUps(user));
                    // this will break at moment you dont have any valid rankups
                    status = true;
                    if (user.getRank().getName().equalsIgnoreCase("free")) {
                            main.prest.prestige(player);

                    }
                }
            }if (user.getRank().getName().equalsIgnoreCase("free")) {
                    main.prest.prestige(player);
            }

        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9Ranks » &7You can't use /maxrankup while AutoRankup or AutoPrestige is enabled. Disable these features in /toggle."));
        }

        return status;
    }
}
