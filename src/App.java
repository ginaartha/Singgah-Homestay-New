import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("LandingPage"), 1120, 760);
        stage.setTitle("Singgah - Homestay Booking Bali");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL location = App.class.getResource(fxml + ".fxml");
        if (location == null) {
            throw new IOException("File " + fxml + ".fxml tidak ditemukan di classpath. "
                + "Pastikan ada di src/ (satu folder dengan App.java) dan ter-copy ke bin/.");
        }
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}