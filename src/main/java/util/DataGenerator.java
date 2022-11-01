package util;

import models.Event;
import models.Person;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class DataGenerator {
    private static DataHolder<String> femaleNames;
    private static DataHolder<String> maleNames;
    private static DataHolder<String> lastNames;
    private static LocationData locations;
    private static Set<String> Ids;
    private static final int ID_LENGTH = 8;

    static {
        try {
            femaleNames=DataHolder.create("json/fnames.json", String.class);
            maleNames=DataHolder.create("json/mnames.json", String.class);
            lastNames=DataHolder.create("json/snames.json", String.class);
            locations=LocationData.create("json/locations.json");
            Ids = new HashSet<>();
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Generates a random person from the list of json names
     * @param username The username this person will be associated with
     * @param gender The gender of this person
     * @return The random person object
     */
    public static Person getRandomPerson(String username, char gender) {
        DataHolder<String> firstNames = gender == 'm' ? maleNames : femaleNames;

        String firstname = getRandomValue(firstNames);
        String lastName = getRandomValue(lastNames);
        String personId = getRandomId();

        return new Person(personId, username, firstname, lastName, gender);
    }

    public static Event getRandomEvent(String username, String eventType, String personID, int from, int to) {
        Location location = getRandomValue(locations);
        int year = getRandomValue(from, to);

        return new Event(getRandomId(), username, personID, location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(), eventType, year);
    }

    /**
     * Generates a random id
     * @return A string of random id
     */
    public static String getRandomId() {
//        String id = "";
//        for (int i = 0; i < ID_LENGTH; i++) {
//            int val = getRandomValue((int)'a', (int)'z');
//
//            id += (char)val;
//        }
//
//        //Make sure this id is unique
//        if (Ids.contains(id)) {
//            return getRandomId();
//        }
//
//        Ids.add(id);
//
//        return id;
        return UUID.randomUUID().toString();
    }

    private static <T> T getRandomValue(DataHolder<T> dataHolder) {
        T[] data = dataHolder.getData();
        int num = getRandomValue(0, data.length);

        return data[num];
    }

    private static int getRandomValue(int from, int to) {
        Random rnd = new Random();
        int num = rnd.nextInt(to - from);

        num += from;

        return num;
    }

    static class DataHolder<T> {
        private T[] data;
        private String filePath;

        public static <T> DataHolder<T> create(String filePath, Class<T> type) throws FileNotFoundException {
            DataHolder<T> holder = new DataHolder<>();
            holder = Encoder.DecodeFromFilePath(filePath, holder.getClass());

            return holder;
        }

        public T[] getData() {
            return data;
        }

        public void setData(T[] data) {
            this.data=data;
        }
    }

    static class LocationData extends DataHolder<Location> {
        public static LocationData create(String filePath) throws FileNotFoundException {
            LocationData data = Encoder.DecodeFromFilePath(filePath, LocationData.class);

            return data;
        }
    }


    static class Location {
        private String city;
        private String country;
        private float latitude;
        private float longitude;


        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city=city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country=country;
        }

        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude=latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude=longitude;
        }
    }
}
