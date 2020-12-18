package com.docusign;

public class OSDetector
{
    private static boolean isWindows;
    private static boolean isLinux;
    private static boolean isMac;

    static
    {
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
        isLinux = os.contains("nux") || os.contains("nix");
        isMac = os.contains("mac");
    }

    public static boolean isWindows() { return isWindows; }
    public static boolean isLinux() { return isLinux; }
    public static boolean isMac() { return isMac; };

}