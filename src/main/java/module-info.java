module ru.kpfu.itis.gimaletdinova.kfuitisorisgimaletdinovajavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens ru.kpfu.itis.oris.gimaletdinova to javafx.fxml;
    exports ru.kpfu.itis.oris.gimaletdinova;
    exports ru.kpfu.itis.oris.gimaletdinova.controller;
    opens ru.kpfu.itis.oris.gimaletdinova.controller to javafx.fxml;
}