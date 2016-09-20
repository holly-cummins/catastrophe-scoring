package catastrophe.scoring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier.VisualClass;

public class ImageScorer {

	public Score getScore(String encodedImage) throws IOException {
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		// In Bluemix, this isn't needed
		// Uncomment and fill in the key for local running
		// service.setApiKey("<api-key>");

		if (encodedImage != null) {
			// The Watson API doesn't yet support uploaded base 64
			String strippedString = encodedImage.substring(encodedImage.indexOf(",") + 1);
			byte[] data = javax.xml.bind.DatatypeConverter.parseBase64Binary(strippedString);
			// We know the file will be a PNG, although this is very fragile
			// We should read it from the encoding string
			File file = File.createTempFile("img", ".png");
			try (OutputStream stream = new FileOutputStream(file)) {
				stream.write(data);
				stream.close();
				System.out.println("Wrote image to " + file);
			}
			ClassifyImagesOptions options = new ClassifyImagesOptions.Builder().images(file).build();
			VisualClassification result = service.classify(options).execute();
			System.out.println(result);
			List<VisualClassifier> classes = result.getImages().get(0).getClassifiers();
			VisualClassifier oo = classes.get(0);
			System.out.println(oo.getId());
			VisualClass visualClass = oo.getClasses().get(0);
			int score = (int) (visualClass.getScore() * 100);
			String bestGuess = visualClass.getName();
			System.out.println("Score is " + score);
			return new Score(score, bestGuess);
		} else {
			return new Score(0, "no image");
		}
	}

	public String getScoringAlgorithm() {
		return "an invariant number";
	}

}
