package me.xujichang.xutil.tool;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import java.lang.ref.WeakReference;

/**
 * 定位
 * 包括 WIFI定位 基站定位 GPS 定位
 * Created by xjc on 2017/6/10.
 */

public class LocationTool {
    private WeakReference<Context> contextWeakReference;

    private static LocationTool instance;
    private LocationManager locationManager;
    private LocalizationListener localizationListener;
    private SelfGpsListener GpsListener;
    private SelfLocationListener locationListener;

    public static LocationTool getInstance() {
        if (null == instance) {
            instance = new LocationTool();
        }
        return instance;
    }

    /**
     * 开启定位
     *
     * @param context
     */
    public void startGetLocation(Context context, @NonNull LocalizationListener localizationListener) {
        contextWeakReference = new WeakReference<>(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.localizationListener = localizationListener;
        startWithGps();
    }

    /**
     * 开启GPS定位
     */
    private void startWithGps() {
        boolean isOpened = checkGpsStatus();
        if (!isOpened) {
            localizationListener.onGpsDisable();
        } else {
            getGPSLocation();
        }

    }

    private boolean checkGpsStatus() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void getGPSLocation() {
        if (ActivityCompat.checkSelfPermission(contextWeakReference.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(contextWeakReference.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            localizationListener.onGpsPermissionDenied(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
            return;
        }
        // 获取位置管理服务
        String serviceName = Context.LOCATION_SERVICE;
        String gpsProvider = LocationManager.GPS_PROVIDER;
        locationManager = (LocationManager) contextWeakReference.get().getSystemService(serviceName);
        Location location = locationManager.getLastKnownLocation(gpsProvider); // 通过GPS获取位置
        updateToNewLocation(location);
        GpsListener = new SelfGpsListener();
        locationManager.addGpsStatusListener(GpsListener);
        locationListener = new SelfLocationListener();
        // 设置监听*器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
        locationManager.requestLocationUpdates(gpsProvider, 2 * 1000, 10,
                locationListener);
    }

    private void updateToNewLocation(Location location) {
        LogTool.d("Last Known Location:" + (null == location ? "location is null" : location.toString()));
        localizationListener.onGpsLocation(location);
    }

    public boolean compare(Object tempTarget, Object target) {
        return false;
    }

    public static double[] convertBaidu2Gps(double[] target) {
        double[] mars = BaiduTransform.TransBaidu2Mars(target[0], target[1]);
        return Transform.Mars2WGS(mars[1], mars[0]);
    }

    private class SelfLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LogTool.d("LocationListener:" + location.toString());
            if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
                localizationListener.onGpsLocation(location);
                locationManager.removeGpsStatusListener(GpsListener);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogTool.d("LocationListener:Status Changed:");
            localizationListener.onLocationStatusChanged(status, extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogTool.d("LocationListener: enable");

            localizationListener.onGpsEnable();
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogTool.d("LocationListener: disable");

            localizationListener.onGpsDisable();
        }
    }

    public interface LocalizationListener {
        /**
         * GPS未开启
         */
        void onGpsDisable();

        /**
         * GPS权限 被拒绝
         */
        void onGpsPermissionDenied(String[] permissions);

        /**
         * Gps 定位成功
         *
         * @param location
         */
        void onGpsLocation(Location location);

        /**
         * GPS 状态 改变
         */
        void onGpsStatusChanged();

        /**
         * Gps 可用
         */
        void onGpsEnable();

        void onGpsStop();

        void onGpsFirstFix();

        void onGpsStart();

        void onLocationStatusChanged(int status, Bundle extras);
    }

    public void onDestroy() {
        if (null != locationManager) {
            if (null != GpsListener) {
                locationManager.removeGpsStatusListener(GpsListener);
            }
            if (null != locationListener) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }

    public static class SimpleLocalizationListener implements LocalizationListener {

        @Override
        public void onGpsDisable() {

        }

        @Override
        public void onGpsPermissionDenied(String[] permissions) {

        }

        @Override
        public void onGpsLocation(Location location) {

        }

        @Override
        public void onGpsStatusChanged() {

        }

        @Override
        public void onGpsEnable() {

        }

        @Override
        public void onGpsStop() {

        }

        @Override
        public void onGpsFirstFix() {

        }

        @Override
        public void onGpsStart() {

        }

        @Override
        public void onLocationStatusChanged(int status, Bundle extras) {

        }
    }

    private class SelfGpsListener implements GpsStatus.Listener {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    localizationListener.onGpsFirstFix();

                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    localizationListener.onGpsStatusChanged();

                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    localizationListener.onGpsStart();

                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    localizationListener.onGpsStop();

                    break;
            }
        }
    }


}