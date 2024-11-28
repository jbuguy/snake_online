package snake.util;

public class Time {
    private static float startedTime =System.nanoTime();
    public static float getTime(){
        return (float)((System.nanoTime()-startedTime)*1E-9);
    }
}
