package io.github.warping.gui;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIButton extends ItemStack{
    ButtonType bT;
    Inventory inv;
    

    public GUIButton(ButtonType bT, Inventory inv,int slot,Object info) {
        this.bT = bT;
        this.inv = inv;
        setupItem(slot,info);
    }

    public ButtonType getButtonType(){
        return bT;
    }

    public void setupItem(int slot,Object info) {
        switch(bT) {
            case WARPFULL:
                super.setType(Material.ENDER_PEARL);
                super.setDurability((short) 1);
                ItemMeta warpSInfo = super.getItemMeta();
                warpSInfo.setDisplayName((String)info);
                super.setItemMeta(warpSInfo);
                inv.setItem(slot, this);
                break;
            case KILL:
                ItemStack die = new ItemStack(Material.SKULL_ITEM, 1, (short) 0);
                ItemMeta dieInfo = die.getItemMeta();
                dieInfo.setDisplayName("Suicide");
                die.setItemMeta(dieInfo);
                inv.setItem(slot, die);
                break;
            case NEXT:
                ItemStack forward = new ItemStack(Material.INK_SACK, 1, (short) 9);
                ItemMeta forwardInfo = forward.getItemMeta();
                forwardInfo.setDisplayName("PAGE FORWARD");
                forward.setItemMeta(forwardInfo);
                inv.setItem(slot, forward);
                break;
            case PAGENUM:
                ItemStack pageNum = new ItemStack(Material.BOOK, (int)info-9);
                ItemMeta pageNumInfo = pageNum.getItemMeta();
                pageNumInfo.setDisplayName("PAGE NO.");
                pageNum.setItemMeta(pageNumInfo);
                inv.setItem(slot, pageNum);
                break;
            case PREVIOUS:
                ItemStack back = new ItemStack(Material.INK_SACK, 1, (short) 13);
                ItemMeta backInfo = back.getItemMeta();
                backInfo.setDisplayName("PAGE BACK");
                back.setItemMeta(backInfo);
                inv.setItem(slot, back);
                break;
            case WARPEMPTY:
                ItemStack warpPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
                ItemMeta pane = warpPane.getItemMeta();
                pane.setDisplayName("CLICK TO ADD WARP");
                warpPane.setItemMeta(pane);
                inv.setItem(slot, warpPane);
                break;
            case SPAWN:
                ItemStack spawn = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                ItemMeta spawnInfo = spawn.getItemMeta();
                spawnInfo.setDisplayName("Spawn");
                spawn.setItemMeta(spawnInfo);
                inv.setItem(slot, spawn);
                break;
            case WARP:
                ItemStack warpPearl = new ItemStack(Material.ENDER_PEARL, 1);
                ItemMeta warp = warpPearl.getItemMeta();
                warp.setDisplayName((String)info);
                warpPearl.setItemMeta(warp);
                inv.setItem(slot, warpPearl);
                break;
            case EMPTY:
                ItemStack clear = new ItemStack(Material.FIREBALL, 1);
                ItemMeta clearInfo = clear.getItemMeta();
                clearInfo.setDisplayName("CLEAR SLOT");
                clear.setItemMeta(clearInfo);
                inv.setItem(slot, clear);
                break;
            case ADDLINE:
                ItemStack addLine = new ItemStack(Material.INK_SACK, 1, (short) 10);
                ItemMeta addLineInfo = addLine.getItemMeta();
                addLineInfo.setDisplayName((String)info);
                addLine.setItemMeta(addLineInfo);
                inv.setItem(slot, addLine);
                break;
            case REMOVELINE:
                ItemStack removeLine = new ItemStack(Material.INK_SACK, 1, (short) 1);
                ItemMeta removeLineInfo = removeLine.getItemMeta();
                removeLineInfo.setDisplayName((String)info);
                removeLine.setItemMeta(removeLineInfo);
                inv.setItem(slot, removeLine);
                break;
            case RETURN:
                ItemStack reverse = new ItemStack(Material.BARRIER, 1);
                ItemMeta reverseInfo = reverse.getItemMeta();
                reverseInfo.setDisplayName("RETURN");
                reverse.setItemMeta(reverseInfo);
                inv.setItem(slot, reverse);
                break;
            default:
                break;
            
        }
    }


}
