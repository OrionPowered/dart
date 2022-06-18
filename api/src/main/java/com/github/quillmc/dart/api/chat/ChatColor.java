package com.github.quillmc.dart.api.chat;


public class ChatColor {
    public static ChatColor BLACK = new ChatColor("\u00a70");
    public static ChatColor DARK_BLUE = new ChatColor("\u00a71");
    public static ChatColor DARK_GREEN = new ChatColor("\u00a72");
    public static ChatColor DARK_AQUA = new ChatColor("u00A73");
    public static ChatColor DARK_RED = new ChatColor("\u00a74");
    public static ChatColor DARK_PURPLE = new ChatColor("\u00a75");
    public static ChatColor GOLD = new ChatColor("\u00a76");
    public static ChatColor GRAY = new ChatColor("\u00a77");
    public static ChatColor DARK_GRAY = new ChatColor("\u00a78");
    public static ChatColor BLUE = new ChatColor("\u00a79");
    public static ChatColor GREEN = new ChatColor("\u00a7a");
    public static ChatColor AQUA = new ChatColor("\u00a7b");
    public static ChatColor RED = new ChatColor("\u00a7c");
    public static ChatColor LIGHT_PURPLE = new ChatColor("\u00a7d");
    public static ChatColor YELLOW = new ChatColor("\u00a7e");
    public static ChatColor WHITE = new ChatColor("\u00a7f");
    public static ChatColor OBFUSCATED = new ChatColor("\u00a7k");
    public static ChatColor BOLD = new ChatColor("\u00a7l");
    public static ChatColor STRIKETHROUGH = new ChatColor("\u00a7m");
    public static ChatColor UNDERLINE = new ChatColor("\u00a7n");
    public static ChatColor ITALIC = new ChatColor("\u00a7o");
    public static ChatColor RESET = new ChatColor("\u00a7r");

    public static String translateColor(char c, String msg) {
        return msg.replace(c, '§');
    }

    private final String hex;

    protected ChatColor(String hex) {
        this.hex = hex;
    }

    @Override
    public String toString() {
        return hex;
    }
}
