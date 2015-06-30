package net.bhpachulski.tddcriteriaserver.network.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TDDCriteriaNetworkUtil {

	public String getMacAddress () {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);	 
			byte[] mac = network.getHardwareAddress();
	 
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}

			return sb.toString();
		} catch (UnknownHostException | SocketException e) {
			throw new RuntimeException("Erro ao recuperar MACADRESS"); 	 
		} 
	}
	
}
