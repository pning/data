package data.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

public final class RSAUtil {
	private static String RSA = "RSA";

	private static String PRIVATE_KEY = ReadXML.readvalue("PRIVATE_KEY");;

	/**
	 * 用私钥解密
	 * 
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 从字符串中加载私钥<br>
	 * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
	 * 
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(String privateKeyStr)
			throws Exception {
		try {
			byte[] buffer = new sun.misc.BASE64Decoder()
					.decodeBuffer(privateKeyStr);
			// X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance(RSA);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			System.out.println(e);
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
		try {
			return loadPrivateKey(readKey(in));
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		}
	}

	/**
	 * 读取密钥信息
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String readKey(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}

		return sb.toString();
	}

	public static String getValues(String key) throws Exception {
		String k[] = key.split("=");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < k.length; i++) {
			PrivateKey privateKey = loadPrivateKey(PRIVATE_KEY);
			byte[] decryptByte = decryptData(
					new sun.misc.BASE64Decoder().decodeBuffer((k[i] + "=")
							.replaceAll("-", "+").replaceAll("_", "/")),
					privateKey);
			String decryptStr = new String(decryptByte);
			sb.append(decryptStr);
		}
		return sb.toString();
	}

	public static String getLog(String log) throws Exception {
		Pattern p = Pattern
				.compile("^\\[.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|.*");
		Matcher m = p.matcher(log);
		String shuju = null;
		String data = null;
		if (m.matches()) {
			shuju = log.substring(log.indexOf("]") + 2);
			String vr[] = shuju.split("\\|\\|")[5].split("_");
			try {
				if (new Integer(vr[1]) >= 3 && new Integer(vr[2]) >= 4) {
					data = log.replace(
							shuju.split("\\|\\|")[2],
							getValues(shuju.split("\\|\\|")[2]).replace("\"",
									""));
				} else {

					data = log.replace(shuju.split("\\|\\|")[2],
							shuju.split("\\|\\|")[2].replace("\\x22", ""));
				}
			} catch (Exception e) {
				data = log;
			}
		} else {
			data = log;
		}
		return data;
	}

	// public static void main(String[] args) throws Exception {
	// String log =
	// "[28/Jul/2015:00:00:02 +0800] ios||/banner/listsExt/||EijKNAaZDYUKzkcFh_iRQSsiMpbpXGa-KQWQ1LSWqGzzEvhuZ-VGBO7RLvYAr-qr11O_Vutxyaw3mKbEtoV5bvFOnmzSJrrAJoBC2s7sLeOdSOTcUj91FiVgpZF2uXc3qKTciIewHJY7RUeHlW84HEq3AhTjTsW9sHoDeoPtes8=||iosOfficial||8A3E0B7F-6F37-41B4-B649-ECD361FFA321||ios_3_4_2||121.224.189.138";
	// String log1 =
	// "[28/Jul/2015:00:00:02 +0800] ios||/topcart/count/||||iosOfficial||0756E9CE-89B5-4999-8581-621E4D615862||ios_3_4_2||36.33.98.112";
	// String log2 =
	// "[28/Jul/2015:00:00:02 +0800] ios||/collect/getLists||{\\x22type\\x22:1,\\x22count\\x22:10,\\x22page\\x22:3}||iosOfficial||5FED9703-ED20-43F7-9C51-EF41161588FD||ios_3_3_3||43.224.52.0";
	// System.out.println(getLog(log));
	// }

}
