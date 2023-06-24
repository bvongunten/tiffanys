package ch.nostromo.tiffanys.commons.uci.protocol;

import lombok.Data;

import java.util.List;

/**
 * Option information sent from client to server
 */
@Data
public class ClientOption {

    public enum OptionType {CHECK, SPIN, COMBO, BUTTON, STRING}

    String name;
    OptionType type;

    String value;
    String defaultValue;

    List<String> vars;

    String min;
    String max;

}
