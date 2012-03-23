package tests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import junit.framework.Assert;
import messaging.messages.DataMessage;


import org.junit.BeforeClass;
import org.junit.Test;
import DataFile.*;
/**
 * Pour faire fonctionner le test il faut placer le fichier "test_image_itp.png" dans 
 * un répertoire "test" dans le même dossier que "src".
 * 
 * L'implémentation utilisée ici est peut-être différente de la vôtre, il faudra donc modifier le code
 * pour le faire fonctionner dans votre projet.
 * 
 * La classe DataFile possède deux constructeurs :
 * 
 * -Un pour lire depuis un fichier déjà existant, qui ne prend qu'un seul paramètre , le chemin vers ce fichier.
 * -Un deuxième pour créer un fichier à partir de messages DataMessage. Son constructeur prend deux paramètres : le chemin
 * du nouveau fichier, et le premier message DataMessage reçu, qui contient des informations utiles telles que le hash, la taille
 * totale du fichier et le format ( pour compléter le nom du fichier ).
 * 
 * @author Jeremy Gotteland
 * 
 */
public class DataMessageTest {

	private static LinkedList<DataMessage> pieces;
	private static DataFile dataFile;

	//Constants computed from the test file. Students have to compute themselves to get to the same result.
	private final static byte[] fileHash = { 107, -72, -59, 15, 51, -46, 119,
			98, -90, 43, 96, -84, -78, -127, -64, 68, -128, 10, -128, 121 };
	private final static byte[] pngFormat = "png".getBytes();


	@BeforeClass
	public static void setUpClass() throws IOException, NoSuchAlgorithmException {
		dataFile = new DataFile("bin"+File.separator+"tests" + File.separator + "test_image_itp.png");
		pieces = generateDataList(dataFile);
	}

	@Test
	public void readingDataTest() throws IOException {
		Assert.assertTrue(Arrays.equals(pngFormat, dataFile.getFormat()));
		Assert.assertTrue(Arrays.equals(fileHash, dataFile.getHash()));
	}

	@Test
	public void writingDataTest() {

		DataFile written = new DataFile("Test_File", pieces.removeFirst());

		for (DataMessage block : pieces) {
			written.writePacket(block);
		}

		Assert.assertTrue(written.isComplete());
		Assert.assertTrue(Arrays.equals(fileHash, written.getHash()));
		Assert.assertTrue(Arrays.equals(pngFormat, written.getFormat()));
		Assert.assertTrue(written.getName().endsWith(".png"));

		written.delete();
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void badDataSizeTest() {

		System.out.println("Bad DataSize Test");

		int fileSize = 4097;

		DataMessage firstDataBlock = new DataMessage(null, 0, -1, -1, null, null, fileSize,
				new byte[DataMessage.MAX_PACKET_SIZE]);
		DataMessage wrongDataBlock = new DataMessage(null, 2, -1, 1, null, null, fileSize,
				new byte[DataMessage.MAX_PACKET_SIZE - 2]);

		DataFile test = new DataFile("dummy.rdm", firstDataBlock);

		test.writePacket(wrongDataBlock);

		test.delete();
	}

	@Test
	public void lastPacketSizeTest() {

		System.out.println("Last PacketSize Test");

		int fileSize = 2046;

		DataMessage firstDataBlock = new DataMessage(null, 0, -1, -1, null, null, fileSize,
				new byte[DataMessage.MAX_PACKET_SIZE]);
		DataMessage lastBlock = new DataMessage(null, 1, -1, 1, null, null, fileSize,
				new byte[DataMessage.MAX_PACKET_SIZE - 2]);

		DataFile test = new DataFile("dummy.rdm", firstDataBlock);

		test.writePacket(lastBlock);

		test.delete();

	}

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
			}

			bis.read(buffer, 0, buffer.length);

			pieces.add(new DataMessage(null, i, -1, -1, fileHash, pngFormat, fileSize,
					buffer));

			i++;
		}

		Collections.shuffle(pieces);

		return pieces;
	
	}

}
