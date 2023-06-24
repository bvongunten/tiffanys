module ch.nostromo.tiffanys.dragonborn {
    requires java.logging;
    requires static lombok;

    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires java.naming;

    requires ch.nostromo.tiffanys.commons;
    requires java.desktop;

    exports ch.nostromo.tiffanys.dragonborn.engine;

    uses org.slf4j.spi.SLF4JServiceProvider;
}
