package ntq.lbs.baucuaca;

import android.widget.ImageView;

import java.util.Random;

/**
 * Created by PhuongQuynh on 19/11/2015.
 */
public class Utility {
    private static final Integer[] imageList = {R.drawable.nai, R.drawable.bau, R.drawable.ga, R.drawable.ca, R.drawable.cua, R.drawable.tom};

    public static Integer[] getImageList(){
        return imageList;
    }

    public static int random(ImageView img) {
        Random random = new Random();
        final int maxNumOfDice = 6;
        int value = random.nextInt(maxNumOfDice);
        switch (value) {
            case 0:
                img.setImageResource(imageList[0]);
                return value;
            case 1:
                img.setImageResource(imageList[1]);
                return value;
            case 2:
                img.setImageResource(imageList[2]);
                return value;
            case 3:
                img.setImageResource(imageList[3]);
                return value;
            case 4:
                img.setImageResource(imageList[4]);
                return value;
            default:
                img.setImageResource(imageList[5]);
                return value;
        }
    }
}