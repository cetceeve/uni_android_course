package ur.mi.android.laufapp_starter_projekt;


public class Calculator {
    private final static int FAST_JOGGING_KCAL_PER_HOUR = 840;
    private final static int SLOW_JOGGING_KCAL_PER_HOUR = 530;
    private final static double MAX_SLOW_KM_H = 7;
    private int distance;
    private int time;
    private int pause;


    public void setValues(int distance, int time, int pause) {
        this.distance = distance;
        this.time = time;
        this.pause = pause;
    }

    private double calculateSpeed() {
        return ((double) distance) / (((double) time - (double) pause) / 60);
    }

    public double calculatePace() {
        return time/(double) distance;
    }


    public double calculateKcal() {
        double speed = calculateSpeed();
        double kcal = 0;
        if (speed > MAX_SLOW_KM_H) {
            kcal = FAST_JOGGING_KCAL_PER_HOUR * ((double) time / 60);
        } else {
            kcal = SLOW_JOGGING_KCAL_PER_HOUR * ((double) time / 60);
        }
        return kcal;
    }


}
