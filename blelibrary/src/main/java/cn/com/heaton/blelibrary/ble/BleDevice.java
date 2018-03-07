package cn.com.heaton.blelibrary.ble;

import android.bluetooth.BluetoothDevice;

/**
 * Created by LiuLei on 2016/11/26.
 * If you need to set the Bluetooth object more properties and behavior can be inherited from the class     such as extends BleDevice
 */

public class BleDevice {

    public final static String          TAG                      = BleDevice.class.getSimpleName();

    /**
     *  Connection Status:
     *  2503 Not Connected
     *  2504 Connected
     *  2505 Connected
     *  2506 Disconnected
     */
    private int mConnectionState = BleStates.BleStatus.DISCONNECT;

    /**
     *   Bluetooth address
     */
    private String mBleAddress;

    /*
        自定义基站id
     */
    private String device_id;

    public String getDevice_id() {
        return device_id;
    }

    /**
     *  Bluetooth name
     */
    private String mBleName;
    /**
     *   Bluetooth modified name
     */
    private String mBleAlias;

    private boolean mAutoConnect = false;//The default is not automatic connection

    /**
     * Use the address and name of the BluetoothDevice object
     * to construct the address and name of the {@code BleDevice} object
     *
     * @param device BleDevice
     */
    protected BleDevice(BluetoothDevice device) {
        this.mBleAddress = device.getAddress();
        this.mBleName = device.getName();
    }
    public BleDevice(String address,String name,String device_id){
        this.mBleAddress=address;
        this.mBleName=name;
        this.device_id=device_id;
    }


    public boolean isConnected() {
        return mConnectionState == BleStates.BleStatus.CONNECTED;
    }

    public boolean isConnectting() {
        return mConnectionState == BleStates.BleStatus.CONNECTING;
    }

    public boolean isAutoConnect() {
        return mAutoConnect;
    }

    public void setAutoConnect(boolean mAutoConnect) {
        this.mAutoConnect = mAutoConnect;
    }

    public int getConnectionState() {
        return mConnectionState;
    }

    public void setConnectionState(@BleStates.BleStatus int state){
        mConnectionState = state;
    }


    public String getBleAddress() {
        return mBleAddress;
    }

    public void setBleAddress(String mBleAddress) {
        this.mBleAddress = mBleAddress;
    }

    public String getmBleName() {
        return mBleName;
    }

    public void setBleName(String mBleName) {
        this.mBleName = mBleName;
    }

    public String getBleAlias() {
        return mBleAlias;
    }

    public void setBleAlias(String mBleAlias) {
        this.mBleAlias = mBleAlias;
    }

}
