package org.dnu.samoylov.controller.main;

import javafx.scene.Node;

public class AbstractFxmlController implements FxmlController {

    private Node view;

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void setView(Node view) {
        this.view = view;
    }
}
