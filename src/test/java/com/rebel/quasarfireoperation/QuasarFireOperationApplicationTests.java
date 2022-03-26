package com.rebel.quasarfireoperation;

import com.google.gson.Gson;
import com.rebel.quasarfireoperation.exception.InexistentSatelliteException;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.model.CargoShip;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteRequest;
import com.rebel.quasarfireoperation.services.DecipherService;
import com.rebel.quasarfireoperation.utils.AppUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuasarFireOperationApplicationTests {

    @Autowired
    private DecipherService decipherService;

    @Autowired
    private MockMvc rebelCommunicationController;

    private final Gson gson = new Gson();

    @Test
    public void message_3_satellites_return_message() throws InsufficientInformationException {
        List<List<String>> messageList = new ArrayList<>();
        messageList.add(Arrays.stream(new String[]{"este", "", "", "mensaje", ""}).collect(Collectors.toList()));
        messageList.add(Arrays.stream(new String[]{"", "es", "", "", "secreto"}).collect(Collectors.toList()));
        messageList.add((Arrays.stream(new String[]{"", "", "un", "", ""}).collect(Collectors.toList())));
        AppUtils.getMessage(messageList);
        String actual = AppUtils.getMessage(messageList);
        String expected = "este es un mensaje secreto";
        assertEquals(expected, actual);
    }

    @Test
    public void message_3_satellitesRequest_return_message() throws InsufficientInformationException {
        SatelliteRequest satelliteRequest = new SatelliteRequest();
        List<Satellite> satellites;

        Satellite satellite1 = new Satellite();
        satellite1.setName("kenobi");
        satellite1.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        satellite1.setDistance(100d);

        Satellite satellite2 = new Satellite();
        satellite2.setName("skywalker");
        satellite2.setMessage(Arrays.asList("", "es", "", "", "secreto"));
        satellite2.setDistance(115.5);

        Satellite satellite3 = new Satellite();
        satellite3.setName("sato");
        satellite3.setMessage(Arrays.asList("", "", "un", "", ""));
        satellite3.setDistance(142.7);

        satellites = Arrays.asList(satellite1, satellite2, satellite3);
        satelliteRequest.setSatellites(satellites);

        CargoShip cargoShip = decipherService.getCargoShip(satelliteRequest);

        String actual = cargoShip.getMessage();
        String expected = "este es un mensaje secreto";
        assertEquals(expected, actual);
    }

    @Test
    public void message_3_split_satellitesRequest_return_message() throws InsufficientInformationException, InexistentSatelliteException {
        Satellite satellite1 = new Satellite();
        satellite1.setName("kenobi");
        satellite1.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        satellite1.setDistance(100d);

        Satellite satellite2 = new Satellite();
        satellite2.setName("skywalker");
        satellite2.setMessage(Arrays.asList("", "es", "", "", "secreto"));
        satellite2.setDistance(115.5);

        Satellite satellite3 = new Satellite();
        satellite3.setName("sato");
        satellite3.setMessage(Arrays.asList("", "", "un", "", ""));
        satellite3.setDistance(142.7);

        decipherService.retreiveMessageFromSatellite(satellite1, satellite1.getName());
        decipherService.retreiveMessageFromSatellite(satellite2, satellite2.getName());
        decipherService.retreiveMessageFromSatellite(satellite3, satellite3.getName());

        CargoShip cargoShip =  decipherService.getCargoShipFromSplitSatellites();

        String actual = cargoShip.getMessage();
        String expected = "este es un mensaje secreto";
        assertEquals(expected, actual);
    }

    @Test
    public void message_3_split_clear_should_throw_exception() throws InsufficientInformationException, InexistentSatelliteException {
        Satellite satellite1 = new Satellite();
        satellite1.setName("kenobi");
        satellite1.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        satellite1.setDistance(100d);

        Satellite satellite2 = new Satellite();
        satellite2.setName("skywalker");
        satellite2.setMessage(Arrays.asList("", "es", "", "", "secreto"));
        satellite2.setDistance(115.5);

        Satellite satellite3 = new Satellite();
        satellite3.setName("sato");
        satellite3.setMessage(Arrays.asList("", "", "un", "", ""));
        satellite3.setDistance(142.7);

        decipherService.retreiveMessageFromSatellite(satellite1, satellite1.getName());
        decipherService.retreiveMessageFromSatellite(satellite2, satellite2.getName());
        decipherService.retreiveMessageFromSatellite(satellite3, satellite3.getName());

        decipherService.getCargoShipFromSplitSatellites();
        decipherService.clearCargoHistory();

        InsufficientInformationException exception = Assertions.assertThrows(InsufficientInformationException.class, () -> {
            decipherService.getCargoShipFromSplitSatellites();
        });

        String expected = "Current data is insufficient to determine message and location";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void split_inexistent_satellite_exception() {
        Satellite satellite1 = new Satellite();
        satellite1.setName("keni");
        satellite1.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        satellite1.setDistance(100d);

        InexistentSatelliteException exception = Assertions.assertThrows(InexistentSatelliteException.class, () -> {
            decipherService.retreiveMessageFromSatellite(satellite1, satellite1.getName());
        });

        String expected = "Satellite name " + satellite1.getName() + " does not exist in our system";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void message_3_satellites_error() {
        List<List<String>> messageList = new ArrayList<>();
        messageList.add(Arrays.stream(new String[]{"", "", "", "", ""}).collect(Collectors.toList()));
        messageList.add(Arrays.stream(new String[]{"", "", "", "", ""}).collect(Collectors.toList()));
        messageList.add((Arrays.stream(new String[]{"", "", "un", "", ""}).collect(Collectors.toList())));

        InsufficientInformationException exception = Assertions.assertThrows(InsufficientInformationException.class, () -> {
            AppUtils.getMessage(messageList);
        });

        String expected = "Message size is incorrect";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    public void location_3_positions() {
        double[][] positions = new double[][]{{500.0, -200.0}, {100.0, -100.0}, {500.0, 100.0}};
        double[] distances = new double[]{200.0, 112.5, 90.7};
        double[] expected = new double[]{326.26878226364704, -10.078218058371585};
        double[] actual = AppUtils.triangulateLocation(positions, distances);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }

    @Test
    public void topsecret_endpoint_should_fetch_message() throws Exception {
        SatelliteRequest satelliteRequest = new SatelliteRequest();
        List<Satellite> satellites;

        Satellite satellite1 = new Satellite();
        satellite1.setName("kenobi");
        satellite1.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        satellite1.setDistance(100d);

        Satellite satellite2 = new Satellite();
        satellite2.setName("skywalker");
        satellite2.setMessage(Arrays.asList("", "es", "", "", "secreto"));
        satellite2.setDistance(115.5);

        Satellite satellite3 = new Satellite();
        satellite3.setName("sato");
        satellite3.setMessage(Arrays.asList("", "", "un", "", ""));
        satellite3.setDistance(142.7);

        satellites = Arrays.asList(satellite1, satellite2, satellite3);
        satelliteRequest.setSatellites(satellites);

        MvcResult result = rebelCommunicationController.perform(post("/api/topsecret")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(satelliteRequest)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        CargoShip cargoShip = gson.fromJson(result.getResponse().getContentAsString(), CargoShip.class);

        String actual = cargoShip.getMessage();
        String expected = "este es un mensaje secreto";
        assertEquals(expected, actual);
    }

    @Test
    public void message_3_split_controller_return_message() throws Exception {
        Satellite satellite1 = new Satellite();
        satellite1.setName("kenobi");
        satellite1.setMessage(Arrays.asList("este", "", "", "mensaje", ""));
        satellite1.setDistance(100d);

        Satellite satellite2 = new Satellite();
        satellite2.setName("skywalker");
        satellite2.setMessage(Arrays.asList("", "es", "", "", "secreto"));
        satellite2.setDistance(115.5);

        Satellite satellite3 = new Satellite();
        satellite3.setName("sato");
        satellite3.setMessage(Arrays.asList("", "", "un", "", ""));
        satellite3.setDistance(142.7);

        rebelCommunicationController.perform(post("/api/topsecret_split/" + satellite1.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(satellite1)))
                .andExpect(status().isOk());

        rebelCommunicationController.perform(post("/api/topsecret_split/" + satellite2.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(satellite2)))
                .andExpect(status().isOk());

        rebelCommunicationController.perform(post("/api/topsecret_split/" + satellite3.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(satellite3)))
                .andExpect(status().isOk());

        MvcResult result = rebelCommunicationController.perform(get("/api/topsecret_split")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        CargoShip cargoShip = gson.fromJson(result.getResponse().getContentAsString(), CargoShip.class);

        String actual = cargoShip.getMessage();
        String expected = "este es un mensaje secreto";
        assertEquals(expected, actual);
    }

    @Test
    public void clear_cargoShip_controller_should_return_ok() throws Exception {
        rebelCommunicationController.perform(get("/api/topsecret_split/clear")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}
