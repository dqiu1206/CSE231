package opencv.setup;

import java.awt.Desktop;
import java.net.URI;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import opencv.core.OpenCVUtils;

/**
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class DownloadOpenCVNativeLib {
	public static void main(String[] args) throws Exception {
		try {
			OpenCVUtils.loadLibrary();
			Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
			System.out.println(mat.dump());
			System.out.println();
			System.out.println("success");
		} catch (Throwable t) {
			t.printStackTrace();
			URI uri = new URI("http://classes.engineering.wustl.edu/cse231/core/index.php/Homebrew");
			System.err.println("failure.  follow directions at: ");
			System.err.println(uri);
			if (Desktop.isDesktopSupported()) {
				System.err.println("launching browser...");
				Desktop.getDesktop().browse(uri);
			}
		}
	}
}
