package org.ekh.laboiteasons;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {

	boolean currentElement = false;
	String currentValue = "";

	SoundInfo soundInfo;
	ArrayList<SoundInfo> soundList;

	//return list of sounds
	public ArrayList<SoundInfo> getSoundList() {
		return soundList;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		currentElement = true;

		if (qName.equals("sounds")){
			soundList = new ArrayList<SoundInfo>();
		} 
		else if (qName.equals("sound")) {
			soundInfo = new SoundInfo();
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;

		if (qName.equalsIgnoreCase("id"))
			soundInfo.setSoundId(currentValue.trim());
		else if (qName.equalsIgnoreCase("name"))
			soundInfo.setNameSound(currentValue.trim());
		else if (qName.equalsIgnoreCase("sound"))
			soundList.add(soundInfo);

		currentValue = "";
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}

	}


}

