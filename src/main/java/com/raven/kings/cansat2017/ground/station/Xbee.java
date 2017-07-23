package com.raven.kings.cansat2017.ground.station;

//import java.awt.List;
import java.util.List;

//import com.digi.xbee.api.WiFiDevice;
import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.OperationNotSupportedException;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.XBee16BitAddress;
import com.digi.xbee.api.models.XBee64BitAddress;
import com.digi.xbee.api.models.XBeeMessage;
import com.digi.xbee.api.utils.ByteUtils;

import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.listeners.IDiscoveryListener;

public class Xbee implements IDiscoveryListener, IDataReceiveListener
{

    long lncTime;
    long time;
    boolean missionStarted = false;
    XBeeDevice myDevice;
    XBeeNetwork network;
    //0013A20041544CA4
    static byte[] deployCommand = { (byte) 0xCC, 0x0D };
    static XBee64BitAddress ctnAdd = new XBee64BitAddress(0x00, 0x13, 0xA2, 0x00, 0x41, 0x54, 0x4C, 0xA4);//Manually setting it to my xbee's address

    public Xbee()
    {
    }

    public void connect(String PORT, int BAUD_RATE)
    {
        myDevice = new XBeeDevice(PORT, BAUD_RATE);
        try
        {
            myDevice.open();
            myDevice.addDataListener(this);
            network = myDevice.getNetwork();
            network.addDiscoveryListener(this);

        } catch (XBeeException e)
        {
            System.out.println(">> Error opening XBee device on " + PORT + " with " + BAUD_RATE + " baud.");
            e.printStackTrace();
        }
    }

    public void close()
    {
        if (myDevice != null)
        {
            if (myDevice.isOpen())
            {
                System.out.println("Port closed.");
                myDevice.close();
            } else
            {
                System.out.println("Port already closed.");
            }
        }
    }

    public void startDiscovery()
    {
        if (myDevice != null)
        {
            if (myDevice.isOpen())
            {
                if (!network.isDiscoveryRunning())
                {
                    // updateConnected.run();
                    System.out.println("NETWORK: Starting Network Discovery...");
                    network.clearDeviceList();
                    Main.getMain().myGui.setContainerConnected(false);
                    Main.getMain().myGui.setPayloadConnected(false);
                    network.startDiscoveryProcess();
                }
            } else
            {
                System.out.println("Local device is not open.");
            }
        } else
        {
            System.out.println("Local device is not open.");
        }
    }

    @Override
    public void deviceDiscovered(RemoteXBeeDevice discoveredDevice)
    {
        System.out.println("NETWORK: New device discovered: " + discoveredDevice.toString());
        if (discoveredDevice.getNodeID().equals("ctn"))
        {
            ctnAdd = discoveredDevice.get64BitAddress();
            Main.getMain().myGui.setContainerConnected(true);
        } else if (discoveredDevice.getNodeID().equals("pay"))
        {
            Main.getMain().myGui.setPayloadConnected(true);
        }
    }

    @Override
    public void discoveryError(String error)
    {
        System.out.println("NETWORK: Discovery error -" + error);

    }

    @Override
    public void discoveryFinished(String error)
    {
        if (error != null)
        {
            System.out.println("NETWORK: Discovery error -" + error);
        } else
        {
            System.out.println(
                    "NETWORK: Discovery finished successfully. Connected Devices: " + network.getNumberOfDevices());
        }
    }

    // Thread updateConnected = new Thread()
    // {
    // @Override
    // public void run()
    // {
    // List<RemoteXBeeDevice> devices = network.getDevices();
    // //Main.getMain().myGui.setPayloadConnected(false);
    // //Main.getMain().myGui.setContainerConnected(false);
    // for (int i = 0; i < devices.size(); i++)
    // {
    // if (devices.get(i).getNodeID().equals(" ctn"))
    // {
    // Main.getMain().myGui.setContainerConnected(true);
    // } else if (devices.get(i).getNodeID().equals(" pay"))
    // {
    // Main.getMain().myGui.setPayloadConnected(true);
    // }
    // }
    // }
    // };

    public void sendDeployCommand()
    {
        if (network != null)
        {
            
            try
            {
                myDevice.sendDataAsync(network.getDevice(ctnAdd), deployCommand);
            } catch (XBeeException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dataReceived(XBeeMessage xbeeMessage)
    {
        System.out.println(xbeeMessage.getDevice().get64BitAddress());
        String nodeId = xbeeMessage.getDevice().getNodeID();
        byte[] d = xbeeMessage.getData();
        String dString = ByteUtils.byteArrayToString(xbeeMessage.getData());
        char[] dChars = dString.toCharArray();
        //System.out.println(String.copyValueOf(dChars, 2, 7).equals("PAYLOAD"));
        if (String.copyValueOf(dChars, 2, 9).equals("CONTAINER"))// || nodeId.equals("ctn"))
        {
            Main.getMain().addContainerDataPoint(ctnParseData(d));
        } else if (String.copyValueOf(dChars, 2, 7).equals("PAYLOAD"))
        {
            Main.getMain().addPayloadDataPoint(payParseData(d));
        }
    }

    public ContainerData ctnParseData(byte[] data)
    {
        String dataString = ByteUtils.byteArrayToString(data);
        char[] dataChars = dataString.toCharArray();
        ContainerData c;
        if (!missionStarted)
        {
                    c = new ContainerData((((data[1] << 8) & 0xFF00) | data[0] & 0x00FF),
                    String.copyValueOf(dataChars, 2, 9),
                    0f,
                    ByteUtils.byteArrayToShort(new byte[] { data[16], data[15] }),
                    ByteArrayToFloat(new byte[] { data[17], data[18], data[19], data[20] }),
                    ByteArrayToFloat(new byte[] { data[21], data[22], data[23], data[24] }),
                    ByteArrayToFloat(new byte[] { data[25], data[26], data[27], data[28] }),
                    ContainerState.values()[(ByteUtils.byteArrayToInt(new byte[]
            { data[29] }))]);
            if (c.getState() == ContainerState.LNC)
            {
                System.out.println("STARTING MISSION");
                missionStarted = true;
                lncTime = System.currentTimeMillis();
            }
        }
        else 
        {
            c = new ContainerData((((data[1] << 8) & 0xFF00) | data[0] & 0x00FF),
                    String.copyValueOf(dataChars, 2, 9),
                    (float)((System.currentTimeMillis() - lncTime)/1000.0),
                    ByteUtils.byteArrayToShort(new byte[] { data[16], data[15] }),
                    ByteArrayToFloat(new byte[] { data[17], data[18], data[19], data[20] }),
                    ByteArrayToFloat(new byte[] { data[21], data[22], data[23], data[24] }),
                    ByteArrayToFloat(new byte[] { data[25], data[26], data[27], data[28] }),
                    ContainerState.values()[(ByteUtils.byteArrayToInt(new byte[]
            { data[29] }))]);
        }
        return c;
    }

    public PayloadData payParseData(byte[] data)
    {
        String dataString = ByteUtils.byteArrayToString(data);
        char[] dataChars = dataString.toCharArray();
        PayloadData p;
        if (!missionStarted)
        {
                    p = new PayloadData((((data[1] << 8) & 0xFF00) | data[0] & 0x00FF), // TeamId
                    String.copyValueOf(dataChars, 2, 7), // PAYLOAD
                    //ByteArrayToFloat(new byte[] { data[9], data[10], data[11], data[12] }), // Mission
                    0f,                                                                        // time
                    ByteUtils.byteArrayToShort(new byte[] { data[14], data[13] }), // Packet
                                                                                   // count
                    ByteArrayToFloat(new byte[] { data[15], data[16], data[17], data[18] }), // altitude
                    ByteArrayToFloat(new byte[] { data[19], data[20], data[21], data[22] }), // pressure
                    ByteArrayToFloat(new byte[] { data[23], data[24], data[25], data[26] }), // speed
                    ByteArrayToFloat(new byte[] { data[27], data[28], data[29], data[30] }), // temperature
                    ByteArrayToFloat(new byte[] { data[31], data[32], data[33], data[34] }), // volt
                    ByteArrayToFloat(new byte[] { data[35], data[36], data[37], data[38] }), // heading
                    ByteUtils.byteArrayToShort(new byte[] { data[41], data[40] }), // PictureCount
                    PayloadState.values()[(ByteUtils.byteArrayToInt(new byte[]
            { data[39] }))] // Payload State
            );
        }
        else
        {
                p = new PayloadData((((data[1] << 8) & 0xFF00) | data[0] & 0x00FF), // TeamId
                    String.copyValueOf(dataChars, 2, 7), // PAYLOAD
                    //ByteArrayToFloat(new byte[] { data[9], data[10], data[11], data[12] }), // Mission
                    (float)((System.currentTimeMillis() - lncTime)/1000.0),                                                                        // time
                    ByteUtils.byteArrayToShort(new byte[] { data[14], data[13] }), // Packet
                                                                                   // count
                    ByteArrayToFloat(new byte[] { data[15], data[16], data[17], data[18] }), // altitude
                    ByteArrayToFloat(new byte[] { data[19], data[20], data[21], data[22] }), // pressure
                    ByteArrayToFloat(new byte[] { data[23], data[24], data[25], data[26] }), // speed
                    ByteArrayToFloat(new byte[] { data[27], data[28], data[29], data[30] }), // temperature
                    ByteArrayToFloat(new byte[] { data[31], data[32], data[33], data[34] }), // volt
                    ByteArrayToFloat(new byte[] { data[35], data[36], data[37], data[38] }), // heading
                    ByteUtils.byteArrayToShort(new byte[] { data[41], data[40] }), // PictureCount
                    PayloadState.values()[(ByteUtils.byteArrayToInt(new byte[]
            { data[39] }))] // Payload State
            );
        }
        return p;
    }

    public static float ByteArrayToFloat(byte[] data)
    {
        return Float.intBitsToFloat(
                ((data[3] & 0xFF) << 24) | ((data[2] & 0xFF) << 16) | ((data[1] & 0xFF) << 8) | (data[0] & 0xFF));
    }
}
