package cn.com.heaton.blelibrary.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Iterator;

import cn.com.heaton.blelibrary.ble.callback.BleConnCallback;
import cn.com.heaton.blelibrary.ble.callback.BleNotiftCallback;
import cn.com.heaton.blelibrary.ble.callback.BleReadCallback;
import cn.com.heaton.blelibrary.ble.callback.BleReadRssiCallback;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;
import cn.com.heaton.blelibrary.ble.exception.BleServiceException;
import cn.com.heaton.blelibrary.ble.proxy.RequestImpl;
import cn.com.heaton.blelibrary.ble.proxy.RequestLisenter;
import cn.com.heaton.blelibrary.ble.proxy.RequestProxy;
import cn.com.heaton.blelibrary.ble.request.ConnectRequest;
import cn.com.heaton.blelibrary.ble.request.ScanRequest;

/**
 * This class provides various APIs for Bluetooth operation
 * Created by liulei on 2016/12/7.
 */

public class Ble<T extends BleDevice> implements BleLisenter<T>{

    /** Log tag, apps may override it. */
    private final static String TAG = "Ble";

    private static volatile Ble instance;

    private Options mOptions;

    private RequestLisenter<T> mRequest;

    /*private static final Map<String,List<Class<?>>> bleDeviceCache = new HashMap<>();*/

    private final Object mLocker = new Object();

    private BluetoothLeService mBluetoothLeService;

    public static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;

    private final ArrayList<T> mAutoDevices = new ArrayList<>();

    /**
     * Initializes a newly created {@code BleManager} object so that it represents
     * a bluetooth management class .  Note that use of this constructor is
     * unnecessary since Can not be externally constructed.
     */
    private Ble() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ///Temporarily comment out the code post-maintenance may re-use the code
       /* Type superClass = getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        mDeviceClass = getClass(type,0);*/
    }

    public boolean init(Context context,Options opts){
        if(opts == null){
            opts = new Options();
        }
        mOptions = opts;
        BleLog.init(opts);
        //Set up a dynamic proxy
        mRequest = (RequestLisenter) RequestProxy
                .getInstance()
                .bindProxy(RequestImpl.getInstance(opts));

        boolean result = instance.startService(context);
        BleLog.w(TAG, "bind service result is"+ result);
        return result;
    }

    public void startScan(BleScanCallback<T> callback){
        mRequest.startScan(callback);
    }

    public void stopScan(){
        mRequest.stopScan();
    }

    /**
     * connecte bleDevice
     *
     * @param device
     * @return
     */
    public boolean connect(T device, BleConnCallback<T> callback) {
        synchronized (mLocker) {
            return mRequest.connect(device, callback);
        }
    }

    /**
     * Reconnection equipment
     * <p>
     * TODO Later will add reconnection times
     *
     * @param device device
     * @return Whether the connection is successful
     */
//    private boolean reconnect(T device) {
//        // TODO: 2017/10/16 auth:Alex-Jerry  [2017/11/16]
//        return connect(device);
//    }

    /**
     * disconnect device
     *
     * @param device ble address
     */
    public void disconnect(T device) {
        mRequest.disconnect(device);
//        synchronized (mLocker) {
//            if (mBluetoothLeService != null) {
//                //Traverse the connected device collection to disconnect automatically cancel the automatic connection
//                for (T bleDevice : mConnetedDevices) {
//                    if (bleDevice.getBleAddress().equals(device.getBleAddress())) {
//                        Log.e(TAG, "disconnect: " + "设置自动连接false");
//                        bleDevice.setAutoConnect(false);
//                    }
//                }
//                mBluetoothLeService.disconnect(device.getBleAddress());
//            RequestManager.executeDisConnectRequest(device);
//        }
    }

    public void startNotify(T device, BleNotiftCallback<T> callback){
        mRequest.notify(device, callback);
    }

    public void read(T device, BleReadCallback<T> callback){
        mRequest.read(device, callback);
    }

    public void readRssi(T device, BleReadRssiCallback<T> callback){
        mRequest.readRssi(device, callback);
    }

    public boolean write(T device, byte[]data, BleWriteCallback<T> callback){
        return mRequest.write(device, data, callback);
    }

    public Class<T> getClassType(){
        Type genType = this.getClass().getGenericSuperclass();
        Class<T> entityClass = (Class<T>)((ParameterizedType)genType).getActualTypeArguments()[0];
        return entityClass;
    }

    /**
     * Get the class object
     *
     * @param type TYPE
     * @param i    LOCATION
     * @return Object
     */
    private static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { //Processing generic types
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0); // Handle the generic wipe object
        } else {// Class itself is also type, forced transformation
            return (Class) type;
        }
    }

    private static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // Processing multistage generic
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // Processing array generics
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { //Handle the generic wipe object
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

    public static <T extends BleDevice> Ble<T> getInstance(){
        if (instance == null) {
            synchronized (Ble.class) {
                if (instance == null) {
                    instance = new Ble();
                }
            }
        }
        return instance;
    }

    public BluetoothLeService getBleService() {
        return mBluetoothLeService;
    }

    /**
     * start bind service
     *
     * @return Whether the service is successfully bound
     */
    private boolean startService(Context context) {
        Intent gattServiceIntent = new Intent(context, BluetoothLeService.class);
        boolean bll = false;
        if (context != null) {
            bll = context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
        if (bll) {
            BleLog.i(TAG, "service bind succseed!!!");
        } else if(mOptions.throwBleException){
            try {
                throw new BleServiceException("Bluetooth service binding failed," +
                        "Please check whether the service is registered in the manifest file!");
            } catch (BleServiceException e) {
                e.printStackTrace();
            }
        }
        return bll;
    }

    /**
     * unbind service
     */
    public void unService(Context context) {
        if (context != null) {
            context.unbindService(mServiceConnection);
            mBluetoothLeService = null;
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if(instance != null)
                mBluetoothLeService.setBleManager(instance, mOptions);

            BleLog.e(TAG, "Service connection successful");
            if (!mBluetoothLeService.initialize()) {
                BleLog.e(TAG, "Unable to initialize Bluetooth");
//                for (BleLisenter bleLisenter : mBleLisenters) {
//                    bleLisenter.onInitFailed();
//                }
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    public T getBleDevice(int index) {
        ConnectRequest request = ConnectRequest.getInstance();
        if(request != null){
            return (T) request.getBleDevice(index);
        }
        return null;
    }

    public T getBleDevice(BluetoothDevice device) {
        ConnectRequest request = ConnectRequest.getInstance();
        if(request != null){
            return (T) request.getBleDevice(device);
        }
        return null;
    }

    /**
     *   Get the device type   for example: BleDevice.class
     * @return device type
     */
    //Temporarily comment out the code post-maintenance may re-use the code
//    public Class<T> getDeviceClass(){
//        return mDeviceClass;
//    }

    /**
     * Get the lock
     */
    public Object getLocker() {
        return mLocker;
    }

    /**
     * Whether it is scanning
     */
    public boolean isScanning() {
        ScanRequest request = ScanRequest.getInstance();
        return request.isScanning();
    }

    /**
     * Gets the connected device
     *
     * @return connected device
     */

    public ArrayList<T> getConnetedDevices() {
        ConnectRequest request = ConnectRequest.getInstance();
        if(request != null){
            return request.getConnetedDevices();
        }
        return null;
    }

//    private class AutoConThread extends Thread {
//        @Override
//        public void run() {
//            while (true) {
//                if (mAutoDevices.size() > 0) {
//                    //Turn on cyclic scan
//                    if (!mScanning) {
//                        Log.e(TAG, "run: " + "Thread began scanning...");
////                        scanLeDevice(true);
//                    }
//                }
//                SystemClock.sleep(2 * 1000);
//            }
//        }
//
//    }

    /**
     * If it is automatically connected device is removed from the automatic connection pool
     *
     * @param device Device object
     */
    private void removeAutoPool(BleDevice device) {
        if (device == null) return;
        Iterator<T> iterator = mAutoDevices.iterator();
        while (iterator.hasNext()) {
            BleDevice item = iterator.next();
            if (device.getBleAddress().equals(item.getBleAddress())) {
                iterator.remove();
            }
        }
    }

    /**
     * Add a disconnected device to the autouppool
     *
     * @param device Device object
     */
    private void addAutoPool(T device) {
        if (device == null) return;
        for (BleDevice item : mAutoDevices) {
            if (device.getBleAddress().equals(item.getBleAddress())) {
                return;
            }
        }
        if (device.isAutoConnect()) {
            BleLog.w(TAG, "addAutoPool: "+"Add automatic connection device to the connection pool");
            mAutoDevices.add(device);
        }
    }


    //when application has onDestory, release all resources
    public void destory(Context context){
        unService(context);
    }

    /**
     * Release Empty all resources
     */
//    public void clear() {
//        synchronized (mLocker) {
//            for (BleDevice bleDevice : mConnetedDevices) {
//                disconnect(bleDevice);
//            }
//            mConnetedDevices.clear();
//            mConnectingDevices.clear();
//        }
//    }

    /**
     * Whether to support Bluetooth
     *
     * @return Whether to support Ble
     */
    public boolean isSupportBle(Context context) {
        return (mBluetoothAdapter != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE));
    }

    /**
     * Bluetooth is turned on
     *
     * @return true  Bluetooth is turned on
     */
    public boolean isBleEnable() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * open ble
     *
     * @param activity The context object
     */
    public void turnOnBlueTooth(Activity activity) {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!isBleEnable()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * close ble
     */
    public boolean turnOffBlueTooth() {
        return !mBluetoothAdapter.isEnabled() || mBluetoothAdapter.disable();
    }

    public static class Options extends BluetoothLeService.Options{
        public boolean logBleExceptions = true;
        public boolean throwBleException = true;
        public boolean autoConnect = false;
        public int connectTimeout = 10 * 1000;
        public int scanPeriod = 10 * 1000;
        public int serviceBindFailedRetryCount = 3;
        public int connectFailedRetryCount = 3;

    }


}
