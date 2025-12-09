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
package fr.insa.beuvron.vaadin.projets.tournoi.webui.utils;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import java.util.Optional;

/**
 * @author fdebertranddeb01
 */
public class MyIntegerField extends TextField {

  private int poub;

  public MyIntegerField() {
    super();
    this.finInit();
  }

  public MyIntegerField(String label) {
    super(label);
    this.finInit();
  }

  private void finInit() {
    Binder<MyIntegerField> binder = new Binder<>(MyIntegerField.class);
    binder
        .forField(this)
        .withConverter(new StringToIntegerConverter("doit être un entier"))
        .bind(MyIntegerField::getPoub, MyIntegerField::setPoub);
  }

  /**
   * @return the intValue
   */
  public Optional<Integer> getIntValue() {
    try {
      return Optional.of(Integer.parseInt(this.getValue()));
    } catch (NumberFormatException ex) {
      return Optional.empty();
    }
  }

  /**
   * @return the poub
   */
  public int getPoub() {
    return poub;
  }

  /**
   * @param poub the poub to set
   */
  public void setPoub(int poub) {
    this.poub = poub;
  }
}
