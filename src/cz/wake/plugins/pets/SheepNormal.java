package cz.wake.plugins.pets;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.metadata.FixedMetadataValue;

import cz.wake.plugins.Main;
import cz.wake.plugins.utils.mobs.RideableSheep;

public class SheepNormal {
	
	public static ArrayList<String> sb = new ArrayList();
	
	public static void activate(Player p, boolean baby, DyeColor type){
		for (Iterator localIterator = p.getWorld().getEntities().iterator(); localIterator.hasNext();)
	    {
	      Object localObject = (CraftEntity)localIterator.next();
	      if (localObject == PetManager.pet.get(p))
	      {
	    	PetManager.forceRemovePet(p);
	        ((CraftEntity)localObject).remove();
	      }
	    }
		final Sheep sheep = RideableSheep.spawn(p.getLocation());
	    PetManager.PetFollow(p, (CraftEntity)sheep, 0.16D, 2D);
	    setMetadata((Sheep)sheep, "Pet", "Pet", Main.getInstance());
        if(baby){
            ((Sheep)sheep).setBaby();
        }
	    ((Sheep)sheep).setAgeLock(true);
	    ((Sheep)sheep).setColor(type);
	    ((Sheep)sheep).setCustomNameVisible(true);
	    ((Sheep)sheep).setCustomName(p.getName());
	    PetManager.pet.put(p, (CraftEntity) sheep);
	    sb.add(p.getName());
	    p.closeInventory();
	}
	
	public static void setMetadata(Sheep paramPig, String paramString, Object paramObject, Main paramMain)
	  {
	    paramPig.setMetadata(paramString, new FixedMetadataValue(paramMain, paramObject));
	  }

}