package ch.nostromo.tiffanys.dragonborn.uci;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Constants to be used by all applications
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UciConstants {

    public static final String VERSION = "{$project.version}";

    public static final String APPLICATION = "Tiffanys" + " " + VERSION;

    public static final String AUTHOR = "Bernhard von Gunten";

}
