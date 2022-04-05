import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Control {
    // This is the class where I will put a bunch of static methods related to control actions for the
    // AddressBook class

    /**
     * Get a TabPane to display a specific tab based on an index
     *
     * @param tabPane TabPane
     * @param idx int
     */
    public static void switchToTab(TabPane tabPane, int idx) {
        ObservableList<Tab> tabs = tabPane.getTabs();
        SelectionModel<Tab> selector = tabPane.getSelectionModel();
        selector.select(tabs.get(idx));
    }

    /**
     * Find any TextInputControl Nodes within a Pane and clear them of text
     *
     * @param p Pane
     */
    public static void clearText(Pane p) {
        for (Node node: p.getChildren()) {
            if (node instanceof TextInputControl) {
                ((TextInputControl) node).clear();
            }
        }
    }

    /**
     * Search for any empty text entry points
     *
     * @param p Pane
     * @return True if empty entries found
     */
    public static Boolean emptyTextInput(Pane p) {
        for (Node node: p.getChildren()) {
            if (node instanceof TextInputControl) {
                if (((TextInputControl) node).getText().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }
}
