package me.undeadguppy.lunarutils;

import org.bukkit.plugin.java.JavaPlugin;

import me.undeadguppy.lunarutils.commands.AdminCMD;
import me.undeadguppy.lunarutils.commands.FreezeCMD;
import me.undeadguppy.lunarutils.commands.InvseeCMD;
import me.undeadguppy.lunarutils.commands.ReportCMD;
import me.undeadguppy.lunarutils.commands.StaffChat;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		new FreezeCMD(this);
		new AdminCMD(this);
		new InvseeCMD(this);
		new ReportCMD(this);
		new StaffChat(this);
	}

}
