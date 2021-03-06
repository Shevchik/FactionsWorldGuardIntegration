package factionsworldguardintegration;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PSBuilder;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;

public class WorldGuardRegionClaimListener implements Listener {

	private final Set<String> wgRegionCommands = new HashSet<>(CommandUtils.getCommandAliases("region"));

	@EventHandler(ignoreCancelled = true)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String[] args = event.getMessage().split("\\s+");
		if (args.length >= 3 && wgRegionCommands.contains(args[0].substring(1, args[0].length())) && args[1].equalsIgnoreCase("claim")) {
			try {
				Region selection = JavaPlugin.getPlugin(WorldEditPlugin.class).getSession(player).getSelection(BukkitAdapter.adapt(player.getWorld()));
				int chunkXMin = selection.getMinimumPoint().getBlockX() >> 4;
				int chunkZMin = selection.getMinimumPoint().getBlockZ() >> 4;
				int chunkXMax = selection.getMaximumPoint().getBlockX() >> 4;
				int chunkZMax = selection.getMaximumPoint().getBlockZ() >> 4;
				for (int chunkX = chunkXMin; chunkX <= chunkXMax; chunkX++) {
					for (int chunkZ = chunkZMin; chunkZ <= chunkZMax; chunkZ++) {
						Faction faction = BoardColl.get().getFactionAt(
							new PSBuilder()
							.world(player.getWorld())
							.chunkX(chunkX)
							.chunkZ(chunkZ)
							.build()
						);
						if (isPlayerCreatedFaction(faction)) {
							event.setCancelled(true);
							player.sendMessage(Locale.intersectsFactionTerritory());
							break;
						}
					}
				}
			} catch (IncompleteRegionException e) {
			}
		}
	}

	protected static boolean isPlayerCreatedFaction(Faction faction) {
		if (faction == null) {
			return false;
		}
		String factionId = faction.getId();
		return !factionId.equals(Factions.ID_NONE) && !factionId.equals(Factions.ID_SAFEZONE) && !factionId.equals(Factions.ID_WARZONE);
	}

}
