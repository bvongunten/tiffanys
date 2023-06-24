package ch.nostromo.tiffanys.commons.uci.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Register line sent from server to client
 */
@Data
@AllArgsConstructor
@ToString
public class ServerRegister {

    public enum RegisterType {LATER, NAME, CODE}

    RegisterType registerType;

    String name;
    String code;
}
