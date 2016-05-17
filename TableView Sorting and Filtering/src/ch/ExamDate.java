package ch;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thana
 */
public class ExamDate {

    private final StringProperty day;
    private final StringProperty Date;
    private final BooleanProperty check;
    private final BooleanProperty firstSesstion;
    private final BooleanProperty secondSesstion;
    private final BooleanProperty thirdSesstion;

    public ExamDate(String day, String Date, boolean check, boolean firstSesstion, boolean secondSesstion, boolean thirdSesstion) {

        this.day = new SimpleStringProperty(day);
        this.Date = new SimpleStringProperty(Date);
        this.check = new SimpleBooleanProperty(check);
        this.firstSesstion = new SimpleBooleanProperty(firstSesstion);
        this.secondSesstion = new SimpleBooleanProperty(secondSesstion);
        this.thirdSesstion = new SimpleBooleanProperty(thirdSesstion);
    }

    public String getDay() {
        return day.get();
    }

    public String getDate() {
        return Date.get();
    }

    public boolean isCheck() {
        return check.get();
    }

    public StringProperty dayProperty() {
        return day;
    }

    public StringProperty dateProperty() {
        return Date;
    }

    public BooleanProperty check() {
        return check;
    }

    public BooleanProperty firstSesstion() {
        return firstSesstion;
    }

    public BooleanProperty secondSesstion() {
        return secondSesstion;
    }

    public BooleanProperty thirdSesstion() {
        return thirdSesstion;
    }

    public boolean isFirstSesstion() {
        return firstSesstion.get();
    }

    public boolean isSecondSesstion() {
        return secondSesstion.get();
    }

    public boolean isThirdSesstion() {
        return thirdSesstion.get();
    }    

}
