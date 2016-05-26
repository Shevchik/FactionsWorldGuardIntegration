package factionsworldguardintegration;

import org.bukkit.plugin.java.JavaPlugin;

public class FactionsWorldGuardIntegration extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new FactionsTerritoryCalimListener(), this);
		getServer().getPluginManager().registerEvents(new WorldGuardRegionClaimListener(), this);
	}

}
