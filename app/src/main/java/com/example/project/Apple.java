package com.example.project;

import java.util.ArrayList;
import java.util.Arrays;

public class Apple {
    private String name;
    private String location;
    private String ID;
    private Auxdata auxdata;

    public Apple(String name, String location, String ID, Auxdata auxdata) {
        this.name = name;
        this.location = location;
        this.ID = ID;

        this.auxdata = auxdata;
    }

    public class Auxdata {
        private String img;
        private ArrayList<String> characteristics;
        private ArrayList<String> color;

        public Auxdata(String img, String[] color, String[] characteristics) {
            this.img = img;
            this.color = new ArrayList<String>(Arrays.asList(color));
            this.characteristics = new ArrayList<>(Arrays.asList(characteristics));
        }

        public String getImg() {
            return img;
        }

        public ArrayList<String> getCharacteristics() {
            return characteristics;
        }

        public ArrayList<String> getColors() {
            return color;
        }
    }

    public Auxdata getAuxdata() {
        return auxdata;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public String getLocation() {
        return location;
    }
}
