package factionsworldguardintegration;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
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

	protected static boolean hasRegion(World world, int chunkX, int chunkZ) {
		ProtectedCuboidRegion testrg = new ProtectedCuboidRegion(
			"testrg",
			BlockVector3.at((chunkX << 4) + 15, 255, (chunkZ << 4) + 15),
			BlockVector3.at(chunkX << 4, 0, chunkZ << 4)
		);
		return WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world)).getApplicableRegions(testrg).size() > 0;
	}

}
