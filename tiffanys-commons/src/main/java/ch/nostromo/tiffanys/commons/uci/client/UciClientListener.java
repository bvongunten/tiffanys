package ch.nostromo.tiffanys.commons.uci.client;

import ch.nostromo.tiffanys.commons.uci.protocol.ServerGo;
import ch.nostromo.tiffanys.commons.uci.protocol.ServerOption;
import ch.nostromo.tiffanys.commons.uci.protocol.ServerPosition;
import ch.nostromo.tiffanys.commons.uci.protocol.ServerRegister;

public interface UciClientListener {

   void handleUci();

   void handleUciNewGame();

   void handlePosition(ServerPosition uciServerPosition);

   void handleGo(ServerGo serverGo);

   void handleDebugOn();

   void handleDebugOff();

   void handleSetOption(ServerOption serverOption);

   void handleRegister(ServerRegister serverRegister);

   void handleIsReady();

   void handleStop();

   void handlePonderHit();

   void handleQuit();

}
