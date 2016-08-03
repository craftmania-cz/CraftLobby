package cz.wake.plugins.pets;

import cz.wake.plugins.Main;
import cz.wake.plugins.utils.mobs.RideableCreeper;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Iterator;

public class CreeperNormal {

    public static ArrayList<String> cn = new ArrayList();

    public static void activateWitch(Player p, boolean powered) {
        for (Iterator localIterator = p.getWorld().getEntities().iterator(); localIterator.hasNext(); ) {
            Object localObject = (CraftEntity) localIterator.next();
            if (localObject == PetManager.pet.get(p)) {
                PetManager.forceRemovePet(p);
                ((CraftEntity) localObject).remove();
            }
        }
        final Creeper e = RideableCreeper.spawn(p.getLocation());
        PetManager.PetFollow(p, (CraftEntity) e, 0.16D, 2D);
        setMetadata((Creeper) e, "Pet", "Pet", Main.getInstance());
        ((Creeper) e).setPowered(powered);
        ((Creeper) e).setCustomNameVisible(true);
        ((Creeper) e).setCustomName(p.getName());
        PetManager.pet.put(p, (CraftEntity) e);
        cn.add(p.getName());
        p.closeInventory();

    }

    public static void setMetadata(Creeper paramPig, String paramString, Object paramObject, Main paramMain) {
        paramPig.setMetadata(paramString, new FixedMetadataValue(paramMain, paramObject));
    }
}
