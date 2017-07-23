package com.raven.kings.cansat2017.ground.station;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import gnu.io.CommPortIdentifier;

public class comPortRadioButtons implements ActionListener
{

    public comPortRadioButtons()
    {
        // TODO Auto-generated constructor stub
    }

    public void makeComPortRadioButtons(JMenu comSetting)
    {
        comSetting.removeAll();
        
        JRadioButtonMenuItem portRadio;
        ButtonGroup portGroup = new ButtonGroup();

        int length = Main.availablePorts.size();

        for (int i = 0; i < length; i++)
        {
            portRadio = new JRadioButtonMenuItem(Main.availablePorts.get(i) + "");
            portRadio.addActionListener((ActionListener) this);

            comSetting.add(portRadio);
            portGroup.add(portRadio);
            if (i == length - 1)
            {
                portRadio.setSelected(true);
                //Main.setBaudRate([i]);
            }
        }
        
        comSetting.addSeparator();
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        comSetting.add(refreshButton);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "Refresh")
        {
            Main.getMain().myGui.updatePortList();
        }
        else //if (e.getActionCommand().contains("COM"))
        {
            Main.setPort(e.getActionCommand());
            System.out.println("Set active port to " + e.getActionCommand());
        }
    }

    public static ArrayList<String> getAvailablePorts()
    {

        ArrayList<String> list = new ArrayList<String>();

        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements())
        {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                //System.out.println(portId.getName());
                list.add(portId.getName());
            }
        }

        return list;
    }
}
