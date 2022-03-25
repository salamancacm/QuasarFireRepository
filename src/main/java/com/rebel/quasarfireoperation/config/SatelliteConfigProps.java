package com.rebel.quasarfireoperation.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "rebel")
public class SatelliteConfigProps {

    private Map<String, SatelliteCoordinates> satellites;

    public Map<String, SatelliteCoordinates> getSatellites() {
        return satellites;
    }

    public void setSatellites(Map<String, SatelliteCoordinates> satellites) {
        this.satellites = satellites;
    }

    public static class SatelliteCoordinates {
        private String position;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
}
