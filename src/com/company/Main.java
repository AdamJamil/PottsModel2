package com.company;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.text.DecimalFormat;

public class Main extends Application
{
    static int n = 7;
    static boolean printTM = false;
    static Driver d;

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Potts Model");
        Group root = new Group();
        Canvas canvas = new Canvas(GUtil.width, GUtil.height);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);

        new GUtil(d, gc);
    }

    public static void main(String[] args)
    {
        long time = System.nanoTime();

        d = new Driver();

        if (d.run())
        {
            System.out.println("shouri wo mitsuketa!");
            launch(args);
        }
        else
        {
            System.out.println("shippaishita :(");
            Platform.exit();
        }

        System.out.println(new DecimalFormat("0.###").format(((double) (System.nanoTime() - time)) / 1000000000) + "s");
    }
}
