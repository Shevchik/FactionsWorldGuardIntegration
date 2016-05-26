package factionsworldguardintegration;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;

public class FactionsTerritoryCalimListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onChunksClaim(EventFactionsChunksChange event) {
		for (PS ps : event.getChunks()) {
			if (hasRegion(ps.asBukkitWorld(), ps.getChunkX(), ps.getChunkZ())) {
				event.setCancelled(true);
				event.getMPlayer().message(Locale.intersectsWorldGuardRegion());
				break;
			}
		}
	}

	private boolean hasRegion(World world, int chunkX, int chunkZ) {
		ProtectedCuboidRegion testrg = new ProtectedCuboidRegion(
			"testrg",
			new BlockVector(chunkX << 4 + 15, 255, chunkZ << 4),
			new BlockVector(chunkX << 4, 0, chunkZ << 4)
		);
		return WGBukkit.getRegionManager(world).getApplicableRegions(testrg).size() > 0;
	}

}
