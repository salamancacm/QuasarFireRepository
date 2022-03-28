package com.rebel.quasarfireoperation.utils;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.rebel.quasarfireoperation.exception.InsufficientInformationException;
import com.rebel.quasarfireoperation.model.Position;
import com.rebel.quasarfireoperation.model.Satellite;
import com.rebel.quasarfireoperation.model.SatelliteRequest;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.*;

public class AppUtils {
    //Metodo para obtener distancias
    public static double[] getDistances(SatelliteRequest satelliteRequest) {
        double[] distanceArray = new double[satelliteRequest.getSatellites().size()];
        int i = 0;
        for(Satellite satellite : satelliteRequest.getSatellites()){
            double distance = satellite.getDistance();
            distanceArray[i] = distance;
            i++;
        }
        return distanceArray;
    }
    //Metodo para obtener posiciones
    public static double[][] getPositions(SatelliteRequest satelliteRequest, int positionSize) {
        List<Satellite> satellites = satelliteRequest.getSatellites();
        double[][] positions = new double[satellites.size()][positionSize];
        for (int i = 0; i < satellites.size(); i++) {
            if (satellites.get(i).getPosition() != null) {
                String[] points = satellites.get(i).getPosition().toString().split(",");
                positions[i][0] = Double.parseDouble(points[0]);
                positions[i][1] = Double.parseDouble(points[1]);
            }
        }
        return positions;
    }
    //Metodo para setear posiciones
    public static void setPositions(double[][] pointsList, SatelliteRequest satelliteRequest) {
        Position position;
        for (int i = 0; i < pointsList.length; i++) {
            position = new Position(pointsList[i]);
            satelliteRequest.getSatellites().get(i).setPosition(position);
        }
    }
    //Metodo para extraer mensaje del satelite
    public static List<List<String>> extractMessagesFromSatellites(SatelliteRequest satelliteRequest) {
        List<List<String>> messages = new ArrayList<>();
        for (Satellite s : satelliteRequest.getSatellites()) {
            messages.add(s.getMessage());
        }
        return messages;
    }
    //Metodo para obtener palabras unicas
    private static List<String> getUniqueWords(List<List<String>> messageList) {
        Set<String> wordSet = new HashSet<>();
        messageList.forEach(message -> message.forEach(word -> {
            if (!word.isEmpty() && word.trim().length() > 0) {
                wordSet.add(word);
            }
        }));
        return new ArrayList<>(wordSet);
    }
    //Metodo para obtener mensaje completo
    private static String getCompleteMessage(List<List<String>> messageList) {

        String[] phraseArray = new String[messageList.get(0).size()];

        for (List<String> subList : messageList) {
            for (int i = 0; i < subList.size(); i++) {
                if (!subList.get(i).equals("")) {
                    if (i != subList.size() - 1) {
                        phraseArray[i] = subList.get(i) + " ";
                    } else {
                        phraseArray[i] = subList.get(i);
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        Arrays.stream(phraseArray).forEach(sb::append);

        return sb.toString();
    }
    //Metodo para obtener mensaje
    public static String getMessage(List<List<String>> msgList) throws InsufficientInformationException {
        List<String> messagePhraseList = getUniqueWords(msgList);

        if (!isMessageListSizeValid(msgList, messagePhraseList.size())) {
            throw new InsufficientInformationException("Message size is incorrect");
        }

        return getCompleteMessage(msgList);
    }
    //Metodo para validar tamaño del mensaje
    private static boolean isMessageListSizeValid(List<List<String>> messageList, int size) {
        boolean isValid = true;
        for (List<String> message : messageList) {
            if(size < message.size()){
                isValid = false;
                break;
            }
        }

        return isValid;
    }
    //Metodo para triangular la localizacion de la nave
    public static double[] triangulateLocation(double[][] positions, double[] distances) {
        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
        NonLinearLeastSquaresSolver nSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());
        return nSolver.solve().getPoint().toArray();
    }
}
