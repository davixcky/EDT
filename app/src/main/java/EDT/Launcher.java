package EDT;

import EDT.app.App;

public class Launcher {
    public static void main(String[] args) {
        // Setup internal tracing and graphics acceleration
        System.setProperty("sun.java2d.opengl", "True");
        System.setProperty("sun.java2d.trace", "log,out:mylogfilename.log");

        new App("JIRA").start();
    }
}
