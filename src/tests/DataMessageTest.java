package tests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import messaging.messages.DataMessage;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import dataFile.DataFile;

/**
 * Pour faire fonctionner le test il faut placer le fichier "test_image_itp.png"
 * dans un rŽpertoire "test" dans le m�me dossier que "src".
 * 
 * L'implŽmentation utilisŽe ici est peut-�tre diffŽrente de la v™tre, il faudra
 * donc modifier le code pour le faire fonctionner dans votre projet.
 * 
 * La classe DataFile poss�de deux constructeurs :
 * 
 * -Un pour lire depuis un fichier dŽjˆ existant, qui ne prend qu'un seul
 * param�tre , le chemin vers ce fichier. -Un deuxi�me pour crŽer un fichier ˆ
 * partir de messages Data. Son constructeur prend deux param�tres : le chemin
 * du nouveau fichier, et le premier message Data re�u, qui contient des
 * informations utiles telles que le hash, la taille totale du fichier et le
 * format ( pour complŽter le nom du fichier ).
 * 
 * @author Jeremy Gotteland
 * 
 */
public class DataMessageTest {

	private static LinkedList<DataMessage> pieces;
	private static DataFile dataFile;

	// Constants computed from the test file. Students have to compute
	// themselves to get to the same result.
	private final static byte[] fileHash = { 107, -72, -59, 15, 51, -46, 119,
			98, -90, 43, 96, -84, -78, -127, -64, 68, -128, 10, -128, 121 };
	private final static byte[] pngFormat = { 112, 110, 103, 32 };
	private final byte[] noFormat = { 32, 32, 32, 32 };

	@BeforeClass
	public static void setUpClass() throws IOException {

		dataFile = new DataFile("src" + File.separator + "tests"
				+ File.separator + "test_image_itp.png");

		pieces = generateDataList(dataFile);
	}

	/**
	 * Si l'extension ne contient que trois lettres, rajouter un espace pour
	 * remplir les 4 bytes obligatoires du format. Ici "png ".getBytes("ASCII")
	 * = { 112, 110, 103, 32 }
	 * 
	 */
	@Test
	public void readingDataTest() {

		Assert.assertTrue(Arrays.equals(fileHash, dataFile.getHash()));
		Assert.assertTrue(Arrays.equals(pngFormat, dataFile.getFormat()));
	}

	/**
	 * L'extension n'est pas ajoutŽe car en temps normal on ne la connait pas.
	 * 
	 * Il faut utiliser l'attribut "format" de l'objet Data passŽ en param�tre
	 * dans le constructeur pour crŽer le fichier proprement avec la bonne
	 * extension.
	 */
	@Test
	public void writingDataTest() throws UnsupportedEncodingException {

		DataFile written = new DataFile("Test_File", pieces.removeFirst());
		written.deleteOnExit();

		for (DataMessage block : pieces) {
			written.writePacket(block);
		}
		Assert.assertTrue(written.isComplete());
		Assert.assertTrue(Arrays.equals(fileHash, written.getHash()));
		Assert.assertTrue(Arrays.equals(pngFormat, written.getFormat()));

	}

	/**
	 * Format de mon constructeur Data :
	 * 
	 * public Data(byte[] planeID, int continuation, int posx, int posy, byte[]
	 * hash, byte[] format,int filesize, byte[] payload)
	 * 
	 * Adaptez en fonction du v™tre.
	 * 
	 * Ce test devrait lancer une "IllegalArgumentException" car on essaye
	 * d'Žcrire un bloc de donnŽe de taille infŽrieure ˆ 1024 bytes alors que ce
	 * n'est pas le dernier bloc.
	 * 
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void badDataSizeTest() {

		System.out.println("Bad DataSize Test");

		int fileSize = 4097;

		DataMessage firstDataBlock = new DataMessage(null, 0, -1, -1, null,
				noFormat, fileSize, new byte[DataMessage.MAX_PACKET_SIZE]);
		DataMessage wrongDataBlock = new DataMessage(null, 2, -1, 1, null,
				noFormat, fileSize, new byte[DataMessage.MAX_PACKET_SIZE / 2]);

		DataFile test = new DataFile("dummy", firstDataBlock);

		test.writePacket(wrongDataBlock);

		test.delete();
	}

	/**
	 * Ici aucune exception n'est levŽe car il s'agit du dernier bloc donc sa
	 * taille peut-�tre infŽrieure ˆ 1024 bytes.
	 */
	@Test
	public void lastPacketSizeTest() {

		System.out.println("Last PacketSize Test");

		int fileSize = 1536;

		DataMessage firstDataBlock = new DataMessage(null, 0, -1, -1, null,
				noFormat, fileSize, new byte[DataMessage.MAX_PACKET_SIZE]);
		DataMessage lastBlock = new DataMessage(null, 1, -1, 1, null, noFormat,
				fileSize, new byte[DataMessage.MAX_PACKET_SIZE / 2]);

		DataFile test = new DataFile("dummy", firstDataBlock);

		test.writePacket(lastBlock);

		test.delete();

	}

	/**
	 * 
	 * Used to get the file as a list of Data.
	 * 
	 * Reference error was corrected.
	 * 
	 * @param dataFile
	 *            which we want to split in blocks.
	 * @return chunks of the file in the form of a "Data" list.
	 * @throws IOException
	 *             in case of there is a problem reading the file
	 */
	private static LinkedList<DataMessage> generateDataList(File dataFile)
			throws IOException {

		LinkedList<DataMessage> pieces = new LinkedList<DataMessage>();

		int fileSize = (int) dataFile.length();

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
				dataFile));

		byte[] buffer = new byte[DataMessage.MAX_PACKET_SIZE];
		int available = 0;
		int i = 0;

		while ((available = bis.available()) > 0) {

			if (available < DataMessage.MAX_PACKET_SIZE) {
				buffer = new byte[available];
			} else {
				buffer = new byte[DataMessage.MAX_PACKET_SIZE];
			}

			bis.read(buffer, 0, buffer.length);

			pieces.add(new DataMessage(null, i, -1, -1, fileHash, pngFormat,
					fileSize, buffer));

			i++;
		}

		Collections.shuffle(pieces);
		bis.close();
		return pieces;
	}

}
