package io.github.kilobytz.sa.tips;

import io.github.kilobytz.sa.SA;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TipManager {

    SA main;

    public TipManager(SA main) {
        this.main = main;
    }

    public void setTip(String tip) {
        main.getConfig().set("tips."+(numOfTips()+1), tip);
        main.saveConfig();
    }

    public void delTip(int tipNum) {
        if(main.getConfig().get("tips."+(tipNum+1)) == null) {
            main.getConfig().set("tips."+tipNum,null);
            main.saveConfig();
            return;
        }
        reshuffle(tipNum,numOfTips());
        main.saveConfig();
    }

    public String getTip() {
        List<String> tips = new LinkedList<>();
        try {
            for (String key : main.getConfig().getConfigurationSection("tips").getKeys(false)) {
                tips.add(key);
            }
            Random random = new Random();
            String message = (String) main.getConfig().get("tips."+((random.nextInt(numOfTips() - 1 + 1) + 1)));
            return message;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getTipSpec(int num) {
        List<String> tips = new LinkedList<>();
        try {
            for (String key : main.getConfig().getConfigurationSection("tips").getKeys(false)) {
                tips.add(key);
            }
            String message = (String) main.getConfig().get("tips."+num);
            return message;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public int numOfTips() {
        List<String> tips = new LinkedList<>();
        try {
            for (String key : main.getConfig().getConfigurationSection("tips").getKeys(false)) {
                tips.add(key);
            }
            return tips.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public void listTips(CommandSender sender) {
        int roll = 0;
        try {
            while (roll < numOfTips()) {
                sender.sendMessage(Integer.toString(roll + 1) + " : " + getTipSpec(roll+1));
                ++roll;
            }
            if(roll == 0) {
                sender.sendMessage(ChatColor.RED + "There are no tips added.");
            }
        }catch (NullPointerException e) {
            sender.sendMessage(ChatColor.RED + "There are no tips added.");
        }

    }


    public void reshuffle(int num,int max) {
        int base = num;
        while(num < max) {

            main.getConfig().set("tips."+base,main.getConfig().get("tips."+(base+1)));
            ++base;
        }
        main.getConfig().set("tips."+max,null);
    }

}
