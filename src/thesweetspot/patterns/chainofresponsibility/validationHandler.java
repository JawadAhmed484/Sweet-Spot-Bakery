/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package thesweetspot.patterns.chainofresponsibility;

import thesweetspot.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public abstract class validationHandler {
    private validationHandler next;

    public validationHandler setNext(validationHandler next) {
        this.next = next;
        return next;
    }

    public boolean handle() {
        if (!validate()) return false;
        if (next != null) return next.handle();
        return true;
    }

    protected abstract boolean validate();
}


