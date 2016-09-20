package catastrophe.scoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions.Builder;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier.VisualClass;

public class ImageScorer {

	private static final int FILE_SIZE_WEIGHT = 30;
	private static final int AVERAGE_IMAGE_SIZE = 4000;

	public Score getScore(String encodedImage) throws IOException {
		VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
		// In Bluemix, this isn't needed
		// Fill in the key for local running
		if (System.getenv().get("VCAP_SERVICES") == null) {
			service.setApiKey("{api-key}");
		}

		if (encodedImage != null) {
			File file = convertStringToImageFile(encodedImage);
			ClassifyImagesOptions options = buildOptions(file);
			VisualClassification result = service.classify(options).execute();
			// We know we passed through an image, so we can safely get the 0th
			// classifier
			List<VisualClassifier> classifiers = result.getImages().get(0).getClassifiers();
			if (classifiers.size() > 0) {
				VisualClassifier classifier = classifiers.get(0);
				List<VisualClass> visualClasses = classifier.getClasses();
				if (visualClasses.size() > 0) {
					Score score = generateScoreFromClasses(visualClasses);
					// Now add a weighting for the image size - more detail is
					// to be rewarded
					double sizeRatio = ((double) file.length()) / AVERAGE_IMAGE_SIZE;
					// Use a log, since the size should be diminishing returns
					int sizeWeighting = (int) (0.5 - Math.log(sizeRatio) * FILE_SIZE_WEIGHT);
					return new Score(score.getScore() - sizeWeighting, score.getBestGuess());
				} else {
					return new Score(0, "unrecognisable");
				}
			} else {
				return new Score(0, "no classifier");
			}
		} else {
			return new Score(0, "no image");
		}
	}

	private Score generateScoreFromClasses(List<VisualClass> visualClasses) {
		// We want the top pick
		VisualClass visualClass = visualClasses.get(0);
		String bestGuess = visualClass.getName();
		int score = (int) (visualClass.getScore() * 100);

		// If they didn't get the right animal, invert the score
		if (!"cat".equals(bestGuess)) {
			score = 100 - score;
		}
		return new Score(score, bestGuess);
	}

	private ClassifyImagesOptions buildOptions(File file) {
		Builder optionsBuilder = new ClassifyImagesOptions.Builder().images(file);
		String classifierId = System.getenv().get("CLASSIFIER_ID");

		if (classifierId != null) {
			optionsBuilder.classifierIds(classifierId);
		} else {
			System.out.println(
					"Using the default classifier. Results are not trained for line drawings. Set the CLASSIFIER_ID environment variable to override.");
		}
		ClassifyImagesOptions options = optionsBuilder.build();
		return options;
	}

	private File convertStringToImageFile(String encodedImage) throws IOException, FileNotFoundException {
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
		return file;
	}

	public String getScoringAlgorithm() {
		return "an invariant number";
	}

}
