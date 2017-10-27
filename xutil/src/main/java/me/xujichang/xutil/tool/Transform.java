package me.xujichang.xutil.tool;

/**
 * Created by admin on 2017/1/6.
 */
public class Transform {
    final static double Pi = Math.PI;

    //
    // Krasovsky 1940
    //
    // a = 6378245.0, 1/f = 298.3
    // b = a * (1 - f)
    // ee = (a^2 - b^2) / a^2;
    final static double A = 6378245.0;
    final static double EE = 0.00669342162296594323;

    /// <summary>
    /// 地球WGS84坐标转换至中国火星坐标,如果在中国区域外则返回原值
    /// </summary>
    /// <param name="wgsLat">wgs坐标系纬度</param>
    /// <param name="wgsLon">wgs坐标系经度</param>
    /// <param name="marsLat">计算返回值:火星坐标系纬度</param>
    /// <param name="marsLon">计算返回值:火星坐标系经度</param>
    public static double[] WGS842Mars(double wgsLat, double wgsLon) {
        double[] xyarr = new double[2];
        if (outOfChina(wgsLat, wgsLon)) {
            xyarr[1] = wgsLat;
            xyarr[0] = wgsLon;
            return xyarr;
        }

        double dLat = transformLat(wgsLon - 105.0, wgsLat - 35.0);
        double dLon = transformLon(wgsLon - 105.0, wgsLat - 35.0);

        double radLat = wgsLat / 180.0 * Pi;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * Pi);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Pi);

        xyarr[1] = wgsLat + dLat;
        xyarr[0] = wgsLon + dLon;
        return xyarr;
    }

    static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }

    static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Pi) + 20.0 * Math.sin(2.0 * x * Pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Pi) + 40.0 * Math.sin(y / 3.0 * Pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Pi) + 320 * Math.sin(y * Pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Pi) + 20.0 * Math.sin(2.0 * x * Pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Pi) + 40.0 * Math.sin(x / 3.0 * Pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Pi) + 300.0 * Math.sin(x / 30.0 * Pi)) * 2.0 / 3.0;
        return ret;
    }

    /// <summary>
    /// 火星坐标转换到经纬度坐标，使用二分法，当二分法出现异常时使用回归方程，回归方程精度很低
    /// 经度依赖于wgs842Mars函数
    /// </summary>
    /// <param name="marsLat">火星坐标系--纬度</param>
    /// <param name="marsLng">火星坐标系--经度</param>
    /// <param name="wgsLat">WGS84坐标系--纬度</param>
    /// <param name="wgsLng">WGS84坐标系--经度</param>
    /// <param name="prec">二分法逼近的精度</param>
    public static double[] Mars2WGS(double marsLat, double marsLng) {
        double[] rexy = new double[2];
        double prec = 0.000000001;
        double minx = marsLng - 0.5;
        double miny = marsLat - 0.5;
        double maxx = marsLng + 0.5;
        double maxy = marsLat + 0.5;
        double dis = Double.MAX_VALUE;
        double curx = marsLng;
        double cury = marsLat;
        double calx;
        double caly;
        int count = 0;
        while (dis > prec) {
            curx = (minx + maxx) / 2;
            cury = (miny + maxy) / 2;
            double[] calxy = WGS842Mars(cury, curx);
            calx = calxy[0];
            caly = calxy[1];
            if (caly >= marsLat) {
                maxy = cury;
            } else {
                miny = cury;
            }
            if (calx >= marsLng) {
                maxx = curx;
            } else {
                minx = curx;
            }
            dis = Math.abs(calx - marsLng) + Math.abs(caly - marsLat);
            count++;
            if (count >= 3) {
                ///二元二分法无法保证在单方向单调，所以采用修补试探的方法
                if (Math.abs(maxx - minx) < prec / 5 && Math.abs(calx - marsLng) > prec / 2) {
                    if (calx >= marsLng) {
                        minx -= 0.01;
                    } else {
                        maxx += 0.01;
                    }
                }
                ///二元二分法无法保证在单方向单调，所以采用修补试探的方法
                if (Math.abs(maxy - miny) < prec / 5 && Math.abs(caly - marsLat) > prec / 2) {
                    if (caly >= marsLat) {
                        miny -= 0.01;
                    } else {
                        maxy += 0.01;
                    }
                }
                ///说明判别失败，原因可能在中国区域之外，使用全国拟合试探
                if (minx > maxx || miny > maxy || count > 300) {
                    double wgsLng = 1.368660281996339E-7 * marsLng * marsLng * marsLng + -1.180928130750135E-8 * marsLng * marsLng * marsLat + -2.8882557135286497E-8 * marsLng * marsLat * marsLat + -1.9701061211739227E-8 * marsLat * marsLat * marsLat + -4.3871816410430085E-5 * marsLng * marsLng + 2.312263036974062E-6 * marsLng * marsLat + 3.524130062939154E-6 * marsLat * marsLat + 1.0045663287213806 * marsLng + -1.3931706059903598E-4 * marsLat + -0.15629046141059658;
                    double wgsLat = 1.9633521507503826E-10 * marsLng * marsLng * marsLng + 5.042926958596946E-12 * marsLng * marsLng * marsLat + 1.4343237310979937E-10 * marsLng * marsLat * marsLat + 1.7413852403156825E-7 * marsLat * marsLat * marsLat + -7.126752970697697E-8 * marsLng * marsLng + -9.092793512456868E-7 * marsLng * marsLat + -1.8936576425611334E-5 * marsLat * marsLat + 2.1967158739942877E-5 * marsLng + 1.0006054151828006 * marsLat + -0.003172773997829536;
                    double calx1, caly1;
                    double[] xys = WGS842Mars(wgsLat, wgsLng);
                    calx1 = xys[0];
                    caly1 = xys[1];
                    double dis1 = Math.abs(calx1 - marsLng) + Math.abs(caly1 - marsLat);
                    ///选取合适的结果
                    if (dis1 < dis) {
                        rexy[0] = wgsLng;
                        rexy[1] = wgsLat;
                        return rexy;
                    } else {
                        wgsLat = cury;
                        wgsLng = curx;

                        rexy[0] = wgsLng;
                        rexy[1] = wgsLat;
                        return rexy;
                    }
                }
            }
        }
        rexy[0] = curx;
        rexy[1] = cury;
        return rexy;
    }
}
