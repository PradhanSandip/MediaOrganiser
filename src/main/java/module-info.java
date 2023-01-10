module com.java.project.MediaOrganiserProgram {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires xuggle.xuggler;
	requires slf4j.reload4j;

    opens com.java.project.MediaOrganiserProgram to javafx.fxml;
    exports com.java.project.MediaOrganiserProgram;
}
