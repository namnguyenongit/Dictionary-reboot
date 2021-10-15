/*
 * Copyright (c) 2016 acmi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package example._mydictionary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class AutoCompleteComboBox {

    public enum AutoCompleteMode {
        STARTS_WITH((s1, s2) -> s1.toLowerCase().startsWith(s2.toLowerCase())),
        CONTAINING((s1, s2) -> s1.toLowerCase().contains(s2.toLowerCase()));

        private BiPredicate<String, String> filter;

        AutoCompleteMode(BiPredicate<String, String> filter) {
            this.filter = filter;
        }
    }

    public static <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode) {
        ObservableList<T> data = comboBox.getItems();

        comboBox.setEditable(true);
        comboBox.setOnKeyPressed(event -> comboBox.hide());
        comboBox.getEditor().setOnMousePressed(event ->
        {
            ObservableList<T> history = (ObservableList<T>) FXCollections.observableArrayList(DBController.historyWords);
            comboBox.setItems(history);
            comboBox.show();
            comboBox.getEditor().positionCaret(comboBox.getEditor().getText().length());
            comboBox.setVisibleRowCount(Math.min(history.size(), 4));
        });

        comboBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<>() {
            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                ObservableList<T> list = FXCollections.observableList(
                        data.stream()
                                .filter(aData -> mode.filter.test(aData.toString(), comboBox.getEditor().getText()))
                                .collect(Collectors.toList()));
                boolean trigger = false;
                if (event.getCode().toString().equals("ENTER")) {
                    event.consume();
                }
                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox.isShowing()) {
                        comboBox.hide();
                    }
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    setVisibleRows(list.size());
                    if (comboBox.getEditor().getText().length() == 0) {
                        comboBox.hide();
                    }
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                } else if (event.getCode() == KeyCode.DELETE) {
                    setVisibleRows(list.size());
                    if (comboBox.getEditor().getText().length() == 0) {
                        comboBox.hide();
                        comboBox.getEditor().setText("");
                    }
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                } else if (event.getCode() == KeyCode.ENTER) {
                    System.out.println("ENTER");
                    DBController.addHistory(comboBox.getEditor().getText());
                    moveCaretToPos = true;
                    trigger = true;
                }
                if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT || event.getCode().equals(KeyCode.SHIFT) || event.getCode().equals(KeyCode.CONTROL)
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB
                ) {
                    return;
                }
                String t = comboBox.getEditor().getText();
                comboBox.setItems(list);
                comboBox.getEditor().setText(t);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(t.length());
                if (!list.isEmpty() && comboBox.getEditor().getText().length() != 0) {
                    comboBox.show();
                    setVisibleRows(list.size());
                }
                if (trigger) {
                    comboBox.hide();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }

            private void setVisibleRows(int count) {
                comboBox.hide();
                comboBox.setVisibleRowCount(Math.min(count, 4));
                comboBox.show();
            }

        });
    }
}
