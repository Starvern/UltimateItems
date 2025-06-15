package net.sierr.ultimateitems.data;

import net.sierr.ultimateitems.mods.ItemModHolder;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ItemModHolderDataType implements PersistentDataType<byte[], ItemModHolder>
{
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemModHolder> getComplexType() {
        return ItemModHolder.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemModHolder complex, @NotNull PersistentDataAdapterContext context)
    {
        return SerializationUtils.serialize(complex);
    }

    @Override
    public @NotNull ItemModHolder fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context)
    {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(primitive);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return (ItemModHolder) objectInputStream.readObject();
        }
        catch (ClassNotFoundException | IOException e)
        {
            Bukkit.getLogger().warning("Failed to deserialize.");
        }

        return new ItemModHolder();
    }
}
