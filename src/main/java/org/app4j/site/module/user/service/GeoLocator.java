package org.app4j.site.module.user.service;

import java.net.InetAddress;

/**
 * @author chi
 */
public interface GeoLocator {
    String city(InetAddress inetAddress);

    String country(InetAddress inetAddress);
}
