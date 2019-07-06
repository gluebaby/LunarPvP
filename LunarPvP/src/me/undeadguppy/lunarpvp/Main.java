package me.undeadguppy.lunarpvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.undeadguppy.lunarpvp.commands.BountyCMD;
import me.undeadguppy.lunarpvp.commands.HelpCMD;
import me.undeadguppy.lunarpvp.commands.RulesCMD;
import me.undeadguppy.lunarpvp.commands.SetSpawn;
import me.undeadguppy.lunarpvp.commands.SetStatsCMD;
import me.undeadguppy.lunarpvp.commands.ShopCMD;
import me.undeadguppy.lunarpvp.commands.SpawnCMD;
import me.undeadguppy.lunarpvp.commands.StatsCMD;
import me.undeadguppy.lunarpvp.kits.KitGiver;
import me.undeadguppy.lunarpvp.listeners.ClickListener;
import me.undeadguppy.lunarpvp.listeners.PlayerListener;
import me.undeadguppy.vaults.managers.DataManager;

public class Main extends JavaPlugin {

	private static Main m;

	public static Main getInstance() {
		return m;
	}

	Koth k;

	private SpawnCMD spawncmd = new SpawnCMD();

	@Override
	public void onEnable() {
		DataManager.getInstance().setup(this);
		k = new Koth();
		m = this;
		setup();
		if (!getConfig().contains("Spawn")) {
			getConfig().set("Spawn", Bukkit.getWorld("world").getSpawnLocation());
			saveConfig();

		}
	}

	private void setup() {
		getCommand("setstats").setExecutor(new SetStatsCMD());
		getCommand("bounty").setExecutor(new BountyCMD());
		getCommand("shop").setExecutor(new ShopCMD());
		getCommand("help").setExecutor(new HelpCMD());
		getCommand("rules").setExecutor(new RulesCMD());
		getCommand("stats").setExecutor(new StatsCMD());
		getCommand("spawn").setExecutor(spawncmd);
		getCommand("setspawn").setExecutor(new SetSpawn());
		getCommand("koth").setExecutor(k);
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new KitGiver(this), this);
		pm.registerEvents(spawncmd, this);
		pm.registerEvents(k, this);
		pm.registerEvents(new ClickListener(), this);
	}
}
