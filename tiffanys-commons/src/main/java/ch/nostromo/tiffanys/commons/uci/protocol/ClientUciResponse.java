package ch.nostromo.tiffanys.commons.uci.protocol;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 * Uci line sent from client to server, including options
 */
@Data
@ToString
public class ClientUciResponse {

    String name;
    String author;

    List<ClientOption> clientOptions = new ArrayList<>();
}
