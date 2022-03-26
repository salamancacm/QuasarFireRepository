package com.rebel.quasarfireoperation;

import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteRequest;
import com.rebel.quasarfireoperation.utils.AppUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class QuasarFireOperationApplicationTests {

    @Test
    public void message_3_satellites_return_message() throws InsufficientInformationException {
        List<List<String>> messageList = new ArrayList<>();
        messageList.add(Arrays.stream(new String[]{"este", "", "", "secreto", ""}).collect(Collectors.toList()));
        messageList.add(Arrays.stream(new String[]{"", "es", "", "", "mensaje"}).collect(Collectors.toList()));
        messageList.add((Arrays.stream(new String[]{"", "", "un", "", ""}).collect(Collectors.toList())));
        AppUtils.getMessage(messageList);
        String actual = AppUtils.getMessage(messageList);
        String expected = "este es un mensaje secreto";
        assertEquals(expected, actual);
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

}
