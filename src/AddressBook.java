import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddressBook extends Application {
    enum Countries {
        UK,
        US,
        DE,
        CN
    }

    // This is a change

    ContactsModel contactsModel = new ContactsModel();

    public static void main(String[] args) {
        launch(args);
    }

    private MenuBar setupMenu(BorderPane parent) {
        MenuBar menuBar = new MenuBar();
        TabPane parentTabPane = (TabPane) parent.getCenter();

        // FILE
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem load = new MenuItem("Load");
        file.getItems().addAll(save, load);

        // VIEW
        Menu view = new Menu("View");
        MenuItem entry = new MenuItem("Entry");
        entry.setOnAction(e -> {
            Control.switchToTab(parentTabPane, 0);
        });
        MenuItem list = new MenuItem("List");
        list.setOnAction(e -> {
            Control.switchToTab(parentTabPane, 1);
        });
        MenuItem search = new MenuItem("Search");
        search.setOnAction(e -> {
            Control.switchToTab(parentTabPane, 2);
        });
        view.getItems().addAll(entry, list, search);

        menuBar.getMenus().addAll(file, view);

        return menuBar;
    }

    private Tab setupEntryTab() {
        Tab entry = new Tab("Entry");
        HBox buttons = new HBox();
        TilePane tilePane = new TilePane(10, 4);
        BorderPane borderPane = new BorderPane();

        // Setting up the nodes to be added to the tile pane
        Label nameLabel = new Label("Name:");
        Label numberLabel = new Label("Number:");
        Label addressLabel = new Label("Address:");
        Label countryLabel = new Label("Country:");
        TextField nameField = new TextField();
        TextField numberField = new TextField();
        TextField addressField = new TextField();
        ComboBox<Countries> countryBox = new ComboBox();
        countryBox.getItems().addAll(Countries.values());
        countryBox.setValue(Countries.UK);
        tilePane.setPrefColumns(2);
        tilePane.getChildren().addAll(
                nameLabel,
                nameField,
                numberLabel,
                numberField,
                addressLabel,
                addressField,
                countryLabel,
                countryBox
        );

        // Adding the buttons
        Button entryClear = new Button("Clear");
        entryClear.setOnAction(e -> {
            Control.clearText(tilePane);
        });
        Button entryEdit = new Button("Edit");
        Button entryAdd = new Button("Add");
        entryAdd.setOnAction(e -> {
            // Check for empty required fields
            if (Control.emptyTextInput(tilePane)) {
                showAlert("Empty Inputs",
                        "Please fill in all required entries");
                return;
            }
            // Check for duplicate name/number entries
            else if (contactsModel.exists(nameField.getText(), numberField.getText())) {
                showAlert("Duplicate Input",
                        "An entry with that name and number already exists");
                return;
            }
            // Create our new contact
            Contact newContact = new Contact(
                    nameField.getText(),
                    numberField.getText(),
                    addressField.getText(),
                    countryBox.getValue()
            );
            contactsModel.add(newContact);
        });
        Button entryDelete = new Button("Delete");
        entryDelete.setOnAction(event -> {
            // Remove the matching entry from the data
            if (!contactsModel.remove(nameField.getText(), numberField.getText())) {
                showAlert("No Such Entry",
                        "Could not find an entry matching than name and number");;
            }
        });
        buttons.getChildren().addAll(
                entryClear,
                entryEdit,
                entryAdd,
                entryDelete
        );
        buttons.setAlignment(Pos.CENTER);

        // Putting it all together
        borderPane.setCenter(tilePane);
        borderPane.setBottom(buttons);
        entry.setContent(borderPane);

        return entry;
    }

    private Tab setupListTab() {
        Tab list = new Tab("List");
        TableView table = contactsModel.getContactTable();

        list.setContent(table);

        return list;
    }


    private Tab setupSearchTab() {
        Tab search = new Tab("Search");
        TilePane tilePane = new TilePane(10, 4);
        VBox vbox = new VBox();

        // Nodes to go in the TilePane
        Label nameLabel = new Label("Name:");
        Label numberLabel = new Label("Number:");
        TextField nameField = new TextField();
        TextField numberField = new TextField();
        Button clearButton = new Button("Clear");
        Button searchButton = new Button("Search");

        // Adding nodes to the TilePane
        tilePane.setPrefColumns(2);
        tilePane.getChildren().addAll(
                nameLabel,
                nameField,
                numberLabel,
                numberField,
                clearButton,
                searchButton
        );

        vbox.getChildren().addAll(tilePane, contactsModel.getContactTable());
        search.setContent(vbox);
        return search;
    }

    @Override
    public void start(Stage primaryStage) {
        //FlowPane root = new FlowPane(); // The root node of our scene
        BorderPane root = new BorderPane(); // The border pane, to which everything will be added
        Scene scene = new Scene(root); // The scene
        TabPane tabPane = new TabPane();

        // Adding the tabs
        tabPane.getTabs().addAll(
                setupEntryTab(),
                setupListTab(),
                setupSearchTab()
        );

        root.setCenter(tabPane);
        root.setTop(setupMenu(root));

        //root.getChildren().add(root);

        primaryStage.setTitle("Address Book");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
}
