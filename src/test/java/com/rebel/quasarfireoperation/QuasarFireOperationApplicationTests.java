package com.rebel.quasarfireoperation;

import com.rebel.quasarfireoperation.exception.MessageException;
import com.rebel.quasarfireoperation.services.LocationService;
import com.rebel.quasarfireoperation.services.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class QuasarFireOperationApplicationTests {

    @Autowired
    private MessageService messageService;

    @Autowired
    private LocationService locationService;

    @Test
    public void message_3_satellites_return_message() throws MessageException {
        List<List<String>> messages = new ArrayList<>();
        String[] m1 = {"this", "", "", "secret", ""};
        String[] m2 = {"", "is", "", "", "message"};
        String[] m3 = {"", "", "a", "", ""};
        messages.add(Arrays.stream(m1).collect(Collectors.toList()));
        messages.add(Arrays.stream(m2).collect(Collectors.toList()));
        messages.add(Arrays.stream(m3).collect(Collectors.toList()));
        String message = messageService.getMessage(messages);
        String expectedMsg = "this is a secret message";
        assertEquals(message, expectedMsg);
    }

    @Test
    public void message_3_satellites_error() {
        List<List<String>> messages = new ArrayList<>();
        String[] m1 = {"this", "", "", "secret", ""};
        String[] m2 = {"", "is", "", "", "message"};
        String[] m3 = {"this", "", "a", "", "", ""};
        messages.add(Arrays.stream(m1).collect(Collectors.toList()));
        messages.add(Arrays.stream(m2).collect(Collectors.toList()));
        messages.add(Arrays.stream(m3).collect(Collectors.toList()));
        try {
            String message = messageService.getMessage(messages);
        } catch (MessageException e) {
            assertEquals("Can't determine message content", e.getMessage());
        }
    }

    @Test
    public void location_3_positions() throws Exception {
        double[][] positions = new double[][]{{-500.0, -200.0}, {100.0, -100.0}, {500.0, 100.0}};
        double[] distances = new double[]{100.0, 115.5, 142.7};
        double[] expectedPosition = new double[]{-58.315252587138595, -69.55141837312165};
        double[] calculatedPosition = locationService.triangulateLocation(positions, distances);
        for (int i = 0; i < calculatedPosition.length; i++) {
            assertEquals(expectedPosition[i], calculatedPosition[i]);
        }
    }

}
