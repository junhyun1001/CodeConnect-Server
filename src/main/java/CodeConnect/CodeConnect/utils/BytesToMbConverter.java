package CodeConnect.CodeConnect.utils;

import java.text.DecimalFormat;

public class BytesToMbConverter {

    public static String bytesToMegabytes(long bytes) {
        double megabytes = (double) bytes / (1024 * 1024);
        DecimalFormat df = new DecimalFormat("0.0");  // 소수점 첫째 자리까지 표시
        return Double.parseDouble(df.format(megabytes)) + "MB";
    }

}
