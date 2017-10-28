package me.xujichang.util.tool;

/**
 * Created by Administrator on 2017/1/8.
 */
public class BaiduTransform {
    public static double[] TransMars2Baidu(double lat, double lng) {
        double[] outlatlng = new double[2];
        /// 将值转换为19级像素坐标 x,y
        outlatlng = bd_encrypt(lat, lng);
        lat = outlatlng[1];
        lng = outlatlng[0];
        double x, y;
        double[] outxy = LocationToLogicalPoint(lat, lng);
        x = outxy[0];
        y = outxy[1];
        double xx, yy;
        yy = 7.35352340086685E-12 * x * x + 9.39729146931851E-12 * x * y
                + 8.12647141921724E-10 * y * y + 1.98403678528303 * y
                + -0.000212332608690639 * x + 3915.53854399894;
        xx = 2.26599796724512E-12 * x * x + 3.5001926984551E-12 * x * y
                + 7.10391945293842E-13 * y * y + -0.0000551338363883895 * y
                + 1.99995201582597 * x + 546.960811821871;
        x = xx;
        y = yy;

        ///以19级像素坐标计算百度坐标墨卡托坐标值
        double bdy = -9.23252621691802E-13 * x * x + -1.1847454693199E-12 * x * y + 0.0000533054899581017 * x + -1.0289695213812E-10 * y * y + 0.504013673001146 * y + -1945.85220766316;
        double bdx = -2.83449363094412E-13 * x * x + -4.39499358827202E-13 * x * y + 0.500012000279237 * x + -8.97821283288101E-14 * y * y + 0.0000138470631066736 * y + -273.19134165059;
        double[] bdlatlng= Mercator2LngLat(bdx, bdy);
        return  bdlatlng;
    }

    public static double[] TransBaidu2Mars(double bdlat, double bdlng) {

        /// 转换坐标到墨卡托投影坐标系
        double bdx, bdy;
        double[] bdxy = LocationToLogicalPoint(bdlat, bdlng);
        bdx = bdxy[0];
        bdy = bdxy[1];
        /// 换算到百度像素坐标
        double x = 2.27590556595279E-12 * bdx * bdx + 3.48903000589754E-12 * bdx * bdy + 1.99995185232662 * bdx + 6.22586361169189E-13 * bdy * bdy + -0.0000542933221577055 * bdy + 545.606002724657;
        double y = 7.35379347817621E-12 * bdx * bdx + 9.39684565383036E-12 * bdx * bdy + -0.000212336129489022 * bdx + 8.12646661774628E-10 * bdy * bdy + 1.98403679386021 * bdy + 3916.0375954923;

        double xx = -2.73297968370015E-13 * x * x + -4.3688646200802E-13 * x * y + -9.82000305954539E-14 * y * y + 0.0000139020525825477 * y + 0.500011499020342 * x + -267.697879080613;
        double yy = -9.0298913442894E-13 * x * x + -1.19856067495038E-12 * x * y + -1.03052329152388E-10 * y * y + 0.504016205195998 * y + 0.0000524246363084467 * x + -1943.29405470358;

        double[] xy = Mercator2LngLat(xx, yy);
        double[] rexy = bd_decrypt(xy[1], xy[0]);
        return rexy;
    }

    /// 经纬度坐标与墨卡托投影坐标系之间转换

    private static double[] LocationToLogicalPoint(double Latitude, double Longitude) {
        double[] outdoubles = new double[2];
        outdoubles[0] = Longitude * 20037508.34 / 180;
        outdoubles[1] = Math.log(Math.tan((90 + Latitude) * Math.PI / 360)) / (Math.PI / 180);
        outdoubles[1] = outdoubles[1] * 20037508.34 / 180;
        return outdoubles;
    }

    private static double[] Mercator2LngLat(double x, double y) {
        double[] outdoubles = new double[2];
        x = x / 20037508.34 * 180;
        y = y / 20037508.34 * 180;
        y = 180 / Math.PI * (2 * Math.atan(Math.exp(y * Math.PI / 180)) - Math.PI / 2);
        outdoubles[0] = x;
        outdoubles[1] = y;
        return outdoubles;
    }

    ///网上流传百度坐标转换算法
    ///精度很低，此处需要引用
    private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    private static double[] bd_encrypt(double gg_lat, double gg_lon) {
        double[] outdoubles = new double[2];
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        outdoubles[0] = z * Math.cos(theta) + 0.0065;
        outdoubles[1] = z * Math.sin(theta) + 0.006;
        return outdoubles;
    }

    private static double[] bd_decrypt(double bd_lat, double bd_lon) {
        double[] outdoubles = new double[2];
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        outdoubles[0] = z * Math.cos(theta);
        outdoubles[1] = z * Math.sin(theta);
        return outdoubles;
    }
}
