package com.nuclearw.morecharacters;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.minecraft.server.v1_4_6.SharedConstants;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MoreCharacters extends JavaPlugin implements Listener {
	private static final String[][] convertTable = {
		{":smile:" , "\u263A"},     // ☺
		{":smile2:" , "\u263B"},    // ☻
		{":spade:" , "\u2660"},     // ♠
		{":club:" , "\u2663"},      // ♣
		{":heart:" , "\u2665"},     // ♥
		{":diamond:" , "\u2666"},   // ♦
		{":male:" , "\u2642"},      // ♀
		{":female:" , "\u2640"},    // ♂
		{":note:" , "\u266A"},      // ♪
		{":note2:" , "\u266B"},     // ♫
	};

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		try {
			modifyAllowedCharacters();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		getLogger().info("Finished Loading " + getDescription().getFullName());
	}

	@Override
	public void onDisable() {
		getLogger().info("Finished Unloading "+getDescription().getFullName());
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String chat = event.getMessage();
		for(String[] pair : convertTable) {
			chat = chat.replaceAll(pair[0], pair[1]);
		}
		event.setMessage(chat);
	}

	// With so many thanks to Father of Time
	private void modifyAllowedCharacters() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = SharedConstants.class.getDeclaredField("allowedCharacters");
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		String oldallowedchars = (String) field.get(null);

		/*
		 * :smile:      ☺       \u263A
		 * :smile2:     ☻       \u263B
		 * :spade:      ♠       \u2660
		 * :club:       ♣       \u2663
		 * :heart:      ♥       \u2665
		 * :diamond:    ♦       \u2666
		 * :male:       ♀       \u2642
		 * :female:     ♂       \u2640
		 * :note:       ♪       \u266A
		 * :note2:      ♫       \u266B
		 */
		String add = "\u263A\u263B\u2660\u2663\u2665\u2666\u2642\u2640\u266A\u266B";
		StringBuilder sb = new StringBuilder();
		sb.append(oldallowedchars);
		sb.append(add);
		field.set(null, sb.toString());
	}
}
