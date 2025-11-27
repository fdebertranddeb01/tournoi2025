/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.beuvron.vaadin.projets.tournoi.webui.tests;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.MyIntegerField;
import java.util.Optional;

/**
 *
 * @author fdebertranddeb01
 */
@Route(value = "tests/integerField",layout = MainLayout.class)
public class TestIntegerTextField extends VerticalLayout{
    
    private MyIntegerField iField;
    
    public TestIntegerTextField() {
        this.add(new H2("Test de IntegerField"));
        this.iField = new MyIntegerField("Entrez un entier");
        this.add(this.iField);
        this.add(new Button("valeur Actuelle", (t) -> {
            Optional<Integer> val = this.iField.getIntValue();
            Notification.show("val texte : " + this.iField.getValue() +
                    " ; val int : " + (val.isPresent() ?  val.get() : "invalid"));
        }));
        this.add(new H3("Un textfield qui ne fait rien pour tester le focus"));
        this.add(new TextField("pour rien"));
    }
    
}
