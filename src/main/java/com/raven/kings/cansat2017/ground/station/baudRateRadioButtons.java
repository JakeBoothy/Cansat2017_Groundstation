package com.raven.kings.cansat2017.ground.station;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;

import javax.swing.JRadioButtonMenuItem;

public class baudRateRadioButtons implements ActionListener
{

    public baudRateRadioButtons()
    {
        // TODO Auto-generated constructor stub
    }

    public void makeBaudRateRadioButtons(JMenu baudSetting)
    {
        JRadioButtonMenuItem baudRadio;
        ButtonGroup baudGroup = new ButtonGroup();

        for (int i = 0; i < Main.BAUD_RATE_OPTIONS.length; i++)
        {
            baudRadio = new JRadioButtonMenuItem(Main.BAUD_RATE_OPTIONS[i] + "");
            baudRadio.addActionListener((ActionListener) this);
            ;
            baudSetting.add(baudRadio);
            baudGroup.add(baudRadio);
            if (i == Main.BAUD_RATE_OPTIONS.length - 1)
            {
                baudRadio.setSelected(true);
                Main.setBaudRate(Main.BAUD_RATE_OPTIONS[i]);
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        Main.setBaudRate(Integer.parseInt(e.getActionCommand()));
        System.out.println("Set baud to " + e.getActionCommand());
    }
}
