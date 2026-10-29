package proyecto.hm;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Controller for the reusable, fully custom alert window. */
public final class HmAlertController {

    @FXML private HBox alertWindow;
    @FXML private Label windowTitle;
    @FXML private Label iconLabel;
    @FXML private Label statusLabel;
    @FXML private Label headerLabel;
    @FXML private Label contentLabel;
    @FXML private HBox actionButtons;
    @FXML private Button closeButton;

    private Stage stage;
    private ButtonType result;
    private boolean completed;
    private List<ButtonType> buttonTypes;
    private double dragOffsetX;
    private double dragOffsetY;

    void configure(Stage stage, HmAlert.Kind kind, String title, String header,
            String content, List<ButtonType> buttonTypes) {
        this.stage = stage;
        this.buttonTypes = buttonTypes;
        alertWindow.getStyleClass().add(HmAlert.styleClass(kind));
        windowTitle.setText(title == null || title.isBlank() ? "H&M" : title);
        iconLabel.setText(kind.getSymbol());
        iconLabel.setAccessibleText(kind.getAccessibleName());
        statusLabel.setText(kind.getAccessibleName().toUpperCase(java.util.Locale.ROOT));
        headerLabel.setText(header == null || header.isBlank() ? kind.getAccessibleName() : header);
        contentLabel.setText(content == null ? "" : content);
        contentLabel.setManaged(!contentLabel.getText().isBlank());
        contentLabel.setVisible(contentLabel.isManaged());
        closeButton.setAccessibleText("Cerrar alerta");

        ButtonType primaryType = buttonTypes.stream()
                .filter(type -> !isCancel(type))
                .findFirst()
                .orElse(buttonTypes.get(0));

        for (ButtonType buttonType : buttonTypes) {
            Button button = new Button(buttonType.getText());
            button.setMnemonicParsing(false);
            button.setAccessibleText(buttonType.getText());
            button.setUserData(buttonType);
            boolean primary = buttonType == primaryType;
            button.getStyleClass().add(primary ? "alert-action-primary" : "alert-action-secondary");
            button.setDefaultButton(primary);
            button.setCancelButton(isCancel(buttonType));
            button.setOnAction(event -> finish(buttonType));
            actionButtons.getChildren().add(button);
        }

        stage.setOnCloseRequest(event -> {
            if (!completed) {
                result = dismissalButton(buttonTypes);
                completed = true;
            }
        });
    }

    @FXML
    private void closeAlert() {
        dismiss();
    }

    @FXML
    private void startDrag(MouseEvent event) {
        if (event.getTarget() == closeButton) {
            return;
        }
        dragOffsetX = event.getScreenX() - stage.getX();
        dragOffsetY = event.getScreenY() - stage.getY();
    }

    @FXML
    private void dragWindow(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            stage.setX(event.getScreenX() - dragOffsetX);
            stage.setY(event.getScreenY() - dragOffsetY);
        }
    }

    ButtonType getResult() {
        return result;
    }

    void dismiss() {
        finish(dismissalButton(buttonTypes));
    }

    private void finish(ButtonType selected) {
        if (completed) {
            return;
        }
        result = selected;
        completed = true;
        stage.close();
    }

    private static boolean isCancel(ButtonType type) {
        return type.getButtonData() != null && type.getButtonData().isCancelButton();
    }

    private static ButtonType dismissalButton(List<ButtonType> types) {
        return types.stream().filter(HmAlertController::isCancel).findFirst()
                .orElse(types.get(0));
    }
}
