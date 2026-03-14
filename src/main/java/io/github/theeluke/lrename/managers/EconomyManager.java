package io.github.theeluke.lrename.managers;

import io.github.theeluke.LRename;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class EconomyManager {

    private final LRename plugin;
    private final Economy vaultEconomy;

    public EconomyManager(LRename plugin, Economy vaultEconomy) {
        this.plugin = plugin;
        this.vaultEconomy = vaultEconomy;
    }

    public boolean chargePlayer(Player player, String action) {
        if (player.hasPermission("lrename.bypasscost")) {
            return true;
        }

        String type = plugin.getConfig().getString("economy.type", "NONE").toUpperCase();
        if (type.equals("NONE")) return true;

        double cost = plugin.getConfig().getDouble("economy.costs." + action, 0.0);
        if (cost <= 0) return true;

        if (type.equals("VAULT")) {
            if (vaultEconomy == null) {
                plugin.getLogger().warning("Economy is set to VAULT, but Vault is not installed! Denying action.");
                return false;
            }

            if (vaultEconomy.getBalance(player) >= cost) {
                vaultEconomy.withdrawPlayer(player, cost);
                return true;
            }else {
                Component msg = plugin.getConfigManager().getMessage("not-enough-money", "%cost%", vaultEconomy.format(cost));
                plugin.adventure().player(player).sendMessage(msg);
                return false;
            }
        }

        else if (type.equals("XP")) {
            int xpCost = (int) cost;
            if (player.getLevel() >= xpCost) {
                player.setLevel(player.getLevel() - xpCost);
                return true;
            } else {
                Component msg = plugin.getConfigManager().getMessage("not-enough-xp", "%cost%", String.valueOf(xpCost));
                plugin.adventure().player(player).sendMessage(msg);
                return false;
            }
        }

        return true;
    }
}
