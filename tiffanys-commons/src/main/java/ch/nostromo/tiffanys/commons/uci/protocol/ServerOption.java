package ch.nostromo.tiffanys.commons.uci.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Set option command sent from server to client
 */
@Data
@AllArgsConstructor
@ToString
public class ServerOption {

    String name;
    String value;

}
