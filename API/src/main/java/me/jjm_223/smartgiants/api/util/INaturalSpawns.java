package me.jjm_223.smartgiants.api.util;

public interface INaturalSpawns {

    void load(boolean hostile, boolean daylight, float frequency, int minGroupAmount, int maxGroupAmount);

    void cleanup();
}
