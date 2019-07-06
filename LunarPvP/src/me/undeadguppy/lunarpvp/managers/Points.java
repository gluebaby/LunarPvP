package me.undeadguppy.lunarpvp.managers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.undeadguppy.vaults.managers.DataManager;
import net.md_5.bungee.api.ChatColor;

public class Points extends BukkitRunnable {

	private Game game;

	public Points(Game game) {
		this.game = game;
	}

	public void winProcess(Player p) {
		String prize = null;
		Random r = new Random();
		int i = r.nextInt(90);
		if (i <= 30) {
			DataManager.getInstance().addBountyPasses(p, 2);
			prize = "2x Bounty Passes";
		} else if (i <= 60) {
			DataManager.getInstance().addKothPasses(p, 1);
			prize = "1x KoTH Passes";
		} else if (i <= 90) {
			DataManager.getInstance().addMoney(p, 200);
			prize = "200 Credits";
		}
		Bukkit.broadcastMessage(ChatColor.BLUE + p.getName() + "'s " + ChatColor.GRAY + "prize is...");
		Bukkit.broadcastMessage(ChatColor.BLUE + prize + "!");
		p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
	}

	@Override
	public void run() {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (game.isOnHill(p)) {
				if (game.getHashMap().containsKey(p.getUniqueId())) {
					if (game.getHashMap().get(p.getUniqueId()) == 1000) {
						Bukkit.broadcastMessage(ChatColor.BLUE + p.getName() + " has won the KoTH!");
						game.stop();
						this.cancel();
						winProcess(p);
						p.teleport(p.getWorld().getSpawnLocation());
						return;
					}
					game.getHashMap().put(p.getUniqueId(), game.getPoints(p) + 25);
					Bukkit.getServer().broadcastMessage(ChatColor.BLUE + p.getName() + ChatColor.GRAY
							+ " is capturing the hill! " + ChatColor.BLUE + game.getPoints(p) + "/1000!");

				}
			}
		}
	}

}
