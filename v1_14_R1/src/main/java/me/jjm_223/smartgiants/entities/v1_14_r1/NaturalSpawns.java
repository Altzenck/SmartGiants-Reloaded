package me.jjm_223.smartgiants.entities.v1_14_r1;

import me.jjm_223.smartgiants.api.util.INaturalSpawns;
import net.minecraft.server.v1_14_R1.BiomeBase;
import net.minecraft.server.v1_14_R1.EnumCreatureType;

import java.util.List;

public class NaturalSpawns implements INaturalSpawns {
    private boolean hostile;
    private boolean daylight;
    private int frequency;
    private int minGroupAmount;
    private int maxGroupAmount;

    @Override
    public void load(boolean hostile, boolean daylight, int frequency, int minGroupAmount, int maxGroupAmount) {
        this.hostile = hostile;
        this.daylight = daylight;
        this.frequency = frequency;
        this.minGroupAmount = minGroupAmount;
        this.maxGroupAmount = maxGroupAmount;

        if (daylight) {
            daylight();
        } else {
            night();
        }
    }

    private void daylight() {
        for (Object oBiomeBase : BiomeBase.b) {
            final BiomeBase biomeBase = (BiomeBase) oBiomeBase;
            if (biomeBase != null) {
                final List<BiomeBase.BiomeMeta> mobs = biomeBase.getMobs(EnumCreatureType.CREATURE);
                if (!mobs.isEmpty()) {
                    mobs.add(new BiomeBase.BiomeMeta((hostile ? Load.smartGiantHostile : Load.smartGiant), frequency,
                            minGroupAmount, maxGroupAmount));
                }
            }
        }
    }

    private void night() {
        for (Object oBiomeBase : BiomeBase.b) {
            final BiomeBase biomeBase = (BiomeBase) oBiomeBase;
            if (biomeBase != null) {
                final List<BiomeBase.BiomeMeta> mobs = biomeBase.getMobs(EnumCreatureType.MONSTER);
                if (!mobs.isEmpty()) {
                    mobs.add(new BiomeBase.BiomeMeta((hostile ? Load.smartGiantHostile : Load.smartGiant), frequency,
                            minGroupAmount, maxGroupAmount));
                }
            }
        }
    }

    @Override
    public void cleanup() {
        for (Object oBiomeBase : BiomeBase.b) {
            final BiomeBase biomeBase = (BiomeBase) oBiomeBase;
            final List<BiomeBase.BiomeMeta> mobs = biomeBase.getMobs(daylight ? EnumCreatureType.CREATURE : EnumCreatureType.MONSTER);

            mobs.removeIf(meta -> meta.b == Load.smartGiantHostile || meta.b == Load.smartGiant);
        }
    }
}
