package com.sfc.sf2.map.io;

import com.sfc.sf2.core.io.EmptyPackage;
import com.sfc.sf2.core.io.asm.AbstractAsmProcessor;
import com.sfc.sf2.core.io.asm.AsmException;
import com.sfc.sf2.helpers.StringHelpers;
import com.sfc.sf2.map.MapEntity;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads mapsetup s1_entities.asm (msFixedEntity / msWalkingEntity).
 *
 * @author TiMMy
 */
public class MapEntitiesAsmProcessor extends AbstractAsmProcessor<MapEntity[], EmptyPackage> {

    @Override
    protected MapEntity[] parseAsmData(BufferedReader reader, EmptyPackage pckg) throws IOException, AsmException {
        ArrayList<MapEntity> entities = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = StringHelpers.trimAndRemoveComments(line);
            if (line.startsWith("msEntitiesEnd") || line.startsWith("endWord")) {
                break;
            }
            boolean walking = line.startsWith("msWalkingEntity");
            boolean fixed = line.startsWith("msFixedEntity");
            if (!walking && !fixed) {
                continue;
            }
            String[] split = line.substring(line.indexOf(' ')).split(",");
            if (split.length < 4) {
                continue;
            }
            int x = StringHelpers.getValueInt(split[0].trim());
            int y = StringHelpers.getValueInt(split[1].trim());
            String facing = split[2].trim();
            String sprite = split[3].trim();
            entities.add(new MapEntity(x, y, facing, sprite, walking));
        }
        return entities.toArray(new MapEntity[0]);
    }

    @Override
    protected String getHeaderName(MapEntity[] item, EmptyPackage pckg) {
        return "Map entities";
    }

    @Override
    protected void packageAsmData(FileWriter writer, MapEntity[] item, EmptyPackage pckg) throws IOException, AsmException {
        throw new UnsupportedOperationException("Entity export is not implemented; edit mapsetup ASM directly.");
    }
}
